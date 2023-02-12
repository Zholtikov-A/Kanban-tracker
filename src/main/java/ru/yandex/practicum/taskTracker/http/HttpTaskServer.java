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

    private void handler(HttpExchange h) {
        try {
            final String path = h.getRequestURI().getPath().substring(7);
            switch (path) {
                case "": {
                    if (!h.getRequestMethod().equals("GET")) {
                        h.sendResponseHeaders(405, 0);
                    }
                    final String response = gson.toJson(taskManager.getPrioritizedTasks());
                    sendText(h, response);
                }
                break;
                case "history": {
                    if (!h.getRequestMethod().equals("GET")) {
                        h.sendResponseHeaders(405, 0);
                    }
                    final String response = gson.toJson(taskManager.getHistory());
                    sendText(h, response);
                }
                break;
                case "task":
                    handleTask(h);
                    break;
                case "subtask":
                    handleSubtask(h);
                    break;
                case "subtask/epic": {
                    if (!h.getRequestMethod().equals("GET")) {
                        h.sendResponseHeaders(405, 0);
                    }
                    final String query = h.getRequestURI().getQuery();
                    String idParam = query.substring(3); //?id=
                    final Long id = Long.parseLong(idParam);
                    final List<SubTask> subtasks = taskManager.showSubTasksOfEpic(id);
                    final String response = gson.toJson(subtasks);
                    sendText(h, response);
                }
                break;
                case "epic":
                    handleEpic(h);
                    break;
                default: {
                    h.sendResponseHeaders(404, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            h.close();
        }
        h.close();
    }

    private void handleTask(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET": {
                if (query == null) {
                    final List<Task> tasks = taskManager.showSimpleTasks();
                    final String response = gson.toJson(tasks);
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);
                final Long id = Long.parseLong(idParam);
                final Task task = taskManager.getSimpleTaskById(id);
                final String response = gson.toJson(task);
                sendText(h, response);
            }
            break;
            case "DELETE": {
                if (query == null) {
                    taskManager.removeAllSimpleTasks();
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);
                final Long id = Long.parseLong(idParam);
                taskManager.removeSimpleTaskById(id);
                h.sendResponseHeaders(200, 0);
            }
            break;
            case "POST": {
                String json = readText(h);
                if (json.isEmpty()) {
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final Task task = gson.fromJson(json, Task.class);
                final Long id = task.getId();
                if (id != null) {
                    taskManager.updateSimpleTask(task);
                    final String response = gson.toJson(task);
                    sendText(h, response);
                } else {
                    Long taskId = taskManager.saveSimpleTask(task);
                    task.setId(taskId);
                    final String response = gson.toJson(task);
                    sendText(h, response);
                }
            }
        }
        h.close();
    }

    private void handleSubtask(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET": {
                if (query == null) {
                    final List<SubTask> subTasks = taskManager.showSubTasks();
                    final String response = gson.toJson(subTasks);
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);
                final Long id = Long.parseLong(idParam);
                final SubTask subTask = taskManager.getSubTaskById(id);
                final String response = gson.toJson(subTask);
                sendText(h, response);
            }
            break;
            case "DELETE": {
                if (query == null) {
                    taskManager.removeAllSubTasks();
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);
                final Long id = Long.parseLong(idParam);
                taskManager.removeSubTaskById(id);
                h.sendResponseHeaders(200, 0);
            }
            break;
            case "POST": {
                String json = readText(h);
                if (json.isEmpty()) {
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final SubTask subTask = gson.fromJson(json, SubTask.class);
                final Long id = subTask.getId();
                if (id != null) {
                    taskManager.updateSubTask(subTask);
                    final String response = gson.toJson(subTask);
                    sendText(h, response);
                } else {
                    Long subTaskId = taskManager.saveSubTask(subTask);
                    subTask.setId(subTaskId);
                    final String response = gson.toJson(subTask);
                    sendText(h, response);
                }
            }
        }
        h.close();
    }

    private void handleEpic(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET": {
                if (query == null) {
                    final List<EpicTask> epicTasks = taskManager.showEpicTasks();
                    final String response = gson.toJson(epicTasks);
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);
                final Long id = Long.parseLong(idParam);
                final EpicTask epic = taskManager.getEpicTaskById(id);
                final String response = gson.toJson(epic);
                sendText(h, response);
            }
            break;
            case "DELETE": {
                if (query == null) {
                    taskManager.removeAllEpicTasks();
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);
                final Long id = Long.parseLong(idParam);
                taskManager.removeEpicTaskById(id);
                h.sendResponseHeaders(200, 0);
            }
            break;
            case "POST": {
                String json = readText(h);
                if (json.isEmpty()) {
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final EpicTask epicTask = gson.fromJson(json, EpicTask.class);
                final Long id = epicTask.getId();
                if (id != null) {
                    taskManager.updateEpicTask(epicTask);
                    final String response = gson.toJson(epicTask);
                    sendText(h, response);
                } else {
                    Long epicTaskId = taskManager.saveEpicTask(epicTask);
                    epicTask.setId(epicTaskId);
                    final String response = gson.toJson(epicTask);
                    sendText(h, response);
                }
            }
        }
        h.close();
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(1);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }
}