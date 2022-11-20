package ru.yandex.practicum.taskTracker;

import ru.yandex.practicum.taskTracker.manager.TaskManager;
import ru.yandex.practicum.taskTracker.tasks.EpicTask;
import ru.yandex.practicum.taskTracker.tasks.SubTask;
import ru.yandex.practicum.taskTracker.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        //тут много длинных строк, как правильнее разбить строки? По запятой между name и description?
        long simpleTaskId1 = taskManager.recordSimpleTask(new Task("First SimpleTask", "SimpleTask(ID=1)"));
        long simpleTaskId2 = taskManager.recordSimpleTask(new Task("Second SimpleTask", "DSimpleTask(ID=2)"));
        long epicTaskId3 = taskManager.recordEpicTask(new EpicTask("First EpicTask", "EpicTask(ID=3)"));
        long subTaskId4 = taskManager.recordSubTask(new SubTask("First SubTask", "SubTask(ID=4) of first EpicTask(ID=3)", epicTaskId3));
        long subTaskId5 = taskManager.recordSubTask(new SubTask("Second SubTask", "SubTask(ID=5) of first EpicTask(ID=3)", epicTaskId3));
        long epicTaskId6 = taskManager.recordEpicTask(new EpicTask("Second EpicTask", "EpicTask(ID=6)"));
        long subTaskId7 = taskManager.recordSubTask(new SubTask("Third SubTask", "SubTask(ID=7) of second EpicTask(ID=3)", epicTaskId6));
        System.out.println(taskManager.showSimpleTasks());
        System.out.println(taskManager.showEpicTasks());
        System.out.println(taskManager.showSubTasks());
        System.out.println();

        Task simpleTaskId1ForUpdate = taskManager.getSimpleTaskById(simpleTaskId1);
        simpleTaskId1ForUpdate.setName("New First SimpleTask");
        simpleTaskId1ForUpdate.setDescription("Update SimpleTask(ID=1)");
        simpleTaskId1ForUpdate.setStatus("DONE");
        taskManager.replaceSimpleTask(simpleTaskId1ForUpdate);

        Task simpleTaskId2ForUpdate = taskManager.getSimpleTaskById(simpleTaskId2);
        simpleTaskId2ForUpdate.setName("New Second SimpleTask");
        simpleTaskId2ForUpdate.setDescription("Update SimpleTask(ID=2)");
        simpleTaskId2ForUpdate.setStatus("IN_PROGRESS");
        taskManager.replaceSimpleTask(simpleTaskId2ForUpdate);

        EpicTask epicTaskId3ForUpdate = taskManager.getEpicTaskById(epicTaskId3);
        epicTaskId3ForUpdate.setName("New First EpicTask");
        epicTaskId3ForUpdate.setDescription("Update EpicTask(ID=3)");
        taskManager.replaceEpicTask(epicTaskId3ForUpdate);

        SubTask subTaskId4ForUpdate = taskManager.getSubTaskById(subTaskId4);
        subTaskId4ForUpdate.setName("New First SubTask");
        subTaskId4ForUpdate.setDescription("Update SubTask(ID=4) of first EpicTask(ID=3)");
        subTaskId4ForUpdate.setStatus("IN_PROGRESS");
        taskManager.replaceSubTask(subTaskId4ForUpdate);

        SubTask subTaskId5ForUpdate = taskManager.getSubTaskById(subTaskId5);
        subTaskId5ForUpdate.setName("New Second SubTask");
        subTaskId5ForUpdate.setDescription("Update SubTask(ID=5) of first EpicTask(ID=3)");
        subTaskId5ForUpdate.setStatus("NEW");
        taskManager.replaceSubTask(subTaskId5ForUpdate);

        EpicTask epicTaskId6ForUpdate = taskManager.getEpicTaskById(epicTaskId6);
        epicTaskId6ForUpdate.setName("New Second EpicTask");
        epicTaskId6ForUpdate.setDescription("Update EpicTask(ID=6)");
        taskManager.replaceEpicTask(epicTaskId6ForUpdate);

        SubTask subTaskId7ForUpdate = taskManager.getSubTaskById(subTaskId7);
        subTaskId7ForUpdate.setName("New Third SubTask");
        subTaskId7ForUpdate.setDescription("Update SubTask(ID=7) of second EpicTask(ID=6)");
        subTaskId7ForUpdate.setStatus("DONE");
        taskManager.replaceSubTask(subTaskId7ForUpdate);

        System.out.println(taskManager.showSimpleTasks());
        System.out.println(taskManager.showEpicTasks());
        System.out.println(taskManager.showSubTasks());
        System.out.println();
        System.out.println(taskManager.showSubTasksOfEpic(epicTaskId3));
        System.out.println();
        taskManager.removeSimpleTaskById(simpleTaskId1);
        taskManager.removeEpicTaskById(epicTaskId3);
        System.out.println(taskManager.showSimpleTasks());
        System.out.println(taskManager.showEpicTasks());
        System.out.println(taskManager.showSubTasks());
    }
}

