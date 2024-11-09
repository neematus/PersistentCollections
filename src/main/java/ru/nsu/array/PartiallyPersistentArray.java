package ru.nsu.array;

import ru.nsu.util.PersistentCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Частично персистентный массив
 */
public class PartiallyPersistentArray<E> implements PersistentCollection<E> {

    private int latestVersion;
    private final List<PersistentElement<E>> elements = new ArrayList<>();

    @Override
    public E get(int index) {
        return elements.get(index).get();
    }

    @Override
    public E get(int index, int version) {
        return elements.get(index).get(version);
    }

    public E set(int index, E e) {
        return elements.get(index).set(++latestVersion, e);
    }

    @Override
    public boolean add(E e) {
        PersistentElement<E> newElement = new PersistentElement<>(++latestVersion);
        newElement.set(latestVersion, e);
        return elements.add(newElement);
    }

    @Override
    public String toString() {
        return "[" + elements.stream()
                .map(PersistentElement::toString)
                .collect(Collectors.joining(", ")) + "] (v." + latestVersion + ")";
    }

    @Override
    public String toString(int version) {
        return "[" + elements.stream()
                .map(element -> element.toString(version))
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", ")) + "] (v." + version + ")";
    }

    public void printHistory() {
        System.out.println("Changes:");
        for (int version = 0; version <= latestVersion; version++) {
            System.out.println(toString(version));
        }
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size(int version) {
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
}
