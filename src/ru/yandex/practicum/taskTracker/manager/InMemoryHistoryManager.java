package ru.yandex.practicum.taskTracker.manager;

import ru.yandex.practicum.taskTracker.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_HISTORY_SIZE = 10;

    private List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        history.add(task);
        checkHistorySize();
    }

    @Override
    public List<Task> getHistory() {
        System.out.println(history.toString());
        return history;
    }

    private void checkHistorySize() {
        if (history.size() > MAX_HISTORY_SIZE) {
            history.remove(0);
        }
    }

}
