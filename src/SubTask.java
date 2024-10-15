public class SubTask extends Task {
    private final Epic epic;

    public SubTask(String title, String description, Epic epic) {
        super(title, description);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "SubTask{" + " title='" + getTitle() + '\'' + " description='" + getDescription() + '\'' + " id=" + id + '}';
    }

    protected void setStatus(Status status, Epic epic) {
        this.status = status;
        epic.setEpicStatus();
    }
}
