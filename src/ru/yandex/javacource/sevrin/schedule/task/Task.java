package ru.yandex.javacource.sevrin.schedule.task;

import java.util.Objects;
import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task() {
        this.status = Status.NEW;
    }

    public Task(String title, String description) {
        this();
        this.title = title;
        this.description = description;
    }

    public Task(String title, String description, LocalDateTime startTime, Duration duration) {
        this(title, description);
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, String title, String description, Status status) {
        this(title, description);
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Task otherTask = (Task) obj;
        return id == otherTask.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ru.yandex.javacource.sevrin.schedule.task.Task {" + "title='" + title + '\'' + " description='"
                + description + '\'' + " id=" + id + '}';
    }
}
