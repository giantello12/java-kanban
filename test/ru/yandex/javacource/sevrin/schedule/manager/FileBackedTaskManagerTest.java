package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.task.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

class FileBackedTaskManagerTest {
    private File tempFile;
    private FileBackedTaskManager manager;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        assertTrue(tempFile.canWrite());
        manager = new FileBackedTaskManager(tempFile);
    }

    @Test
    void shouldSaveAndLoadEmptyManager() {
        try {
            manager.save();
            FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

            assertTrue(loadedManager.getTasks().isEmpty());
            assertTrue(loadedManager.getEpics().isEmpty());
            assertTrue(loadedManager.getSubtasks().isEmpty());
        } catch (Exception e) {
            fail("Test execution failed: " + e.getMessage());
        }
    }

    @Test
    void shouldSaveMultipleTasks() {
        try {
            Task task1 = new Task("Task 1", "Description 1");
            Task task2 = new Task("Task 2", "Description 2");

            manager.addTask(task1);
            manager.addTask(task2);

            List<Task> tasks = manager.getTasks();
            assertEquals(2, tasks.size());

            assertEquals("Task 1", manager.getTask(task1.getId()).getTitle());
            assertEquals("Task 2", manager.getTask(task2.getId()).getTitle());
        } catch (Exception e) {
            fail("Test execution failed: " + e.getMessage());
        }
    }

    @Test
    void shouldLoadMultipleTasks() {
        try {
            Task task1 = new Task("Task 1", "Description 1");
            Task task2 = new Task("Task 2", "Description 2");
            manager.addTask(task1);
            manager.addTask(task2);

            manager.save();

            FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

            List<Task> loadedTasks = loadedManager.getTasks();
            assertEquals(2, loadedTasks.size());

            Task loadedTask1 = loadedTasks.get(0);
            assertEquals("Task 1", loadedTask1.getTitle());
            assertEquals("Description 1", loadedTask1.getDescription());

            Task loadedTask2 = loadedTasks.get(1);
            assertEquals("Task 2", loadedTask2.getTitle());
            assertEquals("Description 2", loadedTask2.getDescription());
        } catch (Exception e) {
            fail("Test execution failed: " + e.getMessage());
        }
    }
}
