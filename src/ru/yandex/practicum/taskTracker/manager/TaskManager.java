package ru.yandex.practicum.taskTracker.manager;

import ru.yandex.practicum.taskTracker.tasks.EpicTask;
import ru.yandex.practicum.taskTracker.tasks.SubTask;
import ru.yandex.practicum.taskTracker.tasks.Task;

import java.io.IOException;
import java.util.List;

public interface TaskManager {

    Long recordSimpleTask(Task task);

    void replaceSimpleTask(Task task);

    List<Task> showSimpleTasks();

    void removeAllSimpleTasks();

    Task getSimpleTaskById(Long simpleTaskId);

    void removeSimpleTaskById(Long simpleTaskId);

    Long recordSubTask(SubTask subTask);

    void replaceSubTask(SubTask subTask);

    List<SubTask> showSubTasks();

    void removeAllSubTasks();

    SubTask getSubTaskById(Long subTaskId);

    void removeSubTaskById(Long subTaskId);

    Long recordEpicTask(EpicTask epicTask);

    void replaceEpicTask(EpicTask epicTask);

    List<EpicTask> showEpicTasks();

    void removeAllEpicTasks() throws IOException;

    EpicTask getEpicTaskById(Long epicTaskId);

    void removeEpicTaskById(Long epicTaskId);

    List<SubTask> showSubTasksOfEpic(Long epicTaskId);
}
