package ru.yandex.javacource.sevrin.schedule.task;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, Integer epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public Subtask(int id, String title, String description, Status status, int epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "ru.yandex.javacource.sevrin.schedule.task.Subtask{" + "title='" + getTitle() + '\'' + " description='"
                + getDescription() + '\'' + " id=" + id + '}';
    }
}
