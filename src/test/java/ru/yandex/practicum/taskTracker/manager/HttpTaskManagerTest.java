package ru.yandex.practicum.taskTracker.manager;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.taskTracker.http.HttpTaskServer;
import ru.yandex.practicum.taskTracker.http.KVServer;
import ru.yandex.practicum.taskTracker.tasks.EpicTask;
import ru.yandex.practicum.taskTracker.tasks.SubTask;
import ru.yandex.practicum.taskTracker.tasks.Task;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    static KVServer kvServer;
    static HttpTaskServer httpTaskServer;

    @BeforeEach
    public void setUp() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        taskManager = Managers.getDefaultHttp();
        createTask();
    }

    @AfterEach
    public void stopServers() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    void restoreManagerFromFilledServer() {
        taskManager.getSimpleTaskById(simpleTaskId1);
        taskManager.getEpicTaskById(epicTaskId3);
        taskManager.getSubTaskById(subTaskId4);

        taskManager.removeSimpleTaskById(simpleTaskId2);
        taskManager.removeSubTaskById(subTaskId5);
        taskManager.removeEpicTaskById(epicTaskId6);

        Set<Task> historySet = new HashSet<>(taskManager.getHistory());
        Assertions.assertEquals(historySet.size(), taskManager.getHistory().size());

        HttpTaskManager httpTaskManagerRestored = new HttpTaskManager(8078, true);

        List<Task> simpleTaskList = taskManager.showSimpleTasks();
        List<Task> simpleTaskListRestored = httpTaskManagerRestored.showSimpleTasks();
        Assertions.assertEquals(simpleTaskList, simpleTaskListRestored);

        List<EpicTask> epicTaskList = taskManager.showEpicTasks();
        List<EpicTask> epicTaskListRestored = httpTaskManagerRestored.showEpicTasks();
        Assertions.assertEquals(epicTaskList, epicTaskListRestored);

        List<SubTask> subTaskList = taskManager.showSubTasks();
        List<SubTask> subTaskListRestored = httpTaskManagerRestored.showSubTasks();
        Assertions.assertEquals(subTaskList, subTaskListRestored);

        List<Task> history = taskManager.getHistory();
        List<Task> historyRestored = httpTaskManagerRestored.getHistory();
        Assertions.assertEquals(history, historyRestored);

        List<Task> priorityList = taskManager.getPrioritizedTasks();
        List<Task> priorityListRestored = httpTaskManagerRestored.getPrioritizedTasks();
        Assertions.assertEquals(priorityList, priorityListRestored);
    }

    @Test
    public void restoreManagerFromEmptyServer() {
        HttpTaskManager httpTaskManager = new HttpTaskManager(8078, false);

        HttpTaskManager httpTaskManagerRestored = new HttpTaskManager(8078, true);

        List<Task> simpleTaskList = httpTaskManager.showSimpleTasks();
        List<Task> simpleTaskListRestored = httpTaskManagerRestored.showSimpleTasks();
        Assertions.assertEquals(simpleTaskList, simpleTaskListRestored);

        List<Task> epicTaskList = httpTaskManager.showSimpleTasks();
        List<Task> epicTaskListRestored = httpTaskManagerRestored.showSimpleTasks();
        Assertions.assertEquals(epicTaskList, epicTaskListRestored);

        List<Task> subTaskList = httpTaskManager.showSimpleTasks();
        List<Task> subTaskListRestored = httpTaskManagerRestored.showSimpleTasks();
        Assertions.assertEquals(subTaskList, subTaskListRestored);

        List<Task> history = httpTaskManager.getHistory();
        List<Task> historyRestored = httpTaskManagerRestored.getHistory();
        Assertions.assertEquals(0, history.size());
        Assertions.assertEquals(0, historyRestored.size());
    }

}