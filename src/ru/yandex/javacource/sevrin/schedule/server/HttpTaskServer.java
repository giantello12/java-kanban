package ru.yandex.javacource.sevrin.schedule.server;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.javacource.sevrin.schedule.manager.Managers;
import ru.yandex.javacource.sevrin.schedule.manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this.taskManager = Managers.getDefault();
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        configureServer();
    }

    private void configureServer() {
        // Базовые обработчики (пока заглушки)
        server.createContext();
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
            HttpTaskServer taskServer = new HttpTaskServer();
            taskServer.start();

        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}