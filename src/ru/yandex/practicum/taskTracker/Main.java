package ru.yandex.practicum.taskTracker;

import ru.yandex.practicum.taskTracker.manager.HistoryManager;
import ru.yandex.practicum.taskTracker.manager.Managers;
import ru.yandex.practicum.taskTracker.manager.TaskManager;
import ru.yandex.practicum.taskTracker.tasks.EpicTask;
import ru.yandex.practicum.taskTracker.tasks.SubTask;
import ru.yandex.practicum.taskTracker.tasks.Task;
import ru.yandex.practicum.taskTracker.tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {

        Managers managers = new Managers();
        TaskManager inMemoryTaskManager = managers.getDefault();
        HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
        long simpleTaskId1 = inMemoryTaskManager.recordSimpleTask(new Task("First SimpleTask", "SimpleTask(ID=1)"));
        long simpleTaskId2 = inMemoryTaskManager.recordSimpleTask(new Task("Second SimpleTask", "DSimpleTask(ID=2)"));
        long epicTaskId3 = inMemoryTaskManager.recordEpicTask(new EpicTask("First EpicTask", "EpicTask(ID=3)"));
        long subTaskId4 = inMemoryTaskManager.recordSubTask(new SubTask("First SubTask", "SubTask(ID=4) of first EpicTask(ID=3)", epicTaskId3));
        long subTaskId5 = inMemoryTaskManager.recordSubTask(new SubTask("Second SubTask", "SubTask(ID=5) of first EpicTask(ID=3)", epicTaskId3));
        long epicTaskId6 = inMemoryTaskManager.recordEpicTask(new EpicTask("Second EpicTask", "EpicTask(ID=6)"));
        long subTaskId7 = inMemoryTaskManager.recordSubTask(new SubTask("Third SubTask", "SubTask(ID=7) of second EpicTask(ID=3)", epicTaskId6));
        System.out.println(inMemoryTaskManager.showSimpleTasks());
        System.out.println(inMemoryTaskManager.showEpicTasks());
        System.out.println(inMemoryTaskManager.showSubTasks());
        System.out.println();

        Task simpleTaskId1ForUpdate = inMemoryTaskManager.getSimpleTaskById(simpleTaskId1);
        simpleTaskId1ForUpdate.setName("New First SimpleTask");
        simpleTaskId1ForUpdate.setDescription("Update SimpleTask(ID=1)");
        simpleTaskId1ForUpdate.setStatus(TaskStatus.DONE);
        inMemoryTaskManager.replaceSimpleTask(simpleTaskId1ForUpdate);

        Task simpleTaskId2ForUpdate = inMemoryTaskManager.getSimpleTaskById(simpleTaskId2);
        simpleTaskId2ForUpdate.setName("New Second SimpleTask");
        simpleTaskId2ForUpdate.setDescription("Update SimpleTask(ID=2)");
        simpleTaskId2ForUpdate.setStatus(TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.replaceSimpleTask(simpleTaskId2ForUpdate);

        EpicTask epicTaskId3ForUpdate = inMemoryTaskManager.getEpicTaskById(epicTaskId3);
        epicTaskId3ForUpdate.setName("New First EpicTask");
        epicTaskId3ForUpdate.setDescription("Update EpicTask(ID=3)");
        inMemoryTaskManager.replaceEpicTask(epicTaskId3ForUpdate);

        SubTask subTaskId4ForUpdate = inMemoryTaskManager.getSubTaskById(subTaskId4);
        subTaskId4ForUpdate.setName("New First SubTask");
        subTaskId4ForUpdate.setDescription("Update SubTask(ID=4) of first EpicTask(ID=3)");
        subTaskId4ForUpdate.setStatus(TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.replaceSubTask(subTaskId4ForUpdate);

        SubTask subTaskId5ForUpdate = inMemoryTaskManager.getSubTaskById(subTaskId5);
        subTaskId5ForUpdate.setName("New Second SubTask");
        subTaskId5ForUpdate.setDescription("Update SubTask(ID=5) of first EpicTask(ID=3)");
        subTaskId5ForUpdate.setStatus(TaskStatus.NEW);
        inMemoryTaskManager.replaceSubTask(subTaskId5ForUpdate);

        EpicTask epicTaskId6ForUpdate = inMemoryTaskManager.getEpicTaskById(epicTaskId6);
        epicTaskId6ForUpdate.setName("New Second EpicTask");
        epicTaskId6ForUpdate.setDescription("Update EpicTask(ID=6)");
        inMemoryTaskManager.replaceEpicTask(epicTaskId6ForUpdate);

        SubTask subTaskId7ForUpdate = inMemoryTaskManager.getSubTaskById(subTaskId7);
        subTaskId7ForUpdate.setName("New Third SubTask");
        subTaskId7ForUpdate.setDescription("Update SubTask(ID=7) of second EpicTask(ID=6)");
        subTaskId7ForUpdate.setStatus(TaskStatus.DONE);
        inMemoryTaskManager.replaceSubTask(subTaskId7ForUpdate);

        System.out.println(inMemoryTaskManager.showSimpleTasks());
        System.out.println(inMemoryTaskManager.showEpicTasks());
        System.out.println(inMemoryTaskManager.showSubTasks());
        System.out.println();
        System.out.println(inMemoryTaskManager.showSubTasksOfEpic(epicTaskId3));
        System.out.println();
        inMemoryTaskManager.removeSimpleTaskById(simpleTaskId1);
        inMemoryTaskManager.removeEpicTaskById(epicTaskId3);
        System.out.println(inMemoryTaskManager.showSimpleTasks());
        System.out.println(inMemoryTaskManager.showEpicTasks());
        System.out.println(inMemoryTaskManager.showSubTasks());
        System.out.println();
        System.out.println(inMemoryHistoryManager.getHistory());
    }
}

