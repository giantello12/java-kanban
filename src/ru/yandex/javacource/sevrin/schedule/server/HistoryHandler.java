package ru.yandex.javacource.sevrin.schedule.server;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.sevrin.schedule.task.*;
import ru.yandex.javacource.sevrin.schedule.manager.*;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            List<Task> history = taskManager.getHistory();
            String jsonResponse = gson.toJson(history);
            sendText(exchange, jsonResponse);

        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }
}