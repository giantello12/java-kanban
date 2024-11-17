package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> browsingHistory;

    public InMemoryHistoryManager() {
        this.browsingHistory = new ArrayList<>();
    }

    @Override
    public void addToHistory(Task task) {
        if (browsingHistory.size() == 10) {
            browsingHistory.removeFirst();
        }
        browsingHistory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return browsingHistory;
    }
}
