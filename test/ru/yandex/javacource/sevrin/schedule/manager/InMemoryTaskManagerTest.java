package ru.yandex.javacource.sevrin.schedule.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.sevrin.schedule.task.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    InMemoryTaskManager taskManager;

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @BeforeEach
    void setUp() throws IOException {
        super.setUp();
        taskManager = new InMemoryTaskManager();
    }


    @Test
    public void shouldBeEqualTasksIfEqualTaskId() {
        Task task1 = new Task("Test title1", "Test description1");
        Task task2 = new Task("Test title2", "Test description2");
        task1.setId(1);
        task2.setId(1);
        Epic epic1 = new Epic("Test", "Test");
        Epic epic2 = new Epic("Test", "Test");
        epic1.setId(2);
        epic2.setId(2);

        assertEquals(task1, task2, "Задачи с одинаковым ID не совпадают!");
        assertEquals(epic1, epic2, "Наследники с одинаковым ID не совпадают!");
    }


    @Test
    public void cantAddSubtaskAsEpic() {
        Epic epic = new Epic("test", "test");
        int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test", "test", epicId);
        subtask.setId(epicId);

        Integer result = taskManager.addSubtask(subtask);

        assertNull(result, "Subtask можно добавить в качестве эпика!");
    }

}