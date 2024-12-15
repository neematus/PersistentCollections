package ru.nsu.array.pathcopy;

import java.util.ArrayList;
import java.util.Stack;

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
     * Возвращает текущую версию коллекции
     * @return текущая версию коллекции
     */
    public int getCurrentVersion() {
        return versions.size();
    }

    /**
     * Проверяет, что текущая версия коллекции пуста
     * @return true, если текущая версия коллекции пуста
     */
    public boolean isEmpty() {
        return versions.get(getCurrentVersion()-1).getNumberOfElements() == 0;
    }

    /**
     * Получение элемента по индексу из последней версии массива
     */
    public E get(int index) {
        return versions.get(versions.size() - 1).getValue(index);
    }

    /**
     * Получение элемента по индексу из данной версии массива
     */
    public E get(int index, int version) {
        return versions.get(version).getValue(index);
    }

    /**
     * Изменение значения элемента по индексу в последней версии массива
     */
    public void set(int index, E value) {
        set(index, value, versions.size() - 1);
    }

    /**
     * Изменение значения элемента по индексу в данной версии массива
     */
    public void set(int index, E value, int version) {
        Trie<E> prevVersion = versions.get(version);
        if (prevVersion.getNumberOfElements() - 1 < index) {
            throw new IndexOutOfBoundsException();
        }

        Trie<E> newVersion = new Trie<>(prevVersion.getNumberOfElements());
        TrieNode<E> newNode = getNewNode(index, prevVersion, newVersion);

        newNode.setValue(value);
        versions.add(newVersion);
    }

    /**
     * Добавление элемента в последнюю версию массива
     */
    public void add(E value) {
        add(value, versions.size() - 1);
    }

    /**
     * Добавление элемента в данную версию массива
     */
    public void add(E value, int version) {
        Trie<E> prevVersion = versions.get(version);
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
                if (prevNode.getLeftChild() == null) {  // такое происходит только при добавлении нового элемента
                    newNode.setLeftChild(new TrieNode<>());
                    newNode = newNode.getLeftChild();
                    break;
                }
                newNode.setRightChild((prevNode.getRightChild() == null) ? new TrieNode<>() : prevNode.getRightChild());
                newNode.setLeftChild(new TrieNode<>());
                newNode = newNode.getLeftChild();
                prevNode = (prevNode.getLeftChild() == null) ? new TrieNode<>() : prevNode.getLeftChild();
            } else {
                newNode.setLeftChild((prevNode.getLeftChild() == null) ? new TrieNode<>() : prevNode.getLeftChild());
                newNode.setRightChild(new TrieNode<>());
                newNode = newNode.getRightChild();
                prevNode = (prevNode.getRightChild() == null) ? new TrieNode<>() : prevNode.getRightChild();
            }
        }

        return newNode;
    }

    /**
     * Возвращает строковое представление текущей версии коллекции
     * @return строковое представление текущей версии коллекции
     */
    public String toString() {
        return toString(getCurrentVersion());
    }

    /**
     * Возвращает строковое представление указанной версии коллекции
     * @param version версия коллекции
     * @return строковое представление указанной версии коллекции
     */
    public String toString(int version) {
        StringBuilder result = new StringBuilder("version: " + version + "\n{");
        Trie<E> root = versions.get(version-1);

        for (int i = 0; i < versions.get(version-1).getNumberOfElements(); i++) {
            E entry = root.getValue(i);
            if (entry != null)
                result.append(entry.toString()).append(", ");
        }

        if (result.lastIndexOf(", ") == - 1) {
            return result.append("empty}").toString();
        }
        return result.delete(result.lastIndexOf(", "), result.lastIndexOf(", ") + 2).append("}").toString();
    }

    /**
     * Отмена предыдущего изменения (UNDO)
     */
    public void undo() {
        if (!versions.isEmpty()) {
            redo.push(versions.remove(versions.size() - 1));
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
