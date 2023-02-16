package ru.yandex.practicum.taskTracker.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskTracker.manager.HttpTaskManager;
import ru.yandex.practicum.taskTracker.manager.Managers;
import ru.yandex.practicum.taskTracker.tasks.EpicTask;
import ru.yandex.practicum.taskTracker.tasks.SubTask;
import ru.yandex.practicum.taskTracker.tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KVTaskClientTest {
    static KVServer kvServer;
    static KVTaskClient kvTaskClient;
    static HttpTaskManager taskManager;
    static Gson gson;
    static LocalDateTime testStartTime1;
    static LocalDateTime testStartTime1Plus15m;
    static Duration duration10H;
    static LocalDateTime testStartTime2;
    static LocalDateTime testStartTime2Plus15m;
    static Duration duration2H;
    static LocalDateTime testStartTime3;
    static LocalDateTime testStartTime3Minus15m;
    static Duration duration5H;
    static Long simpleTaskId1;
    static Long simpleTaskId2;
    static Long epicTaskId3;
    static Long subTaskId4;
    static Long subTaskId5;
    static Long epicTaskId6;
    static Long subTaskId7;

    static void createTask() {
        testStartTime1 = LocalDateTime.of(2023, Month.MARCH, 5, 14, 0, 0);
        testStartTime1Plus15m = testStartTime1.plusMinutes(15);
        duration10H = Duration.ofHours(10);
        testStartTime2 = LocalDateTime.of(2023, Month.MARCH, 1, 18, 30, 0);
        testStartTime2Plus15m = testStartTime2.plusMinutes(15);
        duration2H = Duration.ofHours(2);
        testStartTime3 = LocalDateTime.of(2023, Month.MARCH, 3, 10, 0, 0);
        testStartTime3Minus15m = testStartTime3.minusMinutes(15);
        duration5H = Duration.ofHours(5);

        simpleTaskId1 = taskManager.saveSimpleTask(
                new Task("First SimpleTask", "SimpleTask(ID=1) without DateTime"));
        simpleTaskId2 = taskManager.saveSimpleTask(
                new Task("Second SimpleTask", "SimpleTask(ID=2) with DateTime", testStartTime1, duration10H));
        epicTaskId3 = taskManager.saveEpicTask(
                new EpicTask("First EpicTask", "EpicTask(ID=3) with DateTime"));
        subTaskId4 = taskManager.saveSubTask(
                new SubTask("First SubTask", "SubTask(ID=4) of first EpicTask(ID=3) with DateTime",
                        testStartTime2, duration2H, epicTaskId3));
        subTaskId5 = taskManager.saveSubTask(
                new SubTask("Second SubTask", "SubTask(ID=5) of first EpicTask(ID=3) with DateTime",
                        testStartTime3, duration5H, epicTaskId3));
        epicTaskId6 = taskManager.saveEpicTask(
                new EpicTask("Second EpicTask", "EpicTask(ID=6 without DateTime)"));
        subTaskId7 = taskManager.saveSubTask(
                new SubTask("Third SubTask", "SubTask(ID=7) of first EpicTask(ID=6) without DateTime", epicTaskId6));
    }

    @BeforeAll
    static void init() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        gson = Managers.getGson();
        taskManager = Managers.getDefaultHttp();
        kvTaskClient = taskManager.getClient();
        createTask();
    }

    @AfterAll
    static void stopServers() {
        kvServer.stop();
    }

    @Test
    void dataOnServerShouldEqualDataInMemory() {
        taskManager.getSimpleTaskById(simpleTaskId1);
        taskManager.getEpicTaskById(epicTaskId3);
        taskManager.getSubTaskById(subTaskId4);

        ArrayList<Task> tasks = gson.fromJson(kvTaskClient.load("tasks"),
                new TypeToken<ArrayList<Task>>() {
                }.getType());
        ArrayList<SubTask> subtasks = gson.fromJson(kvTaskClient.load("subtasks"),
                new TypeToken<ArrayList<SubTask>>() {
                }.getType());
        ArrayList<EpicTask> epics = gson.fromJson(kvTaskClient.load("epics"),
                new TypeToken<ArrayList<EpicTask>>() {
                }.getType());
        ArrayList<Long> historyIds = gson.fromJson(kvTaskClient.load("history"),
                new TypeToken<ArrayList<Long>>() {
                }.getType());
        List<Task> historyTasks = new ArrayList<>();
        for (Long id : historyIds) {
            if (taskManager.findTask(id) != null) {
                historyTasks.add(taskManager.findTask(id));
            }
        }

        assertEquals(tasks, taskManager.showSimpleTasks(), "Коллекции tasks не равны!");
        assertEquals(subtasks, taskManager.showSubTasks(), "Коллекции subTasks не равны!");
        assertEquals(epics, taskManager.showEpicTasks(), "Коллекции epicTasks не равны!");
        assertEquals(historyTasks, taskManager.getHistory(), "Истории не равны!");
    }

    @Test
    void dataOnServerShouldEqualDataInMemoryAfterUpdatingTasks() {
        ArrayList<Task> tasksOld = gson.fromJson(kvTaskClient.load("tasks"),
                new TypeToken<ArrayList<Task>>() {
                }.getType());
        ArrayList<SubTask> subtasksOld = gson.fromJson(kvTaskClient.load("subtasks"),
                new TypeToken<ArrayList<SubTask>>() {
                }.getType());
        ArrayList<EpicTask> epicsOld = gson.fromJson(kvTaskClient.load("epics"),
                new TypeToken<ArrayList<EpicTask>>() {
                }.getType());
        ArrayList<Long> historyIdsOld = gson.fromJson(kvTaskClient.load("history"),
                new TypeToken<ArrayList<Long>>() {
                }.getType());
        List<Task> historyTasksOld = new ArrayList<>();
        for (Long id : historyIdsOld) {
            if (taskManager.findTask(id) != null) {
                historyTasksOld.add(taskManager.findTask(id));
            }
        }

        Task taskOld = taskManager.getSimpleTaskById(simpleTaskId1);
        Task taskForUpdate = new Task(simpleTaskId1, taskOld.getType(), "New name(simpleTaskId1)",
                taskOld.getStatus(), "New description");
        taskManager.updateSimpleTask(taskForUpdate);
        SubTask subTaskOld = taskManager.getSubTaskById(subTaskId7);
        SubTask subTaskForUpdate = new SubTask(subTaskId7, subTaskOld.getType(), "New name(subTaskId5)",
                subTaskOld.getStatus(), "New description", epicTaskId6);
        taskManager.updateSubTask(subTaskForUpdate);
        EpicTask epicTaskOld = taskManager.getEpicTaskById(epicTaskId6);
        EpicTask epicTaskForUpdate = new EpicTask(epicTaskId6, epicTaskOld.getType(), "New name (epicTaskId3)",
                epicTaskOld.getStatus(), "New description");
        taskManager.updateEpicTask(epicTaskForUpdate);

        ArrayList<Task> tasks = gson.fromJson(kvTaskClient.load("tasks"),
                new TypeToken<ArrayList<Task>>() {
                }.getType());
        ArrayList<SubTask> subtasks = gson.fromJson(kvTaskClient.load("subtasks"),
                new TypeToken<ArrayList<SubTask>>() {
                }.getType());
        ArrayList<EpicTask> epics = gson.fromJson(kvTaskClient.load("epics"),
                new TypeToken<ArrayList<EpicTask>>() {
                }.getType());
        ArrayList<Long> historyIds = gson.fromJson(kvTaskClient.load("history"),
                new TypeToken<ArrayList<Long>>() {
                }.getType());
        List<Task> historyTasks = new ArrayList<>();
        for (Long id : historyIds) {
            if (taskManager.findTask(id) != null) {
                historyTasks.add(taskManager.findTask(id));
            }
        }

        assertEquals(tasks, taskManager.showSimpleTasks(), "Коллекции tasks не равны!");
        assertEquals(subtasks, taskManager.showSubTasks(), "Коллекции subTasks не равны!");
        assertEquals(epics, taskManager.showEpicTasks(), "Коллекции epicTasks не равны!");
        assertEquals(historyTasks, taskManager.getHistory(), "Истории не равны!");

        assertNotEquals(tasksOld, taskManager.showSimpleTasks(), "Хранятся старые tasks!");
        assertNotEquals(subtasksOld, taskManager.showSubTasks(), "Хранятся старые subTasks!");
        assertNotEquals(epicsOld, taskManager.showEpicTasks(), "Хранятся старые epicTasks!");
        assertNotEquals(historyTasksOld, taskManager.getHistory(), "Хранится старая история!");
    }
}

