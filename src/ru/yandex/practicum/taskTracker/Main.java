package ru.yandex.practicum.taskTracker;

import ru.yandex.practicum.taskTracker.manager.HistoryManager;
import ru.yandex.practicum.taskTracker.manager.Managers;
import ru.yandex.practicum.taskTracker.manager.TaskManager;
import ru.yandex.practicum.taskTracker.tasks.EpicTask;
import ru.yandex.practicum.taskTracker.tasks.SubTask;
import ru.yandex.practicum.taskTracker.tasks.Task;

public class Main {

    public static void main(String[] args) {

        Managers managers = new Managers();
        TaskManager inMemoryTaskManager = managers.getDefault();
        HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
        Long simpleTaskId1 = inMemoryTaskManager.recordSimpleTask(new Task("First SimpleTask", "SimpleTask(ID=1)"));
        Long simpleTaskId2 = inMemoryTaskManager.recordSimpleTask(new Task("Second SimpleTask", "DSimpleTask(ID=2)"));
        Long epicTaskId3 = inMemoryTaskManager.recordEpicTask(new EpicTask("First EpicTask", "EpicTask(ID=3)"));
        Long subTaskId4 = inMemoryTaskManager.recordSubTask(new SubTask("First SubTask", "SubTask(ID=4) of first EpicTask(ID=3)", epicTaskId3));
        Long subTaskId5 = inMemoryTaskManager.recordSubTask(new SubTask("Second SubTask", "SubTask(ID=5) of first EpicTask(ID=3)", epicTaskId3));
        Long subTaskId6 = inMemoryTaskManager.recordSubTask(new SubTask("Third SubTask", "SubTask(ID=6) of first EpicTask(ID=3)", epicTaskId3));
        Long epicTaskId7 = inMemoryTaskManager.recordEpicTask(new EpicTask("Second EpicTask", "EpicTask(ID=7)"));

        inMemoryTaskManager.getSimpleTaskById(1L);
        inMemoryTaskManager.getSimpleTaskById(2L);
        inMemoryTaskManager.getEpicTaskById(3L);
        inMemoryTaskManager.getSubTaskById(4L);
        inMemoryTaskManager.getSubTaskById(5L);
        inMemoryTaskManager.getSubTaskById(6L);
        inMemoryTaskManager.getEpicTaskById(7L);
        System.out.println(inMemoryHistoryManager.getHistory());
        System.out.println();

        inMemoryTaskManager.getEpicTaskById(7L);
        inMemoryTaskManager.getSubTaskById(6L);
        inMemoryTaskManager.getSubTaskById(5L);
        inMemoryTaskManager.getSubTaskById(4L);
        inMemoryTaskManager.getEpicTaskById(3L);
        inMemoryTaskManager.getSimpleTaskById(2L);
        inMemoryTaskManager.getSimpleTaskById(1L);
        System.out.println(inMemoryHistoryManager.getHistory());
        System.out.println();

        inMemoryTaskManager.getSubTaskById(4L);
        inMemoryTaskManager.getSimpleTaskById(1L);
        inMemoryTaskManager.getEpicTaskById(7L);
        inMemoryTaskManager.getSimpleTaskById(2L);
        inMemoryTaskManager.getSubTaskById(6L);
        inMemoryTaskManager.getEpicTaskById(3L);
        inMemoryTaskManager.getSubTaskById(5L);
        System.out.println(inMemoryHistoryManager.getHistory());
        System.out.println();

        inMemoryTaskManager.removeSimpleTaskById(1L);
        inMemoryTaskManager.removeEpicTaskById(3L);
        System.out.println(inMemoryHistoryManager.getHistory());
        System.out.println();
    }
}

