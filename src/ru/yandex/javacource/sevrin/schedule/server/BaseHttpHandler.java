package ru.yandex.javacource.sevrin.schedule.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.sevrin.schedule.manager.*;
import ru.yandex.javacource.sevrin.schedule.task.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class BaseHttpHandler implements HttpHandler {
    protected final Gson gson = Managers.getGson();
    protected final TaskManager taskManager;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public abstract void handle(HttpExchange exchange) throws IOException;

    // Универсальная отправка текстового ответа
    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    // Успешный ответ с телом
    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, -1);
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange, String message) throws IOException {
        String response = gson.toJson(Map.of("error", "Time conflict", "message", message));
        sendText(exchange, response); // Используем базовый sendText с кодом 200
        exchange.sendResponseHeaders(406, response.getBytes().length); // Переопределяем код
    }

    protected String readRequest(HttpExchange exchange) throws IOException {
        try (InputStream input = exchange.getRequestBody()) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}