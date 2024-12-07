package ru.nsu.array.pathcopy;

import lombok.Data;

/**
 * Префиксное дерево, листья дерева - элементы массива.
 * Для получения элемента массива по индексу, будем смотреть на
 * соответствующий бит в двоичном представлении индекса,
 * если бит равен 0 - идем по левой ветке, если 1 - по правой
 */
@Data
public class Trie<E> {

    /**
     * Корень дерева
     */
    private final TrieNode<E> root = new TrieNode<>();

    /**
     * Количество элементов в массиве
     */
    private final int numberOfElements;

    /**
     * Глубина дерева
     */
    private final int depth;

    public Trie(int numberOfElements) {
        this.numberOfElements = numberOfElements;
        if (numberOfElements == 0) {
            depth = 0;
        } else if (numberOfElements == 1) {
            depth = 1;
        } else {
            depth = (int) Math.ceil(Math.log(numberOfElements) / Math.log(2.)); // ln(numberOfElements) / ln(2)
        }
    }

    /**
     * Получение элемента массива по индексу
     */
    public E getValue(int index) {
        TrieNode<E> nodeWithValue = root;
        for (int i = depth - 1; i >= 0; i--) {
            nodeWithValue = (index >> i) % 2 == 0 ? nodeWithValue.getLeftChild() : nodeWithValue.getRightChild();
            if (nodeWithValue == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return nodeWithValue.getValue();
    }

}
