package utilities;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/** Обёртка над файловым путём.
 *
 */
public class FilePath implements Serializable {
    static final long serialVersionUID = 99L;
    ArrayList<String> parts  = new ArrayList<String>();; // Составляющие пути.

    /** Создаёт новый путь к файлу.
     *
     * @param parts Составляющие пути.
     */
    public FilePath(String... parts) {
        for (String eachPart: parts)
            if (eachPart.length() != 0)
            this.parts.add(eachPart);
    }

    public FilePath(FilePath other) {
        for (String eachPart: other.parts)
            parts.add(eachPart);
    }

    /** Возвращает элемент по индексу.
     *
     * @param index Индекс элемента.
     * @return Элемент.
     */
    public String get(int index) {
        return parts.get(index);
    }

    public int size() {
        return parts.size();
    }

    /** Проверяет наличие указанной части в пути.
     *
     * @param part Часть пути.
     * @return наличие указанной части в пути.
     */
    public boolean contains(String part) {
        for (String eachPart: parts)
            if (eachPart.equals(part))
                return true;

        return false;
    }

    public void add(String part) {
        parts.add(part);
    }

    @Override
    public String toString() {
        String result = "";
        for(String eachPart: parts) {
            if (result.length() != 0)
                result += File.separator;
            result += eachPart;
        }
        return result;
    }

    /** Восстанавливает путь к файлу из строки.
     *
     * @param line Строка.
     * @return Путь к файлу.
     */
    public static FilePath parseString(String line) {
        String[] parts = line.split("[\\/:]");
        return new FilePath(parts);
    }

    /** Восстанавливает путь к файлу из URL.
     *
     * @param url URL.
     * @return Путь к файлу.
     */
    public static FilePath parseURL(String url) {
        String[] parts = url.split("/");
        return new FilePath(parts);
    }
}
