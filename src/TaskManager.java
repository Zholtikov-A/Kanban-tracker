import java.util.HashMap;
import java.util.Objects;

public class TaskManager {

    private int newTaskId = 0;
    HashMap<Long, Task> simpleTasks = new HashMap<>();
    HashMap<Long, SubTask> subTasks = new HashMap<>();
    HashMap<Long, EpicTask> epicTasks = new HashMap<>();

    public int generateID() {
        newTaskId++;
        return newTaskId;
    }

    public void recordSimpleTask(Task task) {
        task.setId(generateID());
        simpleTasks.put(task.getId(), task);
    }

    public void replaceSimpleTask(Task task) {

        simpleTasks.replace(task.getId(), task);
    }

    public HashMap<Long, Task> simpleTasksList() {
        if (!simpleTasks.isEmpty()) {
            return simpleTasks;
        } else {
            return null;
        }
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

    public void recordSubTask(SubTask subTask) {
        subTask.setId(generateID());
        subTasks.put(subTask.getId(), subTask);
        epicTasks.get(subTask.getEpicTaskID()).subTasksOfEpic.put(subTask.getId(), subTask);
        checkEpicStatus(subTask.getEpicTaskID());
    }

    public void replaceSubTask(SubTask subTask) {

        subTasks.replace(subTask.getId(), subTask);
        epicTasks.get(subTask.getEpicTaskID()).subTasksOfEpic.replace(subTask.getId(), subTask);
        checkEpicStatus(subTask.getEpicTaskID());
    }

    public HashMap<Long, SubTask> subTasksList() {
        if (!subTasks.isEmpty()) {
            return subTasks;
        } else {
            return null;
        }
    }

    public void removeAllSubTasks() {
        subTasks.clear();
        if (!epicTasks.isEmpty()) {
            for (EpicTask epicTask : epicTasks.values()) {
                epicTask.setStatus("NEW");
            }
        }
    }

    public SubTask getSubTaskById(long subTaskId) {
        return subTasks.getOrDefault(subTaskId, null);
    }

    public void removeSubTaskById(long subTaskId) {
        long epicId = subTasks.get(subTaskId).getEpicTaskID();
        subTasks.remove(subTaskId);
        checkEpicStatus(epicId);
    }

    public void checkEpicStatus(long epicTaskId) {
        if (epicTasks.get(epicTaskId).subTasksOfEpic.isEmpty()) {
            epicTasks.get(epicTaskId).setStatus("NEW");
        } else {
            int numberOfNewSubTasks = 0;
            int numberOfDoneSubTasks = 0;
            for (SubTask subTask : epicTasks.get(epicTaskId).subTasksOfEpic.values()) {
                if (Objects.equals(subTask.getStatus(), "NEW")) {
                    numberOfNewSubTasks++;
                } else if (Objects.equals(subTask.getStatus(), "DONE")) {
                    numberOfDoneSubTasks++;
                } else {
                    break;
                }
            }
            if (numberOfNewSubTasks == epicTasks.get(epicTaskId).subTasksOfEpic.size()) {
                epicTasks.get(epicTaskId).setStatus("NEW");
            } else if (numberOfDoneSubTasks == epicTasks.get(epicTaskId).subTasksOfEpic.size()) {
                epicTasks.get(epicTaskId).setStatus("DONE");
            } else {
                epicTasks.get(epicTaskId).setStatus("IN_PROGRESS");
            }
        }
    }


    public void recordEpicTask(EpicTask epicTask) {
        epicTask.setId(generateID());
        epicTasks.put(epicTask.getId(), epicTask);
        checkEpicStatus(epicTask.getId());
    }

    public void replaceEpicTask(EpicTask epicTask) {
        epicTasks.replace(epicTask.getId(), epicTask);
        checkEpicStatus(epicTask.getId());
    }

    public HashMap<Long, EpicTask> epicTasksList() {
        if (!epicTasks.isEmpty()) {
            return epicTasks;
        } else {
            return null;
        }
    }

    public void removeAllEpicTasks() {
        epicTasks.clear();
        subTasks.clear();
    }

    public EpicTask getEpicTaskById(long epicTaskId) {
        return epicTasks.getOrDefault(epicTaskId, null);
    }

    public void removeEpicTaskById(long epicTaskId) {
        for (SubTask subTaskForDelete : epicTasks.get(epicTaskId).subTasksOfEpic.values()) {
            long idForDel = subTaskForDelete.getId();
            subTasks.remove(idForDel);
        }
        epicTasks.remove(epicTaskId);
    }
}

