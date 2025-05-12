package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.task.*;

import java.util.Comparator;
import java.time.LocalDateTime;

public class TaskComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        LocalDateTime task1Time = task1.getStartTime();
        LocalDateTime task2Time = task2.getStartTime();

        if (task1Time == null && task2Time == null) {
            return Integer.compare(task1.getId(), task2.getId());
        }
        if (task1Time == null) {
            return 1;
        }
        if (task2Time == null) {
            return -1;
        }

        int timeCompare = task1Time.compareTo(task2Time);
        return timeCompare != 0 ? timeCompare : Integer.compare(task1.getId(), task2.getId());
    }
}
