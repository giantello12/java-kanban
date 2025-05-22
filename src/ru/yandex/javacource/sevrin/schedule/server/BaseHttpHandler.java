package ru.yandex.javacource.sevrin.schedule.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.sevrin.schedule.manager.Managers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BaseHttpHandler {
    protected final Gson gson = Managers.getGson();

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
        sendText(exchange, text, 200);
    }

    // Объект не найден
    protected void sendNotFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, -1);
        exchange.close();
    }

    // Конфликт времени задач
    protected void sendHasInteractions(HttpExchange exchange, String message) throws IOException {
        String response = gson.toJson(Map.of("error", "Time conflict", "message", message));
        sendText(exchange, response, 406);
    }

    // Ошибка сервера
    protected void sendInternalError(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(500, -1);
        exchange.close();
    }

    // Чтение тела запроса
    protected String readRequest(HttpExchange exchange) throws IOException {
        try (InputStream input = exchange.getRequestBody()) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    // Проверка HTTP метода
    protected void validateMethod(HttpExchange exchange, String expectedMethod) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase(expectedMethod)) {
            throw new IOException("Method " + exchange.getRequestMethod() + " not allowed");
        }
    }
}