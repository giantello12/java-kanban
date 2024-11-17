package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.task.Epic;
import ru.yandex.javacource.sevrin.schedule.task.Subtask;
import ru.yandex.javacource.sevrin.schedule.task.Task;

import java.util.ArrayList;

 public interface TaskManager {

     ArrayList<Task> getTasks();

     ArrayList<Epic> getEpics();

     ArrayList<Subtask> getSubtasks();

     void deleteAll();

     void deleteTasks();

     void deleteSubtasks();

     void deleteEpics();

     Task getTask(int id);

     Epic getEpic(int id);

     Subtask getSubtask(int id);

     ArrayList<Subtask> getEpicSubtasks(int epicId);

     Integer addTask(Task task);

     Integer addEpic(Epic epic);

     Integer addSubtask(Subtask subtask);

     void updateTask(Task task);

     void updateEpic(Epic epic);

     void updateSubtask(Subtask subtask);

     void deleteTask(int id);

     void deleteEpic(int id);

     void deleteSubtask(int id);

     void updateEpicStatus(int epicId);
}
