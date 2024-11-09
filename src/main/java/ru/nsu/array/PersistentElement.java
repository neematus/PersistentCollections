package ru.nsu.array;

import lombok.Getter;
import ru.nsu.common.AVLTree;

@Getter
public class PersistentElement<E> {

    private final int initialVersion;
    private final AVLTree<E> versions = new AVLTree<>();

    public PersistentElement(int version) {
        initialVersion = version;
    }

    /**
     * @return элемент текущей версии
     */
    public E get() {
        return versions.findMaxNotGreaterThan(Integer.MAX_VALUE);
    }

    /**
     * @param version версия
     * @return элемент указанной версии
     */
    public E get(int version) {
        return versions.findMaxNotGreaterThan(version);
    }

}
