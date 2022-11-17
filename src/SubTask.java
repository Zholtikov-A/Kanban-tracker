public class SubTask extends Task {

    private long epicTaskID;
    private String status = "NEW";

    public SubTask(String name, String description, long id, String status, long epicTaskID) {
        super(name, description, id, status);
        this.epicTaskID = epicTaskID;
    }

    public SubTask(String name, String description, long epicTaskID) {
        super(name, description);
        this.epicTaskID = epicTaskID;
    }

    public long getEpicTaskID() {
        return epicTaskID;
    }
}
