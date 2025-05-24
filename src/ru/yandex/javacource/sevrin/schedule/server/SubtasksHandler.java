package ru.yandex.javacource.sevrin.schedule.server;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.sevrin.schedule.task.*;
import ru.yandex.javacource.sevrin.schedule.manager.*;
import ru.yandex.javacource.sevrin.schedule.exceptions.*;

import java.io.IOException;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler {

    public SubtasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            List<String> pathParts = splitPath(exchange);
            String method = exchange.getRequestMethod();

            // GET /subtasks/epic/{epicId}
            if (method.equals("GET") && pathParts.size() == 4
                    && pathParts.get(1).equals("subtasks")
                    && pathParts.get(2).equals("epic")) {

                handleGetEpicSubtasks(exchange, pathParts.get(3));
                return;
            }

            // GET /subtasks
            if (method.equals("GET") && pathParts.size() == 2
                    && pathParts.get(1).equals("subtasks")) {

                handleGetAllSubtasks(exchange);
                return;
            }

            // GET /subtasks/{id}
            if (method.equals("GET") && isPathIdValid(pathParts, 3)
                    && pathParts.get(1).equals("subtasks")) {

                handleGetSubtaskById(exchange, pathParts.get(2));
                return;
            }

            // POST /subtasks
            if (method.equals("POST") && pathParts.size() == 2
                    && pathParts.get(1).equals("subtasks")) {

                handleCreateOrUpdateSubtask(exchange);
                return;
            }

            // DELETE /subtasks
            if (method.equals("DELETE") && pathParts.size() == 2
                    && pathParts.get(1).equals("subtasks")) {

                handleDeleteAllSubtasks(exchange);
                return;
            }

            // DELETE /subtasks/{id}
            if (method.equals("DELETE") && isPathIdValid(pathParts, 3)
                    && pathParts.get(1).equals("subtasks")) {

                handleDeleteSubtaskById(exchange, pathParts.get(2));
                return;
            }

            sendNotFound(exchange);

        } catch (ManagerTimeException e) {
            sendHasInteractions(exchange, e.getMessage());
        } catch (NumberFormatException e) {
            sendBadRequest(exchange, "Invalid ID format");
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGetEpicSubtasks(HttpExchange exchange, String epicIdStr) throws IOException {
        int epicId = Integer.parseInt(epicIdStr);
        Epic epic = taskManager.getEpic(epicId);

        if (epic == null) {
            sendNotFound(exchange);
            return;
        }

        List<Subtask> subtasks = taskManager.getEpicSubtasks(epicId);
        sendText(exchange, gson.toJson(subtasks));
    }

    private void handleGetAllSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> subtasks = taskManager.getSubtasks();
        sendText(exchange, gson.toJson(subtasks));
    }

    private void handleGetSubtaskById(HttpExchange exchange, String idStr) throws IOException {
        int id = Integer.parseInt(idStr);
        Subtask subtask = taskManager.getSubtask(id);

        if (subtask == null) {
            sendNotFound(exchange);
            return;
        }

        sendText(exchange, gson.toJson(subtask));
    }

    private void handleCreateOrUpdateSubtask(HttpExchange exchange) throws IOException {
        String body = readRequest(exchange);
        Subtask subtask = gson.fromJson(body, Subtask.class);

        // Проверка существования эпика
        if (subtask.getEpicId() == 0 || taskManager.getEpic(subtask.getEpicId()) == null) {
            sendBadRequest(exchange, "Invalid Epic ID");
            return;
        }

        if (subtask.getId() == 0) {
            int newId = taskManager.addSubtask(subtask);
            sendCreated(exchange, newId);
        } else {
            taskManager.updateSubtask(subtask);
            sendNoContent(exchange);
        }
    }

    private void handleDeleteAllSubtasks(HttpExchange exchange) throws IOException {
        taskManager.deleteSubtasks();
        sendNoContent(exchange);
    }

    private void handleDeleteSubtaskById(HttpExchange exchange, String idStr) throws IOException {
        int id = Integer.parseInt(idStr);
        Subtask subtask = taskManager.getSubtask(id);

        if (subtask == null) {
            sendNotFound(exchange);
            return;
        }

        taskManager.deleteSubtask(id);
        sendNoContent(exchange);
    }
}