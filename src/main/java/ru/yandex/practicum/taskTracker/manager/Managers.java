package ru.yandex.practicum.taskTracker.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.practicum.taskTracker.http.KVServer;

import java.io.IOException;

public class Managers {

    //private static HistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


    public static HttpTaskManager getDefaultHttp() {
        return new HttpTaskManager(KVServer.PORT);
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }
}
