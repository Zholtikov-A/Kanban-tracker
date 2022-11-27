package ru.yandex.practicum.taskTracker.manager;

import ru.yandex.practicum.taskTracker.tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_HISTORY_SIZE = 10;

    private List<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        history.add(task);
        checkHistorySize();
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }

    private void checkHistorySize() {
        LinkedList<Task> history = (LinkedList<Task>) getHistory(); //сделал тип ссылки LinkedList, чтобы воспользоваться removeFirst
        if (history.size() > MAX_HISTORY_SIZE) {
            history.removeFirst();
        }
    }

}
