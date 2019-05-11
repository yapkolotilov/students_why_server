package studentswhyserver.data.items;

import com.google.gson.Gson;
import studentswhyserver.debug.Console;
import studentswhyserver.exceptions.DuplicateElemException;
import studentswhyserver.exceptions.NoSuchElemException;
import studentswhyserver.utilities.FilePath;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/** Представляет ленту новостей.
 *
 */
public class ItemList implements Serializable {
    static final long serialVersionUID = 13L;

    private List<Item> items = new LinkedList<>(); // Новости.
    private FilePath path; // Адрес хранения файла.


    private ItemList(FilePath path) {
        this.path = path;
    }

    private ItemList(List<Item> items) {
        this.items = items;
    }


    // --- Работа с файлами ---

    /** Сохраняет состояние в файл.
     *
     */
    private void saveToFile() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path.toString()));
            outputStream.writeObject(this);
            outputStream.close();
        } catch (IOException e ) {
            studentswhyserver.debug.Console.println("ОШИБКА ПРИ СОХРАНЕНИИ ПАРОЛЕЙ");
            e.printStackTrace();
        }
    }

    /** Считывает список из файла.
     *
     * @param path Путь к файлу.
     * @return список из файла.
     */
    public static ItemList readFromFile(FilePath path) {
        File file = new File(path.toString());

        try {
            if (!file.exists()) {
                file.createNewFile();
                ItemList result = new ItemList(path);
                result.saveToFile();
                return result;
            }

            ObjectInputStream inputStream = new ObjectInputStream(
                    new FileInputStream(path.toString())
            );
            return (ItemList) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Console.println("Ошибка при чтении файла!");
            e.printStackTrace();
            return new ItemList(path);
        }
    }


    // --- Работа с новостями ---

    /** Получает новость по её заголовку.
     *
     * @param header Заголовок новости.
     * @return новость по её заголовку.
     */
    synchronized public Item getItem(String header) throws NoSuchElemException {
        for (Item eachItem: items)
            if (eachItem.matches(header))
                return eachItem;
        throw new NoSuchElementException("Нет новости с таким заголовком!");
    }

    /** Получает новость по её заголовку.
     *
     * @param header Заголовок новости.
     * @return новость по её заголовку.
     */
    synchronized public Event getEvent(String header) throws NoSuchElemException {
        for (Item eachItem: items)
            if (eachItem.matches(header, Event.class))
                return (Event) eachItem;
        throw new NoSuchElementException("Нет новости с таким заголовком!");
    }

    /** Проверяет, содержит ли список новость с указанным заголовком.
     *
     * @param header Заголовок.
     * @return содержит ли список новость с указанным заголовком.
     */
    synchronized public boolean contains(String header) {
        for (Item eachItem: items)
            if (eachItem.matches(header))
                return true;
        return false;
    }

    /** Добавляет новую новость в ленту.
     *
     * @param value Новость.
     */
    synchronized public void add(Item value) throws DuplicateElemException {
        if (contains(value.getHeader()))
            throw new DuplicateElemException("В списке новостей уже есть такая новость!");

        // Устанавливаем текущую дату:
        DateFormat dateFormat = new SimpleDateFormat("");
        Date curDate = new Date();
        value.setPublishDate(dateFormat.format(curDate));

        items.add(value);
        saveToFile();
    }

    /** Меняет новость по заголовку.
     *
     * @param header Заголовок новости.
     * @param newValue Новый контент и заголовок новости.
     */
    synchronized public void change(String header, Item newValue) throws NoSuchElemException, DuplicateElemException {
        Item item = getItem(header);
        items.remove(item);

        add(newValue);
        saveToFile();
    }

    /** Удаляет новость из новостной ленты.
     *
     * @param header Заголовок новости.
     */
    synchronized public void remove(String header) throws NoSuchElemException {
        Item item = getItem(header);
        items.remove(item);
        saveToFile();
    }


    // --- Форматирование ---

    /** Возвращает JSON-список новостей.
     *
     * @return JSON-список новостей.
     */
    synchronized public String getItemsJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /** Возвращает JSON-список последних n новостей.
     *
     * @param number Число новостей.
     * @return JSON-список последних n новостей.
     */
    synchronized public String getLastItemsJson(int number) {
        Gson gson = new Gson();

        List<Item> result = new LinkedList<>();
        for (int i = items.size() - 1; i >= 0; i--) {
            if (i == 0)
                break;
            result.add(this.items.get(i));
        }

        return gson.toJson(new ItemList(result));
    }

    /** Возвращает JSON-список последних n новостей.
     *
     * @return JSON-список последних n новостей.
     */
    synchronized public String getEventsJson() {
        Gson gson = new Gson();

        List<Item> result = new LinkedList<>();
        for (Item eachItem: items) {
            if (eachItem instanceof Event)
                result.add(eachItem);
        }

        return gson.toJson(new ItemList(result));
    }

    /** Возвращает JSON-список последних n событий.
     *
     * @param number Число событий.
     * @return JSON-список последних n событий.
     */
    synchronized public String getLastEventsJson(int number) {
        Gson gson = new Gson();

        List<Item> result = new LinkedList<>();
        for (int i = items.size() - 1; i >= 0; i--) {
            if (i == 0)
                break;
            Item eachItem = items.get(i);
            if (eachItem instanceof Event)
                result.add(eachItem);
        }

        return gson.toJson(new ItemList(result));
    }

    @Override
    public String toString() {
        String result = "";
        for (Item item: items) {
            result += item;
        }
        return result;
    }
}
