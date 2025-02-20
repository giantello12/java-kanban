package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.task.*;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();

    void remove(int id);
}
