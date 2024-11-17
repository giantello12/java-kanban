package ru.yandex.javacource.sevrin.schedule.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.sevrin.schedule.task.*;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskManagerTest {
    InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
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
    public void cantAddEpicToSubtasks() {
        Epic epic = new Epic("Test title", "Test description");
        int testEpic = taskManager.addEpic(epic);
        int subtasksSizeExpected = 0;

        Subtask subtask = new Subtask("Test title", "Test description", testEpic);
        subtask.setId(testEpic);
        taskManager.addSubtask(subtask);
        int subtaskSizeInFact = taskManager.getSubtasks().size();

        assertEquals(subtasksSizeExpected, subtaskSizeInFact, "Epic можно передать в качестве подзадачи!");
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

    @Test
    public void testAddAndGetTask() {
        Task task = new Task("test", "test");
        Integer taskId = taskManager.addTask(task);
        Task gettingTask = taskManager.getTask(taskId);
        assertNotNull(taskId, "Задача не создана!");
        assertEquals(taskId, gettingTask.getId(), "ID не совпадают!");
        assertEquals(task.getTitle(), gettingTask.getTitle(), "Заголовки не совпадают!");
    }

    @Test
    public void testAddAndGetEpicAndSubtask() {
        Epic epic = new Epic("test", "test");
        Integer epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test", "test", epicId);
        Integer subtaskId = taskManager.addSubtask(subtask);

        assertNotNull(epicId, "Эпик не создан!");
        assertNotNull(subtaskId, "Подзадача не создана!");
        Epic gettingEpic = taskManager.getEpic(epicId);
        assertNotNull(gettingEpic, "Эпик не находится по ID!");
        Subtask gettingSubtask = taskManager.getSubtask(subtaskId);
        assertNotNull(gettingSubtask, "Подзадача не находится по ID!");
        assertEquals(epicId, gettingEpic.getId(), "ID Эпиков не совпадают!");
        assertEquals(subtaskId, gettingSubtask.getId(), "ID Подзадач не совпадают!");
        assertEquals(epic.getTitle(), gettingEpic.getTitle(), "Заголовки эпиков не совпадают");
        assertEquals(subtask.getTitle(), gettingSubtask.getTitle(), "Заголовки подзадача не совпадают!");
    }

    @Test
    public void testImmutabilityOfTask() {
        Task task = new Task("test", "test");
        String expectedTitle = "test";
        String expectedDescription = "test";
        Status expectedStatus = Status.NEW;

        Integer taskId = taskManager.addTask(task);

        assertEquals(expectedTitle, task.getTitle(), "Заголовки не совпадают!");
        assertEquals(expectedDescription, task.getDescription(), "Описание не совпадает!");
        assertEquals(taskId, task.getId(), "ID не совпадают!");
        assertEquals(expectedStatus, task.getStatus(), "Статус не совпадает!");
    }
}