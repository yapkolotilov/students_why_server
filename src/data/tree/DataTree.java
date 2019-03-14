package data.tree;

import exceptions.DuplicateElemException;
import exceptions.NoSuchElemException;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/** Представляет собой дерево данных.
 *
 */
public class DataTree implements Serializable {
    protected final DataTreeNode root; // Корень дерева.
    static final long serialVersionUID = 41L;

    /** Создаёт новое дерево.
     *
     * @param rootName Имя корня.
     */
    public DataTree(String rootName) {
        root = new DataTreeNode(rootName);
    }

    /** Создаёт новое дерево с корнем в указанном узле.
     *
     * @param root Корень.
     */
    DataTree(DataTreeNode root) {
        this.root = root;
    }

    public DataTreeNode getRoot() {
        return root;
    }

    // --- Узлы ---

    /** Проверяет, содержит ли дерево узел с указанным id.
     *
     * @param id id узла.
     * @return содержит ли дерево узел с указанным id.
     */
    protected boolean containsNode(String id) {
        LinkedList<DataTreeNode> nodes = new LinkedList<>();
        nodes.add(root);

        while (nodes.size() != 0) {
            DataTreeNode node = nodes.pop();
            if (node.matches(id))
                return true;
            for (DataTreeNode eachNode: node.getNodes())
                if (!nodes.contains(eachNode))
                    nodes.add(eachNode);
        }
        return false;
    }

    /** Проверяет, содержит ли дерево указанный узел.
     *
     * @param node Узел.
     * @return содержит ли дерево указанный узел.
     */
    protected boolean containsNode(DataTreeNode node) {
        return containsNode(node.getId());
    }

    /** Получает узел по его id.
     *
     * @param id id искомого узла.
     * @return узел по его id.
     */
    protected DataTreeNode getNode(String id) throws NoSuchElemException {
        LinkedList<DataTreeNode> nodes = new LinkedList<>();
        nodes.add(root);

        while (nodes.size() != 0) {
            DataTreeNode node = nodes.pop();
            if (node.matches(id))
                return node;
            for (DataTreeNode eachNode: node.getNodes())
                if (!nodes.contains(eachNode))
                    nodes.add(eachNode);
        }
        throw new NoSuchElemException("Узла с таким именем в дереве нет!");
    }

    /** Находит родителя узла.
     *
     * @param node Узел.
     * @return Родитель узла.
     */
    protected DataTreeNode getParent(DataTreeNode node) throws NoSuchElemException {
        LinkedList<DataTreeNode> nodes = new LinkedList<>();
        nodes.add(root);

        while (nodes.size() != 0) {
            DataTreeNode parentNode = nodes.pop();
            for (DataTreeNode eachNode: parentNode.getNodes()) {
                if (eachNode == node)
                    return parentNode;
                if (!nodes.contains(eachNode))
                    nodes.add(eachNode);
            }
        }
        throw new NoSuchElemException("Такого узла в дереве нет!");
    }

    /** Добавляет новый узел в дерево.
     *
     * @param id id узла.
     * @param parent Родительский узел.
     */
    protected DataTreeNode addNewNode(String id, DataTreeNode parent) throws DuplicateElemException {
        if (containsNode(id))
            throw new DuplicateElemException("Данный узел уже содержится в дереве!");
        DataTreeNode result = parent.addNewNode(id);

        return result;
    }

    /** Устанавливает другого родителя для узла.
     *
     * @param node Узел.
     * @param parent Новый родитель.
     */
    protected void setParent(DataTreeNode node, DataTreeNode parent) throws NoSuchElemException, DuplicateElemException {
        DataTreeNode oldParent = getParent(node);
        oldParent.removeNode(node);

        parent.addNode(node);
    }

    /** Удаляет узел из дерева.
     *
     * @param node Узел.
     */
    protected void removeNode(DataTreeNode node) throws NoSuchElemException, DuplicateElemException {
        if (node == root)
            throw new NoSuchElemException("Нельзя удалить корень!");
        DataTreeNode parentNode = getParent(node);
        parentNode.removeNode(node);

        for (DataTreeNode eachNode: node.getNodes())
            parentNode.addNode(eachNode);
        for (DataTreeLeaf eachLeaf: node.getLeaves())
            parentNode.addLeaf(eachLeaf);
    }

    protected void changeNode(DataTreeNode node, String newId) throws DuplicateElemException {
        if (!node.getId().equals(newId) && containsNode(newId))
            throw new DuplicateElemException("Уже есть такой узел!");

        node.setId(newId);
    }

    // --- Листья ---

    /** Проверяет, содержит ли дерево лист с указанным id.
     *
     * @param id id листа.
     * @return содержит ли дерево лист с указанным id.
     */
    protected boolean containsLeaf(String id, Class valueClass) {
        LinkedList<DataTreeNode> nodes = new LinkedList<>();
        nodes.push(root);

        while (nodes.size() != 0) {
            DataTreeNode node = nodes.pop();
            for (DataTreeLeaf eachLeaf: node.getLeaves())
                if (eachLeaf.matches(id, valueClass))
                    return true;
            for (DataTreeNode eachNode: node.getNodes())
                if (!nodes.contains(eachNode))
                    nodes.push(eachNode);
        }
        return false;
    }

