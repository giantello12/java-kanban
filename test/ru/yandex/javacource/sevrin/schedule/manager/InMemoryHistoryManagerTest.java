package ru.yandex.javacource.sevrin.schedule.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.javacource.sevrin.schedule.task.*;

class InMemoryHistoryManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void testAddingToHistoryReplacesPreviousVersionTask() {
        Task task = new Task("test", "test");
        taskManager.addTask(task);
        taskManager.getTask(task.getId());
        task.setTitle("changed");
        taskManager.updateTask(task);
        taskManager.getTask(task.getId());
        List<Task> history = taskManager.getHistory();

        assertEquals(1, history.size(), "Размер списка содержащего историю отличается от ожидаемого!");
    }

    @Test
    public void testAddToHistoryPreservesPreviousVersionEpicAndSubtask() {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test", "test", epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.getEpic(epic.getId());
        taskManager.getSubtask(subtask.getId());

        epic.setTitle("changed");
        subtask.setTitle("changed");
        taskManager.updateEpic(epic);
        taskManager.updateSubtask(subtask);

        taskManager.getEpic(epic.getId());
        taskManager.getSubtask(subtask.getId());
        List<Task> history = taskManager.getHistory();

        assertEquals(2, history.size(), "Размер списка содержащего историю отличается от ожидаемого!");
    }

    @Test
    public void testAddingViewToHistoryReplacesPreviousView() {
        Task task1 = new Task("test1", "test1");
        taskManager.addTask(task1);
        taskManager.getTask(task1.getId());
        Task task2 = new Task("test2", "test2");
        taskManager.addTask(task2);
        taskManager.getTask(task2.getId());
        taskManager.getTask(task1.getId());
        List<Task> history = taskManager.getHistory();

        assertEquals(2, history.size(), "Размер истории больше ожидаемого!");
    }

    @Test
    public void testAddingToHistoryWorksCorrect() {
        Task task1 = new Task("test", "test");
        List<Task> history = taskManager.getHistory();
        int historySizeBeforeAdding = history.size();
        taskManager.addTask(task1);
        taskManager.getTask(task1.getId());
        history = taskManager.getHistory();
        int historySizeAfterAdding = history.size();

        assertEquals(1, history.size(), "Размер истории отличается от ожидаемого!");
        assertNotEquals(historySizeAfterAdding, historySizeBeforeAdding, "Размер истории не изменился после " +
                "добавления элемента!");
    }

    @Test
    public void testRemovingFromHistoryWorksCorrect() {
        Task task1 = new Task("test", "test");
        taskManager.addTask(task1);
        taskManager.getTask(task1.getId());
        List<Task> history = taskManager.getHistory();
        int historySizeBeforeDeleting = history.size();
        taskManager.remove(task1.getId());
        history = taskManager.getHistory();
        int historySizeAfterDeleting = history.size();
        assertNotEquals(historySizeAfterDeleting, historySizeBeforeDeleting, "Размер истории до и после " +
                "удаления элемента должен отличаться!");
    }
}