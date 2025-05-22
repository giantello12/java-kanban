package ru.yandex.javacource.sevrin.schedule.manager;

import com.google.gson.Gson;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        return new Gson();
    }
}