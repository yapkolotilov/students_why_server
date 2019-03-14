package debug;

/** Утилитный класс для вывода в консоль.
 * Экономит пару строчек кода.
 */
public class Console {
    /** Печатает объект в консоль.
     *
     * @param object Объект.
     */
    public static void print(Object object) {
        System.out.print(object);
    }

    /** Выводит в консоль форматную строку.
     *
     * @param line Строка.
     * @param vars Параметры строки.
     */
    public static void print(String line, Object... vars) {
        System.out.print(String.format(line, vars));
    }

    public static void println() {
        System.out.println();
    }

    /** Печатает объект в консоль.
     *
     * @param object Объект.
     */
    public static void println(Object object) {
        System.out.println(object);
    }

    /** Выводит в консоль форматную строку.
     *
     * @param line Строка.
     * @param vars Параметры строки.
     */
    public static void println(String line, Object... vars) {
        System.out.println(String.format(line, vars));
    }


    public static void printlnln() {
        System.out.println("\n");
    }

    /** Печатает объект в консоль.
     *
     * @param object Объект.
     */
    public static void printlnln(Object object) {
        System.out.println(object + "\n");
    }

    /** Выводит в консоль форматную строку.
     *
     * @param line Строка.
     * @param vars Параметры строки.
     */
    public static void printlnln(String line, Object... vars) {
        System.out.println(String.format(line, vars) + "\n");
    }

    public static void printArray(Object[] array) {
        for (Object object: array)
            Console.print(object + ", ");
    }

    public static void printlnArray(Object[] array) {
        for (Object object: array)
            Console.print(object + ", ");
        Console.println("");
    }
}
