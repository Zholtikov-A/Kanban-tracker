package ru.yandex.practicum.taskTracker.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import ru.yandex.practicum.taskTracker.manager.Managers;
import ru.yandex.practicum.taskTracker.manager.TaskManager;
import ru.yandex.practicum.taskTracker.tasks.EpicTask;
import ru.yandex.practicum.taskTracker.tasks.SubTask;
import ru.yandex.practicum.taskTracker.tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
    }

    public HttpTaskServer() throws IOException {
        this(Managers.getDefaultHttp());
    }

    public static void main(String[] args) throws IOException {
        final HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start();
    }

    private void handler(HttpExchange exchange) {
        try {
            final String path = exchange.getRequestURI().getPath().substring(7);
            switch (path) {
                case "": {
                    if (!exchange.getRequestMethod().equals("GET")) {
                        exchange.sendResponseHeaders(405, 0);
                    }
                    final String response = gson.toJson(taskManager.getPrioritizedTasks());
                    sendText(exchange, response);
                }
                break;
                case "history": {
                    if (!exchange.getRequestMethod().equals("GET")) {
                        exchange.sendResponseHeaders(405, 0);
                    }
                    final String response = gson.toJson(taskManager.getHistory());
                    sendText(exchange, response);
                }
                break;
                case "task":
                    handleTask(exchange);
                    break;
                case "subtask":
                    handleSubtask(exchange);
                    break;
                case "subtask/epic": {
                    if (!exchange.getRequestMethod().equals("GET")) {
                        exchange.sendResponseHeaders(405, 0);
                    }
                    final String query = exchange.getRequestURI().getQuery();
                    String idParam = query.substring(3); //?id=
                    final Long id = Long.parseLong(idParam);
                    final List<SubTask> subtasks = taskManager.showSubTasksOfEpic(id);
                    final String response = gson.toJson(subtasks);
                    sendText(exchange, response);
                }
                break;
                case "epic":
                    handleEpic(exchange);
                    break;
                default: {
                    exchange.sendResponseHeaders(404, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exchange.close();
        }
        exchange.close();
    }

    private void handleTask(HttpExchange exchange) throws IOException {
        final String query = exchange.getRequestURI().getQuery();
        switch (exchange.getRequestMethod()) {
            case "GET": {
                if (query == null) {
                    final List<Task> tasks = taskManager.showSimpleTasks();
                    final String response = gson.toJson(tasks);
                    sendText(exchange, response);
                    return;
                }
                String idParam = query.substring(3);
                final Long id = Long.parseLong(idParam);
                final Task task = taskManager.getSimpleTaskById(id);
                final String response = gson.toJson(task);
                sendText(exchange, response);
            }
            break;
            case "DELETE": {
                if (query == null) {
                    taskManager.removeAllSimpleTasks();
                    exchange.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);
                final Long id = Long.parseLong(idParam);
                taskManager.removeSimpleTaskById(id);
                exchange.sendResponseHeaders(200, 0);
            }
            break;
            case "POST": {
                String json = readText(exchange);
                if (json.isEmpty()) {
                    exchange.sendResponseHeaders(400, 0);
                    return;
                }
                final Task task = gson.fromJson(json, Task.class);
                final Long id = task.getId();
                if (id != null) {
                    taskManager.updateSimpleTask(task);
                    final String response = gson.toJson(task);
                    sendText(exchange, response);
                } else {
                    Long taskId = taskManager.saveSimpleTask(task);
                    task.setId(taskId);
                    final String response = gson.toJson(task);
                    sendText(exchange, response);
                }
            }
        }
        exchange.close();
    }

    private void handleSubtask(HttpExchange exchange) throws IOException {
        final String query = exchange.getRequestURI().getQuery();
        switch (exchange.getRequestMethod()) {
            case "GET": {
                if (query == null) {
                    final List<SubTask> subTasks = taskManager.showSubTasks();
                    final String response = gson.toJson(subTasks);
                    sendText(exchange, response);
                    return;
                }
                String idParam = query.substring(3);
                final Long id = Long.parseLong(idParam);
                final SubTask subTask = taskManager.getSubTaskById(id);
                final String response = gson.toJson(subTask);
                sendText(exchange, response);
            }
            break;
            case "DELETE": {
                if (query == null) {
                    taskManager.removeAllSubTasks();
                    exchange.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);
                final Long id = Long.parseLong(idParam);
                taskManager.removeSubTaskById(id);
                exchange.sendResponseHeaders(200, 0);
            }
            break;
            case "POST": {
                String json = readText(exchange);
                if (json.isEmpty()) {
                    exchange.sendResponseHeaders(400, 0);
                    return;
                }
                final SubTask subTask = gson.fromJson(json, SubTask.class);
                final Long id = subTask.getId();
                if (id != null) {
                    taskManager.updateSubTask(subTask);
                    final String response = gson.toJson(subTask);
                    sendText(exchange, response);
                } else {
                    Long subTaskId = taskManager.saveSubTask(subTask);
                    subTask.setId(subTaskId);
                    final String response = gson.toJson(subTask);
                    sendText(exchange, response);
                }
            }
        }
        exchange.close();
    }

    private void handleEpic(HttpExchange exchange) throws IOException {
        final String query = exchange.getRequestURI().getQuery();
        switch (exchange.getRequestMethod()) {
            case "GET": {
                if (query == null) {
                    final List<EpicTask> epicTasks = taskManager.showEpicTasks();
                    final String response = gson.toJson(epicTasks);
                    sendText(exchange, response);
                    return;
                }
                String idParam = query.substring(3);
                final Long id = Long.parseLong(idParam);
                final EpicTask epic = taskManager.getEpicTaskById(id);
                final String response = gson.toJson(epic);
                sendText(exchange, response);
            }
            break;
            case "DELETE": {
                if (query == null) {
                    taskManager.removeAllEpicTasks();
                    exchange.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);
                final Long id = Long.parseLong(idParam);
                taskManager.removeEpicTaskById(id);
                exchange.sendResponseHeaders(200, 0);
            }
            break;
            case "POST": {
                String json = readText(exchange);
                if (json.isEmpty()) {
                    exchange.sendResponseHeaders(400, 0);
                    return;
                }
                final EpicTask epicTask = gson.fromJson(json, EpicTask.class);
                final Long id = epicTask.getId();
                if (id != null) {
                    taskManager.updateEpicTask(epicTask);
                    final String response = gson.toJson(taskManager.getEpicTaskById(id));
                    sendText(exchange, response);
                } else {
                    Long epicTaskId = taskManager.saveEpicTask(epicTask);
                    epicTask.setId(epicTaskId);
                    final String response = gson.toJson(taskManager.getEpicTaskById(epicTaskId));
                    sendText(exchange, response);
                }
            }
        }
        exchange.close();
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(1);
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }

    protected String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), UTF_8);
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }
}