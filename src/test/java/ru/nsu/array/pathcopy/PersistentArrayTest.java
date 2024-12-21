package ru.nsu.array.pathcopy;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersistentArrayTest {

    private final PersistentArray<Integer> uut = new PersistentArray<>();

    @Test
    void getCurrentVersion() {
        assertEquals(1, uut.getCurrentVersion());

        uut.add(1);
        assertEquals(2, uut.getCurrentVersion());

        uut.undo();
        assertEquals(1, uut.getCurrentVersion());
    }

    @Test
    void isEmpty() {
        assertTrue(uut.isEmpty());

        uut.add(1);
        assertFalse(uut.isEmpty());
    }

    @Test
    void get() {
        uut.add(1);
        assertEquals(1, uut.get(0));
    }

    @Test
    void get_illegalIndex() {
        uut.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> uut.get(2));
    }

    @Test
    void get_version() {
        uut.add(1);
        uut.set(0, 2);

        assertEquals(1, uut.get(0, 2));
        assertEquals(2, uut.get(0, 3));
    }

    @Test
    void get_illegalVersion() {
        uut.add(1);
        assertThrows(IllegalArgumentException.class, () -> uut.get(0, uut.getCurrentVersion() + 1));
    }

    @Test
    void set() {
        uut.add(1);
        uut.add(2);
        uut.set(0, 2);

        assertEquals(List.of(2, 2), uut.toArray());
    }

    @Test
    void set_illegalIndex() {
        uut.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> uut.set(2, 2));
    }

    @Test
    void set_version() {
        uut.add(1);
        uut.add(2);
        uut.add(3);

        uut.set(0, 3, 3);

        assertEquals(List.of(3, 2), uut.toArray());
    }

    @Test
    void set_illegalVersion() {
        uut.add(1);
        assertThrows(IllegalArgumentException.class, () -> uut.set(0, 2, uut.getCurrentVersion() + 1));
    }

    @Test
    void add() {
        uut.add(1);

        assertEquals(List.of(1), uut.toArray());
    }

    @Test
    void add_version() {
        uut.add(1);
        uut.add(2);
        uut.add(3);

        uut.add(3, 2);

        assertEquals(List.of(1, 3), uut.toArray());
    }

    @Test
    void add_illegalVersion() {
        uut.add(1);
        assertThrows(IllegalArgumentException.class, () -> uut.add(2, uut.getCurrentVersion() + 1));
    }

    @Test
    void undo() {
        uut.add(1);
        uut.undo();

        assertTrue(uut.isEmpty());
        assertEquals(1, uut.getCurrentVersion());
    }

    @Test
    void redo() {
        uut.add(1);
        uut.undo();
        uut.redo();

        assertFalse(uut.isEmpty());
        assertEquals(2, uut.getCurrentVersion());
    }
}
