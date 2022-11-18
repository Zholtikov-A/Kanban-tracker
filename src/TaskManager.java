import java.util.HashMap;
import java.util.Objects;
import java.util.ArrayList;

public class TaskManager {

    private long newTaskId = 0;
    HashMap<Long, Task> simpleTasks = new HashMap<>();
    HashMap<Long, SubTask> subTasks = new HashMap<>();
    HashMap<Long, EpicTask> epicTasks = new HashMap<>();

    public long generateID() {
        newTaskId++;
        return newTaskId;
    }

    public void recordSimpleTask(Task task) {
        task.setId(generateID());
        task.setStatus("NEW");
        simpleTasks.put(task.getId(), task);
    }

    public void replaceSimpleTask(Task task, long id, String status) {
        task.setId(id);
        task.setStatus(status);
        simpleTasks.replace(task.getId(), task);
    }

    public HashMap<Long, Task> showSimpleTasks() {
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
        subTask.setStatus("NEW");
        subTasks.put(subTask.getId(), subTask);
        epicTasks.get(subTask.getEpicTaskID()).getSubTasksOfEpicList().add(subTask.getId());
        checkEpicStatus(subTask.getEpicTaskID());
    }

    public void replaceSubTask(SubTask subTask, long id, String status) {
        subTask.setId(id);
        subTask.setStatus(status);
        long epicTaskId = subTasks.get(id).getEpicTaskID();
        subTask.setEpicTaskID(epicTaskId);
        subTasks.replace(id, subTask);
        checkEpicStatus(subTask.getEpicTaskID());
    }

    public HashMap<Long, SubTask> showSubTasks() {
        if (!subTasks.isEmpty()) {
            return subTasks;
        } else {
            return null;
        }
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


    public void recordEpicTask(EpicTask epicTask) {
        epicTask.setId(generateID());
        epicTask.setStatus("NEW");
        epicTasks.put(epicTask.getId(), epicTask);
    }

    public void replaceEpicTask(EpicTask epicTask, long id) {
        ArrayList<Long> subTasksOfEpicList = epicTasks.get(id).getSubTasksOfEpicList();
        epicTask.setSubTasksOfEpicList(subTasksOfEpicList);
        String status = epicTasks.get(id).getStatus();
        epicTask.setStatus(status);
        epicTasks.replace(id, epicTask);
    }

    public HashMap<Long, EpicTask> showEpicTasks() {
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
        for (long subTaskForDeleteId : epicTasks.get(epicTaskId).getSubTasksOfEpicList()) {
            subTasks.remove(subTaskForDeleteId);
        }
        epicTasks.remove(epicTaskId);
    }
}

