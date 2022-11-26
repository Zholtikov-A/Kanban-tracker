package ru.yandex.practicum.taskTracker.manager;

import ru.yandex.practicum.taskTracker.tasks.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();
}
