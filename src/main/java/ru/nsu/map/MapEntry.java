package ru.nsu.map;

public class MapEntry<K, V> {
    private K key = null;
    private V value = null;
//    private MapEntry<K, V> nextEntry = null;

    public MapEntry() {}
    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }
//    public MapEntry(K key, V value, MapEntry<K, V> nextEntry) {
//        this.key = key;
//        this.value = value;
//        this.nextEntry = nextEntry;
//    }

    public K getKey() {
        return key;
    }
    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }
    public void setValue(V value) {
        this.value = value;
    }

//    public MapEntry<K, V> getNextEntry() {
//        return nextEntry;
//    }
//    public void setNextEntry(MapEntry<K, V> nextEntry) {
//        this.nextEntry = nextEntry;
//    }

    @Override
    public int hashCode() {
        return key.hashCode() * 42 + value.hashCode();
    }

    @Override
    public String toString() {
        return key + " = " + value;
    }
}
