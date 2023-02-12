package ru.yandex.practicum.taskTracker;

import com.google.gson.Gson;

import ru.yandex.practicum.taskTracker.http.HttpTaskServer;
import ru.yandex.practicum.taskTracker.http.KVServer;
import ru.yandex.practicum.taskTracker.manager.HttpTaskManager;
import ru.yandex.practicum.taskTracker.manager.Managers;
import ru.yandex.practicum.taskTracker.manager.TaskManager;
import ru.yandex.practicum.taskTracker.tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {

    static TaskManager taskManager;
    static HttpClient client;

    public static void main(String[] args) throws IOException, InterruptedException {
        new KVServer().start();
        new HttpTaskServer().start();

        taskManager = new HttpTaskManager(8080);
        client = HttpClient.newHttpClient();

               getAllTasksTest();
        createSimpleTaskTest();
        getSimpleTaskByIdTest();
    }

    public static void getAllTasksTest() throws IOException, InterruptedException {
                URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void createSimpleTaskTest() throws IOException, InterruptedException {
              URI url = URI.create("http://localhost:8080/tasks/task");
        Gson gson = Managers.getGson();
        Long newTaskId = taskManager.saveSimpleTask(
                new Task("Second SimpleTask", "SimpleTask(ID=2) with DateTime", LocalDateTime.of(2023, Month.MARCH, 5, 14, 0, 0), Duration.ofMinutes(15)));
        Task newTask = taskManager.getSimpleTaskById(newTaskId);
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void getSimpleTaskByIdTest() throws IOException, InterruptedException {
               URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}