public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.recordSimpleTask(new Task("First SimpleTask", "SimpleTask(ID=1)"));
        taskManager.recordSimpleTask(new Task("Second SimpleTask", "DSimpleTask(ID=2)"));
        taskManager.recordEpicTask(new EpicTask("First EpicTask", "EpicTask(ID=3)"));
        taskManager.recordSubTask(new SubTask("First SubTask", "SubTask(ID=4) of first EpicTask(ID=3)", 3));
        taskManager.recordSubTask(new SubTask("Second SubTask", "SubTask(ID=5) of first EpicTask(ID=3)", 3));
        taskManager.recordEpicTask(new EpicTask("Second EpicTask", "EpicTask(ID=6)"));
        taskManager.recordSubTask(new SubTask("Third SubTask", "SubTask(ID=7) of second EpicTask(ID=3)", 6));
        System.out.println(taskManager.simpleTasksList());
        System.out.println(taskManager.epicTasksList());
        System.out.println(taskManager.subTasksList());
        System.out.println();
        taskManager.replaceSimpleTask(new Task("First SimpleTask", "Update SimpleTask(ID=1)", 1, "DONE"));
        taskManager.replaceSimpleTask(new Task("Second SimpleTask", "Update SimpleTask(ID=2)", 2, "IN_PROGRESS"));
        taskManager.replaceEpicTask(new EpicTask("First EpicTask", "Update EpicTask(ID=3)", 3));
        taskManager.replaceSubTask(new SubTask("First SubTask", "Update SubTask(ID=4) of first EpicTask(ID=3) ", 4, "DONE", 3));
        taskManager.replaceSubTask(new SubTask("Second SubTask", "Update SubTask(ID=5) of first EpicTask(ID=3) ", 5, "NEW", 3));
        taskManager.replaceEpicTask(new EpicTask("Second EpicTask", "Update EpicTask(ID=6)", 6));
        taskManager.replaceSubTask(new SubTask("Third SubTask", "Update SubTask(ID=7) of second EpicTask(ID=6)", 7, "IN_PROGRESS", 6));
        System.out.println(taskManager.simpleTasksList());
        System.out.println(taskManager.epicTasksList());
        System.out.println(taskManager.subTasksList());
        System.out.println();
        taskManager.removeSimpleTaskById(1);
        taskManager.removeEpicTaskById(3);
        System.out.println(taskManager.simpleTasksList());
        System.out.println(taskManager.epicTasksList());
        System.out.println(taskManager.subTasksList());
    }
}

