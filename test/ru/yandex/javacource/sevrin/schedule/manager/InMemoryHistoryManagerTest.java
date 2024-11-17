package ru.yandex.javacource.sevrin.schedule.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.yandex.javacource.sevrin.schedule.task.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void testAddToHistoryPreservesPreviousVersion() {
        Task task1 = new Task("test1", "test1");
        task1.setId(1);

        // Добавляем первую задачу в историю
        historyManager.addToHistory(task1);
        List<Task> historyBeforeChangeTask = historyManager.getHistory();

        task1.setTitle("test2");
        task1.setDescription("test2");

        List<Task> historyAfterChangeTask = historyManager.getHistory();

        assertEquals(historyBeforeChangeTask, historyAfterChangeTask, "История не сохраняет " +
                "предыдущую версию задачи!");
    }
}