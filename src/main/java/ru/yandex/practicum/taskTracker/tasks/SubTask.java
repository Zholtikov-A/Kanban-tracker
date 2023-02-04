package ru.yandex.practicum.taskTracker.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

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

    public SubTask(String name, String description, LocalDateTime startTime, Duration duration, Long epicTaskID) {
        super(name, description, startTime, duration);
        this.epicTaskID = epicTaskID;
        this.setType(TaskType.SUBTASK);
    }

    public SubTask(Long id, TaskType type, String name, TaskStatus status, String description, LocalDateTime startTime, Duration duration, Long epicTaskID) {
        super(id, type, name, status, description, startTime, duration);
        this.epicTaskID = epicTaskID;
    }

    public Long getEpicTaskID() {
        return epicTaskID;
    }

    public void setEpicTaskID(Long epicTaskID) {
        this.epicTaskID = epicTaskID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(epicTaskID, subTask.epicTaskID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicTaskID);
    }
}
