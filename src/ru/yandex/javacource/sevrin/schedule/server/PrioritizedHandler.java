package ru.yandex.javacource.sevrin.schedule.server;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.sevrin.schedule.task.Task;
import ru.yandex.javacource.sevrin.schedule.manager.TaskManager;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            // Проверяем метод запроса
            if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            // Получаем отсортированный список задач
            List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

            // Сериализуем в JSON
            String jsonResponse = gson.toJson(prioritizedTasks);

            // Отправляем успешный ответ
            sendText(exchange, jsonResponse);

        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }
}