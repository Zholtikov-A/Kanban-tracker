package ru.yandex.practicum.taskTracker.manager;

import ru.yandex.practicum.taskTracker.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Long, Node<Task>> historyAccess = new HashMap<>();

    @Override
    public void add(Task task) {
        if (historyAccess.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
        historyAccess.put(task.getId(), tail);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(Long id) {
        if (historyAccess.containsKey(id)) {
            removeNode(historyAccess.get(id));
            historyAccess.remove(id);
        }
    }

    public Node<Task> head;
    public Node<Task> tail;

    public void linkLast(Task element) {
        if (head == null) {
            head = new Node<>(null, element, null);
            tail = head;
        } else if (head != null && head.next == null) {
            tail = new Node<>(head, element, null);
            head.next = tail;
        } else {
            Node oldTail = tail;
            Node newNode = new Node(oldTail, element, null);
            tail.next = newNode;
            tail = newNode;
        }
    }

    private List<Task> getTasks() {
        if (historyAccess.isEmpty()) {
            return null;
        }
        List<Task> newList = new ArrayList<>();
        Node<Task> currentNode = head;
        newList.add(head.data);
        while (currentNode.next != null) {
            currentNode = currentNode.next;
            newList.add(currentNode.data);
        }
        return newList;
    }

    public void removeNode(Node<Task> node) {
        if (node == null) {
            return;
        }
        if (node.prev != null && node.next != null) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        } else if (node.prev == null && node.next == null) {
            head = null;
            tail = null;
        } else if (node.prev != null) {
            tail = node.prev;
            node.prev.next = null;
        } else if (node.next != null) {
            head = node.next;
            node.next.prev = node.prev;
        }
    }
}

