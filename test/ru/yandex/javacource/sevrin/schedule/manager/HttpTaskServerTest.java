package ru.yandex.javacource.sevrin.schedule.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.sevrin.schedule.server.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.javacource.sevrin.schedule.task.*;

class HttpTaskServerTest {
    private HttpTaskServer server;
    private HttpClient client;
    private final Gson gson = Managers.getGson();

    @BeforeEach
    void setUp() throws IOException {
        TaskManager manager = new InMemoryTaskManager();
        server = new HttpTaskServer(manager);
        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void shouldCreateTask() throws Exception {
        Task task = new Task("Test", "Description");
        String json = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    void shouldGetTaskById() throws Exception {
        Task task = new Task("Test", "Description");
        int taskId = server.getTaskManager().addTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + taskId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Task result = gson.fromJson(response.body(), Task.class);
        assertEquals(taskId, result.getId());
    }

    @Test
    void shouldUpdateEpicStatus() throws Exception {
        Epic epic = new Epic("Epic", "Desc");
        int epicId = server.getTaskManager().addEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Desc", epicId);

        subtask.setTitle("Updated");
        subtask.setStatus(Status.DONE);
        String json = gson.toJson(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());

        Epic result = server.getTaskManager().getEpic(epicId);
        assertEquals(Status.DONE, result.getStatus());
    }

    @Test
    void shouldDeleteEpicWithSubtasks() throws Exception {
        Epic epic = new Epic("Epic", "Desc");
        int epicId = server.getTaskManager().addEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Desc", epicId);
        int subtaskId = server.getTaskManager().addSubtask(subtask);

        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + epicId))
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, deleteResponse.statusCode());
        assertNull(server.getTaskManager().getEpic(epicId));
        assertNull(server.getTaskManager().getSubtask(subtaskId));
    }

    @Test
    void shouldReturnEmptyHistory() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body());
    }

    @Test
    void shouldPrioritizeTasksCorrectly() throws Exception {
        Task task1 = new Task(
                "Task1",
                "Desc",
                LocalDateTime.now().plusHours(2),
                Duration.ofMinutes(30)
        );
        Task task2 = new Task(
                "Task2",
                "Desc",
                LocalDateTime.now(),
                Duration.ofHours(1)
        );

        server.getTaskManager().addTask(task1);
        server.getTaskManager().addTask(task2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());

        assertEquals(200, response.statusCode());
        assertEquals("Task2", tasks.get(0).getTitle());
        assertEquals("Task1", tasks.get(1).getTitle());
    }

    @Test
    void shouldHandleInvalidTaskId() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/999"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}