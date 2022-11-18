import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<Long> subTasksOfEpicIdList = new ArrayList<>();

    public EpicTask(String name, String description) {
        super(name, description);
    }

    public ArrayList<Long> getSubTasksOfEpicList() {
        return subTasksOfEpicIdList;
    }

    public void setSubTasksOfEpicList(ArrayList<Long> subTasksOfEpicList) {
        this.subTasksOfEpicIdList = subTasksOfEpicList;
    }
}

