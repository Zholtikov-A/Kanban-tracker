package ru.yandex.practicum.taskTracker.manager;

import ru.yandex.practicum.taskTracker.tasks.EpicTask;
import ru.yandex.practicum.taskTracker.tasks.SubTask;
import ru.yandex.practicum.taskTracker.tasks.Task;

import java.util.ArrayList;

public interface TaskManager {

    long recordSimpleTask(Task task);

    void replaceSimpleTask(Task task);

    ArrayList<Task> showSimpleTasks();

    void removeAllSimpleTasks();

    Task getSimpleTaskById(long simpleTaskId);

    void removeSimpleTaskById(long simpleTaskId);

    long recordSubTask(SubTask subTask);

    void replaceSubTask(SubTask subTask);

    ArrayList<SubTask> showSubTasks();

    void removeAllSubTasks();

    SubTask getSubTaskById(long subTaskId);

    void removeSubTaskById(long subTaskId);

    //    void checkEpicStatus(long epicTaskId);
    long recordEpicTask(EpicTask epicTask);

    void replaceEpicTask(EpicTask epicTask);

    ArrayList<EpicTask> showEpicTasks();

    void removeAllEpicTasks();

    EpicTask getEpicTaskById(long epicTaskId);

    void removeEpicTaskById(long epicTaskId);

    ArrayList<SubTask> showSubTasksOfEpic(long epicTaskId);
}
