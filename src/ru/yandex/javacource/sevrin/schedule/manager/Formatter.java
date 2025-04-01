package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.task.Epic;
import ru.yandex.javacource.sevrin.schedule.task.Status;
import ru.yandex.javacource.sevrin.schedule.task.Subtask;
import ru.yandex.javacource.sevrin.schedule.task.Task;

public class Formatter {
    public static Task fromString(String str) {
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

    public static String toString(Task task) {
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
}
