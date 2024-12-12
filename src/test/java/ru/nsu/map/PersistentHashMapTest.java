package ru.nsu.map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PersistentHashMapTest {

    private final PersistentHashMap<Integer, String> map = new PersistentHashMap<>();

    @Test
    void testSize1() {
        assertEquals(0, map.size());
    }

    @Test
    void testSize2() {
        map.put(1, "1");
        assertEquals(1, map.size());
    }

    @Test
    void testSize3() {
        map.put(1, "1");
        map.put(2, "2");
        assertEquals(2, map.size(3));
    }

    @Test
    void testIsEmpty1() {
        assertTrue(map.isEmpty());
    }

    @Test
    void testIsEmpty2() {
        map.put(1, "1");
        assertFalse(map.isEmpty());
    }

    @Test
    void testIsEmpty3() {
        map.put(1, "1");
        assertTrue(map.isEmpty(1));
    }

    @Test
    void testContainsKey1() {
        map.put(1, "1");
        assertTrue(map.containsKey(1));
    }

    @Test
    void testContainsKey2() {
        map.put(1, "1");
        map.put(2, "2");
        assertTrue(map.containsKey(1, 3));
    }

    @Test
    void testContainsValue1() {
        map.put(1, "1");
        assertTrue(map.containsValue("1"));
    }

    @Test
    void testContainsValue2() {
        map.put(1, "1");
        map.put(2, "2");
        assertTrue(map.containsValue("1", 3));
    }

    @Test
    void testGet1() {
        map.put(1, "1");
        assertEquals("1", map.get(1));
    }

    @Test
    void testGet2() {
        map.put(1, "1");
        map.put(2, "2");
        assertEquals("1", map.get(1, 3));
    }

    @Test
    void testPut1() {
        assertEquals("1", map.put(1, "1"));
        assertEquals("version: 2\n{(1 = 1)}", map.toString());
    }

    @Test
    void testPut2() {
        map.put(1, "1");
        assertEquals("2", map.put(2, "2", 2));
        assertEquals("version: 3\n{(1 = 1), (2 = 2)}", map.toString(3));
    }

    @Test
    void testPut3() {
        map.put(1, "1");
        map.put(1, "11");
        assertEquals("version: 3\n{(1 = 11)}", map.toString(3));
    }

    @Test
    void testPut4() {
        map.put(1, "1");
        map.put(17, "11");
        assertEquals("version: 3\n{(1 = 1), (17 = 11)}", map.toString(3));
    }

    @Test
    void testRemove1() {
        map.put(1, "1");
        assertEquals("1", map.remove(1));
        assertEquals("version: 3\n{empty}", map.toString(3));
    }

    @Test
    void testRemove2() {
        map.put(1, "1");
        map.put(2, "2");
        assertEquals("1", map.remove(1, 3));
        assertEquals("version: 4\n{(2 = 2)}", map.toString());
    }

    @Test
    void testClear1() {
        map.put(1, "1");
        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    void testClear2() {
        map.put(1, "1");
        map.put(2, "2");
        map.clear(3);
        assertTrue(map.isEmpty());
        assertFalse(map.isEmpty(2));
    }

    @Test
    void testToString1() {
        assertEquals("version: 1\n{empty}", map.toString());
    }

    @Test
    void testToString2() {
        map.put(1, "1");
        assertEquals("version: 2\n{(1 = 1)}", map.toString());
    }

    @Test
    void testToString3() {
        map.put(1, "1");
        assertEquals("version: 1\n{empty}", map.toString(1));
    }

    @Test
    void testToString4() {
        map.put(1, "1");
        map.put(2, "2");
        assertEquals("version: 3\n{(1 = 1), (2 = 2)}", map.toString(3));
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