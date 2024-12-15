package ru.nsu.map;

import ru.nsu.array.pathcopy.PersistentArray;
import ru.nsu.util.PersistentMap;

import java.util.ArrayList;

public class PersistentHashMap<K, V> implements PersistentMap<K, V> {
    private static int capacity = 16;
    private final PersistentArray<ArrayList<MapEntry<K, V>>> versions;
    private Integer redo = 0;

    public PersistentHashMap() {
        PersistentArray<ArrayList<MapEntry<K, V>>> table = new PersistentArray<>();
        for (int i = 0; i < capacity; i++) {
            table.add(new ArrayList<>());
        }

        versions = table;
    }
    public PersistentHashMap(int  size) {
        PersistentArray<ArrayList<MapEntry<K, V>>> table = new PersistentArray<>();
        capacity = size;
        for (int i = 0; i < size; i++) {
            table.add(new ArrayList<>());
        }

        versions = table;
    }

    /**
     * Откатывает последнее изменение коллекции
     */
    public void undo() {
        if (!versions.isEmpty()) {
            versions.undo();
            redo++;
        }
    }

    /**
     * Возвращает последний откат коллекции
     */
    public void redo() {
        if (redo > 0) {
            versions.redo();
            redo--;
        }
    }

    private int index(int hash) {
        return hash % capacity;
    }

    /**
     * Возвращает текущую версию коллекции
     * @return текущая версию коллекции
     */
    public int getCurrentVersion() {
        return versions.getCurrentVersion() - capacity;
    }

    @Override
    public int size() {
        return size(getCurrentVersion());
    }

    @Override
    public int size(int version) {
        if (version > getCurrentVersion()) {
            return 0;
        }

        int count = 0;

        for (int i = 0; i < capacity; i++) {
            ArrayList<MapEntry<K,V>> entry = versions.get(i, version + capacity -1);
            if (entry != null)
                count += entry.size();
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
        if (version > versions.getCurrentVersion()) {
            return false;
        }

        int hash = key.hashCode();
        int index = index(hash);

        ArrayList<MapEntry<K, V>> el = versions.get(index, version + capacity - 1);
        if (el != null) {
            for (MapEntry<K, V> node : el) {
                if (node != null && node.getKey().equals(key)) {
                    return true;
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
        if (version > versions.getCurrentVersion()) {
            return false;
        }

        for (int i = 0; i < capacity; i++) {
            ArrayList<MapEntry<K, V>> el = versions.get(i, version + capacity - 1);
            if (el != null) {
                for (MapEntry<K, V> node : el) {
                    if (node != null && node.getValue().equals(value)) {
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
        if (version > versions.getCurrentVersion()) {
            return null;
        }

        int hash = key.hashCode();
        int index = index(hash);
        ArrayList<MapEntry<K, V>> el = versions.get(index, version + capacity - 1);
        if (el != null) {
            for (MapEntry<K, V> node : el) {
                if (node != null && node.getKey().equals(key)) {
                    return node.getValue();
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
        if(version > versions.getCurrentVersion()) {
            return null;
        }

        try {
            int hash = key.hashCode();
            int index = index(hash);
            ArrayList<MapEntry<K, V>> entry = versions.get(index, version + capacity -1);
            ArrayList<MapEntry<K, V>> newEntry = new ArrayList<>(entry);

            if (containsKey(key, version)) {
                int counter = 0;
                MapEntry<K, V> el = newEntry.get(counter);
                while (el.getKey() != null && !el.getKey().equals(key)) {
                    el = newEntry.get(++counter);
                }
                el.setValue(value);
                newEntry.set(counter, el);
                versions.set(index, newEntry);

            } else {
                MapEntry<K, V> newEl = new MapEntry<>(key, value);
                newEntry.add(newEl);
                versions.set(index, newEntry, version + capacity -1);
            }

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
        if(version > versions.getCurrentVersion() || !containsKey(key, version)) {
            return null;
        }

        try {
            int hash = key.hashCode();
            int index = index(hash);
            V value = null;
            ArrayList<MapEntry<K, V>> entry = versions.get(index, version + capacity - 1);
            ArrayList<MapEntry<K, V>> newEntry = new ArrayList<>(entry);

            for (MapEntry<K, V> kvMapEntry : newEntry) {
                if (kvMapEntry != null && kvMapEntry.getKey().equals(key)) {
                    value = kvMapEntry.getValue();
                    break;
                }
            }
            newEntry.removeIf(entry2 -> entry2.getKey().equals(key));
            versions.set(index, newEntry, version + capacity - 1);

            return value;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String toString() {
        return toString(getCurrentVersion());
    }

    @Override
    public String toString(int version) {
        if (version > versions.getCurrentVersion()) {
            return "{empty}";
        }

        StringBuilder result = new StringBuilder("version: " + version + "\n");

        result.append("{");
        for (int i = 0; i < capacity; i++) {
            ArrayList<MapEntry<K,V>> entry = versions.get(i, version + capacity -1);
            if (entry != null)
                for (MapEntry<K, V> node : entry) {
                        result.append(node.toString()).append(", ");
                }
        }

        if (result.lastIndexOf(", ") == - 1) {
            return result.append("empty}").toString();
        }
        return result.delete(result.lastIndexOf(", "), result.lastIndexOf(", ") + 2).append("}").toString();
    }
}
