package ru.yandex.practicum.taskTracker.manager;

import ru.yandex.practicum.taskTracker.tasks.EpicTask;
import ru.yandex.practicum.taskTracker.tasks.SubTask;
import ru.yandex.practicum.taskTracker.tasks.Task;
import ru.yandex.practicum.taskTracker.tasks.TaskStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ArrayList;

public class InMemoryTaskManager implements TaskManager {

    private Long newTaskId = 0L;
    Managers managers = new Managers();
    HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    Map<Long, Task> simpleTasks = new HashMap<>();
    Map<Long, SubTask> subTasks = new HashMap<>();
    Map<Long, EpicTask> epicTasks = new HashMap<>();

    private Long generateId() {
        return ++newTaskId;
    }

    @Override
    public Long recordSimpleTask(Task task) {
        task.setId(generateId());
        task.setStatus(TaskStatus.NEW);
        simpleTasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public void replaceSimpleTask(Task task) {
        simpleTasks.replace(task.getId(), task);
    }

    @Override
    public ArrayList<Task> showSimpleTasks() {
        return new ArrayList<>(simpleTasks.values());
    }

    @Override
    public void removeAllSimpleTasks() {
        for (Task taskForDelete : simpleTasks.values()) {
            inMemoryHistoryManager.remove(taskForDelete.getId());
        }
        simpleTasks.clear();
    }

    @Override
    public Task getSimpleTaskById(Long simpleTaskId) {
        if (simpleTasks.containsKey(simpleTaskId)) {
            inMemoryHistoryManager.add(simpleTasks.get(simpleTaskId));
        }
        return simpleTasks.get(simpleTaskId);
    }

    @Override
    public void removeSimpleTaskById(Long simpleTaskId) {
        simpleTasks.remove(simpleTaskId);
        inMemoryHistoryManager.remove(simpleTaskId);
    }

    @Override
    public Long recordSubTask(SubTask subTask) {
        subTask.setId(generateId());
        subTask.setStatus(TaskStatus.NEW);
        subTasks.put(subTask.getId(), subTask);
        epicTasks.get(subTask.getEpicTaskID()).getSubTasksOfEpicList().add(subTask.getId());
        checkEpicStatus(subTask.getEpicTaskID());
        return subTask.getId();
    }

    @Override
    public void replaceSubTask(SubTask subTask) {
        Long id = subTask.getId();
        subTasks.replace(id, subTask);
        checkEpicStatus(subTask.getEpicTaskID());
    }

    @Override
    public ArrayList<SubTask> showSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void removeAllSubTasks() {
        for (Task taskForDelete : subTasks.values()) {
            inMemoryHistoryManager.remove(taskForDelete.getId());
        }
        subTasks.clear();
        for (EpicTask epicTask : epicTasks.values()) {
            epicTask.setStatus(TaskStatus.NEW);
            epicTask.getSubTasksOfEpicList().clear();
        }
    }

    @Override
    public SubTask getSubTaskById(Long subTaskId) {
        if (subTasks.containsKey(subTaskId)) {
            inMemoryHistoryManager.add(subTasks.get(subTaskId));
        }
        return subTasks.get(subTaskId);
    }

    @Override
    public void removeSubTaskById(Long subTaskId) {
        Long epicId = subTasks.get(subTaskId).getEpicTaskID();
        subTasks.remove(subTaskId);
        epicTasks.get(epicId).getSubTasksOfEpicList().remove(subTaskId);
        checkEpicStatus(epicId);
        inMemoryHistoryManager.remove(subTaskId);
    }

    private void checkEpicStatus(Long epicTaskId) {
        EpicTask epicTask = epicTasks.get(epicTaskId);
        if (epicTask.getSubTasksOfEpicList().isEmpty()) {
            epicTask.setStatus(TaskStatus.NEW);
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
                if (Objects.equals(subTask.getStatus(), TaskStatus.NEW)) {
                    numberOfNewSubTasks++;
                } else if (Objects.equals(subTask.getStatus(), TaskStatus.DONE)) {
                    numberOfDoneSubTasks++;
                } else {
                    break;
                }
            }
            if (numberOfNewSubTasks == epicTask.getSubTasksOfEpicList().size()) {
                epicTask.setStatus(TaskStatus.NEW);
            } else if (numberOfDoneSubTasks == epicTask.getSubTasksOfEpicList().size()) {
                epicTask.setStatus(TaskStatus.DONE);
            } else {
                epicTask.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    @Override
    public Long recordEpicTask(EpicTask epicTask) {
        epicTask.setId(generateId());
        epicTask.setStatus(TaskStatus.NEW);
        epicTasks.put(epicTask.getId(), epicTask);
        return epicTask.getId();
    }

    @Override
    public void replaceEpicTask(EpicTask epicTask) {
        Long id = epicTask.getId();
        epicTasks.replace(id, epicTask);
    }

    @Override
    public ArrayList<EpicTask> showEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public void removeAllEpicTasks() {
        for (Task taskForDelete : epicTasks.values()) {
            inMemoryHistoryManager.remove(taskForDelete.getId());
        }
        for (Task taskForDelete : subTasks.values()) {
            inMemoryHistoryManager.remove(taskForDelete.getId());
        }
        epicTasks.clear();
        subTasks.clear();
    }

    @Override
    public EpicTask getEpicTaskById(Long epicTaskId) {
        if (epicTasks.containsKey(epicTaskId)) {
            inMemoryHistoryManager.add(epicTasks.get(epicTaskId));
        }
        return epicTasks.get(epicTaskId);
    }

    @Override
    public void removeEpicTaskById(Long epicTaskId) {
        for (Long subTaskForDeleteId : epicTasks.get(epicTaskId).getSubTasksOfEpicList()) {
            inMemoryHistoryManager.remove(subTaskForDeleteId);
            subTasks.remove(subTaskForDeleteId);
        }
        inMemoryHistoryManager.remove(epicTaskId);
        epicTasks.remove(epicTaskId);
    }

    @Override
    public ArrayList<SubTask> showSubTasksOfEpic(Long epicTaskId) {
        ArrayList<SubTask> subList = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicTaskID() == epicTaskId) {
                subList.add(subTask);
            }
        }
        return subList;
    }
}

