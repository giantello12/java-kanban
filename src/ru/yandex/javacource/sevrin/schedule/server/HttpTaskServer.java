package ru.yandex.javacource.sevrin.schedule.server;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.javacource.sevrin.schedule.manager.Managers;
import ru.yandex.javacource.sevrin.schedule.manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.taskManager = manager;
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        configureServer();
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    private void configureServer() {
        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/subtasks", new SubtasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public void start() {
        server.start();
        System.out.println("HTTP Task Server started on port " + PORT);
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP Task Server stopped");
    }

    public static void main(String[] args) {
        try {
            TaskManager manager = Managers.getDefault();
            HttpTaskServer taskServer = new HttpTaskServer(manager);
            taskServer.start();
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}