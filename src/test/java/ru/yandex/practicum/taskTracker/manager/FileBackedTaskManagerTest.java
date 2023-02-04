package ru.yandex.practicum.taskTracker.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskTracker.tasks.EpicTask;
import ru.yandex.practicum.taskTracker.tasks.SubTask;
import ru.yandex.practicum.taskTracker.tasks.Task;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private File file;

    @BeforeEach
    public void setUp() {
        file = new File("src/main/java/ru/yandex/practicum/taskTracker/dataStorage/FileManager.csv");
        taskManager = new FileBackedTaskManager(file);
        createTask();
    }

    @AfterEach
    protected void tearDown() {
        assertTrue(file.delete());
    }

    @Test
    public void loadFromFile() {

        taskManager.getSubTaskById(subTaskId7);
        taskManager.getEpicTaskById(epicTaskId6);
        taskManager.getSubTaskById(subTaskId5);
        taskManager.getSubTaskById(subTaskId4);
        taskManager.getEpicTaskById(epicTaskId3);
        taskManager.getSimpleTaskById(simpleTaskId2);
        taskManager.getSimpleTaskById(simpleTaskId1);

        final List<Task> expectedTasks = taskManager.showSimpleTasks();
        final List<EpicTask> expectedEpicTasks = taskManager.showEpicTasks();
        final List<SubTask> expectedSubTasks = taskManager.showSubTasks();

        FileBackedTaskManager tasksManagerRestored = FileBackedTaskManager.loadFromFile(file);
        final List<Task> tasks = tasksManagerRestored.showSimpleTasks();
        final List<EpicTask> epicTasks = tasksManagerRestored.showEpicTasks();
        final List<SubTask> subTasks = tasksManagerRestored.showSubTasks();
        assertNotNull(tasks, "Возвращает пустой список задач.");
        assertNotNull(epicTasks, "Возвращает пустой список задач.");
        assertNotNull(subTasks, "Возвращает пустой список задач.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(2, epicTasks.size(), "Неверное количество задач.");
        assertEquals(3, subTasks.size(), "Неверное количество задач.");
        assertEquals(expectedTasks, tasks, "Списки задач не совпадают.");
        assertEquals(expectedEpicTasks, epicTasks, "Списки задач не совпадают.");
        assertEquals(expectedSubTasks, subTasks, "Списки задач не совпадают.");
    }
}
