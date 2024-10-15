import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private static int idCounter;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Object> getEpicList() {
        ArrayList<Object> epicList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicList.add(epic);
            epicList.add(epic.getSubTasks());
        }
        return epicList;
    }

    public void removeSubTaskById(int subTaskId, Epic epic) {
        epic.removeSubTask(subTaskId);
    }

    public void removeAllTasks() {
        tasks.clear();
        epics.clear();
        idCounter = 0;
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задачи с таким ID не существует");
        }
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Эпика с таким ID не существует");
        }
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeEpicById(int id) {
        epics.remove(id);
    }

    public ArrayList<SubTask> getSubTasksOfEpic(Epic epic) {
        return epic.getSubTasks();
    }

    public void addSubTaskToEpic(Epic epic, SubTask subTask) {
        epic.addSubTask(subTask);
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void increaseIdCounter() {
        idCounter++;
    }

}
