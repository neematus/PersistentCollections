package ru.nsu.list;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PersistentLinkedListTest {

    PersistentLinkedList<Integer> list = new PersistentLinkedList<>(new ListNode<>(1));

    @Test
    void testGetCurrentVersion() {
        assertEquals(1, list.getCurrentVersion());
    }

    @Test
    void testSize1() {
        assertEquals(1, list.size());
    }

    @Test
    void testSize2() {
        assertEquals(1, list.size(1));
    }

    @Test
    void testAdd1() {
        list.add(2);
        list.add(3);
        System.out.println(list.toString());
        assertEquals(3, list.size());
    }

    @Test
    void testAdd2() {
        list.add(2);
        list.add(3, 1);
        assertEquals("version: 3\n(1, 3)", list.toString(3));
    }

    @Test
    void testAdd3() {
        list.add(3);
        list.addAtIndex(1, 2);
        assertEquals("version: 3\n(1, 2, 3)", list.toString(3));
    }

    @Test
    void testAdd4() {
        list.addAtIndex(0, 0, 1);
        assertEquals("version: 2\n(0, 1)", list.toString());
    }

    @Test
    void testRemove1() {
        list.add(2);
        list.remove(2);
        assertEquals("version: 3\n(1)", list.toString());
    }

    @Test
    void testRemove2() {
        list.add(2);
        list.add(3);
        list.remove(1, 3);
        assertEquals("version: 4\n(2, 3)", list.toString());
    }

    @Test
    void testRemove3() {
        list.add(2);
        list.add(3);
        list.removeAtIndex(1);
        assertEquals("version: 4\n(1, 3)", list.toString());
    }

    @Test
    void testRemove4() {
        list.add(2);
        list.add(3);
        list.add(4);
        list.removeAtIndex(2, 4);
        assertEquals("version: 5\n(1, 2, 4)", list.toString());
    }

    @Test
    void testGet1() {
        assertEquals(1, list.get(0).getVal());
    }

    @Test
    void testGet2() {
        list.add(2);
        list.add(3);
        assertEquals(2, list.get(1, 2).getVal());
        assertEquals(3, list.get(2, 3).getVal());
    }

    @Test
    void testSet1() {
        list.set(0, 42);
        assertEquals("version: 2\n(42)", list.toString());
    }

    @Test
    void testSet2() {
        list.add(2);
        list.add(3);
        list.set(1, 42, 2);
        assertEquals("version: 4\n(1, 42)", list.toString());
    }

    @Test
    void testIsEmpty1() {
        assertFalse(list.isEmpty());
    }

    @Test
    void testIsEmpty2() {
        list.add(2);
        list.clear();
        assertTrue(list.isEmpty(2));
    }

    @Test
    void testContains1() {
        assertTrue(list.contains(1));
    }

    @Test
    void testContains2() {
        list.add(2);
        list.add(3);
        assertTrue(list.contains(2, 3));
    }

    @Test
    void testClear1() {
        list.clear();
        assertTrue(list.isEmpty());
    }

    @Test
    void testClear2() {
        list.add(2);
        list.clear(1);
        assertTrue(list.isEmpty(1));
    }

    @Test
    void testToString1() {
        assertEquals("version: 1\n(1)", list.toString());
    }

    @Test
    void testToString2() {
        assertEquals("version: 1\n(1)", list.toString(1));
    }

    @Test
    void testUndo() {
        list.add(2);
        list.undo();
        assertEquals("version: 1\n(1)", list.toString());
    }

    @Test
    void testRedo() {
        list.add(2);
        list.undo();
        list.redo();
        assertEquals("version: 2\n(1, 2)", list.toString());
    }
}