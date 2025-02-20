package ru.yandex.javacource.sevrin.schedule.manager;

import ru.yandex.javacource.sevrin.schedule.task.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

class Node {
    Task task;
    Node prev;
    Node next;

    public Node(Task task, Node prev, Node next) {
        this.task = task;
        this.prev = prev;
        this.next = next;
    }

}

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private final Map<Integer, Node> browsingHistory;

    public InMemoryHistoryManager() {
        this.head = null;
        this.tail = null;
        this.browsingHistory = new HashMap<>();
    }

    public void remove(int id) {
        Node nodeToRemove = browsingHistory.remove(id);
        if (nodeToRemove == null) {
            return;
        }
        removeNode(nodeToRemove);
    }

    private void removeNode(Node node) {

        if (node.prev != null) {
            node.prev.next = node.next;
            if (node.next == null) {
                tail = node.prev;
            } else {
                node.next.prev = node.prev;
            }
        } else {
            head = node.next;
            if (head == null) {
                tail = null;
            } else {
                head.prev = null;
            }
        }
    }

    private void linkLast(Task task) {
        final Node node = new Node(task, tail, null);
        if (tail == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null) {
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
        final int id = task.getId();
        remove(id);
        linkLast(task);
        browsingHistory.put(id, tail);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
