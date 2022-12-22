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
    private int size = 0;

    public void linkLast(Task element) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, element, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> newList = new ArrayList<>();
        Node<Task> currentNode = null;
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                newList.add(head.data);
                currentNode = head;
            } else if (i == (size - 1)) {
                newList.add(tail.data);
            } else {
                newList.add(currentNode.next.data);
                currentNode = currentNode.next;
            }
        }
        return newList;
    }

    public void removeNode(Node<Task> node) {
        if (node.prev == null) {
            head = node.next;
            node.next.prev = null;
        } else if (node.next == null) {
            tail = node.prev;
            node.prev.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        size--;
    }
}

