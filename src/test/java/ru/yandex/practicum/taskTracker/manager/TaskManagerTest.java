package ru.yandex.practicum.taskTracker.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskTracker.tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected LocalDateTime testStartTime1;
    protected LocalDateTime testStartTime1Plus15m;
    protected Duration duration10H;
    protected LocalDateTime testStartTime2;
    protected LocalDateTime testStartTime2Plus15m;
    protected Duration duration2H;
    protected LocalDateTime testStartTime3;
    protected LocalDateTime testStartTime3Minus15m;
    protected Duration duration5H;
    protected Long simpleTaskId1;
    protected Long simpleTaskId2;
    protected Long epicTaskId3;
    protected Long subTaskId4;
    protected Long subTaskId5;
    protected Long epicTaskId6;
    protected Long subTaskId7;

    protected void createTask() {
        testStartTime1 = LocalDateTime.of(2023, Month.MARCH, 5, 14, 0, 0);
        testStartTime1Plus15m = testStartTime1.plusMinutes(15);
        duration10H = Duration.ofHours(10);
        testStartTime2 = LocalDateTime.of(2023, Month.MARCH, 1, 18, 30, 0);
        testStartTime2Plus15m = testStartTime2.plusMinutes(15);
        duration2H = Duration.ofHours(2);
        testStartTime3 = LocalDateTime.of(2023, Month.MARCH, 3, 10, 0, 0);
        testStartTime3Minus15m = testStartTime3.minusMinutes(15);
        duration5H = Duration.ofHours(5);

        simpleTaskId1 = taskManager.saveSimpleTask(
                new Task("First SimpleTask", "SimpleTask(ID=1) without DateTime"));
        simpleTaskId2 = taskManager.saveSimpleTask(
                new Task("Second SimpleTask", "SimpleTask(ID=2) with DateTime", testStartTime1, duration10H));
        epicTaskId3 = taskManager.saveEpicTask(
                new EpicTask("First EpicTask", "EpicTask(ID=3) with DateTime"));
        subTaskId4 = taskManager.saveSubTask(
                new SubTask("First SubTask", "SubTask(ID=4) of first EpicTask(ID=3) with DateTime",
                        testStartTime2, duration2H, epicTaskId3));
        subTaskId5 = taskManager.saveSubTask(
                new SubTask("Second SubTask", "SubTask(ID=5) of first EpicTask(ID=3) with DateTime",
                        testStartTime3, duration5H, epicTaskId3));
        epicTaskId6 = taskManager.saveEpicTask(
                new EpicTask("Second EpicTask", "EpicTask(ID=6 without DateTime)"));
        subTaskId7 = taskManager.saveSubTask(
                new SubTask("Third SubTask", "SubTask(ID=7) of first EpicTask(ID=6) without DateTime", epicTaskId6));
    }

    @Test
    void saveSimpleTaskSuccessfulCreationNewSimpleTaskWithDateTime() {
        taskManager.removeAllSimpleTasks();
        Task simpleTask = new Task(
                "Second SimpleTask", "SimpleTask(ID=2) with DateTime", testStartTime1, duration10H);
        final Long simpleTaskId8 = taskManager.saveSimpleTask(simpleTask);
        Task expectedSimpleTask = new Task(
                simpleTaskId8, TaskType.TASK, "Second SimpleTask", TaskStatus.NEW,
                "SimpleTask(ID=2) with DateTime", testStartTime1, duration10H);
        final Task savedSimpleTask = taskManager.getSimpleTaskById(simpleTaskId8);
        assertNotNull(savedSimpleTask, "Задача не найдена.");
        assertEquals(simpleTask, savedSimpleTask, "Задачи не совпадают.");
        assertEquals(expectedSimpleTask, savedSimpleTask, "Задача не совпадает с введенной вручную.");
        final List<Task> testSimpleTasks = taskManager.showSimpleTasks();
        assertNotNull(testSimpleTasks, "Задачи нe возвращаются.");
        assertEquals(1, testSimpleTasks.size(), "Неверное количество задач.");
        assertEquals(simpleTask, testSimpleTasks.get(0), "Задачи не совпадают.");
        assertNotNull(taskManager.getSimpleTaskById(simpleTaskId8).getStartTime(),
                "Отсутствует время начала задачи.");
        assertNotNull(taskManager.getSimpleTaskById(simpleTaskId8).getDuration(),
                "Отсутствует продолжительность задачи.");
        assertEquals(taskManager.getSimpleTaskById(simpleTaskId8).getStartTime(), testStartTime1,
                "Время начала задачи  не соответствует заданной.");
        assertEquals(taskManager.getSimpleTaskById(simpleTaskId8).getDuration(), duration10H,
                "Длительность задачи не соответствует заданной.");
    }

    @Test
    void saveSimpleCreationFailTaskDateTimeCollisionTroubleReturnIdMinus1() {
        taskManager.removeAllSimpleTasks();
        Task simpleTask = new Task("Second SimpleTask", "SimpleTask(ID=8) with DateTime", testStartTime1,
                duration10H);
        final Long simpleTaskId8 = taskManager.saveSimpleTask(simpleTask);
        final Long collisionFailIdMinus1 = taskManager.saveSimpleTask(simpleTask);
        final Task failedSimpleTaskIsNull = taskManager.getSimpleTaskById(collisionFailIdMinus1);
        assertNull(failedSimpleTaskIsNull, "Задача создана вопреки ошибке коллизии времени.");
        assertEquals(-1L, collisionFailIdMinus1,
                "ID задачи, не созданной из-за ошибки коллизии времени задан неверно.");
    }

    @Test
    void updateSimpleTaskSuccessfulUpdateSimpleTaskWithDateTime() {
        taskManager.removeSimpleTaskById(simpleTaskId1);
        Task expectedSimpleTask = new Task(simpleTaskId2, TaskType.TASK, "Second SimpleTask",
                TaskStatus.IN_PROGRESS, "SimpleTask(ID=2) with DateTime", testStartTime1Plus15m, duration10H);
        taskManager.updateSimpleTask(expectedSimpleTask);
        final Task updatedSimpleTask = taskManager.getSimpleTaskById(simpleTaskId2);
        assertNotNull(updatedSimpleTask, "Задача не найдена.");
        assertEquals(expectedSimpleTask, updatedSimpleTask, "Задачи не совпадают.");
        final List<Task> testSimpleTasks = taskManager.showSimpleTasks();
        assertNotNull(testSimpleTasks, "Задачи нe возвращаются.");
        assertEquals(1, testSimpleTasks.size(), "Неверное количество задач.");
        assertEquals(expectedSimpleTask, testSimpleTasks.get(0), "Задачи не совпадают.");
        assertNotNull(taskManager.getSimpleTaskById(simpleTaskId2).getStartTime(),
                "Отсутствует время начала задачи.");
        assertNotNull(taskManager.getSimpleTaskById(simpleTaskId2).getDuration(),
                "Отсутствует продолжительность задачи.");
        assertEquals(taskManager.getSimpleTaskById(simpleTaskId2).getStartTime(), expectedSimpleTask.getStartTime(),
                "Время начала задач не совпадает.");
        assertEquals(taskManager.getSimpleTaskById(simpleTaskId2).getDuration(), expectedSimpleTask.getDuration(),
                "Длительность задач не совпадает.");
    }

    @Test
    void updateSimpleTaskFailTaskDateTimeCollisionTroubleInitialTaskSaved() {
        taskManager.removeSimpleTaskById(simpleTaskId1);
        Task initialSimpleTask = taskManager.getSimpleTaskById(simpleTaskId2);
        LocalDateTime occupiedStartTime = taskManager.getSubTaskById(subTaskId4).getStartTime();
        Task expectedSimpleTask = new Task(simpleTaskId2, TaskType.TASK, "Second SimpleTask", TaskStatus.IN_PROGRESS,
                "SimpleTask(ID=2) with DateTime Collision Trouble", occupiedStartTime, duration10H);
        taskManager.updateSimpleTask(expectedSimpleTask);
        final Task updatedSimpleTask = taskManager.getSimpleTaskById(simpleTaskId2);
        assertNotNull(updatedSimpleTask, "Задача не найдена.");
        assertNotEquals(expectedSimpleTask, updatedSimpleTask, "Задача обновлена вопреки ошибке коллизии времени.");
        assertEquals(initialSimpleTask, updatedSimpleTask, "Исходная задача не сохранилась.");
        final List<Task> testSimpleTasks = taskManager.showSimpleTasks();
        assertNotNull(testSimpleTasks, "Задачи нe возвращаются.");
        assertEquals(1, testSimpleTasks.size(), "Неверное количество задач.");
    }

    @Test
    void updateSimpleTaskFailInCauseOfWrongId() {
        final List<Task> initialSimpleTasks = taskManager.showSimpleTasks();
        Task expectedSimpleTask = new Task(0L, TaskType.TASK, "Second SimpleTask", TaskStatus.IN_PROGRESS,
                "SimpleTask(ID=0) with free ID", testStartTime1Plus15m, duration10H);
        taskManager.updateSimpleTask(expectedSimpleTask);
        final Task updatedTaskExpectedIdZero = taskManager.getSimpleTaskById(0L);
        assertNull(updatedTaskExpectedIdZero, "Обновленная задача с не существовавшим ранее ID сохранена.");
        final List<Task> testSimpleTasks = taskManager.showSimpleTasks();
        assertEquals(2, testSimpleTasks.size(), "Неверное количество задач.");
        assertEquals(initialSimpleTasks, testSimpleTasks, "Списки задач отличаются.");
    }

    @Test
    void updateSimpleTaskFailInCauseOfIdOfWrongTaskType() {
        final List<Task> initialSimpleTasks = taskManager.showSimpleTasks();
        Task expectedSimpleTask = new Task(subTaskId4, TaskType.TASK, "Second SimpleTask", TaskStatus.IN_PROGRESS,
                "SimpleTask(ID=0) with free ID", testStartTime1Plus15m, duration10H);
        taskManager.updateSimpleTask(expectedSimpleTask);
        final Task updatedTaskExpectedId4 = taskManager.getSimpleTaskById(subTaskId4);
        assertNull(updatedTaskExpectedId4, "Обновленная задача с ID задачи другого типа сохранена.");
        final List<Task> testSimpleTasks = taskManager.showSimpleTasks();
        assertEquals(2, testSimpleTasks.size(), "Неверное количество задач.");
        assertEquals(initialSimpleTasks, testSimpleTasks, "Списки задач отличаются.");
    }

    @Test
    void updateSimpleTaskFailInCauseOfEmptyId() {
        final List<Task> initialSimpleTasks = taskManager.showSimpleTasks();
        Task expectedSimpleTask = new Task(null, TaskType.TASK, "Second SimpleTask", TaskStatus.IN_PROGRESS,
                "SimpleTask(ID=0) with free ID", testStartTime1Plus15m, duration10H);
        taskManager.updateSimpleTask(expectedSimpleTask);
        final Task updatedTaskExpectedIdNull = taskManager.getSimpleTaskById(null);
        assertNull(updatedTaskExpectedIdNull, "Обновленная задача без ID сохранена.");
        final List<Task> testSimpleTasks = taskManager.showSimpleTasks();
        assertEquals(2, testSimpleTasks.size(), "Неверное количество задач.");
        assertEquals(initialSimpleTasks, testSimpleTasks, "Списки задач отличаются.");
    }

    @Test
    void showSimpleTasksSuccessfulReturnListOfTasks() {
        Task expectedSimpleTask = new Task(simpleTaskId2, TaskType.TASK, "Second SimpleTask", TaskStatus.NEW,
                "SimpleTask(ID=2) with DateTime", testStartTime1, duration10H);
        final List<Task> testSimpleTasks = taskManager.showSimpleTasks();
        assertNotNull(testSimpleTasks, "Задачи нe возвращаются.");
        assertEquals(2, testSimpleTasks.size(), "Неверное количество задач.");
        assertEquals(expectedSimpleTask, testSimpleTasks.get(1), "Задачи не совпадают.");
    }

    @Test
    void showSimpleTasksFailReturnEmptyList() {
        taskManager.removeAllSimpleTasks();
        final List<Task> testSimpleTasks = taskManager.showSimpleTasks();
        assertNotNull(testSimpleTasks, "Пустой список задач не возвращается.");
        assertTrue(testSimpleTasks.isEmpty(), "Список задач не пуст.");
        assertEquals(0, testSimpleTasks.size(), "Неверное количество задач.");
    }

    @Test
    void removeAllSimpleTasksSuccessfullyRemovedAllSimpleTasks() {
        taskManager.removeAllSimpleTasks();
        final List<Task> emptyTestSimpleTasks = taskManager.showSimpleTasks();
        assertNotNull(emptyTestSimpleTasks, "Пустой список задач не возвращается.");
        assertTrue(emptyTestSimpleTasks.isEmpty(), "Список задач не пуст.");
        assertEquals(0, emptyTestSimpleTasks.size(), "Неверное количество задач.");
    }

    @Test
    void removeAllSimpleTasksFromEmptySimpleTaskMap() {
        taskManager.removeAllSimpleTasks();
        final List<Task> emptyTestSimpleTasks = taskManager.showSimpleTasks();
        assertNotNull(emptyTestSimpleTasks, "Пустой список задач не возвращается.");
        assertTrue(emptyTestSimpleTasks.isEmpty(), "Список задач не пуст.");
        assertEquals(0, emptyTestSimpleTasks.size(), "Неверное количество задач.");
        taskManager.removeAllSimpleTasks();
        final List<Task> emptyEmptySimpleTasks = taskManager.showSimpleTasks();
        assertNotNull(emptyEmptySimpleTasks, "Пустой список задач не возвращается.");
        assertTrue(emptyEmptySimpleTasks.isEmpty(), "Список задач не пуст.");
        assertEquals(0, emptyEmptySimpleTasks.size(), "Неверное количество задач.");
        assertEquals(emptyTestSimpleTasks, emptyEmptySimpleTasks, "Списки не совпадают.");
    }

    @Test
    void getSimpleTaskByIdSuccessfulReturnSimpleTask() {
        Task expectedSimpleTask = new Task(simpleTaskId2, TaskType.TASK, "Second SimpleTask", TaskStatus.NEW,
                "SimpleTask(ID=2) with DateTime", testStartTime1, duration10H);
        final Task savedSimpleTask = taskManager.getSimpleTaskById(simpleTaskId2);
        assertNotNull(savedSimpleTask, "Задача не найдена.");
        assertEquals(expectedSimpleTask, savedSimpleTask, "Задачи не совпадают.");
    }

    @Test
    void getSimpleTaskByIdReturnFailWrongId() {
        final Task savedSimpleTask = taskManager.getSimpleTaskById(0L);
        assertNull(savedSimpleTask, "Обращение по несуществующему ID возвращает задачу.");
    }

    @Test
    void getSimpleTaskByIdReturnFailIdOfWrongTaskType() {
        final Task savedSimpleTask = taskManager.getSimpleTaskById(subTaskId4);
        assertNull(savedSimpleTask, "Обращение по  ID задачи другого типа возвращает задачу данного типа.");
    }

    @Test
    void getSimpleTaskByIdReturnFailEmptyId() {
        final Task savedSimpleTask = taskManager.getSimpleTaskById(null);
        assertNull(savedSimpleTask, "Обращение по  ID null возвращает задачу.");
    }

    @Test
    void removeSimpleTaskByIdSuccessfullyRemovedTask() {
        final List<Task> initialSimpleTasks = taskManager.showSimpleTasks();
        taskManager.removeSimpleTaskById(simpleTaskId2);
        final List<Task> testSimpleTasks = taskManager.showSimpleTasks();
        assertEquals(1, testSimpleTasks.size(), "Неверное количество задач.");
        assertNotEquals(initialSimpleTasks, testSimpleTasks, "Списки совпадают.");
        assertNotEquals(initialSimpleTasks.size(), testSimpleTasks.size(), "Размер списков совпадает.");
        assertNull(taskManager.getSimpleTaskById(simpleTaskId2), "Задача не удалена.");
    }

    @Test
    void removeSimpleTaskByIdFailWrongId() {
        final List<Task> initialSimpleTasks = taskManager.showSimpleTasks();
        taskManager.removeSimpleTaskById(0L);
        final List<Task> testSimpleTasks = taskManager.showSimpleTasks();
        assertEquals(2, testSimpleTasks.size(), "Неверное количество задач.");
        assertEquals(initialSimpleTasks, testSimpleTasks, "Списки не совпадают.");
        assertEquals(initialSimpleTasks.size(), testSimpleTasks.size(), "Размер списков не совпадает.");
    }

    @Test
    void removeSimpleTaskByIdFailWrongTaskTypeId() {
        final List<Task> initialSimpleTasks = taskManager.showSimpleTasks();
        taskManager.removeSimpleTaskById(subTaskId4);
        final List<Task> testSimpleTasks = taskManager.showSimpleTasks();
        assertEquals(2, testSimpleTasks.size(), "Неверное количество задач.");
        assertEquals(initialSimpleTasks, testSimpleTasks, "Списки не совпадают.");
        assertEquals(initialSimpleTasks.size(), testSimpleTasks.size(), "Размер списков не совпадает.");
    }

    @Test
    void removeSimpleTaskByIdFailEmptyId() {
        final List<Task> initialSimpleTasks = taskManager.showSimpleTasks();
        taskManager.removeSimpleTaskById(null);
        final List<Task> testSimpleTasks = taskManager.showSimpleTasks();
        assertEquals(2, testSimpleTasks.size(), "Неверное количество задач.");
        assertEquals(initialSimpleTasks, testSimpleTasks, "Списки не совпадают.");
        assertEquals(initialSimpleTasks.size(), testSimpleTasks.size(), "Размер списков не совпадает.");
    }

    @Test
    void saveSubTaskSuccessfulCreationNewSubTaskWithDateTime() {
        taskManager.removeAllSubTasks();
        SubTask subTask = new SubTask("First SubTask", "SubTask(ID=8) of first EpicTask(ID=3) with DateTime",
                testStartTime2, duration2H, epicTaskId3);
        final Long subTaskId8 = taskManager.saveSubTask(subTask);
        final SubTask savedSubTask = taskManager.getSubTaskById(subTaskId8);
        SubTask expectedSubTask = new SubTask(subTaskId8, TaskType.SUBTASK, "First SubTask", TaskStatus.NEW,
                "SubTask(ID=8) of first EpicTask(ID=3) with DateTime", testStartTime2, duration2H, epicTaskId3);
        assertNotNull(taskManager.getEpicTaskById(savedSubTask.getEpicTaskID()), "EpicTask  отсутствует.");
        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTask, savedSubTask, "Задачи не совпадают.");
        assertEquals(expectedSubTask, savedSubTask, "Задачи не совпадают.");
        assertTrue(taskManager.getEpicTaskById(savedSubTask.getEpicTaskID()).getSubTasksOfEpicList().contains(savedSubTask.getId()),
                "Задача не добавлена в subTasksOfEpicList.");
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertNotNull(testSubTasks, "Задачи нe возвращаются.");
        assertEquals(1, testSubTasks.size(), "Неверное количество задач.");
        assertEquals(subTask, testSubTasks.get(0), "Задачи не совпадают.");
        final List<Long> testSubTasksOfEpic = taskManager.getEpicTaskById(savedSubTask.getEpicTaskID()).getSubTasksOfEpicList();
        assertEquals(1, testSubTasksOfEpic.size(), "Неверное количество задач.");
        assertEquals(subTask, taskManager.getSubTaskById(testSubTasksOfEpic.get(0)), "Задачи не совпадают.");
        assertNotNull(taskManager.getSubTaskById(subTaskId8).getStartTime(), "Отсутствует время начала задачи.");
        assertNotNull(taskManager.getSubTaskById(subTaskId8).getDuration(), "Отсутствует продолжительность задачи.");
        assertEquals(taskManager.getSubTaskById(subTaskId8).getStartTime(), testStartTime2,
                "Время начала задач не совпадает.");
        assertEquals(taskManager.getSubTaskById(subTaskId8).getDuration(), duration2H,
                "Длительность задач не совпадает.");
        SubTask subTask2 = new SubTask("Second SubTask", "SubTask(ID=9) of first EpicTask(ID=3) with DateTime",
                testStartTime3, duration5H, epicTaskId3);
        final Long subTaskId9 = taskManager.saveSubTask(subTask2);
        LocalDateTime epicStartTime;
        if (taskManager.getSubTaskById(subTaskId8).getStartTime().isBefore(taskManager.getSubTaskById(subTaskId9).getStartTime())) {
            epicStartTime = taskManager.getSubTaskById(subTaskId8).getStartTime();
        } else {
            epicStartTime = taskManager.getSubTaskById(subTaskId9).getStartTime();
        }
        final Duration epicDuration = taskManager.getSubTaskById(subTaskId8).getDuration().
                plus(taskManager.getSubTaskById(subTaskId9).getDuration());
        assertNotNull(taskManager.getEpicTaskById(epicTaskId3).getStartTime(), "Отсутствует время начала Epic задачи.");
        assertNotNull(taskManager.getEpicTaskById(epicTaskId3).getDuration(), "Отсутствует продолжительность Epic задачи.");
        assertEquals(taskManager.getEpicTaskById(epicTaskId3).getStartTime(), epicStartTime,
                "Неверно пересчитано время начала Epic задачи.");
        assertEquals(taskManager.getEpicTaskById(epicTaskId3).getDuration(), epicDuration,
                "Неверно пересчитана длительность Epic задачи.");
    }

    @Test
    void saveSubTaskCreationFailTaskDateTimeCollisionTroubleReturnIdMinus1L() {
        taskManager.removeAllSubTasks();
        SubTask subTask = new SubTask("First SubTask", "SubTask(ID=8) of first EpicTask(ID=3) with DateTime",
                testStartTime2, duration2H, epicTaskId3);
        final Long subTaskId8 = taskManager.saveSubTask(subTask);
        final Long collisionFailIdMinus1 = taskManager.saveSubTask(subTask);
        final SubTask failedSubTaskIsNull = taskManager.getSubTaskById(collisionFailIdMinus1);
        assertNull(failedSubTaskIsNull, "Задача создана вопреки ошибке коллизии времени.");
        assertEquals(-1L, collisionFailIdMinus1,
                "ID задачи, не созданной из-за ошибки коллизии времени задан неверно.");
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertNotNull(testSubTasks, "Задачи нe возвращаются.");
        assertEquals(1, testSubTasks.size(), "Неверное количество задач.");
    }

    @Test
    void saveSubTaskFailNoSuchEpicTaskReturnIdMinus2L() {
        taskManager.removeAllEpicTasks();
        SubTask subTask = new SubTask("First SubTask", "SubTask(ID=8) of first EpicTask(ID=3) with DateTime",
                testStartTime2, duration2H, epicTaskId3);
        final Long noSuchEpicFailIdMinus2 = taskManager.saveSubTask(subTask);
        final SubTask savedSubTask = taskManager.getSubTaskById(noSuchEpicFailIdMinus2);
        assertNull(taskManager.getEpicTaskById(subTask.getEpicTaskID()), "По запрашиваемому ID возвращается Epic.");
        assertNotEquals(subTask, savedSubTask, "Задачи не совпадают.");
        assertNull(taskManager.getSubTaskById(noSuchEpicFailIdMinus2), "SubTask создана вопреки отсутствию EpicTask");
        assertEquals(-2L, noSuchEpicFailIdMinus2,
                "ID задачи, не созданной из-за отсутствия EpicTask задан неверно.");
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertNotNull(testSubTasks, "Задачи нe возвращаются.");
        assertEquals(0, testSubTasks.size(), "Неверное количество задач.");
        assertTrue(testSubTasks.isEmpty(), "Список не пуст.");
    }

    @Test
    void updateSubTaskSuccessfulUpdateSubTaskWithDateTime() {
        final TaskStatus initialEpicStatus = taskManager.getEpicTaskById(epicTaskId3).getStatus();
        SubTask expectedSubTask = new SubTask(subTaskId4, TaskType.SUBTASK, "First SubTask", TaskStatus.IN_PROGRESS,
                "SubTask(ID=4) of first EpicTask(ID=3) with DateTime", testStartTime2Plus15m, duration2H, epicTaskId3);
        taskManager.updateSubTask(expectedSubTask);
        final SubTask updatedSubTask = taskManager.getSubTaskById(subTaskId4);
        assertNotNull(updatedSubTask, "Задача не найдена.");
        assertEquals(expectedSubTask, updatedSubTask, "Задачи не совпадают.");
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertNotNull(testSubTasks, "Задачи нe возвращаются.");
        assertEquals(3, testSubTasks.size(), "Неверное количество задач.");
        assertEquals(expectedSubTask, testSubTasks.get(0), "Задачи не совпадают.");
        final List<Long> testSubTasksOfEpic = taskManager.getEpicTaskById(epicTaskId3).getSubTasksOfEpicList();
        assertEquals(2, testSubTasksOfEpic.size(), "Неверное количество задач.");
        assertEquals(expectedSubTask, taskManager.getSubTaskById(testSubTasksOfEpic.get(0)), "Задачи не совпадают.");
        final TaskStatus newEpicStatus = taskManager.getEpicTaskById(epicTaskId3).getStatus();
        assertNotEquals(initialEpicStatus, newEpicStatus, "Статус Epic задачи не пересчитан.");
        assertNotNull(taskManager.getSubTaskById(subTaskId4).getStartTime(), "Отсутствует время начала задачи.");
        assertNotNull(taskManager.getSubTaskById(subTaskId4).getDuration(), "Отсутствует продолжительность задачи.");
        assertEquals(taskManager.getSubTaskById(subTaskId4).getStartTime(), expectedSubTask.getStartTime(),
                "Время начала задач не совпадает.");
        assertEquals(taskManager.getSubTaskById(subTaskId4).getDuration(), expectedSubTask.getDuration(),
                "Длительность задач не совпадает.");
        LocalDateTime epicStartTime;
        if (taskManager.getSubTaskById(subTaskId4).getStartTime().isBefore(taskManager.getSubTaskById(subTaskId5).getStartTime())) {
            epicStartTime = taskManager.getSubTaskById(subTaskId4).getStartTime();
        } else {
            epicStartTime = taskManager.getSubTaskById(subTaskId5).getStartTime();
        }
        final Duration epicDuration = taskManager.getSubTaskById(subTaskId4).getDuration().
                plus(taskManager.getSubTaskById(subTaskId5).getDuration());
        assertNotNull(taskManager.getEpicTaskById(epicTaskId3).getStartTime(), "Отсутствует время начала Epic задачи.");
        assertNotNull(taskManager.getEpicTaskById(epicTaskId3).getDuration(), "Отсутствует продолжительность Epic задачи.");
        assertEquals(taskManager.getEpicTaskById(epicTaskId3).getStartTime(), epicStartTime,
                "Неверно пересчитано время начала Epic задачи.");
        assertEquals(taskManager.getEpicTaskById(epicTaskId3).getDuration(), epicDuration,
                "Неверно пересчитана длительность Epic задачи.");
    }

    @Test
    void updateSubTaskFailSubTaskDateTimeCollisionTroubleInitialSubTaskSaved() {
        SubTask initialSubTask = taskManager.getSubTaskById(subTaskId4);
        LocalDateTime occupiedStartTime = taskManager.getSimpleTaskById(simpleTaskId2).getStartTime();
        SubTask expectedSubTask = new SubTask(subTaskId4, TaskType.SUBTASK, "First SubTask", TaskStatus.IN_PROGRESS,
                "SubTask(ID=4) of first EpicTask(ID=3) with DateTime", occupiedStartTime, duration2H, epicTaskId3);
        taskManager.updateSubTask(expectedSubTask);
        final SubTask updatedSubTask = taskManager.getSubTaskById(subTaskId4);
        assertNotNull(updatedSubTask, "Задача не найдена.");
        assertNotEquals(expectedSubTask, updatedSubTask, "Задача обновлена вопреки ошибке коллизии времени.");
        assertEquals(initialSubTask, updatedSubTask, "Исходная задача не сохранилась.");
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertNotNull(testSubTasks, "Задачи нe возвращаются.");
        assertEquals(3, testSubTasks.size(), "Неверное количество задач.");
    }

    @Test
    void updateSubTaskFailNoSuchEpic() {
        final List<SubTask> initialSubTasks = taskManager.showSubTasks();
        SubTask initialSubTask = taskManager.getSubTaskById(subTaskId4);
        final List<Long> subTasksOfEpicId3 = taskManager.getEpicTaskById(initialSubTask.getEpicTaskID()).getSubTasksOfEpicList();
        SubTask expectedSubTask = new SubTask(subTaskId4, TaskType.SUBTASK, "First SubTask", TaskStatus.IN_PROGRESS,
                "SubTask(ID=4) of first EpicTask(ID=0) with DateTime", testStartTime2Plus15m, duration2H, 0L);
        taskManager.updateSubTask(expectedSubTask);
        final SubTask updatedSubTask = taskManager.getSubTaskById(subTaskId4);
        assertNotNull(updatedSubTask, "Задача не найдена.");
        assertNotEquals(expectedSubTask, updatedSubTask, "Задача обновлена вопреки отсутствию EpicTask.");
        assertEquals(initialSubTask, updatedSubTask, "Исходная задача не сохранилась.");
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertNotNull(testSubTasks, "Задачи нe возвращаются.");
        assertEquals(3, testSubTasks.size(), "Неверное количество задач.");
        final List<Long> newSubTasksOfEpicId3 = taskManager.getEpicTaskById(initialSubTask.getEpicTaskID()).getSubTasksOfEpicList();
        assertEquals(subTasksOfEpicId3, newSubTasksOfEpicId3, "Изменен список подзадач EpicTask.");
        assertEquals(3, testSubTasks.size(), "Неверное количество задач.");
        assertEquals(initialSubTasks, testSubTasks, "Списки задач отличаются.");
    }

    @Test
    void updateSubTaskFailTryToChangeEpicTaskIdToExisting() {
        final List<SubTask> initialSubTasks = taskManager.showSubTasks();
        final List<Long> subTasksOfEpicId3 = taskManager.getEpicTaskById(epicTaskId3).getSubTasksOfEpicList();
        final List<Long> subTasksOfEpicId6 = taskManager.getEpicTaskById(epicTaskId6).getSubTasksOfEpicList();
        SubTask initialSubTask = taskManager.getSubTaskById(subTaskId4);
        SubTask expectedSubTask = new SubTask(subTaskId4, TaskType.SUBTASK, "First SubTask", TaskStatus.IN_PROGRESS,
                "SubTask(ID=4) of first EpicTask(ID=6) with DateTime", testStartTime2Plus15m, duration2H, epicTaskId6);
        taskManager.updateSubTask(expectedSubTask);
        final SubTask updatedSubTask = taskManager.getSubTaskById(subTaskId4);
        assertNotNull(updatedSubTask, "Задача не найдена.");
        assertNotEquals(expectedSubTask, updatedSubTask, "Задача обновлена вопреки запрета смены EpicTask.");
        assertEquals(initialSubTask, updatedSubTask, "Исходная задача не сохранилась.");
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertNotNull(testSubTasks, "Задачи нe возвращаются.");
        assertEquals(3, testSubTasks.size(), "Неверное количество задач.");
        final List<Long> newSubTasksOfEpicId3 = taskManager.getEpicTaskById(epicTaskId3).getSubTasksOfEpicList();
        final List<Long> newSubTasksOfEpicId6 = taskManager.getEpicTaskById(epicTaskId6).getSubTasksOfEpicList();
        assertEquals(subTasksOfEpicId3, newSubTasksOfEpicId3, "Изменен список подзадач EpicTask.");
        assertEquals(subTasksOfEpicId6, newSubTasksOfEpicId6, "Изменен список подзадач EpicTask.");
        assertEquals(3, testSubTasks.size(), "Неверное количество задач.");
        assertEquals(initialSubTasks, testSubTasks, "Списки задач отличаются.");
    }

    @Test
    void updateSubTaskFailInCauseOfWrongId() {
        final List<SubTask> initialSubTasks = taskManager.showSubTasks();
        SubTask expectedSubTask = new SubTask(0L, TaskType.SUBTASK, "First SubTask", TaskStatus.IN_PROGRESS,
                "SubTask(ID=0) with free ID", testStartTime2Plus15m, duration2H, epicTaskId3);
        taskManager.updateSubTask(expectedSubTask);
        final SubTask updatedSubTaskExpectedIdZero = taskManager.getSubTaskById(0L);
        assertNull(updatedSubTaskExpectedIdZero, "Обновленная задача с не существовавшим ранее ID сохранена.");
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertEquals(3, testSubTasks.size(), "Неверное количество задач.");
        assertEquals(initialSubTasks, testSubTasks, "Списки задач отличаются.");
    }

    @Test
    void updateSubTaskFailInCauseOfIdOfWrongTaskType() {
        final List<SubTask> initialSubTasks = taskManager.showSubTasks();
        SubTask expectedSubTask = new SubTask(simpleTaskId2, TaskType.SUBTASK, "First SubTask", TaskStatus.IN_PROGRESS,
                "SubTask(ID=0) with wrong ID", testStartTime2Plus15m, duration2H, epicTaskId3);
        taskManager.updateSubTask(expectedSubTask);
        final SubTask updatedSubTaskExpectedId2 = taskManager.getSubTaskById(simpleTaskId2);
        assertNull(updatedSubTaskExpectedId2, "Обновленная задача с ID задачи другого типа сохранена.");
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertEquals(3, testSubTasks.size(), "Неверное количество задач.");
        assertEquals(initialSubTasks, testSubTasks, "Списки задач отличаются.");
    }

    @Test
    void updateSubTaskFailInCauseOfEmptyId() {
        final List<SubTask> initialSubTasks = taskManager.showSubTasks();
        SubTask expectedSubTask = new SubTask(null, TaskType.SUBTASK, "First SubTask", TaskStatus.IN_PROGRESS,
                "SubTask(ID=0) with empty ID", testStartTime2Plus15m, duration2H, epicTaskId3);
        taskManager.updateSubTask(expectedSubTask);
        final SubTask updatedSubTaskExpectedId2 = taskManager.getSubTaskById(null);
        assertNull(updatedSubTaskExpectedId2, "Обновленная задача без ID сохранена.");
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertEquals(3, testSubTasks.size(), "Неверное количество задач.");
        assertEquals(initialSubTasks, testSubTasks, "Списки задач отличаются.");
    }

    @Test
    void showSubTasksSuccessfulReturnListOfSubTasks() {
        SubTask expectedSubTask = new SubTask(subTaskId4, TaskType.SUBTASK, "First SubTask", TaskStatus.NEW,
                "SubTask(ID=4) of first EpicTask(ID=3) with DateTime", testStartTime2, duration2H, epicTaskId3);
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertNotNull(testSubTasks, "Задачи нe возвращаются.");
        assertEquals(3, testSubTasks.size(), "Неверное количество задач.");
        assertEquals(expectedSubTask, testSubTasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void showSubTasksFailReturnEmptyList() {
        taskManager.removeAllSubTasks();
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertNotNull(testSubTasks, "Задачи нe возвращаются.");
        assertTrue(testSubTasks.isEmpty(), "Список задач не пуст.");
        assertEquals(0, testSubTasks.size(), "Неверное количество задач.");
    }

    @Test
    void removeAllSubTasksSuccessfullyRemovedAllSimpleTasks() {
        taskManager.removeAllSubTasks();
        final List<SubTask> emptyTestSubTasks = taskManager.showSubTasks();
        assertNotNull(emptyTestSubTasks, "Задачи нe возвращаются.");
        assertTrue(emptyTestSubTasks.isEmpty(), "Список задач не пуст.");
        assertEquals(0, emptyTestSubTasks.size(), "Неверное количество задач.");
        assertTrue(taskManager.getEpicTaskById(epicTaskId3).getSubTasksOfEpicList().isEmpty(),
                "Список задач SubTasksOfEpicList не пуст.");
        assertTrue(taskManager.getEpicTaskById(epicTaskId6).getSubTasksOfEpicList().isEmpty(),
                "Список задач SubTasksOfEpicList не пуст.");
        final TaskStatus noSubTasksEpicStatusOfEpicId3 = taskManager.getEpicTaskById(epicTaskId3).getStatus();
        final TaskStatus noSubTasksEpicStatusOfEpicId6 = taskManager.getEpicTaskById(epicTaskId3).getStatus();
        assertEquals(TaskStatus.NEW, noSubTasksEpicStatusOfEpicId3, "Статус Epic задачи без подзадач задан неверно.");
        assertEquals(TaskStatus.NEW, noSubTasksEpicStatusOfEpicId6, "Статус Epic задачи без подзадач задан неверно.");
    }

    @Test
    void removeAllSubTasksFromEmptySubTaskMap() {
        taskManager.removeAllSubTasks();
        final List<SubTask> emptyTestSubTasks = taskManager.showSubTasks();
        assertNotNull(emptyTestSubTasks, "Задачи нe возвращаются.");
        assertTrue(emptyTestSubTasks.isEmpty(), "Список задач не пуст.");
        assertEquals(0, emptyTestSubTasks.size(), "Неверное количество задач.");
        assertTrue(taskManager.getEpicTaskById(epicTaskId3).getSubTasksOfEpicList().isEmpty(),
                "Список задач SubTasksOfEpicList не пуст.");
        assertTrue(taskManager.getEpicTaskById(epicTaskId6).getSubTasksOfEpicList().isEmpty(),
                "Список задач SubTasksOfEpicList не пуст.");
        taskManager.removeAllSubTasks();
        final List<SubTask> emptyEmptyTestSubTasks = taskManager.showSubTasks();
        assertNotNull(emptyEmptyTestSubTasks, "Задачи нe возвращаются.");
        assertTrue(emptyEmptyTestSubTasks.isEmpty(), "Список задач не пуст.");
        assertEquals(0, emptyEmptyTestSubTasks.size(), "Неверное количество задач.");
        assertTrue(taskManager.getEpicTaskById(epicTaskId3).getSubTasksOfEpicList().isEmpty(),
                "Список задач SubTasksOfEpicList не пуст.");
        assertTrue(taskManager.getEpicTaskById(epicTaskId6).getSubTasksOfEpicList().isEmpty(),
                "Список задач SubTasksOfEpicList не пуст.");
        assertEquals(emptyTestSubTasks, emptyEmptyTestSubTasks, "Списки не совпадают.");
    }

    @Test
    void getSubTaskByIdSuccessfullyReturnSubTask() {
        SubTask expectedSubTask = new SubTask(subTaskId4, TaskType.SUBTASK, "First SubTask", TaskStatus.NEW,
                "SubTask(ID=4) of first EpicTask(ID=3) with DateTime", testStartTime2, duration2H, epicTaskId3);
        final SubTask savedSubTask = taskManager.getSubTaskById(subTaskId4);
        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(expectedSubTask, savedSubTask, "Задачи не совпадают.");
    }

    @Test
    void getSubTaskByIdReturnFailWrongId() {
        final SubTask savedSubTask = taskManager.getSubTaskById(0L);
        assertNull(savedSubTask, "Обращение по несуществующему ID возвращает задачу.");
    }

    @Test
    void getSubTaskByIdReturnFailIdOfWrongTaskType() {
        final Task savedSubTask = taskManager.getSubTaskById(simpleTaskId2);
        assertNull(savedSubTask, "Обращение по  ID задачи другого типа возвращает задачу данного типа.");
    }

    @Test
    void getSubTaskByIdReturnFailEmptyId() {
        final Task savedSubTask = taskManager.getSubTaskById(null);
        assertNull(savedSubTask, "Обращение по  ID null возвращает задачу.");
    }

    @Test
    void removeSubTaskByIdSuccessfullyRemovedSubTask() {
        taskManager.updateSubTask(new SubTask(subTaskId4, TaskType.SUBTASK, "First SubTask", TaskStatus.IN_PROGRESS,
                "SubTask(ID=4) of first EpicTask(ID=3) with DateTime", testStartTime2Plus15m, duration2H, epicTaskId3));
        taskManager.updateSubTask(new SubTask(subTaskId5, TaskType.SUBTASK, "Second SubTask", TaskStatus.DONE,
                "SubTask(ID=5) of first EpicTask(ID=3) with DateTime", testStartTime3Minus15m, duration2H, epicTaskId3));
        final Long epicId = taskManager.getSubTaskById(subTaskId4).getEpicTaskID();
        assertEquals(2, taskManager.getEpicTaskById(epicId).getSubTasksOfEpicList().size(),
                "Неверное количество задач.");
        final TaskStatus initialEpicStatus = taskManager.getEpicTaskById(epicId).getStatus();
        final List<SubTask> initialSubTasks = taskManager.showSubTasks();
        final LocalDateTime epicStartTime = taskManager.getEpicTaskById(epicId).getStartTime();
        final Duration epicDuration = taskManager.getEpicTaskById(epicId).getDuration();
        taskManager.removeSubTaskById(subTaskId4);
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertEquals(2, testSubTasks.size(), "Неверное количество задач.");
        assertNull(taskManager.getSubTaskById(subTaskId4), "Задача не удалена.");
        final TaskStatus newEpicStatus = taskManager.getEpicTaskById(epicId).getStatus();
        assertNotEquals(initialEpicStatus, newEpicStatus, "Статус Epic задачи не пересчитан.");
        final LocalDateTime newEpicStartTime = taskManager.getEpicTaskById(epicId).getStartTime();
        final Duration newEpicDuration = taskManager.getEpicTaskById(epicId).getDuration();
        assertEquals(taskManager.getSubTaskById(subTaskId5).getStartTime(), newEpicStartTime,
                "Неверно пересчитано время начала Epic задачи.");
        assertEquals(taskManager.getSubTaskById(subTaskId5).getDuration(), newEpicDuration,
                "Неверно пересчитана длительность Epic задачи.");
        taskManager.removeSubTaskById(subTaskId5);
        assertTrue(taskManager.getEpicTaskById(epicId).getSubTasksOfEpicList().isEmpty(),
                "Список задач SubTasksOfEpicList не пуст.");
        final TaskStatus noSubTasksEpicStatus = taskManager.getEpicTaskById(epicId).getStatus();
        assertNotEquals(newEpicStatus, noSubTasksEpicStatus, "Статус Epic задачи не пересчитан.");
        assertEquals(TaskStatus.NEW, noSubTasksEpicStatus, "Статус Epic задачи без подзадач задан неверно.");
        final LocalDateTime noSubTasksEpicStartTime = taskManager.getEpicTaskById(epicId).getStartTime();
        final Duration noSubTasksEpicDuration = taskManager.getEpicTaskById(epicId).getDuration();
        assertNotEquals(newEpicStartTime, noSubTasksEpicStartTime, "Не пересчитано время начала Epic задачи.");
        assertNotEquals(newEpicDuration, noSubTasksEpicDuration, "Не пересчитано длительность Epic задачи.");
        assertNull(noSubTasksEpicStartTime, "У Epic без подзадач есть время начала задачи.");
        assertNull(noSubTasksEpicDuration, "У Epic без подзадач есть продолжительность.");
    }

    @Test
    void removeSubTaskByIdFailWrongId() {
        final List<SubTask> initialSubTasks = taskManager.showSubTasks();
        taskManager.removeSubTaskById(0L);
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertEquals(3, testSubTasks.size(), "Неверное количество задач.");
        assertEquals(initialSubTasks, testSubTasks, "Списки не совпадают.");
        assertEquals(initialSubTasks.size(), testSubTasks.size(), "Размер списков не совпадает.");
    }

    @Test
    void removeSubTaskByIdFailWrongTaskTypeId() {
        final List<SubTask> initialSubTasks = taskManager.showSubTasks();
        taskManager.removeSubTaskById(simpleTaskId2);
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertEquals(3, testSubTasks.size(), "Неверное количество задач.");
        assertEquals(initialSubTasks, testSubTasks, "Списки не совпадают.");
        assertEquals(initialSubTasks.size(), testSubTasks.size(), "Размер списков не совпадает.");
    }

    @Test
    void removeSubTaskByIdFailEmptyId() {
        final List<SubTask> initialSubTasks = taskManager.showSubTasks();
        taskManager.removeSubTaskById(null);
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertEquals(3, testSubTasks.size(), "Неверное количество задач.");
        assertEquals(initialSubTasks, testSubTasks, "Списки не совпадают.");
        assertEquals(initialSubTasks.size(), testSubTasks.size(), "Размер списков не совпадает.");
    }

    @Test
    void saveEpicTaskSuccessfulCreationNewEpicTask() {
        taskManager.removeAllEpicTasks();
        EpicTask epicTask = new EpicTask("First EpicTask", "EpicTask(ID=7)");
        final Long epicTaskId8 = taskManager.saveEpicTask(epicTask);
        EpicTask expectedEpicTask = new EpicTask(
                epicTaskId8, TaskType.EPIC, "First EpicTask", TaskStatus.NEW, "EpicTask(ID=7)");
        final EpicTask savedEpicTask = taskManager.getEpicTaskById(epicTaskId8);
        assertNotNull(savedEpicTask, "Задача не найдена.");
        assertEquals(epicTask, savedEpicTask, "Задачи не совпадают.");
        assertEquals(expectedEpicTask, savedEpicTask, "Задача не совпадает с введенной вручную.");
        final List<EpicTask> testEpicTasks = taskManager.showEpicTasks();
        assertNotNull(testEpicTasks, "Задачи нe возвращаются.");
        assertEquals(1, testEpicTasks.size(), "Неверное количество задач.");
        assertEquals(expectedEpicTask, testEpicTasks.get(0), "Задачи не совпадают.");
        final LocalDateTime epicStartTime = taskManager.getEpicTaskById(epicTaskId8).getStartTime();
        final Duration epicDuration = taskManager.getEpicTaskById(epicTaskId8).getDuration();
        assertNull(epicDuration, "У Epic без подзадач есть время начала задачи.");
        assertNull(epicDuration, "У Epic без подзадач есть продолжительность.");
    }

    @Test
    void updateEpicTaskSuccessfulUpdateEpicTask() {
        SubTask expectedSubTask = new SubTask(subTaskId7, TaskType.SUBTASK, "First SubTask", TaskStatus.IN_PROGRESS,
                "SubTask(ID=6) of first EpicTask(ID=6)", epicTaskId6);
        taskManager.updateSubTask(expectedSubTask);
        EpicTask expectedEpicTask = new EpicTask(epicTaskId6, "Second EpicTask", "EpicTask(ID=6 without DateTime)");
        taskManager.updateEpicTask(expectedEpicTask);
        final EpicTask updatedEpicTask = taskManager.getEpicTaskById(epicTaskId6);
        assertNotNull(updatedEpicTask, "Задача не найдена.");
        assertEquals(expectedEpicTask, updatedEpicTask, "Задачи не совпадают.");
        final List<EpicTask> testEpicTasks = taskManager.showEpicTasks();
        assertNotNull(testEpicTasks, "Задачи нe возвращаются.");
        assertEquals(2, testEpicTasks.size(), "Неверное количество задач.");
        assertEquals(expectedEpicTask, testEpicTasks.get(1), "Задачи не совпадают.");
        final TaskStatus epicStatus = taskManager.getEpicTaskById(epicTaskId6).getStatus();
        assertEquals(TaskStatus.IN_PROGRESS, epicStatus, "Статус Epic задачи не пересчитан.");
    }

    @Test
    void updateEpicTaskFailInCauseOfWrongId() {
        final List<EpicTask> initialEpicTasks = taskManager.showEpicTasks();
        EpicTask expectedEpicTask = new EpicTask(0L, "Second EpicTask", "EpicTask(ID=6 without DateTime)");
        taskManager.updateEpicTask(expectedEpicTask);
        final Task updatedTaskExpectedIdZero = taskManager.getEpicTaskById(0L);
        assertNull(updatedTaskExpectedIdZero, "Обновленная задача с не существовавшим ранее ID сохранена.");
        final List<EpicTask> testEpicTasks = taskManager.showEpicTasks();
        assertEquals(2, testEpicTasks.size(), "Неверное количество задач.");
        assertEquals(initialEpicTasks, testEpicTasks, "Списки задач отличаются.");
    }

    @Test
    void updateEpicTaskFailInCauseOfWrongTaskType() {
        final List<EpicTask> initialEpicTasks = taskManager.showEpicTasks();
        EpicTask expectedEpicTask = new EpicTask(simpleTaskId2, "Second EpicTask", "EpicTask(ID=6 without DateTime)");
        taskManager.updateEpicTask(expectedEpicTask);
        final Task updatedTaskExpectedId4 = taskManager.getEpicTaskById(simpleTaskId2);
        assertNull(updatedTaskExpectedId4, "Обновленная задача с ID задачи другого типа сохранена.");
        final List<EpicTask> testEpicTasks = taskManager.showEpicTasks();
        assertEquals(2, testEpicTasks.size(), "Неверное количество задач.");
        assertEquals(initialEpicTasks, testEpicTasks, "Списки задач отличаются.");
    }

    @Test
    void updateEpicTaskFailInCauseOfEmptyId() {
        final List<EpicTask> initialEpicTasks = taskManager.showEpicTasks();
        EpicTask expectedEpicTask = new EpicTask(null, "Second EpicTask", "EpicTask(ID=6 without DateTime)");
        taskManager.updateEpicTask(expectedEpicTask);
        final Task updatedTaskExpectedId4 = taskManager.getEpicTaskById(null);
        assertNull(updatedTaskExpectedId4, "Обновленная задача без ID сохранена.");
        final List<EpicTask> testEpicTasks = taskManager.showEpicTasks();
        assertEquals(2, testEpicTasks.size(), "Неверное количество задач.");
        assertEquals(initialEpicTasks, testEpicTasks, "Списки задач отличаются.");
    }

    @Test
    void showEpicTasksSuccessfulReturnListOfEpicTasks() {
        taskManager.removeSubTaskById(subTaskId7); //наличие сабтаски мешает equals
        EpicTask expectedEpicTask = new EpicTask(epicTaskId6, TaskType.EPIC, "Second EpicTask", TaskStatus.NEW,
                "EpicTask(ID=6 without DateTime)");
        final List<EpicTask> testEpicTasks = taskManager.showEpicTasks();
        assertNotNull(testEpicTasks, "Задачи нe возвращаются.");
        assertEquals(2, testEpicTasks.size(), "Неверное количество задач.");
        assertEquals(expectedEpicTask, testEpicTasks.get(1), "Задачи не совпадают.");
    }

    @Test
    void showEpicTasksFailReturnEmptyList() {
        taskManager.removeAllEpicTasks();
        final List<EpicTask> testSubTasks = taskManager.showEpicTasks();
        assertNotNull(testSubTasks, "Задачи нe возвращаются.");
        final List<SubTask> emptySubTasks = taskManager.showSubTasks();
        assertTrue(emptySubTasks.isEmpty(), "Список задач не пуст.");
        assertEquals(0, emptySubTasks.size(), "Неверное количество задач.");
    }

    @Test
    void removeAllEpicTasksSuccessfullyRemovedAllEpicTasksAndSubTasks() {
        final List<EpicTask> initialEpicTasks = taskManager.showEpicTasks();
        assertFalse(initialEpicTasks.isEmpty(), "Список задач пуст.");
        final List<SubTask> initialSubTasks = taskManager.showSubTasks();
        assertFalse(initialSubTasks.isEmpty(), "Список задач пуст.");
        taskManager.removeAllEpicTasks();
        final List<EpicTask> emptyEpicTasks = taskManager.showEpicTasks();
        assertTrue(emptyEpicTasks.isEmpty(), "Список задач не пуст.");
        assertEquals(0, emptyEpicTasks.size(), "Неверное количество задач.");
        final List<SubTask> emptySubTasks = taskManager.showSubTasks();
        assertTrue(emptySubTasks.isEmpty(), "Список задач не пуст.");
        assertEquals(0, emptyEpicTasks.size(), "Неверное количество задач.");
    }

    @Test
    void removeAllEpicTasksRemoveFromEmptyMap() {
        taskManager.removeAllEpicTasks();
        final List<EpicTask> initialEpicTasks = taskManager.showEpicTasks();
        assertTrue(initialEpicTasks.isEmpty(), "Список не задач пуст.");
        final List<SubTask> initialSubTasks = taskManager.showSubTasks();
        assertTrue(initialSubTasks.isEmpty(), "Список не задач пуст.");
        taskManager.removeAllEpicTasks();
        final List<EpicTask> emptyEpicTasks = taskManager.showEpicTasks();
        assertTrue(emptyEpicTasks.isEmpty(), "Список задач не пуст.");
        assertEquals(0, emptyEpicTasks.size(), "Неверное количество задач.");
        final List<SubTask> emptySubTasks = taskManager.showSubTasks();
        assertTrue(emptySubTasks.isEmpty(), "Список задач не пуст.");
        assertEquals(0, emptyEpicTasks.size(), "Неверное количество задач.");
    }

    @Test
    void getEpicTaskByIdIdSuccessfulReturnEpicTask() {
        taskManager.removeSubTaskById(subTaskId7); //наличие сабтаски мешает equals
        EpicTask expectedEpicTask = new EpicTask(epicTaskId6, TaskType.EPIC, "Second EpicTask", TaskStatus.NEW,
                "EpicTask(ID=6 without DateTime)");
        final EpicTask savedEpicTask = taskManager.getEpicTaskById(epicTaskId6);
        assertNotNull(savedEpicTask, "Задача не найдена.");
        assertEquals(expectedEpicTask, savedEpicTask, "Задачи не совпадают.");
    }

    @Test
    void getEpicTaskByIdFailReturnNullInCauseOfWrongId() {
        final EpicTask savedEpicTask = taskManager.getEpicTaskById(0L);
        assertNull(savedEpicTask, "Обращение по несуществующему ID возвращает задачу.");
    }

    @Test
    void getEpicTaskByIdFailReturnNullInCauseOfWrongTaskType() {
        final EpicTask savedEpicTask = taskManager.getEpicTaskById(simpleTaskId2);
        assertNull(savedEpicTask, "Обращение по несуществующему ID возвращает задачу.");
    }

    @Test
    void getEpicTaskByIdFailReturnNullInCauseOfEmptyId() {
        final EpicTask savedEpicTask = taskManager.getEpicTaskById(null);
        assertNull(savedEpicTask, "Обращение по несуществующему ID возвращает задачу.");
    }

    @Test
    void removeEpicTaskByIdSuccessfullyRemovedEpicTaskAndSubTasksOfThisEpic() {
        taskManager.getEpicTaskById(epicTaskId3);
        assertNotNull(taskManager.getEpicTaskById(epicTaskId3), "Задача отсутствует.");
        final List<EpicTask> initialEpicTasks = taskManager.showEpicTasks();
        final List<SubTask> initialSubTasks = taskManager.showSubTasks();
        taskManager.removeEpicTaskById(epicTaskId3);
        final List<EpicTask> testEpicTasks = taskManager.showEpicTasks();
        assertEquals(1, testEpicTasks.size(), "Неверное количество задач.");
        assertNull(taskManager.getEpicTaskById(epicTaskId3), "Задача не удалена.");
        assertNotEquals(initialEpicTasks.size(), testEpicTasks.size(), "Неверное количество задач.");
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertEquals(1, testSubTasks.size(), "Неверное количество задач.");
        assertNotEquals(initialSubTasks.size(), testSubTasks.size(), "Неверное количество задач.");
    }

    @Test
    void removeEpicTaskByIdFailInCauseOfWrongId() {
        final List<EpicTask> initialEpicTasks = taskManager.showEpicTasks();
        final List<SubTask> initialSubTasks = taskManager.showSubTasks();
        taskManager.removeEpicTaskById(0L);
        final List<EpicTask> testEpicTasks = taskManager.showEpicTasks();
        assertEquals(2, testEpicTasks.size(), "Неверное количество задач.");
        assertEquals(initialEpicTasks.size(), testEpicTasks.size(), "Неверное количество задач.");
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertEquals(3, testSubTasks.size(), "Неверное количество задач.");
        assertEquals(initialSubTasks.size(), testSubTasks.size(), "Неверное количество задач.");
    }

    @Test
    void removeEpicTaskByIdFailInCauseOfWrongTaskType() {
        final List<EpicTask> initialEpicTasks = taskManager.showEpicTasks();
        final List<SubTask> initialSubTasks = taskManager.showSubTasks();
        taskManager.removeEpicTaskById(simpleTaskId2);
        final List<EpicTask> testEpicTasks = taskManager.showEpicTasks();
        assertEquals(2, testEpicTasks.size(), "Неверное количество задач.");
        assertEquals(initialEpicTasks.size(), testEpicTasks.size(), "Неверное количество задач.");
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertEquals(3, testSubTasks.size(), "Неверное количество задач.");
        assertEquals(initialSubTasks.size(), testSubTasks.size(), "Неверное количество задач.");
    }

    @Test
    void removeEpicTaskByIdFailInCauseOfEmptyId() {
        final List<EpicTask> initialEpicTasks = taskManager.showEpicTasks();
        final List<SubTask> initialSubTasks = taskManager.showSubTasks();
        taskManager.removeEpicTaskById(null);
        final List<EpicTask> testEpicTasks = taskManager.showEpicTasks();
        assertEquals(2, testEpicTasks.size(), "Неверное количество задач.");
        assertEquals(initialEpicTasks.size(), testEpicTasks.size(), "Неверное количество задач.");
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertEquals(3, testSubTasks.size(), "Неверное количество задач.");
        assertEquals(initialSubTasks.size(), testSubTasks.size(), "Неверное количество задач.");
    }

    @Test
    void showSubTasksOfEpicSuccessfulReturnSubTasksOfEpicList() {
        taskManager.removeEpicTaskById(epicTaskId6);
        SubTask expectedSubTask = new SubTask(subTaskId4, TaskType.SUBTASK, "First SubTask", TaskStatus.NEW,
                "SubTask(ID=4) of first EpicTask(ID=3) with DateTime", testStartTime2, duration2H, epicTaskId3);
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        final List<Long> epicSubTasks = taskManager.getEpicTaskById(epicTaskId3).getSubTasksOfEpicList();
        assertEquals(testSubTasks.size(), epicSubTasks.size(), "Неверное количество задач.");
        assertNotNull(testSubTasks, "Задачи нe возвращаются.");
        assertEquals(2, testSubTasks.size(), "Неверное количество задач.");
        SubTask subTaskFromEpicList = taskManager.getSubTaskById(
                taskManager.getEpicTaskById(epicTaskId3).getSubTasksOfEpicList().get(0));
        assertEquals(expectedSubTask, subTaskFromEpicList, "Задачи не совпадают.");
        assertEquals(expectedSubTask, testSubTasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void showSubTasksOfEpicFailReturnEmptyListInCauseOfEmptySubTasksMap() {
        taskManager.removeAllSubTasks();
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertTrue(testSubTasks.isEmpty(), "Список не пуст.");
        final List<Long> epicSubTasks = taskManager.getEpicTaskById(epicTaskId3).getSubTasksOfEpicList();
        assertNotNull(testSubTasks, "Задачи нe возвращаются.");
        assertEquals(0, testSubTasks.size(), "Неверное количество задач.");
        assertTrue(epicSubTasks.isEmpty(), "Список не пуст.");
    }

    @Test
    void showSubTasksOfEpicFailReturnEmptyListInCauseOfEmptySubTasksOfEpicList() {
        taskManager.removeSubTaskById(subTaskId7);
        final List<SubTask> testSubTasks = taskManager.showSubTasks();
        assertFalse(testSubTasks.isEmpty(), "Список пуст.");
        final List<Long> epicSubTasks = taskManager.getEpicTaskById(epicTaskId6).getSubTasksOfEpicList();
        assertNotNull(testSubTasks, "Задачи нe возвращаются.");
        assertEquals(0, epicSubTasks.size(), "Неверное количество задач.");
        assertTrue(epicSubTasks.isEmpty(), "Список не пуст.");
    }
}