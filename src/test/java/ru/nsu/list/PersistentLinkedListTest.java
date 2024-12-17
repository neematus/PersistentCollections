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
    void testSizeEmpty() {
        assertEquals(1, list.size());
    }

    @Test
    void testSizeByVersion() {
        assertEquals(1, list.size(1));
    }

    @Test
    void testAdd() {
        list.add(2);
        list.add(3);
        assertEquals(3, list.size());
    }

    @Test
    void testAddByVersion() {
        list.add(2);
        list.add(3, 1);
        assertEquals("version: 3\n(1, 3)", list.toString(3));
    }

    @Test
    void testAddAtIndex() {
        list.add(3);
        list.addAtIndex(1, 2);
        assertEquals("version: 3\n(1, 2, 3)", list.toString(3));
    }

    @Test
    void testAddAtFirst() {
        list.addAtIndex(0, 0, 1);
        assertEquals("version: 2\n(0, 1)", list.toString());
    }

    @Test
    void testRemove() {
        list.add(2);
        list.remove(2);
        assertEquals("version: 3\n(1)", list.toString());
    }

    @Test
    void testRemoveByVersion() {
        list.add(2);
        list.add(3);
        list.remove(1, 3);
        assertEquals("version: 4\n(2, 3)", list.toString());
    }

    @Test
    void testRemoveAtIndex() {
        list.add(2);
        list.add(3);
        list.removeAtIndex(1);
        assertEquals("version: 4\n(1, 3)", list.toString());
    }

    @Test
    void testRemoveAtIndexByVersion() {
        list.add(2);
        list.add(3);
        list.add(4);
        list.removeAtIndex(2, 4);
        assertEquals("version: 5\n(1, 2, 4)", list.toString());
    }

    @Test
    void testGet() {
        assertEquals(1, list.get(0).getVal());
    }

    @Test
    void testGetByVersion() {
        list.add(2);
        list.add(3);
        assertEquals(2, list.get(1, 2).getVal());
        assertEquals(3, list.get(2, 3).getVal());
    }

    @Test
    void testSet() {
        list.set(0, 42);
        assertEquals("version: 2\n(42)", list.toString());
    }

    @Test
    void testSetByVersion() {
        list.add(2);
        list.add(3);
        list.set(1, 42, 2);
        assertEquals("version: 4\n(1, 42)", list.toString());
    }

    @Test
    void testIsEmpty() {
        assertFalse(list.isEmpty());
    }

    @Test
    void testContains() {
        assertTrue(list.contains(1));
    }

    @Test
    void testContainsByVersion() {
        list.add(2);
        list.add(3);
        assertTrue(list.contains(2, 3));
    }

    @Test
    void testToString() {
        assertEquals("version: 1\n(1)", list.toString());
    }

    @Test
    void testToStringByVersion() {
        list.add(2);
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