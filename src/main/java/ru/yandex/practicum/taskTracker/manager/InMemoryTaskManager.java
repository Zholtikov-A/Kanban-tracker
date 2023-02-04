package ru.yandex.practicum.taskTracker.manager;

import ru.yandex.practicum.taskTracker.tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private Long newTaskId = 0L;
    private HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    protected Map<Long, Task> simpleTasks = new HashMap<>();
    protected Map<Long, SubTask> subTasks = new HashMap<>();
    protected Map<Long, EpicTask> epicTasks = new HashMap<>();
    protected Set<Task> priority = new TreeSet<>((Task task1, Task task2) -> task1.getStartTime().compareTo(task2.getStartTime()));
    private Map<LocalDateTime, Boolean> schedule = createSchedule();

    private Long generateId() {
        return ++newTaskId;
    }

    @Override
    public Long saveSimpleTask(Task task) {
        if (collisionCheck(task)) {
            return -1L;
        }
        task.setId(generateId());
        task.setStatus(TaskStatus.NEW);
        simpleTasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public void updateSimpleTask(Task task) {
        if ((task.getStartTime() != null) && (task.getDuration() != null)) {
            if (simpleTasks.containsKey(task.getId())) {
                Task taskReserved = simpleTasks.get(task.getId());
                if ((taskReserved.getStartTime() != null) && (taskReserved.getDuration() != null)) {
                    LocalDateTime checkTime = taskReserved.getStartTime();
                    while (checkTime.isBefore(taskReserved.getEndTime())) {
                        schedule.put(checkTime, false);
                        checkTime = checkTime.plusMinutes(15);
                    }
                    if (collisionCheck(task)) {
                        checkTime = taskReserved.getStartTime();
                        while (checkTime.isBefore(taskReserved.getEndTime())) {
                            schedule.put(checkTime, true);
                            checkTime = checkTime.plusMinutes(15);
                        }
                        return;
                    }
                }
            }
        }
        simpleTasks.replace(task.getId(), task);
    }

    @Override
    public List<Task> showSimpleTasks() {
        return new ArrayList<>(simpleTasks.values());
    }

    @Override
    public void removeAllSimpleTasks() {
        for (Task taskForDelete : simpleTasks.values()) {
            inMemoryHistoryManager.remove(taskForDelete.getId());
            if ((taskForDelete.getStartTime() != null) && (taskForDelete.getDuration() != null)) {
                LocalDateTime checkTime = taskForDelete.getStartTime();
                while (checkTime.isBefore(taskForDelete.getEndTime())) {
                    schedule.put(checkTime, false);
                    checkTime = checkTime.plusMinutes(15);
                }
                priority.remove(taskForDelete);
            }
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
        if (simpleTasks.containsKey(simpleTaskId)) {
            Task taskForDelete = simpleTasks.get(simpleTaskId);
            if ((taskForDelete.getStartTime() != null) && (taskForDelete.getDuration() != null)) {
                LocalDateTime checkTime = taskForDelete.getStartTime();
                while (checkTime.isBefore(taskForDelete.getEndTime())) {
                    schedule.put(checkTime, false);
                    checkTime = checkTime.plusMinutes(15);
                }
                priority.remove(simpleTasks.get(simpleTaskId));
            }
            simpleTasks.remove(simpleTaskId);
            inMemoryHistoryManager.remove(simpleTaskId);
        }
    }

    @Override
    public Long saveSubTask(SubTask subTask) {
        if (epicTasks.containsKey(subTask.getEpicTaskID())) {
            if (collisionCheck(subTask)) {
                return -1L;
            }
            subTask.setId(generateId());
            subTask.setStatus(TaskStatus.NEW);
            subTasks.put(subTask.getId(), subTask);
            epicTasks.get(subTask.getEpicTaskID()).getSubTasksOfEpicList().add(subTask.getId());
            checkEpicStatus(subTask.getEpicTaskID());
            calculateEpicStartTimeAndDuration(subTask.getEpicTaskID());
        } else {
            return -2L;
        }
        return subTask.getId();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId()) && epicTasks.containsKey(subTask.getEpicTaskID())) {
            if (Objects.equals(subTask.getEpicTaskID(), subTasks.get(subTask.getId()).getEpicTaskID())) {
                if ((subTask.getStartTime() != null) && (subTask.getDuration() != null)) {
                    SubTask taskReserved = subTasks.get(subTask.getId());
                    if ((taskReserved.getStartTime() != null) && (taskReserved.getDuration() != null)) {
                        LocalDateTime checkTime = taskReserved.getStartTime();
                        while (checkTime.isBefore(taskReserved.getEndTime())) {
                            schedule.put(checkTime, false);
                            checkTime = checkTime.plusMinutes(15);
                        }
                        if (collisionCheck(subTask)) {
                            checkTime = taskReserved.getStartTime();
                            while (checkTime.isBefore(taskReserved.getEndTime())) {
                                schedule.put(checkTime, true);
                                checkTime = checkTime.plusMinutes(15);
                            }
                            return;
                        }
                    }
                }
                Long id = subTask.getId();
                subTasks.replace(id, subTask);
                checkEpicStatus(subTask.getEpicTaskID());
                calculateEpicStartTimeAndDuration(subTask.getEpicTaskID());
            }
        }
    }

    @Override
    public List<SubTask> showSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void removeAllSubTasks() {
        for (SubTask taskForDelete : subTasks.values()) {
            if (taskForDelete.getStartTime() != null && taskForDelete.getDuration() != null) {
                LocalDateTime checkTime = taskForDelete.getStartTime();
                while (checkTime.isBefore(taskForDelete.getEndTime())) {
                    schedule.put(checkTime, false);
                    checkTime = checkTime.plusMinutes(15);
                    priority.remove(taskForDelete);
                }
            }
            inMemoryHistoryManager.remove(taskForDelete.getId());
        }
        subTasks.clear();
        for (EpicTask epicTask : epicTasks.values()) {
            epicTask.setStatus(TaskStatus.NEW);
            epicTask.getSubTasksOfEpicList().clear();
            calculateEpicStartTimeAndDuration(epicTask.getId());
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
        if (subTasks.containsKey(subTaskId)) {
            SubTask taskForDelete = subTasks.get(subTaskId);
            if (taskForDelete.getStartTime() != null && taskForDelete.getDuration() != null) {
                LocalDateTime checkTime = taskForDelete.getStartTime();
                while (checkTime.isBefore(taskForDelete.getEndTime())) {
                    schedule.put(checkTime, false);
                    checkTime = checkTime.plusMinutes(15);
                    priority.remove(taskForDelete);
                }
            }
            Long epicId = subTasks.get(subTaskId).getEpicTaskID();
            subTasks.remove(subTaskId);
            epicTasks.get(epicId).getSubTasksOfEpicList().remove(subTaskId);
            checkEpicStatus(epicId);
            calculateEpicStartTimeAndDuration(epicId);
            inMemoryHistoryManager.remove(subTaskId);
        }
    }

    private void checkEpicStatus(Long epicTaskId) {
        EpicTask epicTask = epicTasks.get(epicTaskId);
        if (epicTask.getSubTasksOfEpicList().isEmpty()) {
            epicTask.setStatus(TaskStatus.NEW);
        } else {
            int numberOfNewSubTasks = 0;
            int numberOfDoneSubTasks = 0;
            Map<Long, SubTask> subList = new HashMap<>();
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
    public Long saveEpicTask(EpicTask epicTask) {
        epicTask.setId(generateId());
        epicTask.setStatus(TaskStatus.NEW);
        epicTasks.put(epicTask.getId(), epicTask);
        return epicTask.getId();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        if (epicTasks.containsKey(epicTask.getId())) {
            Long id = epicTask.getId();
            TaskStatus status = epicTasks.get(id).getStatus();
            epicTask.setStatus(status);
            epicTask.setType(TaskType.EPIC);
            epicTasks.replace(id, epicTask);
        }
    }

    @Override
    public List<EpicTask> showEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public void removeAllEpicTasks() {
        for (Task taskForDelete : subTasks.values()) {
            if (taskForDelete.getStartTime() != null && taskForDelete.getDuration() != null) {
                LocalDateTime checkTime = taskForDelete.getStartTime();
                while (checkTime.isBefore(taskForDelete.getEndTime())) {
                    schedule.put(checkTime, false);
                    checkTime = checkTime.plusMinutes(15);
                    priority.remove(taskForDelete);
                }
            }
            inMemoryHistoryManager.remove(taskForDelete.getId());
        }
        for (Task taskForDelete : epicTasks.values()) {
            inMemoryHistoryManager.remove(taskForDelete.getId());
        }
        subTasks.clear();
        epicTasks.clear();
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
        if (epicTasks.containsKey(epicTaskId)) {
            for (Long subTaskForDeleteId : epicTasks.get(epicTaskId).getSubTasksOfEpicList()) {
                SubTask taskForDelete = subTasks.get(subTaskForDeleteId);
                if (taskForDelete.getStartTime() != null && taskForDelete.getDuration() != null) {
                    LocalDateTime checkTime = taskForDelete.getStartTime();
                    while (checkTime.isBefore(taskForDelete.getEndTime())) {
                        schedule.put(checkTime, false);
                        checkTime = checkTime.plusMinutes(15);
                        priority.remove(taskForDelete);
                    }
                }
                inMemoryHistoryManager.remove(subTaskForDeleteId);
                subTasks.remove(subTaskForDeleteId);
            }
            inMemoryHistoryManager.remove(epicTaskId);
            epicTasks.remove(epicTaskId);
        }
    }

    @Override
    public List<SubTask> showSubTasksOfEpic(Long epicTaskId) {
        List<SubTask> subList = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicTaskID() == epicTaskId) {
                subList.add(subTask);
            }
        }
        return subList;
    }

    public HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    private void calculateEpicStartTimeAndDuration(Long epicTaskId) {
        if (epicTasks.containsKey(epicTaskId)) {
            if (epicTasks.get(epicTaskId).getSubTasksOfEpicList().isEmpty()) {
                epicTasks.get(epicTaskId).setDuration(null);
                epicTasks.get(epicTaskId).setStartTime(null);
                return;
            } else {
                Duration epicDuration = Duration.ZERO;
                LocalDateTime epicStartTime = LocalDateTime.MAX;
                boolean epicContainsDataTime = true;
                for (Long subTaskId : epicTasks.get(epicTaskId).getSubTasksOfEpicList()) {
                    if ((subTasks.get(subTaskId).getDuration() != null) && (subTasks.get(subTaskId).getStartTime() != null)) {
                        epicDuration = epicDuration.plus(subTasks.get(subTaskId).getDuration());
                        if (epicStartTime.isAfter(subTasks.get(subTaskId).getStartTime())) {
                            epicStartTime = subTasks.get(subTaskId).getStartTime();
                        }
                    } else epicContainsDataTime = false;
                }
                if (epicContainsDataTime) {
                    epicTasks.get(epicTaskId).setDuration(epicDuration);
                    epicTasks.get(epicTaskId).setStartTime(epicStartTime);
                } else {
                    epicTasks.get(epicTaskId).setDuration(null);
                    epicTasks.get(epicTaskId).setStartTime(null);
                }
            }
        }
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(priority);
    }

    private Map<LocalDateTime, Boolean> createSchedule() {
        Map<LocalDateTime, Boolean> schedule = new HashMap<>();
        LocalDateTime startOfYear = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0);
        LocalDateTime currentDateTime = startOfYear;
        LocalDateTime endOfYear = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0);
        while (currentDateTime.isBefore(endOfYear)) {
            schedule.put(currentDateTime, false);
            currentDateTime = currentDateTime.plusMinutes(15);
        }
        return schedule;
    }

    private boolean collisionCheck(Task task) {
        if (task.getStartTime() == null || task.getDuration() == null) {
            return false;
        }
        boolean isCollision = false;
        LocalDateTime checkTime = task.getStartTime();
        while (checkTime.isBefore(task.getEndTime())) {
            if (schedule.get(checkTime)) {
                return true;
            }
            checkTime = checkTime.plusMinutes(15);
        }
        checkTime = task.getStartTime();
        while (checkTime.isBefore(task.getEndTime())) {
            schedule.put(checkTime, true);
            checkTime = checkTime.plusMinutes(15);
        }
        priority.add(task);
        return isCollision;
    }
}

