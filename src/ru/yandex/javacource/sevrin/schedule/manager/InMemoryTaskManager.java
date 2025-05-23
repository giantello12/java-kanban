package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.exceptions.ManagerTimeException;
import ru.yandex.javacource.sevrin.schedule.task.Epic;
import ru.yandex.javacource.sevrin.schedule.task.Status;
import ru.yandex.javacource.sevrin.schedule.task.Subtask;
import ru.yandex.javacource.sevrin.schedule.task.Task;

import java.util.*;
import java.time.LocalDateTime;

public class InMemoryTaskManager implements TaskManager {
    protected int idCounter;
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Epic> epics;
    protected final Map<Integer, Subtask> subtasks;
    protected final HistoryManager inMemoryHistoryManager;
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(new TaskComparator());

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        inMemoryHistoryManager = Managers.getDefaultHistory();
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public void deleteAll() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        epics.values().forEach(epic -> {
            epic.cleanSubtaskIds();
            updateEpicStatus(epic.getId());
        });
        subtasks.clear();
    }

    @Override
    public void deleteEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public Task getTask(int id) {
        final Task task = tasks.get(id);
        inMemoryHistoryManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        final Epic epic = epics.get(id);
        inMemoryHistoryManager.add(epic);
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        final Subtask subtask = subtasks.get(id);
        inMemoryHistoryManager.add(subtask);
        return subtasks.get(id);
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> tasks = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        for (int id : epic.getSubtaskIds()) {
            tasks.add(subtasks.get(id));
        }
        return tasks;
    }

    @Override
    public Integer addTask(Task task) {
        checkIntersections(task);
        task.setId(++idCounter);
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        return task.getId();
    }
    @Override
    public Integer addEpic(Epic epic) {
        if (epic.getSubtaskIds().stream().anyMatch(subtaskId -> epic.getId() == subtaskId)) {
            return null;
        }
        int id = ++idCounter;
        epic.setId(id);
        epics.put(epic.getId(), epic);
        return id;
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        checkIntersections(subtask);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null || subtask.getId() == subtask.getEpicId()) {
            return null;
        }
        List<Subtask> epicSubtasks = getEpicSubtasks(epic.getId());
        boolean subtaskHasIntersections = epicSubtasks.stream()
                .anyMatch(existingSubtask -> isIntersection(subtask, existingSubtask));
        if (subtaskHasIntersections) {
            throw new ManagerTimeException("Подзадача пересекается с другими подзадачами!");
        }
        int id = ++idCounter;
        subtask.setId(id);
        subtasks.put(subtask.getId(), subtask);
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(epicId);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        return id;
    }

    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return;
        }
        prioritizedTasks.remove(savedTask);
        tasks.put(id, task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        final Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        epic.setSubtaskIds(savedEpic.getSubtaskIds());
        epic.setStatus(savedEpic.getStatus());
        epics.put(epic.getId(), epic);
    }

    public boolean isIntersection(Task task1, Task task2) {
        if (task1.getStartTime() == null || task1.getDuration() == null ||
                task2.getStartTime() == null || task2.getDuration() == null) {
            return false;
        }

        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = start1.plus(task1.getDuration());
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = start2.plus(task2.getDuration());

        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    @Override
    public void checkIntersections(Task newTask) {
        if (newTask.getStartTime() == null || newTask.getDuration() == null) return;

        LocalDateTime newStart = newTask.getStartTime();
        LocalDateTime newEnd = newStart.plus(newTask.getDuration());

        for (Task existing : prioritizedTasks) {
            LocalDateTime existingStart = existing.getStartTime();
            LocalDateTime existingEnd = existingStart.plus(existing.getDuration());

            if (newStart.isBefore(existingEnd) && existingStart.isBefore(newEnd)) {
                throw new ManagerTimeException("Обнаружено пересечение с задачей " + existing.getTitle());
            }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        final int id = subtask.getId();
        final int epicId = subtask.getEpicId();
        final Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        final Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        prioritizedTasks.remove(savedSubtask);
        subtasks.put(id, subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        updateEpicStatus(epicId);
    }

    @Override
    public void deleteTask(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            prioritizedTasks.remove(task);
        }
    }

    @Override
    public void deleteEpic(int id) {
        final Epic epic = epics.remove(id);
        if (epic == null) {
            return;
        }
        epic.getSubtaskIds().forEach(subtasks::remove);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(id);
        updateEpicStatus(epic.getId());
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        long doneCounter = epic.getSubtaskIds().stream()
                .filter(subtaskId -> subtasks.get(subtaskId).getStatus() == Status.DONE)
                .count();

        long inProgressCounter = epic.getSubtaskIds().stream()
                .filter(subtaskId -> subtasks.get(subtaskId).getStatus() == Status.IN_PROGRESS)
                .count();

        int numberOfSubtasks = epic.getSubtaskIds().size();

        if (numberOfSubtasks != 0) {
            if (doneCounter == numberOfSubtasks) {
                epic.setStatus(Status.DONE);
            } else if (inProgressCounter > 0 || doneCounter > 0) {
                epic.setStatus(Status.IN_PROGRESS);
            } else {
                epic.setStatus(Status.NEW);
            }
        } else {
            epic.setStatus(Status.NEW);
        }

    }

    public void remove(int id) {
        inMemoryHistoryManager.remove(id);
    }

    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }
}
