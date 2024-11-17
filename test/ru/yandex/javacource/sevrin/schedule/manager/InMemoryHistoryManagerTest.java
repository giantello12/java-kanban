package ru.yandex.javacource.sevrin.schedule.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.javacource.sevrin.schedule.task.*;
import ru.yandex.javacource.sevrin.schedule.manager.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void testAddToHistoryPreservesPreviousVersionTask() {
        Task task = new Task("test", "test");
        taskManager.addTask(task);
        taskManager.getTask(task.getId());
        List<Task> history = taskManager.getHistory();
        task.setTitle("changed");
        taskManager.updateTask(task);

        assertEquals(1, history.size(), "Размер списка содержащего историю отличается от ожидаемого!");
        assertEquals("test", history.getFirst().getTitle(), "Заголовок отличается от " +
                "предыдущей версии!");
        assertNotEquals(task.getTitle(), history.getFirst().getTitle(), "Заголовок совпадает с " +
                "обновленной версией!"); // Убедитесь, что заголовки не совпадают
    }

    @Test
    public void testAddToHistoryPreservesPreviousVersionEpicAndSubtask() {
        Epic epic = new Epic("test", "test");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test", "test", epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.getEpic(epic.getId());
        taskManager.getSubtask(subtask.getId());
        List<Task> history = taskManager.getHistory();
        epic.setTitle("changed");
        subtask.setTitle("changed");
        taskManager.updateEpic(epic);
        taskManager.updateSubtask(subtask);

        assertEquals(2, history.size(), "Размер списка содержащего историю отличается от ожидаемого!");
        assertEquals("test", history.getFirst().getTitle(), "Заголовок эпика отличается от предыдущей " +
                "версии!");
        assertEquals("test", history.get(1).getTitle(), "Заголовок подзадачи отличается от предыдущей " +
                "версии!");
        assertNotEquals(epic.getTitle(), history.getFirst().getTitle(), "Заголовок эпика совпадает с " +
                "обновленной версией!");
        assertNotEquals(subtask.getTitle(), history.get(1).getTitle(), "Заголовок подзадачи совпадает с " +
                "обновленной версией!");
    }
}