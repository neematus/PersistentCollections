package ru.nsu.array.pathcopy;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
        depth = switch (numberOfElements) {
            case 0 -> 0;
            case 1 -> 1;
            default -> (int) Math.ceil(Math.log(numberOfElements) / Math.log(2.)); // ln(numberOfElements) / ln(2)
        };
    }

    /**
     * Получение всех элементов массива
     */
    public List<E> getValues() {
        List<E> values = new ArrayList<>();
        for (int i = 0; i < numberOfElements; i++) {
            values.add(getValue(i));
        }
        return values;
    }

    /**
     * Получение элемента массива по индексу
     */
    public E getValue(int index) {
        if (index > getNumberOfElements() - 1) {
            throw new IndexOutOfBoundsException();
        }

        TrieNode<E> nodeWithValue = root;
        for (int i = depth - 1; i >= 0; i--) {
            nodeWithValue = (index >> i) % 2 == 0 ? nodeWithValue.getLeftChild() : nodeWithValue.getRightChild();
        }
        return nodeWithValue.getValue();
    }

}
