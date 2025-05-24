package ru.yandex.javacource.sevrin.schedule.task;

import java.time.LocalDateTime;
import java.time.Duration;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, LocalDateTime startTime, Duration duration, Integer epicId) {
        super(title, description, startTime, duration);
        this.epicId = epicId;
        setStatus(Status.NEW);
    }

    public Subtask(String title, String description, Integer epicId) {
        super(title, description);
        this.epicId = epicId;
        setStatus(Status.NEW);
    }

    public Subtask(int id, String title, String description, Status status, int epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "ru.yandex.javacource.sevrin.schedule.task.Subtask{" + "title='" + getTitle() + '\'' + " description='"
                + getDescription() + '\'' + " id=" + id + '}';
    }
}
