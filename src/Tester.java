/*
public class Tester {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        //методы для SimpleTask
        System.out.println("Simple Tasks");
        taskManager.recordSimpleTask(new Task("First SimpleTask", "Description of First SimpleTask", taskManager.generateID(), "NEW"));
        taskManager.recordSimpleTask(new Task("Second SimpleTask", "Description of Second SimpleTask", taskManager.generateID(), "NEW"));
        taskManager.recordSimpleTask(new Task("Third SimpleTask", "Description of Third SimpleTask", taskManager.generateID(), "NEW"));
        System.out.println(taskManager.simpleTasksList());
        taskManager.replaceSimpleTask(new Task("Third SimpleTaskEdited", "New description of Third SimpleTask", 3, "IN_PROGRESS"));
        taskManager.replaceSimpleTask(new Task("Non-existent SimpleTaskEdited", "New description of non-existent SimpleTask", 100, "DONE"));
        taskManager.simpleTasksList();
        System.out.println(taskManager.simpleTasksList());
        //taskManager.removeAllSimpleTasks();
        taskManager.getSimpleTaskById(1);
        System.out.println(taskManager.getSimpleTaskById(1));
        taskManager.removeSimpleTaskById(1);
        System.out.println(taskManager.simpleTasks);

        //методы для EpicTask
        System.out.println("Epic Tasks");
        taskManager.recordEpicTask(new EpicTask("First EpicTask", "Description of First EpicTask", taskManager.generateID(), "NEW"));
        taskManager.recordEpicTask(new EpicTask("Second EpicTask", "Description of Second EpicTask", taskManager.generateID(), "NEW"));
        taskManager.recordEpicTask(new EpicTask("Third EpicTask", "Description of Third EpicTask", taskManager.generateID(), "NEW"));
        System.out.println(taskManager.epicTasksList());
        taskManager.replaceEpicTask(new EpicTask("Third EpicTaskEdited", "New description of Third EpicTask", 6, "IN_PROGRESS"));
        taskManager.replaceEpicTask(new EpicTask("Non-existent EpicTaskEdited", "New description of non-existent EpicTask", 200, "DONE"));
        taskManager.epicTasksList();
        System.out.println(taskManager.epicTasksList());
        //taskManager.removeAllEpicTasks();
        taskManager.getEpicTaskById(4);
        System.out.println(taskManager.getEpicTaskById(4));
        // taskManager.removeEpicTaskById(4);
        System.out.println(taskManager.epicTasks);

        //методы для subTask
        System.out.println("Sub Tasks");
        taskManager.recordSubTask(new SubTask("First SubTask", "Description of First SubTask", taskManager.generateID(), "NEW", 6));
        taskManager.recordSubTask(new SubTask("Second SubTask", "Description of Second SubTask", taskManager.generateID(), "NEW", 6));
        taskManager.recordSubTask(new SubTask("Third SubTask", "Description of Third SubTask", taskManager.generateID(), "NEW", 6));
        System.out.println(taskManager.subTasksList());
        taskManager.replaceSubTask(new SubTask("Third SubTaskEdited", "New description of Third SubTask", 9, "NEW", 6));
        taskManager.replaceSubTask(new SubTask("Non-existent SubTaskEdited", "New description of non-existent EpicTask", 300, "DONE", 6));
        taskManager.subTasksList();
        System.out.println(taskManager.subTasksList());
        //taskManager.removeAllSubTasks();
        taskManager.getSubTaskById(7);
        System.out.println(taskManager.getSubTaskById(7));
        taskManager.removeSubTaskById(7);
        System.out.println(taskManager.subTasks);

        //методы тестирования статусов EpicTask
        //TestEpiciD = 10, TestSubId = 11, 12, 13
        System.out.println("Status testing");
        taskManager.recordEpicTask(new EpicTask("TestStatus EpicTask", "Made for testing status, got ID=10", taskManager.generateID(), "NEW"));
        System.out.println(taskManager.getEpicTaskById(10).getStatus());
        System.out.println(taskManager.epicTasks);
        System.out.println(taskManager.epicTasks.get(10L));
        System.out.println("Create 3 subtasks with status NEW for EpicTask ID 10");
        taskManager.recordSubTask(new SubTask("First testSub", "Made for testing status, got ID=11 and epicID=10", taskManager.generateID(), "NEW", 10));
        taskManager.recordSubTask(new SubTask("Second testSub", "Made for testing status, got ID=12 and epicID=10", taskManager.generateID(), "NEW", 10));
        taskManager.recordSubTask(new SubTask("Third testSub", "Made for testing status, got ID=13 and epicID=10", taskManager.generateID(), "NEW", 10));
        System.out.println("Status of TestStatus EpicTask is " + taskManager.getEpicTaskById(10).getStatus());
        System.out.println("Change testSub ID=11 status to DONE");
        taskManager.replaceSubTask(new SubTask("First testSub", "Made for testing status, got ID=11 and epicID=10 and now status DONE", 11, "DONE", 10));
        System.out.println("Status of testSub ID=11 is " + taskManager.getSubTaskById(11).getStatus());
        System.out.println("Status of TestStatus EpicTask is " + taskManager.getEpicTaskById(10).getStatus());
        System.out.println("Change testSub ID=12, ID=13 status to DONE");
        taskManager.replaceSubTask(new SubTask("Second testSub", "Made for testing status, got ID=12 and epicID=10 and now status DONE", 12, "DONE", 10));
        taskManager.replaceSubTask(new SubTask("Third testSub", "Made for testing status, got ID=13 and epicID=10 and now status DONE", 13, "DONE", 10));
        System.out.println("Status of testSub ID=11 is " + taskManager.getSubTaskById(11).getStatus());
        System.out.println("Status of testSub ID=12 is " + taskManager.getSubTaskById(12).getStatus());
        System.out.println("Status of testSub ID=13 is " + taskManager.getSubTaskById(13).getStatus());
        System.out.println("Status of TestStatus EpicTask is " + taskManager.getEpicTaskById(10).getStatus());
        //delete subTasksODeletedEpic
        System.out.println("SubTasks list before Epic delete is:");
        System.out.println(taskManager.subTasks);
        System.out.println("Delete EpicTask with ID=10 and show SubTaskList");
        taskManager.removeEpicTaskById(10);
        System.out.println(taskManager.subTasks);
    }

}

*/
