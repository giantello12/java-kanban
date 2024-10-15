import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<SubTask> subTasks;

    public Epic(String title, String description) {
        super(title, description);
        this.subTasks = new ArrayList<>();
    }

    @Override
    protected void setStatus(Status status) {
        System.out.println("Прямая смена статуса эпика запрещена");
    }

    public void addSubTask(SubTask subTask) {
        this.subTasks.add(subTask);
    }

    public void removeSubTask(int subTaskId) {
        for (int i = 0; i < subTasks.size(); i++) {
            if (subTasks.get(i).getId() == subTaskId) {
                subTasks.remove(i);
                break;
            }
        }
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setEpicStatus() {
        int doneCounter = 0;
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus() == Status.DONE) {
                doneCounter++;
            }
        }
        if (doneCounter > 0) {
            if (doneCounter == subTasks.size()) {
                this.status = Status.DONE;
            } else {
                this.status = Status.IN_PROGRESS;
            }
        }
    }

    @Override
    public String toString() {
        return "Epic {" + "title='" + getTitle() + '\'' + " description='" + getDescription() + '\'' + " id=" + getId() + '}';
    }
}
