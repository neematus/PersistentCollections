package ru.nsu.common;

public class AVLTree<V> {

    private Node<V> root;

    public V findMaxNotGreaterThan(int key) {
        V foundValue = null;
        Node<V> currentNode = root;

        while (currentNode != null) {
            if (currentNode.getKey() == key) {
                return currentNode.getValue();
            }

            if (currentNode.getKey() < key) {
                foundValue = currentNode.getValue();
                currentNode = currentNode.getRightChild();
            } else {
                currentNode = currentNode.getLeftChild();
            }
        }

        return foundValue;
    }

    public void insert(int key, V value) {
        root = insert(key, value, root);
    }

    private Node<V> insert(int key, V value, Node<V> root) {
        if (root == null) {
            return new Node<>(key, value);
        }

        if (root.getKey() == key) {
            root.setValue(value);
            return root;
        }

        if (root.getKey() > key) {
            root.setLeftChild(insert(key, value, root.getLeftChild()));
        } else {
            root.setRightChild(insert(key, value, root.getRightChild()));
        }

        updateNodeHeight(root);
        return balanceTree(root);
    }

    private Node<V> balanceTree(Node<V> root) {
        if (getBalance(root) == 2) {
            if (getBalance(root.getRightChild()) == -1) {
                root.setRightChild(rightRotate(root.getRightChild()));
            }
            return leftRotate(root);
        }

        if (getBalance(root) == -2) {
            if (getBalance(root.getLeftChild()) == 1) {
                root.setLeftChild(leftRotate(root.getLeftChild()));
            }
            return rightRotate(root);
        }

        return root;
    }

    private Node<V> leftRotate(Node<V> root) {
        Node<V> newRoot = root.getRightChild();

        root.setRightChild(newRoot.getLeftChild());
        newRoot.setLeftChild(root);

        updateNodeHeight(root);
        updateNodeHeight(newRoot);

        return newRoot;
    }

    private Node<V> rightRotate(Node<V> root) {
        Node<V> newRoot = root.getLeftChild();

        root.setLeftChild(newRoot.getRightChild());
        newRoot.setRightChild(root);

        updateNodeHeight(root);
        updateNodeHeight(newRoot);

        return newRoot;
    }

    private int getBalance(Node<V> node) {
        return getNodeHeight(node.getRightChild()) - getNodeHeight(node.getLeftChild());
    }

    private void updateNodeHeight(Node<V> node) {
        node.setHeight(1 + Math.max(getNodeHeight(node.getLeftChild()), getNodeHeight(node.getRightChild())));
    }

    private int getNodeHeight(Node<V> node) {
        return node == null ? 0 : node.getHeight();
    }

}
