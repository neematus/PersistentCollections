package ru.nsu.map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PersistentHashMapTest {

    private final PersistentHashMap<Integer, String> map = new PersistentHashMap<>();

    @Test
    void testSizeEmpty() {
        assertEquals(0, map.size());
    }

    @Test
    void testSizeAfterPut() {
        map.put(1, "1");
        assertEquals(1, map.size());
    }

    @Test
    void testSizeByVersion() {
        map.put(1, "1");
        map.put(2, "2");
        assertEquals(1, map.size(2));
    }

    @Test
    void testIsEmpty() {
        assertTrue(map.isEmpty());
    }

    @Test
    void testIsEmptyAfterPut() {
        map.put(1, "1");
        assertFalse(map.isEmpty());
    }

    @Test
    void testIsEmptyByVersion() {
        map.put(1, "1");
        assertTrue(map.isEmpty(1));
    }

    @Test
    void testContainsKeyAfterPut() {
        map.put(1, "1");
        assertTrue(map.containsKey(1));
    }

    @Test
    void testContainsKeyByVersion() {
        map.put(1, "1");
        map.put(2, "2");
        assertFalse(map.containsKey(2, 2));
    }

    @Test
    void testContainsValueAfterPut() {
        map.put(1, "1");
        assertTrue(map.containsValue("1"));
    }

    @Test
    void testContainsValueByVersion() {
        map.put(1, "1");
        map.put(2, "2");
        assertTrue(map.containsValue("1", 3));
    }

    @Test
    void testGetAfterPut() {
        map.put(1, "1");
        assertEquals("1", map.get(1));
    }

    @Test
    void testGetByVersion() {
        map.put(1, "1");
        map.put(2, "2");
        assertEquals("1", map.get(1, 3));
    }

    @Test
    void testPut() {
        assertEquals("1", map.put(1, "1"));
        assertEquals("version: 2\n{(1 = 1)}", map.toString());
    }

    @Test
    void testPutByVersion() {
        map.put(1, "1");
        assertEquals("2", map.put(2, "2", 2));
        assertEquals("version: 3\n{(1 = 1), (2 = 2)}", map.toString(3));
    }

    @Test
    void testPutAsReplace() {
        map.put(1, "1");
        map.put(1, "11");
        assertEquals(1, map.size(3));
        assertEquals("version: 3\n{(1 = 11)}", map.toString(3));
    }

    @Test
    void testPutAsCollision() {
        map.put(1, "1");
        map.put(17, "11");
        assertEquals("version: 3\n{(1 = 1), (17 = 11)}", map.toString(3));
    }

    @Test
    void testRemove() {
        map.put(1, "1");
        assertEquals("1", map.remove(1));
        assertEquals("version: 3\n{empty}", map.toString(3));
    }

    @Test
    void testRemoveByVersion() {
        map.put(1, "1");
        map.put(2, "2");
        assertEquals("1", map.remove(1, 3));
        assertEquals("version: 4\n{(2 = 2)}", map.toString());
    }

    @Test
    void testToStringEmpty() {
        assertEquals("version: 1\n{empty}", map.toString());
    }

    @Test
    void testToStringAfterPut() {
        map.put(1, "1");
        assertEquals("version: 2\n{(1 = 1)}", map.toString());
    }

    @Test
    void testToStringByVersion() {
        map.put(1, "1");
        assertEquals("version: 1\n{empty}", map.toString(1));
    }

    @Test
    void testUndo() {
        map.put(1, "1");
        map.put(2, "2");
        map.undo();
        assertEquals("version: 2\n{(1 = 1)}", map.toString());
    }

    @Test
    void testRedo() {
        map.put(1, "1");
        map.put(2, "2");
        map.undo();
        map.redo();
        assertEquals("version: 3\n{(1 = 1), (2 = 2)}", map.toString());
    }
}