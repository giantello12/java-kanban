package ru.yandex.javacource.sevrin.schedule.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import ru.yandex.javacource.sevrin.schedule.manager.*;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds = new ArrayList<>();
    private final InMemoryTaskManager taskManager = new InMemoryTaskManager();

    public Epic(String title, String description) {
        super(title, description);
        setStatus(Status.NEW);
    }

    public Epic() {
        setStatus(Status.NEW);
    }

    public Epic(int id, String title, String description, Status status) {
        super(id, title, description, status);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public Duration getDuration() {
        if (subtaskIds.isEmpty()) {
            return Duration.ZERO;
        }

        return taskManager.getEpicSubtasks(id).stream()
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public LocalDateTime getStartTime() {
        if (subtaskIds.isEmpty()) {
            return null;
        }

        return taskManager.getEpicSubtasks(id).stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    public LocalDateTime getEndTime() {
        if (subtaskIds.isEmpty()) {
            return null;
        }

        return taskManager.getEpicSubtasks(id).stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void addSubtaskId(int id) {
        this.subtaskIds.add(id);
    }

    public void removeSubtask(Integer id) {
        subtaskIds.remove(id);
    }

    public void cleanSubtaskIds() {
        subtaskIds.clear();
    }

    @Override
    public String toString() {
        return "ru.yandex.javacource.sevrin.schedule.task.Epic {" + "title='" + getTitle() + '\'' + " description='"
                + getDescription() + '\'' + " id=" + getId() + '}';
    }
}