    /** Проверяет лист на наличие в дереве.
     *
     * @param leaf Лист.
     * @return Есть ли он в дереве.
     */
    protected boolean containsLeaf(DataTreeLeaf leaf) {
        return containsLeaf(leaf.getID(), leaf.getValue().getClass());
    }

    /** Возвращает лист по его id.
     *
     * @param id id листа.
     * @return лист по его id.
     */
    protected DataTreeLeaf getLeaf(String id, Class valueClass) throws NoSuchElemException {
        LinkedList<DataTreeNode> nodes = new LinkedList<>();
        nodes.push(root);

        while (nodes.size() != 0) {
            DataTreeNode node = nodes.pop();
            for (DataTreeLeaf eachLeaf: node.getLeaves())
                if (eachLeaf.matches(id, valueClass))
                    return eachLeaf;
            for (DataTreeNode eachNode: node.getNodes())
                if (!nodes.contains(eachNode))
                    nodes.push(eachNode);
        }
        throw new NoSuchElemException("В дереве нет листа с таким id!");
    }

    /** Находит всех родителей указанного листа.
     *
     * @param leaf Лист.
     * @return все родители указанного листа.
     */
    protected LinkedHashSet<DataTreeNode> getAllParents(DataTreeLeaf leaf) throws NoSuchElemException {
        LinkedHashSet<DataTreeNode> result = new LinkedHashSet<DataTreeNode>();
        LinkedList<DataTreeNode> nodes = new LinkedList<>();
        nodes.add(root);

        while (nodes.size() != 0) {
            DataTreeNode parentNode = nodes.pop();
            for (DataTreeLeaf eachLeaf: parentNode.getLeaves())
                if (eachLeaf == leaf) {
                    result.add(parentNode);
                    break;
                }
            for (DataTreeNode eachNode: parentNode.getNodes())
                if (!nodes.contains(eachNode))
                    nodes.add(eachNode);
        }
        return result;
    }

    /** Добавляет новый лист в дерево.
     *
     * @param id id листа.
     * @param value Значение листа.
     * @param parent Родитель.
     */
    protected DataTreeLeaf addNewLeaf(String id, Object value, DataTreeNode parent) throws DuplicateElemException {
        if (containsLeaf(id, value.getClass()))
            throw new DuplicateElemException("Лист с таким id уже есть в дереве!");
        DataTreeLeaf leaf = parent.addNewLeaf(id, value);

        return leaf;
    }

    /** Проверяет, имеет ли лист указанного родителя.
     *
     * @param leaf Лист.
     * @param parent Родитель.
     * @return имеет ли лист указанного родителя.
     */
    protected boolean hasParent(DataTreeLeaf leaf, DataTreeNode parent) {
        return parent.containsLeaf(leaf);
    }

    /** Убирает старых родителей у листа и устанавливает нового.
     *
     * @param leaf Лист.
     * @param parent Родитель.
     */
    protected void setParent(DataTreeLeaf leaf, DataTreeNode parent) throws NoSuchElemException, DuplicateElemException {
        LinkedHashSet<DataTreeNode> parents = getAllParents(leaf);
        for (DataTreeNode eachParent: parents)
            eachParent.removeLeaf(leaf);

        parent.addLeaf(leaf);
    }

    /** Добавляет нового родителя к листу.
     *
     * @param leaf Лист.
     * @param parent Родитель.
     */
    protected void addParent(DataTreeLeaf leaf, DataTreeNode parent) throws DuplicateElemException {
        parent.addLeaf(leaf);
    }

    /** Убирает одного из родителей у листа.
     *
     * @param leaf Лист.
     * @param parent Родитель.
     */
    protected void removeParent(DataTreeLeaf leaf, DataTreeNode parent) throws NoSuchElemException {
        parent.removeLeaf(leaf);
    }

    /** Удаляет лист из дерева.
     *
     * @param leaf Лист.
     */
    protected void removeLeaf(DataTreeLeaf leaf) throws NoSuchElemException {
        LinkedHashSet<DataTreeNode> parents = getAllParents(leaf);
        for (DataTreeNode eachParent: parents)
            eachParent.removeLeaf(leaf);
    }

    protected void changeLeaf(DataTreeLeaf leaf, String newID, Object newValue) throws DuplicateElemException {
        debug.Console.println(leaf.getID());
        debug.Console.println((newID));
        debug.Console.println(leaf.getID().equals(newID));
        if (!leaf.getID().equals(newID) && containsLeaf(newID, leaf.getValue().getClass()))
            throw new DuplicateElemException("Лист с таким id уже существует!");
        leaf.setID(newID);
        leaf.setValue(newValue);
    }

    // --- Форматирование ---

    /** Возвращает клон дерева, содержащий только листья со значениями указанного типа.
     *
     * @param valueClass класс листов.
     * @return клон дерева, содержащий только листья со значениями указанного типа.
     */
    protected DataTree getClone(Class valueClass) {
        return new DataTree(root.getClone(valueClass));
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
