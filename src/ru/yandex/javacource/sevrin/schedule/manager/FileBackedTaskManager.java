package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.exceptions.ManagerSaveException;
import ru.yandex.javacource.sevrin.schedule.task.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Scanner;
import java.util.List;
import java.time.Duration;


public class FileBackedTaskManager extends InMemoryTaskManager {
    File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        int id = super.addSubtask(subtask);
        checkIntersections(subtask);
        save();
        return id;
    }

    @Override
    public Integer addTask(Task task) {
        int id = super.addTask(task);
        checkIntersections(task);
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
                if (line.isEmpty()) continue;

                Task task = Formatter.fromString(line);
                if (task == null) continue;

                if (task instanceof Epic epic) {
                    manager.epics.put(epic.getId(), epic);
                } else if (task instanceof Subtask subtask) {
                    manager.subtasks.put(subtask.getId(), subtask);
                } else {
                    manager.tasks.put(task.getId(), task);
                }

                if (task.getId() >= manager.idCounter) {
                    manager.idCounter = task.getId() + 1;
                }

                if (task.getStartTime() != null) {
                    manager.prioritizedTasks.add(task);
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла", e);
        }

        manager.epics.values().stream()
                .map(epic -> manager.getEpicSubtasks(epic.getId()))
                .forEach(subtasks -> manager.updateEpicTime(subtasks.getFirst().getEpicId(), subtasks));

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
    }

    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");
            getTasks().stream()
                    .map(Formatter::toString)
                    .forEach(taskStr -> {
                        try {
                            writer.write(taskStr + "\n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            getEpics().stream()
                    .map(Formatter::toString)
                    .forEach(epicStr -> {
                        try {
                            writer.write(epicStr + "\n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            getSubtasks().stream()
                    .map(Formatter::toString)
                    .forEach(subtaskStr -> {
                        try {
                            writer.write(subtaskStr + "\n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл", e);
        }
    }
}


