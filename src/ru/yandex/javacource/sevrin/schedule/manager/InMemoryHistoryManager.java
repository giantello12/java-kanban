package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private final Node head;
    private final Node tail;
    private final Map<Integer, Node> browsingHistory;

    public InMemoryHistoryManager() {
        this.head = new Node(null);
        this.tail = new Node(null);
        head.next = tail;
        tail.prev = head;
        this.browsingHistory = new HashMap<>();
    }

    public void remove(int id) {
        Node nodeToRemove = browsingHistory.remove(id);
        if (nodeToRemove != null) {
            removeNode(nodeToRemove);
        }
    }

    public void removeNode(Node node) {
        if (node == null) {
            return;
        }

        node.prev.next = node.next;
        node.next.prev = node.prev;

        browsingHistory.remove(node.task.getId());
    }
    public void linkLast(Task task) {
        Node newNode = new Node(task);
        Node lastNode = tail.prev;
        lastNode.next = newNode;
        newNode.prev = lastNode;
        newNode.next = tail;
        tail.prev = newNode;

        browsingHistory.put(task.getId(), newNode);
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node currentNode = head.next;

        while (currentNode != tail) {
            tasks.add(currentNode.task);
            currentNode = currentNode.next;
        }
        return tasks;
    }

    @Override
    public void add(Task task) {

        if (task == null) {
            return;
        }
        remove(task.getId());
        linkLast(task.copy());
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
