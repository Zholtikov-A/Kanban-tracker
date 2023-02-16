package ru.yandex.practicum.taskTracker.manager;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager> {

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        createTask();
    }
}
