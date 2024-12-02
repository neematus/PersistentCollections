package ru.nsu.map;

import ru.nsu.common.AVLTree;
import ru.nsu.list.ListNode;
import ru.nsu.list.PersistentLinkedList;
import ru.nsu.util.PersistentMap;

import java.util.ArrayList;
import java.util.Stack;

public class PersistentHashMap<K, V> implements PersistentMap<K, V> {
    private static int capacity = 16;
    private final ArrayList<AVLTree<PersistentLinkedList<MapEntry<K, V>>>> versions;
    private final Stack<AVLTree<PersistentLinkedList<MapEntry<K, V>>>> redo = new Stack<>();

    public PersistentHashMap() {
        AVLTree<PersistentLinkedList<MapEntry<K, V>>> table = new AVLTree<>();
        for (int i = 0; i < capacity; i++) {
            table.insert(i, new PersistentLinkedList<>(null));
        }

        versions = new ArrayList<>();
        versions.add(table);
    }
    public PersistentHashMap(int  size) {
        AVLTree<PersistentLinkedList<MapEntry<K, V>>> table = new AVLTree<>();
        capacity = size;
        for (int i = 0; i < size; i++) {
            table.insert(i, new PersistentLinkedList<>(null));
        }

        versions = new ArrayList<>();
        versions.add(table);
    }

    public void undo() {
        if (!versions.isEmpty()) {
            AVLTree<PersistentLinkedList<MapEntry<K, V>>> table = versions.get(versions.size() - 1);
            redo.push(table);
            versions.remove(versions.size() - 1);
        }
    }

    public void redo() {
        if (!redo.isEmpty()) {
            AVLTree<PersistentLinkedList<MapEntry<K, V>>> table = redo.pop();
            versions.add(table);
        }
    }

    private int index(int hash) {
        return hash % capacity;
    }
    private int hash() {
        return super.hashCode();
    }

    /**
     * Возвращает текущую версию коллекции
     * @return текущая версию коллекции
     */
    public int getCurrentVersion() {
        return versions.size();
    }

    @Override
    public int size() {
        return size(getCurrentVersion());
    }

    @Override
    public int size(int version) {
        if (version >= getCurrentVersion()) {
            return 0;
        }

        int count = 0;
        AVLTree<PersistentLinkedList<MapEntry<K, V>>> current = versions.get(version - 1);

        if (current == null) {
            return 0;
        }

        for (int i = 0; i < capacity; i++) {
            PersistentLinkedList<MapEntry<K,V>> el = current.findMaxNotGreaterThan(i);
            if (el != null && el.getCurrentVersion() >= version) {
                count += el.size(version);
            }
        }

        return count;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty(getCurrentVersion());
    }

    @Override
    public boolean isEmpty(int version) {
        return size(version) == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return containsKey(key, getCurrentVersion());
    }

