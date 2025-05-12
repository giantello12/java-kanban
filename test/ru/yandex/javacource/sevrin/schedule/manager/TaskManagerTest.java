package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.task.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    void setUp() throws IOException {
        taskManager = createTaskManager();
    }

    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task task1 = new Task(1, "Task", "Description", Status.NEW);
        Task task2 = new Task(1, "Task", "Description", Status.NEW);
        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
    }

    @Test
    void createAndGetTask() {
        Task task = new Task("Task", "Description");
        taskManager.addTask(task);
        assertEquals(task, taskManager.getTask(task.getId()));
    }

    @Test
    void subtaskShouldKnowItsEpic() {
        Epic epic = new Epic("Title", "Desc");
        Subtask subtask = new Subtask("Title", "Desc", epic.getId());

        assertEquals(subtask.getEpicId(), epic.getId(),
                "Подзадача должна знать свой эпик");
    }

    @Test
    void createAndGetEpic() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);
        assertEquals(epic, taskManager.getEpic(epic.getId()));
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

    @Test
    void epicShouldCalculateStatusCorrectly() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Title", "Description");
        int idEpic1 = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Title", "Desc", idEpic1);
        Subtask subtask2 = new Subtask("Title", "Desc", idEpic1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        assertEquals(Status.NEW, epic1.getStatus(), "Статус эпика должен быть NEW");

        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        assertEquals(Status.DONE, epic1.getStatus(), "Статус эпика должен быть DONE");

        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Статус эпика должен быть IN_PROGRESS!");

        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }
}