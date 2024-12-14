package ru.nsu.list;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListNode<E> {
    private ListNode<E> next = null;
    private ListNode<E> prev = null;
    private E val;

    public ListNode(ListNode<E> node, E e) {
        this.next = node.getNext();
        this.prev = node.getPrev();
        this.val = e;
    }
    public ListNode(E val) { this.val = val; }

    public ListNode(E val, ListNode<E> prev) {
        this.val = val;
        this.prev = prev;
    }

    public ListNode(E val, ListNode<E> next, ListNode<E> prev) {
        this.val = val;
        this.next = next;
        this.prev = prev;
    }
}
