package ru.yandex.practicum.taskTracker.manager;

import ru.yandex.practicum.taskTracker.exceptions.ManagerSaveException;
import ru.yandex.practicum.taskTracker.tasks.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public Long saveSimpleTask(Task task) {
        Long TaskId = super.saveSimpleTask(task);
        save();
        return TaskId;
    }

    @Override
    public void updateSimpleTask(Task task) {
        super.updateSimpleTask(task);
        save();
    }

    @Override
    public void removeAllSimpleTasks() {
        super.removeAllSimpleTasks();
        save();
    }

    @Override
    public Task getSimpleTaskById(Long simpleTaskId) {
        Task task = super.getSimpleTaskById(simpleTaskId);
        save();
        return task;
    }

    @Override
    public void removeSimpleTaskById(Long simpleTaskId) {
        super.removeSimpleTaskById(simpleTaskId);
        save();
    }

    @Override
    public Long saveSubTask(SubTask subTask) {
        Long subTaskId = super.saveSubTask(subTask);
        save();
        return subTaskId;
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    @Override
    public SubTask getSubTaskById(Long subTaskId) {
        SubTask subTask = super.getSubTaskById(subTaskId);
        save();
        return subTask;
    }

    @Override
    public void removeSubTaskById(Long subTaskId) {
        super.removeSubTaskById(subTaskId);
        save();
    }

    @Override
    public Long saveEpicTask(EpicTask epicTask) {
        Long epicTaskId = super.saveEpicTask(epicTask);
        save();
        return epicTaskId;
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        super.updateEpicTask(epicTask);
        save();
    }

    @Override
    public void removeAllEpicTasks() {
        super.removeAllEpicTasks();
        save();
    }

    @Override
    public EpicTask getEpicTaskById(Long epicTaskId) {
        EpicTask epicTask = super.getEpicTaskById(epicTaskId);
        save();
        return epicTask;
    }

    @Override
    public void removeEpicTaskById(Long epicTaskId) {
        super.removeEpicTaskById(epicTaskId);
        save();
    }

    protected void save() {
        try (Writer fileWriter = new FileWriter("src/main/java/ru/yandex/practicum/taskTracker/dataStorage/FileManager.csv")) {
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
        String taskInString;
        if (task.getType().equals(TaskType.SUBTASK)) {
            SubTask subTask = (SubTask) task;
            if ((subTask.getStartTime() != null) && (subTask.getDuration() != null)) {
                taskInString = subTask.getId() + "," + subTask.getType() + "," + subTask.getName() + ","
                        + subTask.getStatus() + "," + subTask.getDescription() + "," + subTask.getStartTime() + ","
                        + subTask.getDuration() + "," + subTask.getEpicTaskId();
            } else {
                taskInString = subTask.getId() + "," + subTask.getType() + "," + subTask.getName() + ","
                        + subTask.getStatus() + "," + subTask.getDescription() + "," + subTask.getEpicTaskId();
            }
        } else {
            if ((task.getStartTime() != null) && (task.getDuration() != null)) {
                taskInString = task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + ","
                        + task.getDescription() + "," + task.getStartTime() + "," + task.getDuration();
            } else {
                taskInString = task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + ","
                        + task.getDescription();
            }
        }
        return taskInString;
    }

    static public String historyToString(HistoryManager manager) {
        String historyString = "";
        for (Task task : manager.getHistory()) {
            historyString += task.getId() + ",";
        }
        return historyString;
    }

    static public FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTasksManager = new FileBackedTaskManager(file);
        try {
            String savedFile = Files.readString(Path.of("src/main/java/ru/yandex/practicum/taskTracker/dataStorage/FileManager.csv"));
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
                    fileBackedTasksManager.epicTasks.get(subTask.getEpicTaskId()).getSubTasksOfEpicList().add(subTask.getId());
                }
            }
            List<Long> historyLoaded = historyFromString(savedLines[savedLines.length - 1]);
            for (Long TaskId : historyLoaded) {
                if (fileBackedTasksManager.simpleTasks.containsKey(TaskId)) {
                    fileBackedTasksManager.getInMemoryHistoryManager().add(fileBackedTasksManager.simpleTasks.get(TaskId));
                } else if (fileBackedTasksManager.epicTasks.containsKey(TaskId)) {
                    fileBackedTasksManager.getInMemoryHistoryManager().add(fileBackedTasksManager.epicTasks.get(TaskId));
                } else if (fileBackedTasksManager.subTasks.containsKey(TaskId)) {
                    fileBackedTasksManager.getInMemoryHistoryManager().add(fileBackedTasksManager.subTasks.get(TaskId));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileBackedTasksManager;
    }

    private static Task fromString(String value) {
        int fieldsOfTaskWithoutTime = 5;
        int fieldsOfSubTaskWithoutTime = 6;
        Task task = new Task("", "");
        String[] taskData = value.split(",");
        if (TaskType.valueOf(taskData[1]).equals(TaskType.TASK)) {
            if (taskData.length == fieldsOfTaskWithoutTime) {
                task = new Task(Long.parseLong(taskData[0]), TaskType.valueOf(taskData[1]), taskData[2],
                        TaskStatus.valueOf(taskData[3]), taskData[4]);
            } else {
                task = new Task(Long.parseLong(taskData[0]), TaskType.valueOf(taskData[1]), taskData[2],
                        TaskStatus.valueOf(taskData[3]), taskData[4], LocalDateTime.parse(taskData[5]), Duration.parse(taskData[6]));
            }
        } else if (TaskType.valueOf(taskData[1]).equals(TaskType.EPIC)) {
            if (taskData.length == fieldsOfTaskWithoutTime) {
                task = new EpicTask(Long.parseLong(taskData[0]), TaskType.valueOf(taskData[1]), taskData[2],
                        TaskStatus.valueOf(taskData[3]), taskData[4]);
            } else {
                task = new EpicTask(Long.parseLong(taskData[0]), TaskType.valueOf(taskData[1]), taskData[2],
                        TaskStatus.valueOf(taskData[3]), taskData[4], LocalDateTime.parse(taskData[5]), Duration.parse(taskData[6]));
            }
        } else if (TaskType.valueOf(taskData[1]).equals(TaskType.SUBTASK)) {
            if (taskData.length == fieldsOfSubTaskWithoutTime) {
                task = new SubTask(Long.parseLong(taskData[0]), TaskType.valueOf(taskData[1]), taskData[2],
                        TaskStatus.valueOf(taskData[3]), taskData[4], Long.parseLong(taskData[5]));
            } else {
                task = new SubTask(Long.parseLong(taskData[0]), TaskType.valueOf(taskData[1]), taskData[2],
                        TaskStatus.valueOf(taskData[3]), taskData[4], LocalDateTime.parse(taskData[5]), Duration.parse(taskData[6]), Long.parseLong(taskData[7]));
            }
        }
        return task;
    }

    static List<Long> historyFromString(String value) {
        List<Long> historyLoaded = new ArrayList<>();
        String[] historyData = value.split(",");
        for (String historyDatum : historyData) {
            historyLoaded.add(Long.parseLong(historyDatum));
        }
        return historyLoaded;
    }
}
