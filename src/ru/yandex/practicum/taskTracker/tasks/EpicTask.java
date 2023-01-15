package ru.yandex.practicum.taskTracker.tasks;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {
    private List<Long> subTasksOfEpicIdList = new ArrayList<>();

    public EpicTask(String name, String description) {
        super(name, description);
        this.setType(TaskType.EPIC);
    }

    public EpicTask(Long id, TaskType type, String name, TaskStatus status, String description) {
        super(id, type, name, status, description);
    }

    public List<Long> getSubTasksOfEpicList() {
        return subTasksOfEpicIdList;
    }

    public void setSubTasksOfEpicList(List<Long> subTasksOfEpicList) {
        this.subTasksOfEpicIdList = subTasksOfEpicList;
    }
}

