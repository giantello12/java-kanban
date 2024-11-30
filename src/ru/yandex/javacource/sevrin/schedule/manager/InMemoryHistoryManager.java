package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> browsingHistory;
    final int MAX_SIZE = 10;

    public InMemoryHistoryManager() {
        this.browsingHistory = new ArrayList<>();
    }

    @Override
    public void addToHistory(Task task) {

        if (task == null) {
            return;
        }
        if (browsingHistory.size() == MAX_SIZE) {
            browsingHistory.removeFirst();
        }
        browsingHistory.add(task.copy());
    }

    @Override
    public List<Task> getHistory() {
        return browsingHistory;
    }
}
