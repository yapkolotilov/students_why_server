package data.tree;

import debug.Console;
import exceptions.DuplicateElemException;
import exceptions.NoSuchElemException;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

/** Представляет узел дерева данных.
 *
 */
public class DataTreeNode implements Serializable, Comparable<DataTreeNode> {
    static final long serialVersionUID = 43L;


    private String id; // Идентификатор узла.
    private Set<DataTreeNode> nodes = new TreeSet<>(); // Потомки.
    private Set<DataTreeLeaf> leaves = new TreeSet<>(); // Листья.

    /** Создаёт новый узел с указанными значениями.
     *
     * @param id Имя.
     */
    DataTreeNode(String id) {
        this.id = id;
    }


    // --- Геттеры и сеттеры ---

    /** Возвращает идентификатор.
     *
     * @return Идентификатор.
     */
    public String getId() {
        return id;
    }

    /** Устанавливает новый идентификатор.
     *
     * @param id новый идентификатор.
     */
    public void setId(String id) {
        this.id = id;
    }

    /** Проверяет узел на соответствие id.
     *
     * @param id id.
     * @return соответствие id.
     */
    boolean matches(String id) {
        return this.id.equals(id);
    }

    /** Возвращает список дочерних вершин.
     *
     * @return список дочерних вершин.
     */
    Set<DataTreeNode> getNodes() {
        return nodes;
    }

    /** Возвращает список листов.
     *
     * @return список листов.
     */
    Set<DataTreeLeaf> getLeaves() {
        return leaves;
    }

    // --- Потомки ---

    /** Проверяет, содержит ли узел лист с указанным id.
     *
     * @param id id лист.
     * @return содержит ли узел лист с указанным id.
     */
    boolean containsNode(String id) {
        for (DataTreeNode eachNode: nodes)
            if (eachNode.matches(id))
                return true;
        return false;
    }

    /** Проверяет, содержит ли узел указанного потомка.
     *
     * @param node потомок.
     * @return содержит ли узел указанного потомка.
     */
    boolean containsNode(DataTreeNode node) {
        return nodes.contains(node);
    }

    /** Возвращает дочерний узел по id.
     *
     * @param id id искомого узла.
     * @return дочерний узел по id.
     */
    DataTreeNode getNode(String id) throws NoSuchElemException {
        for (DataTreeNode eachNode: nodes)
            if (eachNode.matches(id))
                return eachNode;
        throw new NoSuchElemException("Искомого узла нет среди потомков!");
    }

    /** Добавляет новую вершину.
     *
     * @param id ID новой вершины.
     */
    DataTreeNode addNewNode(String id) throws DuplicateElemException {
        if (containsNode(id))
            throw new DuplicateElemException("Данный узел уже является потомком!");
        DataTreeNode result = new DataTreeNode(id);
        nodes.add(result);
        return result;
    }

    /** Добавляет вершину в список потомков.
     *
     * @param node Вершина.
     */
    public void addNode(DataTreeNode node) throws DuplicateElemException {
        if (containsNode(node))
            throw new DuplicateElemException("Данный узел уже содержится в потомках!");
        nodes.add(node);
    }

    /** Удаляет узел из потомков.
     *
     * @param node Удаляемый узел.
     */
    void removeNode(DataTreeNode node) throws NoSuchElemException {
        if (!containsNode(node))
            throw new NoSuchElemException("Данного узла нет в потомках!");
        nodes.remove(node);
    }


    // --- Листья ---

    /** Проверяет, содержит ли узел лист с указанным id.
     *
     * @param id id лист.
     * @return содержит ли узел лист с указанным id.
     */
    boolean containsLeaf(String id) {
        for (DataTreeLeaf eachLeaf: leaves)
            if (eachLeaf.getID().equals(id))
                return true;
        return false;
    }

    /** Проверяет, содержит ли узел лист.
     *
     * @param leaf лист.
     * @return содержит ли узел лист.
     */
    boolean containsLeaf(DataTreeLeaf leaf) {
        return leaves.contains(leaf);
    }

    /** Возвращает лист по id.
     *
     * @param id id искомого листа.
     * @return лист по id.
     */
    DataTreeLeaf getLeaf(String id) throws NoSuchElemException {
        for (DataTreeLeaf eachLeaf: leaves)
            if (eachLeaf.getID().equals(id))
                return eachLeaf;
        throw new NoSuchElemException("Нет такого листа среди потомков!");
    }

    /** Добавляет новый лист в узел.
     *
     * @param id Имя листа.
     * @param value Значение листа.
     */
    DataTreeLeaf addNewLeaf(String id, Object value) throws DuplicateElemException {
        if (containsLeaf(id))
            throw new DuplicateElemException("Данный лист уже содержится среди потомков!");
        DataTreeLeaf result = new DataTreeLeaf<>(id, value);
        leaves.add(result);
        return result;
    }

    /** Добавляет узел в список.
     *
     * @param leaf Узел.
     */
    void addLeaf(DataTreeLeaf leaf) throws DuplicateElemException {
        if (containsLeaf(leaf))
            throw new DuplicateElemException("Данный лист уже содержится!");
        leaves.add(leaf);
    }

    /** Удаляет указанный лист из списка.
     *
     * @param leaf Лист.
     */
    void removeLeaf(DataTreeLeaf leaf) throws NoSuchElemException {
        if (!leaves.contains(leaf))
            throw new NoSuchElemException("Данного листа уже нет среди потомков!");
        leaves.remove(leaf);
    }


    // --- Создание копий ---

    /** Возвращает клон вершины, ограниченный типом листьев.
     *
     * @param valueClass Тип листьев.
     * @return Клон вершины.
     */
    public DataTreeNode getClone(Class valueClass) {
        DataTreeNode result = new DataTreeNode(getId());
        for (DataTreeLeaf eachLeaf: leaves)
            if (eachLeaf.matches(valueClass))
                try {
                    result.addLeaf(eachLeaf.getClone());
                } catch (DuplicateElemException e) {
                    Console.println("ОШИБКА ПРИ ДОБАВЛЕНИИ ЛИСТА!");
                }

        for (DataTreeNode eachNode: nodes)
            try {
                result.addNode(eachNode.getClone(valueClass));
            } catch (DuplicateElemException e) {
                Console.println("ОШИБКА ПРИ ДОБАВЛЕНИИ УЗЛА!");
            }

        return result;
    }

    /** Возвращает поверхностную копию узла.
     *
     * @param valueClass Тип значения.
     * @return Поверхностная копия.
     */
    public DataTreeNode getShallowClone(Class valueClass) {
        DataTreeNode result = new DataTreeNode(getId());
        for (DataTreeLeaf eachLeaf: leaves)
            if (eachLeaf.matches(valueClass))
                try {
                    result.addLeaf(eachLeaf.getClone());
                } catch (DuplicateElemException e) {
                    Console.println("ОШИБКА ПРИ ДОБАВЛЕНИИ ЛИСТА!");
                }

        return result;
    }

    // --- Форматирование ---

    @Override
    public int compareTo(DataTreeNode o) {
        return this.id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return toStringFormat("");
    }

    private String toStringFormat(String indent) {
        String result = id + ":";
        indent += "\t";
        for (DataTreeLeaf eachLeaf: leaves)
            result += "\n" + indent + eachLeaf.toStringFormat(indent);
        for (DataTreeNode eachNode: nodes)
            result += "\n" + indent + eachNode.toStringFormat(indent);
        return result;
    }
}