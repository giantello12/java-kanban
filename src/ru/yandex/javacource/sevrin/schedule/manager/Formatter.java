package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.task.Epic;
import ru.yandex.javacource.sevrin.schedule.task.Status;
import ru.yandex.javacource.sevrin.schedule.task.Subtask;
import ru.yandex.javacource.sevrin.schedule.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

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
            Duration duration = !parts[5].isEmpty() ? Duration.parse(parts[5].trim()) : null;
            LocalDateTime startTime = !parts[6].isEmpty() ? LocalDateTime.parse(parts[6].trim()) : null;
            LocalDateTime endTime = !parts[7].isEmpty() ? LocalDateTime.parse(parts[7].trim()) : null;

            switch (type) {
                case "TASK":
                    Task task;
                    if (duration != null && startTime != null && endTime != null) {
                        task = new Task(title, description, startTime, duration);
                    } else {
                        task = new Task(title, description);
                    }
                    task.setId(id);
                    task.setStatus(status);
                    return task;

                case "EPIC":
                    Epic epic = new Epic(title, description);
                    epic.setId(id);
                    epic.setStatus(status);
                    if (duration != null) {
                        epic.setDuration(duration);
                    }
                    if (startTime != null) {
                        epic.setStartTime(startTime);
                    }
                    if (endTime != null) {
                        epic.setEndTime(endTime);
                    }
                    return epic;

                case "SUBTASK":
                    Subtask subtask;
                    int epicId = Integer.parseInt(parts[8].trim());
                    if (duration != null && startTime != null && endTime != null) {
                        subtask = new Subtask(title, description, startTime, duration, epicId);
                    } else {
                        subtask = new Subtask(title, description, epicId);
                    }
                    subtask.setId(id);
                    subtask.setStatus(status);
                    return subtask;

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
            return String.format("%d, %s, %s, %s, %s, %s, %s, %s, %s",
                    task.getId(),
                    task.getClass().getSimpleName(),
                    task.getTitle(),
                    task.getStatus(),
                    task.getDescription(),
                    task.getDuration() != null ? task.getDuration().toString() : "",
                    task.getStartTime() != null ? task.getStartTime().toString() : "",
                    task.getEndTime() != null ? task.getEndTime().toString() : "",
                    subtask.getEpicId()
            );
        } else {
            return String.format("%d, %s, %s, %s, %s, %s, %s, %s",
                    task.getId(),
                    task.getClass().getSimpleName(),
                    task.getTitle(),
                    task.getStatus(),
                    task.getDescription(),
                    task.getDuration() != null ? task.getDuration().toString() : "",
                    task.getStartTime() != null ? task.getStartTime().toString() : "",
                    task.getEndTime() != null ? task.getEndTime().toString() : ""
            );
        }
    }
}
