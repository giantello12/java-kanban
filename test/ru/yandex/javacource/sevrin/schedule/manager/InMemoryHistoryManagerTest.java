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
    public void testEmptyHistory() {
        List<Task> history = taskManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой!");
    }

    @Test
    public void testDuplicationInHistory() {
        Task task1 = new Task("test1", "test1");
        taskManager.addTask(task1);
        taskManager.getTask(task1.getId());
        Task task2 = new Task("test2", "test2");
        taskManager.addTask(task2);
        taskManager.getTask(task2.getId());
        taskManager.getTask(task1.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size(), "История не должна содержать дубликатов!");
        assertTrue(history.contains(task1), "История должна содержать task1!");
        assertTrue(history.contains(task2), "История должна содержать task2!");
    }

    @Test
    public void testRemovingFromHistoryEdgeCases() {
        Task task1 = new Task("test1", "test1");
        Task task2 = new Task("test2", "test2");
        Task task3 = new Task("test3", "test3");

        taskManager.addTask(task1);
        taskManager.getTask(task1.getId());
        taskManager.addTask(task2);
        taskManager.getTask(task2.getId());
        taskManager.addTask(task3);
        taskManager.getTask(task3.getId());

        taskManager.remove(task1.getId());
        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать 2 элемента после удаления из начала!");
        assertFalse(history.contains(task1), "История не должна содержать удаленный task1!");

        taskManager.remove(task2.getId());
        history = taskManager.getHistory();
        System.out.println(history.size());
        assertEquals(1, history.size(), "История должна содержать 1 элемент после удаления из середины!");
        assertFalse(history.contains(task2), "История не должна содержать удаленный task2!");

        taskManager.getTask(task3.getId());
        taskManager.remove(task3.getId());
        history = taskManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой после удаления последнего элемента!");
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