package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.task.Task;

import java.util.List;

public interface HistoryManager {
    void addToHistory(Task task);
    List<Task> getHistory();
}
