public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.recordSimpleTask(new Task("First SimpleTask", "SimpleTask(ID=1)"));
        taskManager.recordSimpleTask(new Task("Second SimpleTask", "DSimpleTask(ID=2)"));
        taskManager.recordEpicTask(new EpicTask("First EpicTask", "EpicTask(ID=3)"));
        taskManager.recordSubTask(new SubTask("First SubTask", "SubTask(ID=4) of first EpicTask(ID=3)", 3L));
        taskManager.recordSubTask(new SubTask("Second SubTask", "SubTask(ID=5) of first EpicTask(ID=3)", 3L));
        taskManager.recordEpicTask(new EpicTask("Second EpicTask", "EpicTask(ID=6)"));
        taskManager.recordSubTask(new SubTask("Third SubTask", "SubTask(ID=7) of second EpicTask(ID=3)", 6L));
        System.out.println(taskManager.showSimpleTasks());
        System.out.println(taskManager.showEpicTasks());
        System.out.println(taskManager.showSubTasks());
        System.out.println();
        taskManager.replaceSimpleTask(new Task("First SimpleTask", "Update SimpleTask(ID=1)"), 1L, "DONE");
        taskManager.replaceSimpleTask(new Task("Second SimpleTask", "Update SimpleTask(ID=2)"), 2L, "IN_PROGRESS");
        taskManager.replaceEpicTask(new EpicTask("First EpicTask", "Update EpicTask(ID=3)"), 3L);
        taskManager.replaceSubTask(new SubTask("First SubTask", "Update SubTask(ID=4) of first EpicTask(ID=3)", 3L), 4L, "IN_PROGRESS");
        taskManager.replaceSubTask(new SubTask("Second SubTask", "Update SubTask(ID=5) of first EpicTask(ID=3) ", 3L), 5L, "NEW");
        taskManager.replaceEpicTask(new EpicTask("Second EpicTask", "Update EpicTask(ID=6)"), 6L);
        taskManager.replaceSubTask(new SubTask("Third SubTask", "Update SubTask(ID=7) of second EpicTask(ID=6)", 6L), 7L, "DONE");
        System.out.println(taskManager.showSimpleTasks());
        System.out.println(taskManager.showEpicTasks());
        System.out.println(taskManager.showSubTasks());
        System.out.println();
        taskManager.removeSimpleTaskById(1L);
        taskManager.removeEpicTaskById(3L);
        System.out.println(taskManager.showSimpleTasks());
        System.out.println(taskManager.showEpicTasks());
        System.out.println(taskManager.showSubTasks());
    }
}

