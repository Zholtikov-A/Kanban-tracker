public class Task {
    private String name;
    private String description;
    private long id;
    private String status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long Id) {
        this.id = Id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", Id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
