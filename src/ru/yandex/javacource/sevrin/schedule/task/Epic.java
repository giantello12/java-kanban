package ru.yandex.javacource.sevrin.schedule.task;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
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
