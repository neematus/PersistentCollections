package ru.nsu.map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapEntry<K, V> {
    private K key = null;
    private V value = null;

    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "(" + key + " = " + value + ")";
    }
}
