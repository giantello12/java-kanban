package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.task.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File tempFile;
    private FileBackedTaskManager taskManager;

    @Override
    protected FileBackedTaskManager createTaskManager() {
        return FileBackedTaskManager.loadFromFile(tempFile);
    }

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        super.setUp();
        taskManager = createTaskManager();
    }

    @Test
    void shouldSaveAndLoadEmptyManager() {
        try {
            taskManager.save();
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
            Duration duration1 = Duration.ofHours(2);
            Duration duration2 = Duration.ofHours(3);
            LocalDateTime date1 = LocalDateTime.of(2024, 10, 10, 2, 1);
            LocalDateTime date2 = LocalDateTime.of(2023, 10, 10, 2, 1);
            Task task1 = new Task("Task 1", "Description 1", date1, duration1);
            Task task2 = new Task("Task 2", "Description 2", date2, duration2);

            taskManager.addTask(task1);
            taskManager.addTask(task2);

            List<Task> tasks = taskManager.getTasks();
            assertEquals(2, tasks.size());

            assertEquals("Task 1", taskManager.getTask(task1.getId()).getTitle());
            assertEquals("Task 2", taskManager.getTask(task2.getId()).getTitle());
        } catch (Exception e) {
            fail("Test execution failed: " + e.getMessage());
        }
    }

    @Test
    void shouldLoadMultipleTasks() {
        try {
            Duration duration1 = Duration.ofHours(2);
            Duration duration2 = Duration.ofHours(3);
            LocalDateTime date1 = LocalDateTime.of(2024, 10, 10, 2, 1);
            LocalDateTime date2 = LocalDateTime.of(2023, 10, 10, 2, 1);
            Task task1 = new Task("Task 1", "Description 1", date1, duration1);
            Task task2 = new Task("Task 2", "Description 2", date2, duration2);
            taskManager.addTask(task1);
            taskManager.addTask(task2);

            taskManager.save();

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
