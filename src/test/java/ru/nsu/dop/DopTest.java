package ru.nsu.dop;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.nsu.array.pathcopy.PersistentArray;
import ru.nsu.list.PersistentLinkedList;
import ru.nsu.map.PersistentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class DopTest {

    @Nested
    class CollectionLikeElement {

        @Test
        void testArray() {
                PersistentArray<PersistentArray<Integer>> arr = new PersistentArray<>();

                for (int i = 0; i < 5; i++) {
                    arr.add(new PersistentArray<>());
                }

                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        arr.get(j).add(i);
                    }
                }

                for (int i = 0; i < 5; i++) {
                    assertEquals("version: 6\n{0, 1, 2, 3, 4}", arr.get(i).toString());
                }
            }

        @Test
        void testList() {
            PersistentLinkedList<PersistentLinkedList<Integer>> list = new PersistentLinkedList<>();

            for (int i = 0; i < 5; i++) {
                list.add(new PersistentLinkedList<>());
            }

            for (int i = 0; i < 5; i++) {
                PersistentLinkedList<Integer> tmp = list.get(i).getVal();

                for (int j = 0; j < 5; j++) {
                    tmp.add(j);
                }
            }

            for (int i = 0; i < 5; i++) {
                assertEquals("version: 5\n(0, 1, 2, 3, 4)", list.get(i).getVal().toString());
            }
        }

        @Test
        void testMap() {
            PersistentHashMap<Integer, PersistentArray<Integer>> map = new PersistentHashMap<>();

            for (int i = 0; i < 5; i++) {
                map.put(i, new PersistentArray<>());
            }

            for (int i = 0; i < 5; i++) {
                PersistentArray<Integer> tmp = map.get(i);

                for (int j = 0; j < 5; j++) {
                    tmp.add(j);
                }
            }

            for (int i = 0; i < 5; i++) {
                assertEquals("version: 6\n{0, 1, 2, 3, 4}", map.get(i).toString());
            }
        }
    }

    @Nested
    class UndoRedo {

        @Test
        void testArray() {
            PersistentArray<Integer> arr = new PersistentArray<>();
            for (int i = 0; i < 5; i++) {
                arr.add(i);
            }
            assertEquals("version: 6\n{0, 1, 2, 3, 4}", arr.toString());
            arr.undo();
            assertEquals("version: 5\n{0, 1, 2, 3}", arr.toString());
        }

        @Test
        void testList() {
            PersistentLinkedList<Integer> list = new PersistentLinkedList<>();
            for (int i = 0; i < 5; i++) {
                list.add(i);
            }
            assertEquals("version: 5\n(0, 1, 2, 3, 4)", list.toString());
            list.undo();
            assertEquals("version: 4\n(0, 1, 2, 3)", list.toString());
        }

        @Test
        void testMap() {
            PersistentHashMap<Integer, String> map = new PersistentHashMap<>();
            for (int i = 0; i < 5; i++) {
                map.put(i, "" + i);
            }
            assertEquals("version: 6\n{(0 = 0), (1 = 1), (2 = 2), (3 = 3), (4 = 4)}", map.toString());
            map.undo();
            assertEquals("version: 5\n{(0 = 0), (1 = 1), (2 = 2), (3 = 3)}", map.toString());
        }
    }
}