    @Override
    public boolean containsKey(Object key, int version) {
        if (version > versions.size()) {
            return false;
        }

        AVLTree<PersistentLinkedList<MapEntry<K, V>>> root = versions.get(version - 1);

        for (int i = 0; i < capacity; i++) {
            PersistentLinkedList<MapEntry<K, V>> el = root.findMaxNotGreaterThan(i);
            if (el != null) {
                int count;
                if(el.getCurrentVersion() < version) count = el.getCurrentVersion(); else count = el.size(version);
                for (int j = 0; j < count; j++) {
                    ListNode<MapEntry<K,V>> node = el.get(j);
                    if (node != null && node.getVal() != null && node.getVal().getKey().equals(key)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(value, getCurrentVersion());
    }

    @Override
    public boolean containsValue(Object value, int version) {
        if (version > versions.size()) {
            return false;
        }

        AVLTree<PersistentLinkedList<MapEntry<K, V>>> root = versions.get(version - 1);

        for (int i = 0; i < capacity; i++) {
            PersistentLinkedList<MapEntry<K, V>> el = root.findMaxNotGreaterThan(i);
            if (el != null) {
                int count;
                if(el.getCurrentVersion() < version) count = el.getCurrentVersion(); else count = el.size(version);
                for (int j = 0; j < count; j++) {
                    ListNode<MapEntry<K,V>> node = el.get(j);
                    if (node != null && node.getVal() != null && node.getVal().getValue().equals(value)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public V get(Object key) {
        return get(key, getCurrentVersion());
    }

    @Override
    public V get(Object key, int version) {
        if (version > versions.size()) {
            return null;
        }

        AVLTree<PersistentLinkedList<MapEntry<K, V>>> root = versions.get(version - 1);

        for (int i = 0; i < capacity; i++) {
            PersistentLinkedList<MapEntry<K, V>> el = root.findMaxNotGreaterThan(i);
            if (el != null) {
                int count;
                if(el.getCurrentVersion() < version) count = el.getCurrentVersion(); else count = el.size(version);
                for (int j = 0; j < count; j++) {
                    ListNode<MapEntry<K,V>> node = el.get(j);
                    if (node != null && node.getVal() != null && node.getVal().getKey().equals(key)) {
                        return node.getVal().getValue();
                    }
                }
            }
        }

        return null;
    }

    @Override
    public V put(K key, V value) {
        return put(key, value, getCurrentVersion());
    }

    @Override
    public V put(K key, V value, int version) {
        if(version > versions.size()) {
            return null;
        }
        int hash = key.hashCode();
        int index = index(hash);
        AVLTree<PersistentLinkedList<MapEntry<K, V>>> root = versions.get(version - 1);

        try {
            PersistentLinkedList<MapEntry<K, V>> entry = root.findMaxNotGreaterThan(index);
            if (entry == null) {
                entry = new PersistentLinkedList<>(null);
            }
            while (entry.getCurrentVersion() < version) {
                ListNode<MapEntry<K, V>> el = entry.get(entry.getCurrentVersion() - 1);
                if (el == null) {
                    entry.add(null);
                } else {
                    entry.add(el.getVal());
                }
            }
            MapEntry<K, V> newEntry = new MapEntry<>(key, value);
            entry.add(newEntry);
//            undo.push(newEntry);

            versions.add(root);

            return value;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public V remove(Object key) {
        return remove(key, getCurrentVersion());
    }

    @Override
    public V remove(Object key, int version) {
        if(version > versions.size()) {
            return null;
        }

        int hash = key.hashCode();
        int index = index(hash);
        AVLTree<PersistentLinkedList<MapEntry<K, V>>> root = versions.get(version - 1);

        try {
            PersistentLinkedList<MapEntry<K, V>> entry = root.findMaxNotGreaterThan(index);
            if (entry == null) {
                return null;
            }
            while (entry.getCurrentVersion() < version) {
                ListNode<MapEntry<K, V>> el = entry.get(entry.getCurrentVersion() - 1);
                if (el == null) {
                    entry.add(null);
                } else {
                    entry.add(el.getVal());
                }
            }
            for (int i = 0; i < entry.size(version); i++) {
                if (entry.get(i, version) != null && entry.get(i).getVal().getKey().equals(key)) {
                    V value = entry.get(i).getVal().getValue();
                    entry.removeAtIndex(i, version);
//                    MapEntry<K, V> newEntry = new MapEntry<>((K) key, value);
//                    undo.push(newEntry);
                    return value;
                }
            }

            versions.add(root);
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public boolean clear() {
        return clear(getCurrentVersion());
    }

    @Override
    public boolean clear(int version) {
        versions.set(version - 1, null);
        return true;
    }

    @Override
    public String toString() {
        return toString(getCurrentVersion());
    }

    @Override
    public String toString(int version) {
        if (version > versions.size()) {
            return "{empty}";
        }

        StringBuilder result = new StringBuilder("version: " + version + "\n");
        AVLTree<PersistentLinkedList<MapEntry<K, V>>> current = versions.get(version - 1);

        result.append("{");
        for (int i = 0; i < capacity; i++) {
            PersistentLinkedList<MapEntry<K,V>> entry = current.findMaxNotGreaterThan(i);
            String list;
            if(entry.getCurrentVersion() < version)
                list = entry.toString(entry.getCurrentVersion());
            else list = entry.toString(version);
            if (!list.contains("empty")) {
                for (String el : list.substring(list.indexOf("\n")+1).split(", ")) {
                    if (el.contains("=")) {
                        result.append(el).append(", ");
                    }
                }
            }
        }

        if (result.lastIndexOf(", ") == - 1) {
            return result.append("empty}").toString();
        }
        return result.delete(result.lastIndexOf(", "), result.lastIndexOf(", ") + 2).append("}").toString();
    }
}
