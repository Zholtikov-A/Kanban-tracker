package ru.yandex.practicum.taskTracker.tasks;

public class SubTask extends Task {

    private Long epicTaskID;

    public SubTask(String name, String description, Long epicTaskID) {
        super(name, description);
        this.epicTaskID = epicTaskID;
        this.setType(TaskType.SUBTASK);
    }

    public SubTask(Long id, TaskType type, String name, TaskStatus status, String description, Long epicTaskID) {
        super(id, type, name, status, description);
        this.epicTaskID = epicTaskID;
    }

    public Long getEpicTaskID() {
        return epicTaskID;
    }

    public void setEpicTaskID(Long epicTaskID) {
        this.epicTaskID = epicTaskID;
    }
}
