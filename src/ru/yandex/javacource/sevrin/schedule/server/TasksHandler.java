package ru.yandex.javacource.sevrin.schedule.server;

import com.sun.net.httpserver.HttpExchange;

import ru.yandex.javacource.sevrin.schedule.task.*;
import ru.yandex.javacource.sevrin.schedule.exceptions.*;
import ru.yandex.javacource.sevrin.schedule.manager.*;

import java.io.IOException;
import java.util.List;

public class TasksHandler extends BaseHttpHandler {
    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            List<String> pathParts = splitPath(exchange);
            String method = exchange.getRequestMethod();

            // GET /tasks
            if (method.equals("GET") && pathParts.size() == 2 && pathParts.get(1).equals("tasks")) {
                handleGetAllTasks(exchange);
                return;
            }

            // GET /tasks/{id}
            if (method.equals("GET") && isPathIdValid(pathParts, 3) && pathParts.get(1).equals("tasks")) {
                handleGetTaskById(exchange, pathParts.get(2));
                return;
            }

            // POST /tasks
            if (method.equals("POST") && pathParts.size() == 2 && pathParts.get(1).equals("tasks")) {
                handleCreateOrUpdateTask(exchange);
                return;
            }

            // DELETE /tasks
            if (method.equals("DELETE") && pathParts.size() == 2 && pathParts.get(1).equals("tasks")) {
                handleDeleteAllTasks(exchange);
                return;
            }

            // DELETE /tasks/{id}
            if (method.equals("DELETE") && isPathIdValid(pathParts, 3) && pathParts.get(1).equals("tasks")) {
                handleDeleteTaskById(exchange, pathParts.get(2));
                return;
            }

            sendNotFound(exchange);

        } catch (ManagerTimeException e) {
            sendHasInteractions(exchange, e.getMessage());
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGetAllTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskManager.getTasks();
        sendText(exchange, gson.toJson(tasks));
    }

    private void handleCreateOrUpdateTask(HttpExchange exchange) throws IOException {
        String body = readRequest(exchange);
        Task task = gson.fromJson(body, Task.class);

        if (task.getId() == 0) {
            int newId = taskManager.addTask(task);
            sendCreated(exchange, newId);
        } else {
            taskManager.updateTask(task);
            sendNoContent(exchange);
        }
    }

    private void handleDeleteAllTasks(HttpExchange exchange) throws IOException {
        taskManager.deleteTasks();
        sendNoContent(exchange);
    }

    private void handleGetTaskById(HttpExchange exchange, String idStr) throws IOException {
        int id = Integer.parseInt(idStr);
        Task task = taskManager.getTask(id);
        if (task == null) {
            sendNotFound(exchange);
            return;
        }
        sendText(exchange, gson.toJson(task));
    }

    private void handleDeleteTaskById(HttpExchange exchange, String idStr) throws IOException {
        int id = Integer.parseInt(idStr);
        if (taskManager.getTask(id) == null) {
            sendNotFound(exchange);
            return;
        }
        taskManager.deleteTask(id);
        sendNoContent(exchange);
    }
}