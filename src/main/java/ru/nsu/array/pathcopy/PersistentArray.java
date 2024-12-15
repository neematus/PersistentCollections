package ru.nsu.array.pathcopy;

import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Полностью персистентный массив
 */
public class PersistentArray<E> {

    /**
     * Список версий
     */
    private final ArrayList<Trie<E>> versions = new ArrayList<>();

    /**
     * Стек с отмененными версиями
     */
    private final Stack<Trie<E>> redo = new Stack<>();

    public PersistentArray() {
        versions.add(new Trie<>(0));
    }

    /**
     * Получение номера текущей версии массива
     */
    public int getCurrentVersion() {
        return versions.size();
    }

    /**
     * Возвращает true, что текущая версия массива пуста
     */
    public boolean isEmpty() {
        return versions.get(getCurrentVersion() - 1).getNumberOfElements() == 0;
    }

    /**
     * Получение элемента по индексу из текущей версии массива
     */
    public E get(int index) {
        return get(index, getCurrentVersion());
    }

    /**
     * Получение элемента по индексу из данной версии массива
     */
    public E get(int index, int version) {
        return versions.get(version - 1).getValue(index);
    }

    /**
     * Изменение значения элемента по индексу в текущей версии массива
     */
    public void set(int index, E value) {
        set(index, value, getCurrentVersion());
    }

    /**
     * Изменение значения элемента по индексу в данной версии массива
     */
    public void set(int index, E value, int version) {
        Trie<E> prevVersion = versions.get(version - 1);
        if (prevVersion.getNumberOfElements() - 1 < index) {
            throw new IndexOutOfBoundsException();
        }

        Trie<E> newVersion = new Trie<>(prevVersion.getNumberOfElements());
        TrieNode<E> newNode = getNewNode(index, prevVersion, newVersion);

        newNode.setValue(value);
        versions.add(newVersion);
    }

    /**
     * Добавление элемента в текущую версию массива
     */
    public void add(E value) {
        add(value, getCurrentVersion());
    }

    /**
     * Добавление элемента в данную версию массива
     */
    public void add(E value, int version) {
        Trie<E> prevVersion = versions.get(version - 1);
        Trie<E> newVersion = new Trie<>(prevVersion.getNumberOfElements() + 1);

        TrieNode<E> newNode = newVersion.getRoot();
        if (prevVersion.getNumberOfElements() == Math.pow(2, prevVersion.getDepth())) {
            newNode.setLeftChild(prevVersion.getRoot());
            newNode.setRightChild(new TrieNode<>());
            newNode = newNode.getRightChild();
            for (int i = 0; i < newVersion.getDepth() - 1; i++) {
                newNode.setLeftChild(new TrieNode<>());
                newNode = newNode.getLeftChild();
            }
        } else {
            newNode = getNewNode(newVersion.getNumberOfElements() - 1, prevVersion, newVersion);
        }

        newNode.setValue(value);
        versions.add(newVersion);
    }

    private TrieNode<E> getNewNode(int index, Trie<E> prevVersion, Trie<E> newVersion) {
        TrieNode<E> prevNode = prevVersion.getRoot();
        TrieNode<E> newNode = newVersion.getRoot();

        for (int i = newVersion.getDepth() - 1; i >= 0; i--) {
            if ((index >> i) % 2 == 0) {
                if (prevNode == null) {
                    newNode.setLeftChild(new TrieNode<>());
                    newNode = newNode.getLeftChild();
                    continue;
                }
                newNode.setRightChild(prevNode.getRightChild());
                newNode.setLeftChild(new TrieNode<>());
                newNode = newNode.getLeftChild();
                prevNode = prevNode.getLeftChild();
            } else {
                newNode.setLeftChild(prevNode.getLeftChild());
                newNode.setRightChild(new TrieNode<>());
                newNode = newNode.getRightChild();
                prevNode = prevNode.getRightChild();
            }
        }

        return newNode;
    }

    /**
     * Получение строкового представления текущей версии массива
     */
    public String toString() {
        return toString(getCurrentVersion());
    }

    /**
     * Получение строкового представления данной версии массива
     */
    public String toString(int version) {
        return "version: " + version + "\n{" +
                versions.get(version - 1).getValues().stream()
                        .map(E::toString)
                        .collect(Collectors.joining(", ")) + "}";
    }

    /**
     * Отмена предыдущего изменения (UNDO)
     */
    public void undo() {
        if (getCurrentVersion() > 1) {
            redo.push(versions.remove(getCurrentVersion() - 1));
        }
    }

    /**
     * Возврат предыдущего изменения (REDO)
     */
    public void redo() {
        if (!redo.empty()) {
            versions.add(redo.pop());
        }
    }

}
