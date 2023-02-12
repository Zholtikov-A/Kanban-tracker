package ru.yandex.practicum.taskTracker.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.taskTracker.http.KVTaskClient;
import ru.yandex.practicum.taskTracker.tasks.EpicTask;
import ru.yandex.practicum.taskTracker.tasks.SubTask;
import ru.yandex.practicum.taskTracker.tasks.Task;
import ru.yandex.practicum.taskTracker.tasks.TaskType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTaskManager {
    private final Gson gson;
    private final KVTaskClient client;

    public HttpTaskManager(int port) {
        this(port, false);
    }

    public HttpTaskManager(int port, boolean load) {
        super((File) null);
        gson = Managers.getGson();
        client = new KVTaskClient("http://localhost:" + port + "/");
        if (load) {
            load();
        }
    }

    protected void addTasks(List<? extends Task> tasks) {
        for (Task task : tasks) {
            final Long id = task.getId();
            if (id > newTaskId) {
                newTaskId = id;
            }
            TaskType type = task.getType();
            if (type == TaskType.TASK) {
                this.simpleTasks.put(id, task);
                if (task.getStartTime() != null) {
                    priority.add(task);
                }
            } else if (type == TaskType.SUBTASK) {
                subTasks.put(id, (SubTask) task);
                if (task.getStartTime() != null) {
                    priority.add(task);
                }
            } else if (type == TaskType.EPIC) {
                epicTasks.put(id, (EpicTask) task);
                if (task.getStartTime() != null) {
                    priority.add(task);
                }
            }
        }
    }

    private void load() {
        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"),
                new TypeToken<ArrayList<Task>>() {
                }.getType());
        addTasks(tasks);

        ArrayList<EpicTask> epics = gson.fromJson(client.load("epics"),
                new TypeToken<ArrayList<EpicTask>>() {
                }.getType());
        addTasks(epics);

        ArrayList<SubTask> subtasks = gson.fromJson(client.load("subtasks"),
                new TypeToken<ArrayList<SubTask>>() {
                }.getType());
        addTasks(subtasks);

        List<Long> history = gson.fromJson(client.load("history"),
                new TypeToken<ArrayList<Long>>() {
                }.getType());
        for (Long taskId : history) {
            getInMemoryHistoryManager().add(findTask(taskId));
        }
    }

    @Override
    protected void save() {
        String jsonTasks = gson.toJson(new ArrayList<>(simpleTasks.values()));
        client.put("tasks", jsonTasks);

        String jsonSubtasks = gson.toJson(new ArrayList<>(subTasks.values()));
        client.put("subtasks", jsonSubtasks);

        String jsonEpicTasks = gson.toJson(new ArrayList<>(epicTasks.values()));
        client.put("epics", jsonEpicTasks);

        String jsonHistory = gson.toJson(getInMemoryHistoryManager().getHistory()
                .stream().map(Task::getId).collect(Collectors.toList()));
        client.put("history", jsonHistory);
    }

    public KVTaskClient getClient() {
        return client;
    }

}
