package taskTracker.tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskTracker.manager.FileBackedTaskManager;
import ru.yandex.practicum.taskTracker.manager.TaskManager;
import ru.yandex.practicum.taskTracker.tasks.EpicTask;
import ru.yandex.practicum.taskTracker.tasks.SubTask;
import ru.yandex.practicum.taskTracker.tasks.TaskStatus;
import ru.yandex.practicum.taskTracker.tasks.TaskType;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class EpicTaskTest {

    static TaskManager taskManager;
    static Long epicTaskId1;
    static Long subTaskId2;
    static Long subTaskId3;

    @BeforeEach
    void createEpicTaskAndSubTasks() {
        taskManager = new FileBackedTaskManager(new File(
                "src/main/java/ru/yandex/practicum/taskTracker/dataStorage/FileManager.csv"));
        epicTaskId1 = taskManager.saveEpicTask(new EpicTask("First EpicTask", "EpicTask(ID=1)"));
        subTaskId2 = taskManager.saveSubTask(new SubTask(
                "First SubTask", "SubTask(ID=2) of first EpicTask(ID=1)", epicTaskId1));
        subTaskId3 = taskManager.saveSubTask(new SubTask(
                "Second SubTask", "SubTask(ID=3) of first EpicTask(ID=1)", epicTaskId1));
    }

    @Test
    void returnTrueIfSubTaskOfEpicWithId4ListIsEmpty() {
        Long epicTaskId4 = taskManager.saveEpicTask(new EpicTask("Fourth EpicTask", "EpicTask(ID=4)"));
        assertTrue(taskManager.getEpicTaskById(epicTaskId4).getSubTasksOfEpicList().isEmpty());
    }

    @Test
    void EpicTaskWith2NewSubTaskReturnStatusNew() {
        assertEquals(TaskStatus.NEW, taskManager.getEpicTaskById(epicTaskId1).getStatus());
    }

    @Test
    void EpicTaskWith2DoneSubTaskReturnStatusDone() {
        taskManager.updateSubTask((new SubTask(subTaskId2, TaskType.SUBTASK, "First SubTask", TaskStatus.DONE,
                "SubTask(ID=2) of first EpicTask(ID=1)", epicTaskId1)));
        taskManager.updateSubTask((new SubTask(subTaskId3, TaskType.SUBTASK, "Second SubTask", TaskStatus.DONE,
                "SubTask(ID=3) of first EpicTask(ID=1)", epicTaskId1)));
        assertEquals(TaskStatus.DONE, taskManager.getEpicTaskById(epicTaskId1).getStatus());
    }

    @Test
    void EpicTaskWith1NewAnd1DoneSubTaskReturnStatusIn_progress() {
        taskManager.updateSubTask((new SubTask(subTaskId2, TaskType.SUBTASK, "First SubTask", TaskStatus.DONE,
                "SubTask(ID=2) of first EpicTask(ID=1)", epicTaskId1)));
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicTaskById(epicTaskId1).getStatus());
    }

    @Test
    void EpicTaskWith2IN_PROGRESSSubTaskReturnStatusIn_progress() {
        taskManager.updateSubTask((new SubTask(subTaskId2, TaskType.SUBTASK, "First SubTask", TaskStatus.IN_PROGRESS,
                "SubTask(ID=2) of first EpicTask(ID=1)", epicTaskId1)));
        taskManager.updateSubTask((new SubTask(subTaskId3, TaskType.SUBTASK, "Second SubTask", TaskStatus.IN_PROGRESS,
                "SubTask(ID=3) of first EpicTask(ID=1)", epicTaskId1)));
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicTaskById(epicTaskId1).getStatus());
    }
}