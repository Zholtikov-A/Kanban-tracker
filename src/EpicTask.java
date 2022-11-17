import java.util.HashMap;

public class EpicTask extends Task {
    HashMap<Long, SubTask> subTasksOfEpic = new HashMap<>();

    public EpicTask(String name, String description) {
        super(name, description);
    }

    public EpicTask(String name, String description, long id) {
        super(name, description, id);

    }
}

