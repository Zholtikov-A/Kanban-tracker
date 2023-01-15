package ru.yandex.practicum.taskTracker.tasks;

public class Task {
    private String name;
    private String description;
    private Long id;
    private TaskStatus status;
    private TaskType type = TaskType.TASK;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(Long id, TaskType type, String name, TaskStatus status, String description) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public void setId(Long Id) {
        this.id = Id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
