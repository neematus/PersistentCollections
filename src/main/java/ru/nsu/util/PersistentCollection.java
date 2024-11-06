package ru.nsu.util;

public interface PersistentCollection {

    /**
     * Возвращает размер текущей версии коллекции
     * @return размер текущей версии коллекции
     */
    int size();

    /**
     * Возвращает размер указанной версии коллекции
     * @param version версия коллекции
     * @return размер указанной версии коллекции
     */
    int size(int version);

    /**
     * Добавляет новый элемент в конец текущей версии коллекции
     * @param o новый элемент
     * @return true, если элемент был успешно добавлен
     */
    boolean add(Object o);

    /**
     * Добавляет новый элемент в конец указанной версии коллекции
     * @param o новый элемент
     * @param version версия коллекции
     * @return true, если элемент был успешно добавлен
     */
    boolean add(Object o, int version);

    /**
     * Добавляет новый элемент по индексу в указанной версии коллекции
     * @param o новый элемент
     * @param index индекс элемента
     * @param version версия коллекции
     * @return true, если элемент был успешно добавлен
     */
    boolean add(Object o, int index, int version);

    /**
     * Удаляет элемент из текущей версии коллекции
     * @param o удаляемый элемент
     * @return true, если элемент был успешно удалён
     */
    boolean remove(Object o);

    /**
     * Удаляет элемент из указанной версии коллекции
     * @param o удаляемый элемент
     * @param version версия коллекции
     * @return true, если элемент был успешно удалён
     */
    boolean remove(Object o, int version);

    /**
     * Удаляет элемент по индексу из указанной версии коллекции
     * @param o удаляемый элемент
     * @param index индекс элемента
     * @param version версия коллекции
     * @return true, если элемент был успешно удалён
     */
    boolean remove(Object o, int index, int version);

    /**
     * Возвращает элемент по индексу из текущей версии коллекции
     * @param index индекс элемента
     * @return элемент по индексу из текущей версии коллекции
     */
    Object get(int index);

    /**
     * Возвращает элемент по индексу из указанной версии коллекции
     * @param index индекс элемента
     * @param version версия коллекции
     * @return элемент по индексу из указанной версии коллекции
     */
    Object get(int index, int version);


    /**
     * Проверяет, что текущая версия коллекции пуста
     * @return true, если текущая версия коллекции пуста
     */
    boolean isEmpty();

    /**
     * Проверяет, что указанная версия коллекции пуста
     * @param version версия коллекции
     * @return true, если указанная версия коллекции пуста
     */
    boolean isEmpty(int version);

    /**
     * Проверяет, что если текущая версия коллекции содержит указанный элемент
     * @param o элемент
     * @return true, если текущая версия коллекции содержит указанный элемент
     */
    boolean contains(Object o);

    /**
     * Проверяет, что указанная версия коллекции содержит указанный элемент
     * @param o элемент
     * @param version версия коллекции
     * @return true, если указанная версия коллекции содержит указанный элемент
     */
    boolean contains(Object o, int version);

    /**
     * Проверяет, что указанный элемент равен текущему
     * @param o элемент
     * @return true, если указанный элемент равен текущему
     */
    boolean equals(Object o);

    /**
     * "Зануляет" текущую версию коллекции
     * @return true, если текущая версия коллекции была успешно "занулена"
     */
    boolean clear();

    /**
     * "Зануляет" указанную версию коллекции
     * @param version версия коллекции
     * @return true, если указанная версия коллекции была успешно "занулена"
     */
    boolean clear(int version);

    /**
     * Возвращает строковое представление текущей версии коллекции
     * @return строковое представление текущей версии коллекции
     */
    String toString();

    /**
     * Возвращает строковое представление указанной версии коллекции
     * @param version версия коллекции
     * @return строковое представление указанной версии коллекции
     */
    String toString(int version);

}