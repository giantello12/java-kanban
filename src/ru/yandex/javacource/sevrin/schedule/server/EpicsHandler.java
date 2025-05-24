package ru.yandex.javacource.sevrin.schedule.server;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.sevrin.schedule.task.*;
import ru.yandex.javacource.sevrin.schedule.manager.*;

import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler {

    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            List<String> pathParts = splitPath(exchange);
            String method = exchange.getRequestMethod();

            // GET /epics
            if (method.equals("GET") && pathParts.size() == 2
                    && pathParts.get(1).equals("epics")) {

                handleGetAllEpics(exchange);
                return;
            }

            // GET /epics/{id}
            if (method.equals("GET") && isPathIdValid(pathParts, 3)
                    && pathParts.get(1).equals("epics")) {

                handleGetEpicById(exchange, pathParts.get(2));
                return;
            }

            // POST /epics
            if (method.equals("POST") && pathParts.size() == 2
                    && pathParts.get(1).equals("epics")) {

                handleCreateOrUpdateEpic(exchange);
                return;
            }

            // DELETE /epics
            if (method.equals("DELETE") && pathParts.size() == 2
                    && pathParts.get(1).equals("epics")) {

                handleDeleteAllEpics(exchange);
                return;
            }

            // DELETE /epics/{id}
            if (method.equals("DELETE") && isPathIdValid(pathParts, 3)
                    && pathParts.get(1).equals("epics")) {

                handleDeleteEpicById(exchange, pathParts.get(2));
                return;
            }

            sendNotFound(exchange);

        } catch (NumberFormatException e) {
            sendBadRequest(exchange, "Invalid ID format");
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGetAllEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = taskManager.getEpics();
        sendText(exchange, gson.toJson(epics));
    }

    private void handleGetEpicById(HttpExchange exchange, String idStr) throws IOException {
        int id = Integer.parseInt(idStr);
        Epic epic = taskManager.getEpic(id);

        if (epic == null) {
            sendNotFound(exchange);
            return;
        }

        sendText(exchange, gson.toJson(epic));
    }

    private void handleCreateOrUpdateEpic(HttpExchange exchange) throws IOException {
        String body = readRequest(exchange);
        Epic epic = gson.fromJson(body, Epic.class);

        if (epic.getId() == 0) {
            int newId = taskManager.addEpic(epic);
            sendCreated(exchange, newId);
        } else {
            taskManager.updateEpic(epic);
            sendNoContent(exchange);
        }
    }

    private void handleDeleteAllEpics(HttpExchange exchange) throws IOException {
        taskManager.deleteEpics();
        sendNoContent(exchange);
    }

    private void handleDeleteEpicById(HttpExchange exchange, String idStr) throws IOException {
        int id = Integer.parseInt(idStr);
        Epic epic = taskManager.getEpic(id);

        if (epic == null) {
            sendNotFound(exchange);
            return;
        }

        taskManager.deleteEpic(id);
        sendNoContent(exchange);
    }
}