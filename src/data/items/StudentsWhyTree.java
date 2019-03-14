package data.items;

import com.google.gson.Gson;
import data.tree.DataTree;
import data.tree.DataTreeLeaf;
import data.tree.DataTreeNode;
import exceptions.DuplicateElemException;
import exceptions.NoSuchElemException;
import utilities.FilePath;

import java.io.*;
import java.util.LinkedHashSet;

public class StudentsWhyTree extends DataTree {
    static final long serialVersionUID = 14L;

    public static final String rootName = "Часто задаваемые вопросы";

    private FilePath path; // Путь к файлу.

    // --- Работа с файлами ---

    private StudentsWhyTree(FilePath path) {
        super(rootName);
        this.path = path;
    }

    /** Сохраняет состояние в файл.
     *
     */
    protected void saveToFile() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    new FileOutputStream(path.toString())
            );
            outputStream.writeObject(this);
        } catch (IOException e) {
            Console.println("Ошибка при записи в файл!");
            e.printStackTrace();
        }
    }

    public static StudentsWhyTree readFromFile(FilePath path) {
        File file = new File(path.toString());

        try {
            if (!file.exists()) {
                file.createNewFile();
                StudentsWhyTree result = new StudentsWhyTree(path);
                result.saveToFile();
                return result;
            }

            ObjectInputStream inputStream = new ObjectInputStream(
                    new FileInputStream(path.toString())
            );
            return (StudentsWhyTree) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Console.println("Ошибка при чтении файла!");
            e.printStackTrace();
            return new StudentsWhyTree(path);
        }
    }


    // --- Теги ---

    /** Получает копию тега по его названию.
     *
     * @param name Название тега.
     * @return копия тега по его названию.
     */
    synchronized public DataTreeNode getTag(String name) throws NoSuchElemException {
        DataTreeNode program = getNode(name).getClone(StudentsWhyTree.NonExistingClass.class);
        return program;
    }

    /** Добавляет новый тег.
     *
     * @param name Тег.
     */
    synchronized public void addNewTag(String name) throws DuplicateElemException {
        addNewNode(name, root);
        saveToFile();
    }

    /** Добавляет новый тег в качестве дочернего.
     *
     * @param name Тег.
     * @param parentName Родительский тег.
     */
    synchronized public void addNewTag(String name, String parentName) throws NoSuchElemException, DuplicateElemException {
        DataTreeNode parent = getNode(parentName);
        addNewNode(name, parent);
        saveToFile();
    }

    /** Меняет родительский тег.
     *
     * @param name Тег.
     * @param newParentName Новый родительский тег.
     */
    synchronized public void rebaseTag(String name, String newParentName) throws NoSuchElemException, DuplicateElemException {
        DataTreeNode tag = getNode(name);
        DataTreeNode parent = getNode(newParentName);
        setParent(tag, parent);
        saveToFile();
    }

    /** Удаляет тег.
     *
     * @param name Тег.
     */
    synchronized public void removeTag(String name) throws NoSuchElemException, DuplicateElemException {
        DataTreeNode tag = getNode(name);
        removeNode(tag);
        saveToFile();
    }

    synchronized public void changeTag(String name, String newValue) throws NoSuchElemException, DuplicateElemException {
        DataTreeNode tag = getTag(name);
        changeNode(tag, newValue);
    }


    // --- Преподаватели ---

    /** Возвращает преподавателя по ФИО.
     *
     * @param name ФИО.
     * @return преподавателя по ФИО.
     */
    synchronized public Item getQuestion(String name) throws NoSuchElemException {
        DataTreeLeaf question = getLeaf(name, Item.class);
        return (Item) question.getValue();
    }

    /** Добавляет нового преподавателя в систему.
     *
     * @param name ФИО.
     * @param value Преподаватель.
     * @param tag Образовательная программа.
     */
    synchronized public void addNewQuestion(String name, Item value, String tag) throws NoSuchElemException, DuplicateElemException {
        addNewLeaf(name, value, getNode(tag));
        saveToFile();
    }

    synchronized public void addNewQuestion(String name, Item value) throws DuplicateElemException {
        addNewLeaf(name, value, root);
        saveToFile();
    }

    /** Добавляет преподавателя в образовательную программу.
     *
     * @param name ФИО.
     * @param tag Преподаватель.
     */
    synchronized public void addQuestionToTag(String name, String tag) throws NoSuchElemException, DuplicateElemException {
        DataTreeLeaf question = getLeaf(name, Item.class);
        DataTreeNode tagNode = getNode(tag);
        addParent(question, tagNode);
        saveToFile();
    }

    /** Удаляет преподавателя из образовательной программы.
     *
     * @param name ФИО.
     * @param tag Образовательная программа.
     */
    synchronized public void removeQuestionFromTag(String name, String tag) throws NoSuchElemException, DuplicateElemException {
        DataTreeLeaf question = getLeaf(name, Item.class);
        DataTreeNode tagNode = getNode(tag);
        removeParent(question, tagNode);

        if (!containsLeaf(question))
            addParent(question, root);
        saveToFile();
    }

    /** Перемещает преподавателя в другую образовательную программу.
     *
     * @param name ФИО.
     * @param tag Образовательная программа.
     */
    synchronized public void rebaseQuestion(String name, String tag) throws NoSuchElemException, DuplicateElemException {
        DataTreeLeaf question = getLeaf(name, Item.class);
        DataTreeNode tagNode = getNode(tag);
        setParent(question, tagNode);
        saveToFile();
    }

    /** Удаляет преподавателя.
     *
     * @param name ФИО.
     */
    synchronized public void removeQuestion(String name) throws NoSuchElemException {
        DataTreeLeaf question = getLeaf(name, Item.class);
        removeLeaf(question);
        saveToFile();
    }

    /** Меняет преподавателя.
     *
     * @param name ФИО.
     * @param newValue Новые данные.
     */
    synchronized public void changeQuestion(String name, Item newValue) throws NoSuchElemException, DuplicateElemException { ;
        changeLeaf(getLeaf(name, Item.class), newValue.getHeader(), newValue);
        saveToFile();
    }


    synchronized public String getTagsJson() {
        Gson gson = new Gson();
        return gson.toJson(getClone(NonExistingClass.class));
    }

    synchronized public String getQuestionsJson() {
        Gson gson = new Gson();
        return gson.toJson(getClone(Item.class));
    }

    synchronized public String getQuestionsTagsJSON(String login) throws NoSuchElemException {
        DataTreeLeaf question = getLeaf(login, Item.class);
        LinkedHashSet<DataTreeNode> tags = getAllParents(question);

        LinkedHashSet<String> result = new LinkedHashSet<>();
        for (DataTreeNode eachTag: tags)
            result.add(eachTag.getId());
        return new Gson().toJson(result);
    }


    private class NonExistingClass {}
}
