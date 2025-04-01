package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.task.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class FileBackedTaskManager extends InMemoryTaskManager {
    File file;

    public static class ManagerSaveException extends RuntimeException {
        public ManagerSaveException(String message, Throwable cause) {
            super(message, cause);
        }
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

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public Task fromString(String str) {
        try {
            if (str == null || str.isEmpty()) {
                throw new NullPointerException("Передано значение null!");
            }
            String[] parts = str.split(",");

            if (parts.length < 5) {
                throw new IllegalArgumentException("Неверный формат строки!");
            }

            int id = Integer.parseInt(parts[0]);
            String type = parts[1].toUpperCase().trim();
            String title = parts[2].trim();
            Status status = Status.valueOf(parts[3].trim());
            String description = parts[4].trim();

            switch (type) {
                case "TASK":
                    return new Task(id, title, description, status);
                case "EPIC":
                    return new Epic(id, title, description, status);
                case "SUBTASK":
                    int epicId = Integer.parseInt(parts[5]);
                    return new Subtask(id, title, description, status, epicId);
                default:
                    return null;
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String toString(Task task) {
        if (task instanceof Subtask subtask) {
            return String.format("%d, %s, %s, %s, %s, %s",
                    task.getId(),
                    task.getClass().getSimpleName(),
                    task.getTitle(),
                    task.getStatus(),
                    task.getDescription(),
                    subtask.getEpicId()
            );
        } else {
            return String.format("%d, %s, %s, %s, %s",
                    task.getId(),
                    task.getClass().getSimpleName(),
                    task.getTitle(),
                    task.getStatus(),
                    task.getDescription()
            );
        }
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

                Task task = manager.fromString(line);
                if (task != null) {
                    if (task instanceof Epic) {
                        manager.epics.put(task.getId(), (Epic) task);
                    } else if (task instanceof Subtask) {
                        manager.subtasks.put(task.getId(), (Subtask) task);
                        // Добавляем подзадачу в эпик
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

        return manager;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getTasks()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(toString(subtask) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл", e);
        }
    }
}


