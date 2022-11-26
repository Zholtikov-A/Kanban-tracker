package ru.yandex.practicum.taskTracker.manager;

public class Managers {

    private static HistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return inMemoryHistoryManager;
    }

}
