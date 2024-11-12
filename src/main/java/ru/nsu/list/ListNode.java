package ru.nsu.list;

public class ListNode<E> {
    private ListNode<E> next = null;
    private ListNode<E> prev = null;
    private E val;

    public ListNode() {}
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

    public ListNode<E> getNext() {
        return next;
    }

    public void setNext(ListNode<E> next) {
        this.next = next;
    }

    public ListNode<E> getPrev() {
        return prev;
    }

    public void setPrev(ListNode<E> prev) {
        this.prev = prev;
    }

    public E getVal() {
        return val;
    }

    public void setVal(E val) {
        this.val = val;
    }
}
