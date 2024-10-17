package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.task.Epic;
import ru.yandex.javacource.sevrin.schedule.task.Subtask;
import ru.yandex.javacource.sevrin.schedule.task.Task;

import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private int idCounter;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAll() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public Status getTaskStatus(int id) {
        if (tasks.get(id) == null) {
            return null;
        }
        return tasks.get(id).getStatus();
    }

    public Status getEpicStatus(int id) {
        if (epics.get(id) == null) {
            return null;
        }
        return epics.get(id).getStatus();
    }

    public Status getSubtaskStatus(int id) {
        if (subtasks.get(id) == null) {
            return null;
        }
        return subtasks.get(id).getStatus();
    }

    public void setTaskStatus(int id, Status status) {
        tasks.get(id).setStatus(status);
    }

    public void setSubtaskStatus(int id, Status status) {
        Subtask subtask = subtasks.get(id);
        subtask.setStatus(status);
        updateEpicStatus(subtask.getEpicId());
    }

    public int addTask(Task task) {
        int id = ++idCounter;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    public int addEpic(Epic epic) {
        int id = ++idCounter;
        epic.setId(id);
        epics.put(epic.getId(), epic);
        return id;
    }

    public Integer addSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        int id = ++idCounter;
        subtask.setId(id);
        subtasks.put(subtask.getId(), subtask);
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(epicId);
        return id;
    }

    public void updateTask(Task task, String title, String description) {
        int id = task.getId();
        Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return;
        }
        savedTask.setTitle(title);
        savedTask.setDescription(description);
    }

    public void updateEpic(Epic epic, String title, String description) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        savedEpic.setTitle(title);
        savedEpic.setDescription(description);
    }

    public void updateSubtask(Subtask subtask, String title, String description) {
        Subtask savedSubtask = subtasks.get(subtask.getId());
        if (savedSubtask == null) {
            return;
        }
        savedSubtask.setTitle(title);
        savedSubtask.setDescription(description);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        final Epic epic = epics.remove(id);
        if (epic == null) {
            return;
        }
        for (Integer subtaskId : epic.getSubtasksIds()) {
            subtasks.remove(subtaskId);
        }
    }

    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(id);
        updateEpicStatus(epic.getId());
    }

    public void updateEpicStatus(int epicId) {
        int doneCounter = 0;
        Epic epic = epics.get(epicId);
        for (Integer subtaskId : epic.getSubtasksIds()) {
            if (subtasks.get(subtaskId).getStatus() == Status.DONE) {
                doneCounter++;
            }
        }

        int numberOfSubtasks = epic.getSubtasksIds().size();

        if (doneCounter == numberOfSubtasks && numberOfSubtasks != 0) {
            epic.setStatus(Status.DONE);
        } else if (doneCounter == 0) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
