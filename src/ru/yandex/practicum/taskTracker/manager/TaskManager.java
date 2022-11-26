package ru.yandex.practicum.taskTracker.manager;

import ru.yandex.practicum.taskTracker.tasks.EpicTask;
import ru.yandex.practicum.taskTracker.tasks.SubTask;
import ru.yandex.practicum.taskTracker.tasks.Task;

import java.util.HashMap;
import java.util.Objects;
import java.util.ArrayList;

public class TaskManager {

    private long newTaskId = 0;
    HashMap<Long, Task> simpleTasks = new HashMap<>();
    HashMap<Long, SubTask> subTasks = new HashMap<>();
    HashMap<Long, EpicTask> epicTasks = new HashMap<>();

    private long generateID() {
        newTaskId++;
        return newTaskId;
    }

    public long recordSimpleTask(Task task) {
        task.setId(generateID());
        task.setStatus("NEW");
        simpleTasks.put(task.getId(), task);
        return task.getId();
    }

    public void replaceSimpleTask(Task task) {
        simpleTasks.replace(task.getId(), task);
    }

    public ArrayList<Task> showSimpleTasks() {
        return new ArrayList<>(simpleTasks.values());
    }

    public void removeAllSimpleTasks() {
        simpleTasks.clear();
    }

    public Task getSimpleTaskById(long simpleTaskId) {
        return simpleTasks.getOrDefault(simpleTaskId, null);
    }

    public void removeSimpleTaskById(long simpleTaskId) {
        simpleTasks.remove(simpleTaskId);
    }

    public long recordSubTask(SubTask subTask) {
        subTask.setId(generateID());
        subTask.setStatus("NEW");
        subTasks.put(subTask.getId(), subTask);
        epicTasks.get(subTask.getEpicTaskID()).getSubTasksOfEpicList().add(subTask.getId());
        checkEpicStatus(subTask.getEpicTaskID());
        return subTask.getId();
    }

    public void replaceSubTask(SubTask subTask) {
        long id = subTask.getId();
        subTasks.replace(id, subTask);
        checkEpicStatus(subTask.getEpicTaskID());
    }

    public ArrayList<SubTask> showSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void removeAllSubTasks() {
        subTasks.clear();
        for (EpicTask epicTask : epicTasks.values()) {
            epicTask.setStatus("NEW");
            epicTask.getSubTasksOfEpicList().clear();
        }
    }

    public SubTask getSubTaskById(long subTaskId) {
        return subTasks.getOrDefault(subTaskId, null);
    }

    public void removeSubTaskById(long subTaskId) {
        long epicId = subTasks.get(subTaskId).getEpicTaskID();
        subTasks.remove(subTaskId);
        epicTasks.get(epicId).getSubTasksOfEpicList().remove(subTaskId);
        checkEpicStatus(epicId);
    }

    private void checkEpicStatus(long epicTaskId) {
        EpicTask epicTask = epicTasks.get(epicTaskId);
        if (epicTask.getSubTasksOfEpicList().isEmpty()) {
            epicTask.setStatus("NEW");
        } else {
            int numberOfNewSubTasks = 0;
            int numberOfDoneSubTasks = 0;
            HashMap<Long, SubTask> subList = new HashMap<>();
            for (SubTask subTask : subTasks.values()) {
                if (subTask.getEpicTaskID() == epicTaskId) {
                    subList.put(subTask.getId(), subTask);
                }
            }
            for (SubTask subTask : subList.values()) {
                if (Objects.equals(subTask.getStatus(), "NEW")) {
                    numberOfNewSubTasks++;
                } else if (Objects.equals(subTask.getStatus(), "DONE")) {
                    numberOfDoneSubTasks++;
                } else {
                    break;
                }
            }
            if (numberOfNewSubTasks == epicTask.getSubTasksOfEpicList().size()) {
                epicTask.setStatus("NEW");
            } else if (numberOfDoneSubTasks == epicTask.getSubTasksOfEpicList().size()) {
                epicTask.setStatus("DONE");
            } else {
                epicTask.setStatus("IN_PROGRESS");
            }
        }
    }

    public long recordEpicTask(EpicTask epicTask) {
        epicTask.setId(generateID());
        epicTask.setStatus("NEW");
        epicTasks.put(epicTask.getId(), epicTask);
        return epicTask.getId();
    }

    public void replaceEpicTask(EpicTask epicTask) {
        long id = epicTask.getId();
        epicTasks.replace(id, epicTask);
    }

    public ArrayList<EpicTask> showEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    public void removeAllEpicTasks() {
        epicTasks.clear();
        subTasks.clear();
    }

    public EpicTask getEpicTaskById(long epicTaskId) {
        return epicTasks.getOrDefault(epicTaskId, null);
    }

    public void removeEpicTaskById(long epicTaskId) {
        for (long subTaskForDeleteId : epicTasks.get(epicTaskId).getSubTasksOfEpicList()) {
            subTasks.remove(subTaskForDeleteId);
        }
        epicTasks.remove(epicTaskId);
    }

    public ArrayList<SubTask> showSubTasksOfEpic(long epicTaskId) {
        ArrayList<SubTask> subList = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicTaskID() == epicTaskId) {
                subList.add(subTask);
            }
        }
        return subList;
    }
}

