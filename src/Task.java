import java.util.Objects;

public class Task {
    private final String title;
    private final String description;
    protected final int id;
    protected Status status;


    public Task(String title, String description) {
        TaskManager.increaseIdCounter();
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        id = TaskManager.getIdCounter();
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    protected void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj.getClass() != this.getClass()) { return false; }
        Task otherTask = (Task) obj;
        return id == otherTask.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task {" +
                "title='" + title + '\'' +
                " description='" + description + '\'' +
                " id=" + id +
                '}';
    }
}
