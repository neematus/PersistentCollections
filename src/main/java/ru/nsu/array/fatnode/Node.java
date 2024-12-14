package ru.nsu.array.fatnode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Node<V> {

    private final int key;
    private V value;
    private int height;
    private Node<V> leftChild;
    private Node<V> rightChild;

    public Node(int key, V value) {
        this.key = key;
        this.value = value;
        height = 1;
    }
}
