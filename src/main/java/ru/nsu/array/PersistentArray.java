package ru.nsu.array;

import ru.nsu.util.PersistentCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Персистентный массив
 */
public class PersistentArray<E> implements PersistentCollection<E> {

    private List<PersistentElement<E>> elements = new ArrayList<>();





    @Override
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size(int version) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(E e, int version) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(int index, E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(int index, E e, int version) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o, int version) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(int index, int version) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object get(int index) {
        return elements.get(index).get();
    }

    @Override
    public Object get(int index, int version) {
        return elements.get(index).get(version);
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty(int version) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o, int version) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean clear(int version) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString(int version) {
        return null;
    }
}
