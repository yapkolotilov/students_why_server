package data.tree;

import java.io.Serializable;

/** Представляет лист дерева данных.
 *
 */
public class DataTreeLeaf<TValue> implements Serializable, Comparable<DataTreeLeaf> {
    static final long serialVersionUID = 42L;


    private TValue value; // Значение.
    private String id; // Уникальный идентификатор для листа.


    /** Создаёт новый лист с указанными значениями.
     *
     * @param value Значение.
     * @param id Имя.
     */
    DataTreeLeaf(String id, TValue value) {
        this.value = value;
        this.id = id;
    }


    // --- Геттеры и сеттеры ---

    /** Устанавливает новый идентификатор для листа.
     *
     * @param id новый идентификатор для листа.
     */
    void setID(String id) {
        this.id = id;
    }

    /** Устанавливает новое значение для листа.
     *
     * @param value новое значение для листа.
     */
    void setValue(TValue value) {
        this.value = value;
    }

    public TValue getValue() {
        return value;
    }

    /** Возвращает id листа.
     *
     * @return id листа.
     */
    String getID() {
        return id;
    }

    // --- Поиск по дереву ---

    /** Проверяет, соответствует ли лист указанным имени и типу хранимого значения.
     *
     * @param id Имя.
     * @param valueClass Тип значения.
     * @return соответствует ли лист указанным имени и типу хранимого значения.
     */
    boolean matches(String id, Class valueClass) {
        return this.id.equals(id) && value.getClass() == valueClass;
    }

    /** Проверяет хранимое значение на соответствие типу.
     *
     * @param valueClass Тип хранимого значения.
     * @return Соответствие типу.
     */
    boolean matches(Class valueClass) {
        return value.getClass() == valueClass;
    }

    @Override
    public int compareTo(DataTreeLeaf o) {
        return id.compareTo(o.id);
    }

    /** Возвращает клон вершины.
     *
     * @return Клон вершины.
     */
    DataTreeLeaf getClone() {
        return new DataTreeLeaf<>(id, value);
    }

    // --- Форматирование ---

    public String toString() {
        return toStringFormat("");
    }

    String toStringFormat(String indent) {
        return String.format("\"%s\":\n%s  %s", id, indent, value);
    }
}
