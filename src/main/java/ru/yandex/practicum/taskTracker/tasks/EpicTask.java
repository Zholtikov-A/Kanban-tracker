package ru.yandex.practicum.taskTracker.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EpicTask extends Task {
    private List<Long> subTasksOfEpicIdList = new ArrayList<>();

    public EpicTask(String name, String description) {
        super(name, description);
        this.setType(TaskType.EPIC);
    }

    public EpicTask(Long id, TaskType type, String name, TaskStatus status, String description) {
        super(id, type, name, status, description);
    }

    public EpicTask(Long id, String name, String description) {
        super(id, name, description);
    }

    public EpicTask(Long id, TaskType type, String name, TaskStatus status, String description, LocalDateTime startTime, Duration duration) {
        super(id, type, name, status, description, startTime, duration);
    }

    public List<Long> getSubTasksOfEpicList() {
        return subTasksOfEpicIdList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicTask epicTask = (EpicTask) o;
        return Objects.equals(subTasksOfEpicIdList, epicTask.subTasksOfEpicIdList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksOfEpicIdList);
    }
}



