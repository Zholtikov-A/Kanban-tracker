package ru.yandex.practicum.taskTracker.manager;

import ru.yandex.practicum.taskTracker.tasks.EpicTask;
import ru.yandex.practicum.taskTracker.tasks.SubTask;
import ru.yandex.practicum.taskTracker.tasks.Task;

import java.util.ArrayList;

public interface TaskManager {

    Long recordSimpleTask(Task task);

    void replaceSimpleTask(Task task);

    ArrayList<Task> showSimpleTasks();

    void removeAllSimpleTasks();

    Task getSimpleTaskById(Long simpleTaskId);

    void removeSimpleTaskById(Long simpleTaskId);

    Long recordSubTask(SubTask subTask);

    void replaceSubTask(SubTask subTask);

    ArrayList<SubTask> showSubTasks();

    void removeAllSubTasks();

    SubTask getSubTaskById(Long subTaskId);

    void removeSubTaskById(Long subTaskId);

    //    void checkEpicStatus(Long epicTaskId);
    Long recordEpicTask(EpicTask epicTask);

    void replaceEpicTask(EpicTask epicTask);

    ArrayList<EpicTask> showEpicTasks();

    void removeAllEpicTasks();

    EpicTask getEpicTaskById(Long epicTaskId);

    void removeEpicTaskById(Long epicTaskId);

    ArrayList<SubTask> showSubTasksOfEpic(Long epicTaskId);
}
