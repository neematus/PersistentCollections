package ru.nsu.list;

import ru.nsu.util.PersistentCollection;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Персистентный двусвязный список
 */
public class PersistentLinkedList<E> implements PersistentCollection<E> {

    private final List<ListNode<E>> versions = new LinkedList<>();
    private final Stack<ListNode<E>> redo = new Stack<>();

    public PersistentLinkedList() {}

    public PersistentLinkedList(ListNode<E> head) {
        versions.add(head);
    }

    /**
     * Откатывает последнее изменение коллекции
     */
    public void undo() {
        if (!versions.isEmpty()) {
            redo.push(versions.get(versions.size() - 1));
            versions.remove(versions.size() - 1);
        }
    }

    /**
     * Возвращает последний откат коллекции
     */
    public void redo() {
        if (!redo.empty()) {
            versions.add(redo.pop());
        }
    }

    public int getCurrentVersion() {
        return versions.size();
    }

    @Override
    public int size() {
        return size(getCurrentVersion());
    }

    @Override
    public int size(int version) {
        int count = 0;
        ListNode<E> next = versions.get(version - 1);

        while (next != null) {
            next = next.getPrev();
            count++;
        }

        return count;
    }

    @Override
    public boolean add(E e) {
        return add(e, getCurrentVersion());
    }

    @Override
    public boolean add(E e, int version) {
        try {
            ListNode<E> tail = new ListNode<>(e);

            if (version != 0) {
                ListNode<E> prev = versions.get(version - 1);
                ListNode<E> current = new ListNode<>(prev.getVal(), tail, prev.getPrev());
                tail.setPrev(current);
            }
            versions.add(tail);

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean addAtIndex(int index, E e) {
        return addAtIndex(index, e, getCurrentVersion());
    }

    @Override
    public boolean addAtIndex(int index, E e, int version) {
        if (index > size(version)) {
            return false;
        }
        if (index == size(version)) {
            return add(e, version);
        }

        try {
            ListNode<E> prev = versions.get(version - 1);
            ListNode<E> current = new ListNode<>(prev.getVal());
            versions.add(current);

            for (int i = 0; i < size() - index - 1; i++) {
                ListNode<E> next = new ListNode<>(prev.getPrev().getVal(), current, null);
                current.setPrev(next);

                current = next;
                prev = prev.getPrev();
            }
            current.setPrev(new ListNode<>(e, current, prev.getPrev()));

            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean remove(E e) {
        return remove(e, getCurrentVersion());
    }

    @Override
    public boolean remove(E e, int version) {
        try {
            ListNode<E> prev = versions.get(version - 1);

            if (e == prev.getVal()) {
                if (prev.getPrev() != null) {
                    versions.add(new ListNode<>(prev.getPrev().getVal(), prev.getPrev().getPrev()));
                } else {
                    versions.add(null);
                }
                return true;
            }

            ListNode<E> current = new ListNode<>(prev.getVal(), prev.getPrev());
            versions.add(current);

            while (prev.getPrev().getVal() != e && prev.getPrev() != null) {
                ListNode<E> next = new ListNode<>(prev.getPrev().getVal(), current, null);
                current.setPrev(next);

                current = next;
                prev = prev.getPrev();
            }

            if (prev.getPrev() == null) {
                return false;
            }
            if (prev.getPrev().getPrev() != null) {
                current.setPrev(prev.getPrev().getPrev());
            } else {
                current.setPrev(null);
            }

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean removeAtIndex(int index) {
        return removeAtIndex(index, getCurrentVersion());
    }

    @Override
    public boolean removeAtIndex(int index, int version) {
        try {
            ListNode<E> prev = versions.get(version - 1);

            if (index > size(version)) {
                return false;
            }
            if (index == size(version) - 1) {
                if (prev.getPrev() != null) {
                    versions.add(new ListNode<>(prev.getPrev().getVal(), prev.getPrev().getPrev()));
                } else {
                    versions.add(null);
                }
                return true;
            }

            ListNode<E> current = new ListNode<>(prev.getVal(), prev.getPrev());
            versions.add(current);

            for (int i = 1; i < size() - index - 1; i++) {
                ListNode<E> next = new ListNode<>(prev.getPrev().getVal(), current, null);
                current.setPrev(next);

                current = next;
                prev = prev.getPrev();
            }

            if (prev.getPrev() == null) {
                return false;
            }
            if (prev.getPrev().getPrev() != null) {
                current.setPrev(prev.getPrev().getPrev());
            } else {
                current.setPrev(null);
            }

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public ListNode<E> get(int index) {
        return get(index, getCurrentVersion());
    }

    @Override
    public ListNode<E> get(int index, int version) {
        ListNode<E> current = versions.get(version - 1);

        for (int i = 0; i < size(version) - index - 1; i++) {
            if (current.getPrev() != null) {
                current = current.getPrev();
            } else {
                return null;
            }
        }

        return current;
    }

    @Override
    public boolean set(int index, E e) {
        return set(index, e, getCurrentVersion());
    }

    @Override
    public boolean set(int index, E e, int version) {
        if (index > size(version)) {
            return false;
        }
        if (index == size(version) - 1) {
            try {
                ListNode<E> prev = versions.get(version - 1);
                ListNode<E> tail = new ListNode<>(prev, e);
                versions.add(tail);

                return true;
            } catch (Exception ex) {
                return false;
            }
        }

        try {
            ListNode<E> prev = versions.get(version - 1);
            ListNode<E> current = new ListNode<>(prev.getVal());
            versions.add(current);

            for (int i = 0; i < size() - index - 2; i++) {
                ListNode<E> next = new ListNode<>(prev.getPrev().getVal(), current, null);
                current.setPrev(next);

                current = next;
                prev = prev.getPrev();
            }
            current.setPrev(new ListNode<>(e, current, prev.getPrev().getPrev()));

            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean isEmpty() {
        return isEmpty(getCurrentVersion());
    }

    @Override
    public boolean isEmpty(int version) {
        return size(version) == 0;
    }

    @Override
    public boolean contains(Object o) {
        return contains(o, getCurrentVersion());
    }

    @Override
    public boolean contains(Object o, int version) {
        ListNode<E> prev = versions.get(version - 1);

        if (prev.getVal() == o) {
            return true;
        }

        while (prev.getPrev().getVal() != o && prev.getPrev() != null) {
            prev = prev.getPrev();
        }

        return prev.getPrev().getVal() == o;
    }

    @Override
    public String toString() {
        return toString(getCurrentVersion());
    }

    @Override
    public String toString(int version) {
        StringBuilder result = new StringBuilder();
        if (version == 0) {
            return "null";
        }
        ListNode<E> el = versions.get(version - 1);

        if (el == null) {
            return result.append("empty").toString();
        } else {
            result.append(")");
            while (el.getPrev() != null) {
                result.insert(0, ", " + el.getVal());
                el = el.getPrev();
            }
            result.insert(0, "(" + el.getVal());

            return result.insert(0, "version: " + version + "\n").toString();
        }
    }
}
