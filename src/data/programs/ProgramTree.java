package data.programs;

import com.google.gson.Gson;
import data.tree.DataTree;
import data.tree.DataTreeLeaf;
import data.tree.DataTreeNode;
import debug.Console;
import exceptions.DuplicateElemException;
import exceptions.NoSuchElemException;
import utilities.FilePath;

import java.io.*;
import java.util.LinkedHashSet;

/** Представляет дерево образовательных программ.
 *
 */
public class ProgramTree extends DataTree implements Serializable {
    static final long serialVersionUID = 32L;

    public static final String rootName = "Образовательные программы";
    public static final String newUsersName = "Новые пользователи";
    public static final String adminsName = "Администраторы";

    private DataTreeNode newUsers;
    private DataTreeNode admins;
    private FilePath path; // Путь к файлу.

    // --- Работа с файлами ---

    private ProgramTree(FilePath path) {
        super(rootName);
        try {
            newUsers = addNewNode(newUsersName, root);
            admins = addNewNode(adminsName, root);
        } catch (DuplicateElemException e) {
            e.printStackTrace();
        }
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

    public static ProgramTree readFromFile(FilePath path) {
        File file = new File(path.toString());

        try {
            if (!file.exists()) {
                file.createNewFile();
                ProgramTree result = new ProgramTree(path);
                result.saveToFile();
                return result;
            }

            ObjectInputStream inputStream = new ObjectInputStream(
                    new FileInputStream(path.toString())
            );
            return (ProgramTree) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Console.println("Ошибка при чтении файла!");
            e.printStackTrace();
            return new ProgramTree(path);
        }
    }


    // --- Образовательные программы ---

    /** Получает копию программы по её названию.
     *
     * @param name Название программы.
     * @return копию программы по её названию.
     */
    synchronized public DataTreeNode getProgram(String name) throws NoSuchElemException {
        DataTreeNode program = getNode(name).getClone(NonExistingClass.class);
        return program;
    }

    /** Добавляет новую образовательную программу.
     *
     * @param name Название образовательной программы.
     */
    synchronized public void addNewProgram(String name) throws DuplicateElemException {
        addNewNode(name, root);
        saveToFile();
    }

    /** Добавляет новую образовательную программу в качестве дочерней.
     *
     * @param name Имя программы.
     * @param parentName Имя родительской программы.
     */
    synchronized public void addNewProgram(String name, String parentName) throws NoSuchElemException, DuplicateElemException {
        DataTreeNode parent = getNode(parentName);
        addNewNode(name, parent);
        saveToFile();
    }

    /** Меняет родительскую программу.
     *
     * @param name Программа.
     * @param newParentName Новая родительская программа.
     */
    synchronized public void rebaseProgram(String name, String newParentName) throws NoSuchElemException, DuplicateElemException {
        DataTreeNode program = getNode(name);
        DataTreeNode parent = getNode(newParentName);
        setParent(program, parent);
        saveToFile();
    }

    /** Удаляет образовательную программу.
     *
     * @param name Программа.
     */
    synchronized public void removeProgram(String name) throws NoSuchElemException, DuplicateElemException {
        if (name.equals(rootName) || name.equals(newUsersName) || name.equals(adminsName))
            throw new NoSuchElemException("У вас недостаточно прав для удаления данных программ!");
        DataTreeNode program = getNode(name);
        removeNode(program);
        saveToFile();
    }

    synchronized public void changeProgram(String name, String newValue) throws NoSuchElemException, DuplicateElemException {
        DataTreeNode program = getNode(name);
        changeNode(program, newValue);
    }


    // --- Пользователи ---

    /** Возвращает пользователя по его логину.
     *
     * @param login Логин пользователя.
     * @return пользователя по его логину.
     */
    synchronized public User getUser(String login) throws NoSuchElemException {
        DataTreeLeaf user = getLeaf(login, User.class);
        return (User) user.getValue();
    }

    /** Добавляет нового пользователя.
     *
     * @param login логин
     * @param name ФИО.
     */
    synchronized public void addNewUser(String login, String name) throws DuplicateElemException {
        addNewLeaf(login, new User(login, name), newUsers);
        saveToFile();
    }

    /** Добавляет пользователя в новую образовательную программу.
     *
     * @param login Логин.
     * @param programName Образовательная программа.
     */
    synchronized public void addUserToProgram(String login, String programName) throws NoSuchElemException, DuplicateElemException {
        DataTreeLeaf user = getLeaf(login, User.class);
        DataTreeNode program = getNode(programName);
        addParent(user, program);
        saveToFile();
    }

    /** Убирает пользователя из образовательной программы.
     *
     * @param login Логин пользователя.
     * @param programName Название образовательной программы.
     */
    synchronized public void removeUserFromProgram(String login, String programName) throws NoSuchElemException, DuplicateElemException {
        DataTreeLeaf user = getLeaf(login, User.class);
        DataTreeNode program = getNode(programName);
        removeParent(user, program);

        if (!containsLeaf(user))
            addParent(user, newUsers);
        saveToFile();
    }

    /** Переносит пользователя в другую образовательную программу.
     *
     * @param login Логин.
     * @param programName Название программы.
     */
    synchronized public void rebaseUser(String login, String programName) throws NoSuchElemException, DuplicateElemException {
        DataTreeLeaf user = getLeaf(login, User.class);
        DataTreeNode program = getNode(programName);
        setParent(user, program);
        saveToFile();
    }

    /** Удаляет пользователя из системы.
     *
     * @param login Логин.
     */
    synchronized public void removeUser(String login) throws NoSuchElemException {
        DataTreeLeaf user = getLeaf(login, User.class);
        removeLeaf(user);
        saveToFile();
    }

    /** Изменяет данные о пользователе.
     *
     * @param login Логин.
     * @param newLogin Новый логин.
     * @param newName Новые ФИО.
     */
    synchronized public void changeUser(String login, String newLogin, String newName) throws NoSuchElemException, DuplicateElemException {
        User newValue = new User(newLogin, newName);
        changeLeaf(getLeaf(login, User.class), newValue.getLogin(), newValue);
        saveToFile();
    }

    /** Даёт пользователю права администратора.
     *
     * @param login Логин пользователя.
     */
    synchronized public void addAdmin(String login) throws NoSuchElemException, DuplicateElemException {
        DataTreeLeaf leaf = getLeaf(login, User.class);
        if (hasParent(leaf, newUsers))
            removeParent(leaf, newUsers);

        addParent(leaf, admins);
        saveToFile();
    }

    /** Убирает у пользователя права администратора.
     *
     * @param login Логин пользователя.
     */
    synchronized public void removeAdmin(String login) throws NoSuchElemException, DuplicateElemException {
        DataTreeLeaf user = getLeaf(login, User.class);
        removeParent(user, admins);
        Console.println(this);
        if (getAllParents(user).size() == 0)
            addParent(user, newUsers);
        saveToFile();
    }


    // --- Преподаватели ---

    /** Возвращает преподавателя по ФИО.
     *
     * @param name ФИО.
     * @return преподавателя по ФИО.
     */
    synchronized public Tutor getTutor(String name) throws NoSuchElemException {
        DataTreeLeaf tutor = getLeaf(name, Tutor.class);
        return (Tutor) tutor.getValue();
    }

    /** Добавляет нового преподавателя в систему.
     *
     * @param name ФИО.
     * @param value Преподаватель.
     * @param programName Образовательная программа.
     */
    synchronized public void addNewTutor(String name, Tutor value, String programName) throws NoSuchElemException, DuplicateElemException {
        addNewLeaf(name, value, getNode(programName));
        saveToFile();
    }

    /** Добавляет преподавателя в образовательную программу.
     *
     * @param name ФИО.
     * @param programName Преподаватель.
     */
    synchronized public void addTutorToProgram(String name, String programName) throws NoSuchElemException, DuplicateElemException {
        DataTreeLeaf tutor = getLeaf(name, Tutor.class);
        DataTreeNode program = getNode(programName);
        addParent(tutor, program);
        saveToFile();
    }

    /** Удаляет преподавателя из образовательной программы.
     *
     * @param name ФИО.
     * @param programName Образовательная программа.
     */
    synchronized public void removeTutorFromProgram(String name, String programName) throws NoSuchElemException, DuplicateElemException {
        DataTreeLeaf user = getLeaf(name, Tutor.class);
        DataTreeNode program = getNode(programName);
        removeParent(user, program);

        if (!containsLeaf(user))
            addParent(user, newUsers);
        saveToFile();
    }

    /** Перемещает преподавателя в другую образовательную программу.
     *
     * @param name ФИО.
     * @param programName Образовательная программа.
     */
    synchronized public void rebaseTutor(String name, String programName) throws NoSuchElemException, DuplicateElemException {
        DataTreeLeaf user = getLeaf(name, Tutor.class);
        DataTreeNode program = getNode(programName);
        setParent(user, program);
        saveToFile();
    }

    /** Удаляет преподавателя.
     *
     * @param name ФИО.
     */
    synchronized public void removeTutor(String name) throws NoSuchElemException {
        DataTreeLeaf tutor = getLeaf(name, Tutor.class);
        removeLeaf(tutor);
        saveToFile();
    }

    /** Меняет преподавателя.
     *
     * @param name ФИО.
     * @param newValue Новые данные.
     */
    synchronized public void changeTutor(String name, Tutor newValue) throws NoSuchElemException, DuplicateElemException { ;
        changeLeaf(getLeaf(name, Tutor.class), newValue.getName(), newValue);
        saveToFile();
    }


    // --- Дисциплины ---

    /** Возвращает преподавателя по ФИО.
     *
     * @param name ФИО.
     * @return преподавателя по ФИО.
     */
    synchronized public Discipline getDiscipline(String name) throws NoSuchElemException {
        DataTreeLeaf discipline = getLeaf(name, Discipline.class);
        return (Discipline) discipline.getValue();
    }

    /** Добавляет нового преподавателя в систему.
     *
     * @param name ФИО.
     * @param value Преподаватель.
     * @param programName Образовательная программа.
     */
    synchronized public void addNewDiscipline(String name, Discipline value, String programName) throws NoSuchElemException, DuplicateElemException {
        addNewLeaf(name, value, getNode(programName));
        saveToFile();
    }

    /** Добавляет преподавателя в образовательную программу.
     *
     * @param name ФИО.
     * @param programName Преподаватель.
     */
    synchronized public void addDisciplineToProgram(String name, String programName) throws NoSuchElemException, DuplicateElemException {
        DataTreeLeaf discipline = getLeaf(name, Discipline.class);
        DataTreeNode program = getNode(programName);
        addParent(discipline, program);
        saveToFile();
    }

    /** Удаляет преподавателя из образовательной программы.
     *
     * @param name ФИО.
     * @param programName Образовательная программа.
     */
    synchronized public void removeDisciplineFromProgram(String name, String programName) throws NoSuchElemException, DuplicateElemException {
        DataTreeLeaf discipline = getLeaf(name, Discipline.class);
        DataTreeNode program = getNode(programName);
        removeParent(discipline, program);

        if (!containsLeaf(discipline))
            addParent(discipline, newUsers);
        saveToFile();
    }

    /** Перемещает преподавателя в другую образовательную программу.
     *
     * @param name ФИО.
     * @param programName Образовательная программа.
     */
    synchronized public void rebaseDiscipline(String name, String programName) throws NoSuchElemException, DuplicateElemException {
        DataTreeLeaf discipline = getLeaf(name, Discipline.class);
        DataTreeNode program = getNode(programName);
        setParent(discipline, program);
        saveToFile();
    }

    /** Удаляет преподавателя.
     *
     * @param name ФИО.
     */
    synchronized public void removeDiscipline(String name) throws NoSuchElemException {
        DataTreeLeaf discipline = getLeaf(name, Discipline.class);
        removeLeaf(discipline);
        saveToFile();
    }

    /** Меняет преподавателя.
     *
     * @param name ФИО.
     * @param newValue Новые данные.
     */
    synchronized public void changeDiscipline(String name, Discipline newValue) throws NoSuchElemException, DuplicateElemException { ;
        changeLeaf(getLeaf(name, Discipline.class), newValue.getName(), newValue);
        saveToFile();
    }


    // --- Форматирование ---

    /** Возвращает JSON-сериализованное дерево пользователей.
     *
     * @return JSON-сериализованное дерево пользователей.
     */
    synchronized public String getUsersJson() {
        Gson gson = new Gson();
        return gson.toJson(getClone(User.class));
    }

    /** Возвращает JSON-сериализованное дерево преподавателей.
     *
     * @return JSON-сериализованное дерево преподавателей.
     */
    synchronized public String getTutorsJson() {
        Gson gson = new Gson();
        return gson.toJson(getClone(Tutor.class));
    }

    /** Возвращает JSON-представление дерева образовательных программ.
     *
     * @return JSON-представление дерева образовательных программ.
     */
    synchronized public String getProgramsJson() {
        Gson gson = new Gson();
        return gson.toJson(getClone(NonExistingClass.class));
    }

    /** Возвращает JSON-представление дерева преподавателей.
     *
     * @return JSON-представление дерева преподавателей.
     */
    synchronized public String getDisciplinesJson() {
        Gson gson = new Gson();
        return gson.toJson(getClone(Discipline.class));
    }


    /** Возвращает список названий всех образовательных программ, в которых есть данный студент.
     *
     * @param login Логин пользователя.
     * @return список названий всех образовательных программ, в которых есть данный студент.
     */
    synchronized public String getUsersProgramsJSON(String login) throws NoSuchElemException {
        DataTreeLeaf user = getLeaf(login, User.class);
        LinkedHashSet<DataTreeNode> programs = getAllParents(user);

        LinkedHashSet<String> result = new LinkedHashSet<>();
        for (DataTreeNode eachProgram: programs)
            result.add(eachProgram.getId());
        return new Gson().toJson(result);
    }

    /** Возвращает JSON-представление дерева преподавателей, относящихся к пользователю.
     *
     * @param login Логин пользователя.
     * @return JSON-представление дерева преподавателей, относящихся к пользователю.
     */
    synchronized public String getUsersTutorsJSON(String login) throws NoSuchElemException, DuplicateElemException {
        DataTreeLeaf user = getLeaf(login, User.class);
        LinkedHashSet<DataTreeNode> programs = getAllParents(user);

        DataTree result = new DataTree("Ваши преподаватели");
        for (DataTreeNode eachProgram: programs)
            result.getRoot().addNode(eachProgram.getShallowClone(Tutor.class));
        return new Gson().toJson(result);
    }

    /** Возвращает JSON-представление дерева дисциплин, относящихся к пользователю.
     *
     * @param login Логин пользователя.
     * @return JSON-представление дерева дисциплин, относящихся к пользователю.
     */
    synchronized public String getUsersDisciplinesJSON(String login) throws NoSuchElemException, DuplicateElemException {
        DataTreeLeaf user = getLeaf(login, User.class);
        LinkedHashSet<DataTreeNode> programs = getAllParents(user);

        DataTree result = new DataTree("Ваши дисциплины");
        for (DataTreeNode eachProgram: programs)
            result.getRoot().addNode(eachProgram.getShallowClone(Discipline.class));
        return new Gson().toJson(result);
    }


    private class NonExistingClass {

    }
}
