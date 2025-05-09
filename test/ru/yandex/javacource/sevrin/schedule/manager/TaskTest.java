package ru.yandex.javacource.sevrin.schedule.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.sevrin.schedule.task.*;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task task1 = new Task(1, "Task", "Description", Status.NEW);
        Task task2 = new Task(1, "Task", "Description", Status.NEW);
        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
    }

    @Test
    void shouldDetectTimeOverlap() {
        Task task1 = new Task("Task1", "Desc", LocalDateTime.parse("2025-05-10T10:00"), Duration.ofHours(2));
        Task task2 = new Task("Task2", "Desc", LocalDateTime.parse("2025-05-10T11:00"), Duration.ofHours(1));
    }

    @Test
    void epicShouldCalculateStatusCorrectly() {
        Epic epic = new Epic("Epic", "Desc");
        Subtask sub1 = new Subtask("Sub1", "Desc", Status.NEW, epic.getId());
        Subtask sub2 = new Subtask("Sub2", "Desc", Status.DONE, epic.getId());

        epic.getSubtaskIds().add(sub1.getId());
        epic.getSubtaskIds().add(sub2.getId());

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    @Test
    void subtaskShouldKnowItsEpic() {
        Epic epic = new Epic("Epic", "Desc");
        Subtask subtask = new Subtask("Sub", "Desc", Status.NEW, epic.getId());
        assertEquals(epic.getId(), subtask.getEpicId(), "Подзадача должна знать свой эпик");
    }
}