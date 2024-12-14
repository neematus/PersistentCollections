package ru.nsu.array.pathcopy;

import java.util.ArrayList;

/**
 * Полностью персистентный массив
 */
public class PersistentArray<E> {

    /**
     * Список версий
     */
    private final ArrayList<Trie<E>> versions = new ArrayList<>();

    public PersistentArray() {
        versions.add(new Trie<>(0));
    }

    /**
     * Получение элемента по индексу из данной версии массива
     */
    public E get(int index, int version) {
        return versions.get(version).getValue(index);
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

}
