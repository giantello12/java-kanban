package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.task.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Scanner;
import java.util.List;
import java.time.Duration;


public class  FileBackedTaskManager extends InMemoryTaskManager {
    File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        int id = super.addSubtask(subtask);
        save();
        return id;
    }

    @Override
    public Integer addTask(Task task) {
        int id = super.addTask(task);
        save();
        return id;
    }

    @Override
    public Integer addEpic(Epic epic) {
        int id = super.addEpic(epic);
        save();
        return id;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                Task task = Formatter.fromString(line);
                if (task != null) {
                    if (task instanceof Epic) {
                        manager.epics.put(task.getId(), (Epic) task);
                    } else if (task instanceof Subtask) {
                        manager.subtasks.put(task.getId(), (Subtask) task);
                        Subtask subtask = (Subtask) task;
                        Epic epic = manager.epics.get(subtask.getEpicId());
                        if (epic != null) {
                            epic.addSubtaskId(subtask.getId());
                        }
                    } else {
                        manager.tasks.put(task.getId(), task);
                    }

                    if (task.getId() > manager.idCounter) {
                        manager.idCounter = task.getId();
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения из файла", e);
        }

        for (Epic epic : manager.epics.values()) {
            List<Subtask> subtasks = manager.getEpicSubtasks(epic.getId());
            manager.updateEpicTime(epic.getId(), subtasks);
        }

        return manager;
    }

    private void updateEpicTime(int epicId, List<Subtask> subtasks) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        Duration duration = subtasks.stream()
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);

        LocalDateTime startTime = subtasks.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime endTime = subtasks.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        epic.setDuration(duration);
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);

    }

    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getTasks()) {
                writer.write(Formatter.toString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(Formatter.toString(epic) + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(Formatter.toString(subtask) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл", e);
        }
    }
}


