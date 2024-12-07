package ru.nsu.array.fatnode;

import lombok.Getter;

@Getter
public class PersistentElement<E> {

    private final int initialVersion;
    private final AVLTree<E> versions = new AVLTree<>();

    public PersistentElement(int version) {
        initialVersion = version;
    }

    public E get() {
        return versions.findMaxNotGreaterThan(Integer.MAX_VALUE);
    }

    public E get(int version) {
        if (version < initialVersion) {
            return null;
        }
        return versions.findMaxNotGreaterThan(version);
    }

    public E set(int version, E element) {
        versions.insert(version, element);
        return versions.findMaxNotGreaterThan(version - 1);
    }

    public String toString() {
        return get().toString();
    }

    public String toString(int version) {
        if (version < initialVersion) {
            return null;
        }
        return get(version).toString();
    }
}
