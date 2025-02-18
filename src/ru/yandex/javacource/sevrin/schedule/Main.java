package ru.yandex.javacource.sevrin.schedule;

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
        Subtask subtask3 = new Subtask("Геометрия", "Сделать геометрию", idEpic1);

        int idSubtask1 = taskManager.addSubtask(subtask1);
        int idSubtask2 = taskManager.addSubtask(subtask2);
        int idSubtask3 = taskManager.addSubtask(subtask3);

        Epic epic2 = new Epic("Пустой эпик", "Пустой эпик");

        taskManager.getTask(idTask1);
        System.out.println("Просмотр задачи 1");
        System.out.println(taskManager.getHistory());

        taskManager.getTask(idTask2);
        System.out.println("Просмотр задачи 2");
        System.out.println(taskManager.getHistory());

        taskManager.getTask(idTask1);
        System.out.println("Повторный просмотр задачи 1");
        System.out.println(taskManager.getHistory());

        taskManager.remove(idTask1);
        taskManager.remove(idTask2);
        System.out.println("Удаление задачи 1 и 2");
        System.out.println(taskManager.getHistory());

        taskManager.getSubtask(idSubtask1);
        taskManager.getSubtask(idSubtask2);
        taskManager.getSubtask(idSubtask3);
        System.out.println("Просмотр подзадачи 1, 2 и 3");
        System.out.println(taskManager.getHistory());

        taskManager.getEpic(idEpic1);
        System.out.println("Просмотр эпика 1");
        System.out.println(taskManager.getHistory());

        System.out.println(epic1.getSubtaskIds());

        taskManager.remove(idSubtask1);
        System.out.println("Удаление подзадачи 1");
        System.out.println(taskManager.getHistory());

        taskManager.remove(idEpic1);
        System.out.println("Удаление эпика 1");
        System.out.println(taskManager.getHistory());

    }
}