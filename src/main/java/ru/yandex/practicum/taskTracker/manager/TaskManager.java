package ru.yandex.practicum.taskTracker.manager;

import ru.yandex.practicum.taskTracker.tasks.EpicTask;
import ru.yandex.practicum.taskTracker.tasks.SubTask;
import ru.yandex.practicum.taskTracker.tasks.Task;

import java.util.List;

public interface TaskManager {

    Long saveSimpleTask(Task task);

    void updateSimpleTask(Task task);

    List<Task> showSimpleTasks();

    void removeAllSimpleTasks();

    Task getSimpleTaskById(Long simpleTaskId);

    void removeSimpleTaskById(Long simpleTaskId);

    Long saveSubTask(SubTask subTask);

    void updateSubTask(SubTask subTask);

    List<SubTask> showSubTasks();

    void removeAllSubTasks();

    SubTask getSubTaskById(Long subTaskId);

    void removeSubTaskById(Long subTaskId);

    Long saveEpicTask(EpicTask epicTask);

    void updateEpicTask(EpicTask epicTask);

    List<EpicTask> showEpicTasks();

    void removeAllEpicTasks();

    EpicTask getEpicTaskById(Long epicTaskId);

    void removeEpicTaskById(Long epicTaskId);

    List<SubTask> showSubTasksOfEpic(Long epicTaskId);

    List<Task> getPrioritizedTasks();

    List<Task> getHistory();
}
