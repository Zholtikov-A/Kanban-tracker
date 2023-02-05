package ru.yandex.practicum.taskTracker.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {

    private Long epicTaskId;

    public SubTask(String name, String description, Long epicTaskId) {
        super(name, description);
        this.epicTaskId = epicTaskId;
        this.setType(TaskType.SUBTASK);
    }

    public SubTask(Long id, TaskType type, String name, TaskStatus status, String description, Long epicTaskId) {
        super(id, type, name, status, description);
        this.epicTaskId = epicTaskId;
    }

    public SubTask(String name, String description, LocalDateTime startTime, Duration duration, Long epicTaskId) {
        super(name, description, startTime, duration);
        this.epicTaskId = epicTaskId;
        this.setType(TaskType.SUBTASK);
    }

    public SubTask(Long id, TaskType type, String name, TaskStatus status, String description, LocalDateTime startTime, Duration duration, Long epicTaskId) {
        super(id, type, name, status, description, startTime, duration);
        this.epicTaskId = epicTaskId;
    }

    public Long getEpicTaskId() {
        return epicTaskId;
    }

    public void setEpicTaskId(Long epicTaskId) {
        this.epicTaskId = epicTaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(epicTaskId, subTask.epicTaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicTaskId);
    }
}
