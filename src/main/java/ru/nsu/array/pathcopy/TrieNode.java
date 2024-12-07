package ru.nsu.array.pathcopy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Вершина дерева
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrieNode<E> {

    /**
     * Значение (заполняется только для листьев)
     */
    private E value;

    /**
     * Левый потомок
     */
    private TrieNode<E> leftChild;

    /**
     * Правый потомок
     */
    private TrieNode<E> rightChild;

}
