package ru.yandex.javacource.sevrin.schedule.manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    public void shouldReturnInitializedTaskManager() {
        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();

        assertNotNull(taskManager, "Объект taskManager не создается!");
        assertInstanceOf(InMemoryTaskManager.class, taskManager, "taskManager не экземляр InMemoryTaskManager!");
    }
}