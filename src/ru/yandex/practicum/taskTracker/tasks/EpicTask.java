package ru.yandex.practicum.taskTracker.tasks;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {
    private List<Long> subTasksOfEpicIdList = new ArrayList<>();

    public EpicTask(String name, String description) {
        super(name, description);
    }

    public List<Long> getSubTasksOfEpicList() {
        return subTasksOfEpicIdList;
    }

    public void setSubTasksOfEpicList(List<Long> subTasksOfEpicList) {
        this.subTasksOfEpicIdList = subTasksOfEpicList;
    }
}

