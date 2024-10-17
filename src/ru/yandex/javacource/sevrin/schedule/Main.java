package ru.yandex.javacource.sevrin.schedule;

import ru.yandex.javacource.sevrin.schedule.manager.Status;
import ru.yandex.javacource.sevrin.schedule.manager.TaskManager;
import ru.yandex.javacource.sevrin.schedule.task.Epic;
import ru.yandex.javacource.sevrin.schedule.task.Subtask;
import ru.yandex.javacource.sevrin.schedule.task.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Домашнее задание 1", "Сделать домашнее заданее 1");
        int idTask1 = taskManager.addTask(task1);

        Task task2 = new Task("Уборка", "Убраться дома");
        int idTask2 = taskManager.addTask(task2);

        Epic epic1 = new Epic("Домашнее задание 2", "Сделать домашнее задание 2");
        int idEpic1 = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Алгебра", "Сделать алгебру", idEpic1);
        Subtask subtask2 = new Subtask("Литература", "Написать сочинение", idEpic1);
        int idSubtask1 = taskManager.addSubtask(subtask1);
        int idSubtask2 = taskManager.addSubtask(subtask2);

        Epic epic2 = new Epic("Готовка", "Приготовть обед");
        int idEpic2 = taskManager.addEpic(epic2);
        Subtask subtask3 = new Subtask("Суп", "Приготовить суп", idEpic2);
        int idSubtask3 = taskManager.addSubtask(subtask3);

        System.out.println("Задача 1\n" + taskManager.getTask(idTask1)
                + "\nСтатус " + taskManager.getTaskStatus(idTask1));
        System.out.println("Задача2\n" + taskManager.getTask(idTask2)
                + "\nСтатус " + taskManager.getTaskStatus(idTask2));
        System.out.println("Эпик 1\n" + taskManager.getEpic(idEpic1)
                + "\nСтатус " + taskManager.getEpicStatus(idEpic1));
        System.out.println("Подзадача 1 эпика 1\n" + taskManager.getSubtask(idSubtask1)
                + "\nСтатус " + taskManager.getSubtaskStatus(idSubtask1));
        System.out.println("Подзадача 2 эпика 1\n" + taskManager.getSubtask(idSubtask2)
                + "\nСтатус " + taskManager.getSubtaskStatus(idSubtask2));
        System.out.println("Эпик 2\n" + taskManager.getEpic(idEpic2));
        System.out.println("Подзадача 1 эпика 2\n" + taskManager.getSubtask(idSubtask3)
                + "\nСтатус " + taskManager.getSubtaskStatus(idSubtask3) + "\n");


        taskManager.updateTask(task1, "Уборка", "Убраться дома");

        taskManager.updateEpic(epic1, "Домашнее задание", "Сделать домашнее задание");

        taskManager.updateSubtask(subtask1, "Алгебра", "Решить примеры");

        taskManager.deleteSubtask(idSubtask3);
        taskManager.deleteTask(idTask1);

        taskManager.setSubtaskStatus(idSubtask2, Status.DONE);

        taskManager.setTaskStatus(idTask2, Status.IN_PROGRESS);

        System.out.println("Задача 1\n" + taskManager.getTask(idTask1)
                + "\nСтатус " + taskManager.getTaskStatus(idTask1));
        System.out.println("Задача2\n" + taskManager.getTask(idTask2)
                + "\nСтатус " + taskManager.getTaskStatus(idTask2));
        System.out.println("Эпик 1\n" + taskManager.getEpic(idEpic1)
                + "\nСтатус " + taskManager.getEpicStatus(idEpic1));
        System.out.println("Подзадача 1 эпика 1\n" + taskManager.getSubtask(idSubtask1)
                + "\nСтатус " + taskManager.getSubtaskStatus(idSubtask1));
        System.out.println("Подзадача 2 эпика 1\n" + taskManager.getSubtask(idSubtask2)
                + "\nСтатус " + taskManager.getSubtaskStatus(idSubtask2));
        System.out.println("Эпик 2\n" + taskManager.getEpic(idEpic2));
        System.out.println("Подзадача 1 эпика 2\n" + taskManager.getSubtask(idSubtask3)
                + "\nСтатус " + taskManager.getSubtaskStatus(idSubtask3) + "\n");
    }
}

