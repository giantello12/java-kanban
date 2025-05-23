package ru.yandex.javacource.sevrin.schedule.manager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

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
}
