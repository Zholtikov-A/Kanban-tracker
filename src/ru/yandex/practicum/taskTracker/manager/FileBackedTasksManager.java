package ru.yandex.practicum.taskTracker.manager;

import ru.yandex.practicum.taskTracker.exceptions.ManagerSaveException;
import ru.yandex.practicum.taskTracker.tasks.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public Long recordSimpleTask(Task task) {
        super.recordSimpleTask(task);
        save();
        return task.getId();
    }

    @Override
    public void replaceSimpleTask(Task task) {
        super.replaceSimpleTask(task);
        save();
    }

    @Override
    public void removeAllSimpleTasks() {
        super.removeAllSimpleTasks();
        save();
    }

    @Override
    public Task getSimpleTaskById(Long simpleTaskId) {
        try {
            return super.getSimpleTaskById(simpleTaskId);
        } finally {
            save();
        }
    }

    @Override
    public void removeSimpleTaskById(Long simpleTaskId) {
        super.removeSimpleTaskById(simpleTaskId);
        save();
    }

    @Override
    public Long recordSubTask(SubTask subTask) {
        super.recordSubTask(subTask);
        save();
        return subTask.getId();
    }

    @Override
    public void replaceSubTask(SubTask subTask) {
        super.replaceSubTask(subTask);
        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    @Override
    public SubTask getSubTaskById(Long subTaskId) {
        try {
            return super.getSubTaskById(subTaskId);
        } finally {
            save();
        }
    }

    @Override
    public void removeSubTaskById(Long subTaskId) {
        super.removeSubTaskById(subTaskId);
        save();
    }

    @Override
    public Long recordEpicTask(EpicTask epicTask) {
        super.recordEpicTask(epicTask);
        save();
        return epicTask.getId();
    }

    @Override
    public void replaceEpicTask(EpicTask epicTask) {
        super.replaceEpicTask(epicTask);
        save();
    }

    @Override
    public void removeAllEpicTasks() throws IOException {
        super.removeAllEpicTasks();
        save();
    }

    @Override
    public EpicTask getEpicTaskById(Long epicTaskId) {
        try {
            return super.getEpicTaskById(epicTaskId);
        } finally {
            save();
        }
    }

    @Override
    public void removeEpicTaskById(Long epicTaskId) {
        super.removeEpicTaskById(epicTaskId);
        save();
    }

    public void save() {
        try (Writer fileWriter = new FileWriter("src/ru/yandex/practicum/taskTracker/dataStorage/FileManager.csv")) {
            fileWriter.write("id,type,name,status,description,epic" + "\n");

            for (Task simpleTask : showSimpleTasks()) {
                fileWriter.write(taskToString(simpleTask) + "\n");
            }
            for (EpicTask epicTask : showEpicTasks()) {
                fileWriter.write(taskToString(epicTask) + "\n");
            }
            for (SubTask subTask : showSubTasks()) {
                fileWriter.write(taskToString(subTask) + "\n");
            }
            if (super.getInMemoryHistoryManager().getHistory() != null) {
                fileWriter.write("\n");
                fileWriter.write(historyToString(super.getInMemoryHistoryManager()));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("ManagerSaveException");
        }
    }

    private String taskToString(Task task) {
        if (task.getType().equals(TaskType.SUBTASK)) {
            SubTask subTask = (SubTask) task;
            return subTask.getId() + "," + subTask.getType() + "," + subTask.getName() + "," + subTask.getStatus() + ","
                    + subTask.getDescription() + "," + subTask.getEpicTaskID();
        } else {
            return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + ","
                    + task.getDescription();
        }
    }

    static public String historyToString(HistoryManager manager) {
        String historyString = "";
        for (Task task : manager.getHistory()) {
            historyString += task.getId() + ",";
        }
        return historyString;
    }

    static public FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try {
            String savedFile = Files.readString(Path.of("src/ru/yandex/practicum/taskTracker/dataStorage/FileManager.csv"));
            String[] savedLines = savedFile.split("\n");
            for (int i = 1; i < savedLines.length && !savedLines[i].isBlank(); i++) {
                Task task = fromString(savedLines[i]);
                if (task.getType().equals(TaskType.TASK)) {
                    fileBackedTasksManager.simpleTasks.put(task.getId(), task);
                } else if (task.getType().equals(TaskType.EPIC)) {
                    EpicTask epicTask = (EpicTask) task;
                    fileBackedTasksManager.epicTasks.put(epicTask.getId(), epicTask);
                } else if (task.getType().equals(TaskType.SUBTASK)) {
                    SubTask subTask = (SubTask) task;
                    fileBackedTasksManager.subTasks.put(subTask.getId(), subTask);
                    fileBackedTasksManager.epicTasks.get(subTask.getEpicTaskID()).getSubTasksOfEpicList().add(subTask.getId());
                }
            }
            List<Long> historyLoaded = historyFromString(savedLines[savedLines.length - 1]);
            for (Long taskId : historyLoaded) {
                if (fileBackedTasksManager.simpleTasks.containsKey(taskId)) {
                    fileBackedTasksManager.getInMemoryHistoryManager().add(fileBackedTasksManager.simpleTasks.get(taskId));
                } else if (fileBackedTasksManager.epicTasks.containsKey(taskId)) {
                    fileBackedTasksManager.getInMemoryHistoryManager().add(fileBackedTasksManager.epicTasks.get(taskId));
                } else if (fileBackedTasksManager.subTasks.containsKey(taskId)) {
                    fileBackedTasksManager.getInMemoryHistoryManager().add(fileBackedTasksManager.subTasks.get(taskId));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileBackedTasksManager;
    }

    private static Task fromString(String value) {
        Task task = new Task("", "");
        String[] taskData = value.split(",");
        if (TaskType.valueOf(taskData[1]).equals(TaskType.TASK)) {
            task = new Task(Long.parseLong(taskData[0]), TaskType.valueOf(taskData[1]), taskData[2],
                    TaskStatus.valueOf(taskData[3]), taskData[4]);
        } else if (TaskType.valueOf(taskData[1]).equals(TaskType.EPIC)) {
            task = new EpicTask(Long.parseLong(taskData[0]), TaskType.valueOf(taskData[1]), taskData[2],
                    TaskStatus.valueOf(taskData[3]), taskData[4]);
        } else if (TaskType.valueOf(taskData[1]).equals(TaskType.SUBTASK)) {
            task = new SubTask(Long.parseLong(taskData[0]), TaskType.valueOf(taskData[1]), taskData[2],
                    TaskStatus.valueOf(taskData[3]), taskData[4], Long.parseLong(taskData[5]));
        }
        return task;
    }

    static List<Long> historyFromString(String value) {
        List<Long> historyLoaded = new ArrayList<>();
        String[] historyData = value.split(",");
        for (int i = 0; i < historyData.length; i++) {
            historyLoaded.add(Long.parseLong(historyData[i]));
        }
        return historyLoaded;
    }

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(
                new File("src/ru/yandex/practicum/taskTracker/dataStorage/FileManager.csv"));
        Long simpleTaskId1 = fileBackedTasksManager.recordSimpleTask(new Task("First SimpleTask", "SimpleTask(ID=1)"));
        Long simpleTaskId2 = fileBackedTasksManager.recordSimpleTask(new Task("Second SimpleTask", "DSimpleTask(ID=2)"));
        Long epicTaskId3 = fileBackedTasksManager.recordEpicTask(new EpicTask("First EpicTask", "EpicTask(ID=3)"));
        Long subTaskId4 = fileBackedTasksManager.recordSubTask(new SubTask("First SubTask", "SubTask(ID=4) of first EpicTask(ID=3)", epicTaskId3));
        Long subTaskId5 = fileBackedTasksManager.recordSubTask(new SubTask("Second SubTask", "SubTask(ID=5) of first EpicTask(ID=3)", epicTaskId3));
        Long subTaskId6 = fileBackedTasksManager.recordSubTask(new SubTask("Third SubTask", "SubTask(ID=6) of first EpicTask(ID=3)", epicTaskId3));
        Long epicTaskId7 = fileBackedTasksManager.recordEpicTask(new EpicTask("Second EpicTask", "EpicTask(ID=7)"));
        Long subTaskId8 = fileBackedTasksManager.recordSubTask(new SubTask("Fourth SubTask", "SubTask(ID=8) of first EpicTask(ID=7)", epicTaskId7));

        //fileBackedTasksManager.getSimpleTaskById(1L);
         /*Если первое и второе обращение будет к одной и той же задаче
         - она запишется в голову списка и оттуда уже не затрётся, любые повторы после этого программа корректно отрабатывает.
         Почему так выходит - понять не смог. На выполение ТЗ не влияет, но баг есть баг и хочется с ним разобраться.*/

        fileBackedTasksManager.getSimpleTaskById(1L);
        fileBackedTasksManager.getSimpleTaskById(2L);
        fileBackedTasksManager.getEpicTaskById(3L);
        fileBackedTasksManager.getSubTaskById(4L);
        fileBackedTasksManager.getSubTaskById(5L);
        fileBackedTasksManager.getSubTaskById(6L);
        fileBackedTasksManager.getEpicTaskById(7L);
        fileBackedTasksManager.getSubTaskById(8L);

        fileBackedTasksManager.getSubTaskById(8L);
        fileBackedTasksManager.getEpicTaskById(7L);
        fileBackedTasksManager.getSubTaskById(6L);
        fileBackedTasksManager.getSubTaskById(5L);
        fileBackedTasksManager.getSubTaskById(4L);
        fileBackedTasksManager.getEpicTaskById(3L);
        fileBackedTasksManager.getSimpleTaskById(2L);
        fileBackedTasksManager.getSimpleTaskById(1L);

        System.out.println(fileBackedTasksManager.getSubTaskById(8L));
        System.out.println(fileBackedTasksManager.getSubTaskById(4L));
        System.out.println(fileBackedTasksManager.getSimpleTaskById(1L));
        System.out.println(fileBackedTasksManager.getEpicTaskById(7L));
        System.out.println(fileBackedTasksManager.getSimpleTaskById(2L));
        System.out.println(fileBackedTasksManager.getSubTaskById(6L));
        System.out.println(fileBackedTasksManager.getEpicTaskById(3L));
        System.out.println(fileBackedTasksManager.getSubTaskById(5L));
        System.out.println(fileBackedTasksManager.getSimpleTaskById(2L));
        System.out.println();
        fileBackedTasksManager.removeSubTaskById(8L);
        System.out.println(fileBackedTasksManager.getInMemoryHistoryManager().getHistory());

        System.out.println();
        System.out.println();

        FileBackedTasksManager fileBackedTasksManagerRestored = loadFromFile(
                new File("src/ru/yandex/practicum/taskTracker/dataStorage/FileManager.csv"));
        System.out.println(fileBackedTasksManager.getSubTaskById(4L));
        System.out.println(fileBackedTasksManager.getSimpleTaskById(1L));
        System.out.println(fileBackedTasksManager.getEpicTaskById(7L));
        System.out.println(fileBackedTasksManager.getSimpleTaskById(2L));
        System.out.println(fileBackedTasksManager.getSubTaskById(6L));
        System.out.println(fileBackedTasksManager.getEpicTaskById(3L));
        System.out.println(fileBackedTasksManager.getSubTaskById(5L));
        System.out.println(fileBackedTasksManager.getSimpleTaskById(2L));
        System.out.println();
        System.out.println(fileBackedTasksManagerRestored.getInMemoryHistoryManager().getHistory());
    }
}
