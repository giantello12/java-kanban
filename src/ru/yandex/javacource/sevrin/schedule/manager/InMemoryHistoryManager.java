package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.task.*;

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
        if (nodeToRemove == null) {
            return;
        }
        if (nodeToRemove.task instanceof Epic) {
            for (Integer subtaskId : ((Epic) nodeToRemove.task).getSubtaskIds()) {
                removeNode(browsingHistory.remove(subtaskId));
            }
        }
        removeNode(nodeToRemove);
    }

    public void removeNode(Node node) {
        if (node == null) {
            return;
        }

        browsingHistory.remove(node.task.getId());

        if (node.prev != null) {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
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

    public void linkLast(Epic epic) {
        Node newNode = new Node(epic);
        Node lastNode = tail.prev;
        lastNode.next = newNode;
        newNode.prev = lastNode;
        newNode.next = tail;
        tail.prev = newNode;

        browsingHistory.put(epic.getId(), newNode);
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
    public void add(Epic epic) {

        if (epic == null) {
            return;
        }
        remove(epic.getId());
        linkLast(epic);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
