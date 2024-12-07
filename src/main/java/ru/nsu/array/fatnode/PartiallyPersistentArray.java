package ru.nsu.array.fatnode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Частично персистентный массив
 */
public class PartiallyPersistentArray<E> {

    private int latestVersion;
    private final List<PersistentElement<E>> elements = new ArrayList<>();

    public E get(int index) {
        return elements.get(index).get();
    }

    public E get(int index, int version) {
        return elements.get(index).get(version);
    }

    public E set(int index, E e) {
        return elements.get(index).set(++latestVersion, e);
    }

    public boolean add(E e) {
        PersistentElement<E> newElement = new PersistentElement<>(++latestVersion);
        newElement.set(latestVersion, e);
        return elements.add(newElement);
    }

    public String toString() {
        return "[" + elements.stream()
                .map(PersistentElement::toString)
                .collect(Collectors.joining(", ")) + "] (v." + latestVersion + ")";
    }

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

}
