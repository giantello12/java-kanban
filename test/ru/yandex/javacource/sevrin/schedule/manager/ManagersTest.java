package ru.yandex.javacource.sevrin.schedule.manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import ru.yandex.javacource.sevrin.schedule.manager.*;

class ManagersTest {
    @Test
    public void shouldReturnInitializedTaskManager() {
        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();

        assertNotNull(taskManager, "Объект taskManager не создается!");
        assertTrue(taskManager instanceof InMemoryTaskManager, "taskManager не экземляр InMemoryTaskManager!");
    }
}