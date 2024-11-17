package ru.yandex.javacource.sevrin.schedule;

import ru.yandex.javacource.sevrin.schedule.task.Status;
import ru.yandex.javacource.sevrin.schedule.manager.InMemoryTaskManager;
import ru.yandex.javacource.sevrin.schedule.task.Epic;
import ru.yandex.javacource.sevrin.schedule.task.Subtask;
import ru.yandex.javacource.sevrin.schedule.task.Task;


public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

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
                + "\nСтатус " + task1.getStatus());
        System.out.println("Задача2\n" + taskManager.getTask(idTask2)
                + "\nСтатус " + task2.getStatus());
        System.out.println("Эпик 1\n" + taskManager.getEpic(idEpic1)
                + "\nСтатус " + epic1.getStatus());
        System.out.println("Подзадача 1 эпика 1\n" + taskManager.getSubtask(idSubtask1)
                + "\nСтатус " + subtask1.getStatus());
        System.out.println("Подзадача 2 эпика 1\n" + taskManager.getSubtask(idSubtask2)
                + "\nСтатус " + subtask2.getStatus());
        System.out.println("Эпик 2\n" + taskManager.getEpic(idEpic2)
                + "\nСтатус " + epic2.getStatus());
        System.out.println("Подзадача 1 эпика 2\n" + taskManager.getSubtask(idSubtask3)
                + "\nСтатус " + subtask3.getStatus());
        System.out.println();

        task1.setStatus(Status.DONE);
        taskManager.updateTask(task1);

        task2.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task2);

        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);

        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask2);

        taskManager.deleteSubtask(subtask3.getId());

        System.out.println("Задача 1\n" + taskManager.getTask(idTask1)
                + "\nСтатус " + task1.getStatus());
        System.out.println("Задача2\n" + taskManager.getTask(idTask2)
                + "\nСтатус " + task2.getStatus());
        System.out.println("Эпик 1\n" + taskManager.getEpic(idEpic1)
                + "\nСтатус " + epic1.getStatus());
        System.out.println("Подзадача 1 эпика 1\n" + taskManager.getSubtask(idSubtask1)
                + "\nСтатус " + subtask1.getStatus());
        System.out.println("Подзадача 2 эпика 1\n" + taskManager.getSubtask(idSubtask2)
                + "\nСтатус " + subtask2.getStatus());
        System.out.println("Эпик 2\n" + taskManager.getEpic(idEpic2)
                + "\nСтатус " + epic2.getStatus());
        System.out.println("Подзадача 1 эпика 2\n" + taskManager.getSubtask(idSubtask3)
                + "\nСтатус " + subtask3.getStatus());
        System.out.println();
    }
}