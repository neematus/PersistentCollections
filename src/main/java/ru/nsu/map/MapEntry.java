package ru.nsu.map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapEntry<K, V> {
    private K key = null;
    private V value = null;

    public MapEntry() {}
    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int hashCode() {
        return key.hashCode() * 42 + value.hashCode();
    }

    @Override
    public String toString() {
        return key + " = " + value;
    }
}
