package Tasks;

public class SubTask extends Task {

    private long epicTaskID;

    public SubTask(String name, String description, long epicTaskID) {
        super(name, description);
        this.epicTaskID = epicTaskID;
    }

    public long getEpicTaskID() {
        return epicTaskID;
    }

    public void setEpicTaskID(long epicTaskID) {
        this.epicTaskID = epicTaskID;
    }
}
