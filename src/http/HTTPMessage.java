package http;

import exceptions.StringFormatException;

import java.util.LinkedHashMap;
import java.util.Map;

/** Представляет обобщённый класс HTTP-сообщения.
 * 
 */
public abstract class HTTPMessage {
    // --- Поля ---

    protected String version ="HTTP/1.1"; // Версия запроса. Лучше оставить.
    protected Headers headers = new Headers(); // Заголовки запроса.
    protected Params params = new Params(); // Параметры запроса.
    protected byte[] body = new byte[0]; // Тело запроса.

    // Заголовки:

    /** Возвращает значение заголовка по его ключу.
     * <p></p>
     * <i>Возвращает null, если такого заголовка нет.</i>
     *
     * @param name Имя заголовка.
     * @return значение заголовка по его ключу.
     */
    public String getHeader(String name) {
        return headers.get(name);
    }

    /** Устанавливает значение заголовка.
     *
     * @param name Имя заголовка.
     * @param value Значение заголовка.
     */
    public void setHeader(String name, Object value) {
        headers.put(name, value.toString());
    }

    /** Проверяет, содержит ли сообщение указанный заголовок.
     *
     * @param name Имя заголовка.
     * @return содержит ли сообщение указанный заголовок.
     */
    public boolean containsHeader(String name) {
        return headers.containsKey(name);
    }

    // Параметры:

    /** Возвращает значение параметра по его имени.
     *
     * @param name Имя параметра.
     * @return значение параметра по его имени.
     */
    public String getParam(String name) {
        return params.get(name);
    }

    public void setParam(String name, Object value) {
        body = null;
        headers.remove("Content-Length");
        params.put(name, value.toString());
    }

    /** Проверяет, содержит ли сообщение указанный параметр.
     *
     * @param name Имя параметра.
     * @return содержит ли сообщение указанный параметр.
     */
    public boolean containsParam(String name) {
        return params.containsKey(name);
    }

    // Тело:

    /** Возвращает тело сообщения.
     * <p></p>
     * <i>Возвращает null, если оно отсутствует.</i>
     *
     * @return тело сообщения.
     */
    public byte[] getBody() {
        return body;
    }

    /** Устанавливает тело сообщения.
     *
     * @param bytes Тело сообщения.
     */
    public void setBody(byte[] bytes) {
        params = new Params();
        body = bytes.clone();
        if (body.length != 0)
            setHeader("Content-Length", body.length);
    }

    /** Устанавливает тело сообщения.
     *
     * @param text Тело сообщения.
     */
    public void setBody(String text) {
        params = new Params();
        body = text.getBytes();
        if (body.length != 0)
            setHeader("Content-Length", body.length);
    }


    // --- Вложенные классы ---
    /** Представляет список заголовков и их значений.
     */
    protected class Headers extends LinkedHashMap<String, String> {
        /** Парсит строку с заголовками.
         *
         * @param line Строка с заголовками.
         */
        protected Headers(String line) throws StringFormatException {
            if (!line.matches("\n?(.*: .*)?(\n.*: .*)*"))
                throw new StringFormatException("Неправильный формат заголовков!");
            String[] lines = line.split("\n");
            for (int i = 0; i < lines.length; i++) {
                String[] values = lines[i].split(": ");
                put(values[0], values[1]);
            }
        }

        protected Headers() {}

        /** Возвращает строковое представление заголовков.
         *
         * @return Строковое представление заголовков.
         */
        public String toString() {
            String result = "";
            for (Map.Entry<String, String> eachEntry: entrySet()) {
                if (result.length() != 0)
                    result += "\n";
                result += String.format("%s: %s", eachEntry.getKey(), eachEntry.getValue());
            }
            return result;
        }
    }

    /** Представляет класс параметров.
     *
     */
    protected class Params extends LinkedHashMap<String, String> {
        /** Парсит строку с заголовками.
         *
         * @param line Строка с заголовками.
         */
        protected Params(String line) throws StringFormatException {
            if (!line.matches("(.*=.*)?(&.*=.*)*"))
                throw new StringFormatException("Неправильный формат заголовков!");
            String[] lines = line.split("&");
            for (int i = 0; i < lines.length; i++) {
                String[] values = lines[i].split("=");
                put(values[0], values[1]);
            }
        }

        protected Params() {}

        /** Возвращает строковое представление параметров.
         *
         * @return строковое представление параметров.
         */
        public String toString() {
            String result = "";
            for (Map.Entry<String, String> eachEntry: entrySet()) {
                if (result.length() != 0)
                    result += "&";
                result += String.format("%s=%s", eachEntry.getKey(), eachEntry.getValue());
            }
            return result;
        }
    }
}
