package ru.yandex.practicum.taskTracker;

import com.google.gson.Gson;
import ru.yandex.practicum.taskTracker.http.HttpTaskServer;
import ru.yandex.practicum.taskTracker.http.KVServer;
import ru.yandex.practicum.taskTracker.manager.Managers;
import ru.yandex.practicum.taskTracker.manager.TaskManager;

import java.io.IOException;
import java.net.http.HttpClient;

public class Main {

    static TaskManager taskManager;
    static HttpClient client;
    static Gson gson;

    public static void main(String[] args) throws IOException {
        new KVServer().start();
        new HttpTaskServer().start();

        taskManager = Managers.getDefault();
        client = HttpClient.newHttpClient();
        gson = Managers.getGson();
    }

}