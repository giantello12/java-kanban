package ru.yandex.javacource.sevrin.schedule.manager;

public class Managers {
    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}