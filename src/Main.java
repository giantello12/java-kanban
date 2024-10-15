public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Пицца", "Заказать пиццу к празднику");
        taskManager.addTask(task1);

        Task task2 = new Task("Уборка", "Убраться к приходу гостей");
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Учеба", "Подготовиться к учебному дню");
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("Алгебра", "Решить примеры", epic1);
        taskManager.addSubTaskToEpic(epic1, subTask1);
        SubTask subTask2 = new SubTask("Литература", "Написать сочинение", epic1);
        taskManager.addSubTaskToEpic(epic1, subTask2);

        Epic epic2 = new Epic("Спортзал", "Сходить в спортзал");
        taskManager.addEpic(epic2);
        SubTask subTask3 = new SubTask("Упражнения", "Сделать упражнения на спину", epic2);
        taskManager.addSubTaskToEpic(epic2, subTask3);

        System.out.println("Все задачи:");
        System.out.println(taskManager.getTaskList() + "\n");

        System.out.println("Все эпики с подзадачами");
        System.out.println(taskManager.getEpicList() + "\n");

        System.out.println("Задача 1");
        System.out.println(task1);
        System.out.println("Статус задачи 1 - " + task1.getStatus() + "\n");

        System.out.println("Задача 2");
        System.out.println(task2);
        System.out.println("Статус задачи 2 - " + task2.getStatus() + "\n");

        System.out.println("Эпик 1");
        System.out.println(epic1);
        System.out.println("Статус эпика 1 - " + epic1.getStatus());
        System.out.println("Подзадачи эпика 1");
        for (SubTask subTask : epic1.getSubTasks()) {
            System.out.println(subTask);
            System.out.println("Статус подзадачи - " + subTask.getStatus());
        }

        System.out.println("Эпик 2");
        System.out.println(epic2);
        System.out.println("Статус эпика 2 - " + epic2.getStatus());
        System.out.println("Подзадачи эпика 2");
        for (SubTask subTask : epic2.getSubTasks()) {
            System.out.println(subTask);
            System.out.println("Статус подзадачи - " + subTask.getStatus());
        }

        System.out.println("Меняем статус задачи 1 и подзадачи в эпике 1, удаляем подзадачу из эпика 2\n");

        task1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.DONE, epic1);

        taskManager.removeSubTaskById(epic2.getSubTasks().getFirst().getId(), epic2);

        System.out.println("Задача 1");
        System.out.println(task1);
        System.out.println("Статус задачи 1 - " + task1.getStatus() + "\n");

        System.out.println("Эпик 1");
        System.out.println(epic1);
        System.out.println("Статус эпика 1 - " + epic1.getStatus());
        System.out.println("Подзадачи эпика 1");
        for (SubTask subTask : epic1.getSubTasks()) {
            System.out.println(subTask);
            System.out.println("Статус подзадачи - " + subTask.getStatus());
        }

        System.out.println("Эпик 2");
        System.out.println(epic2);
        System.out.println("Статус эпика 2 - " + epic2.getStatus());
        System.out.println("Подзадачи эпика 2");
        for (SubTask subTask : epic2.getSubTasks()) {
            System.out.println(subTask);
            System.out.println("Статус подзадачи - " + subTask.getStatus());
        }
    }

}
