package http;

import debug.Console;
import enums.HTTPMethod;
import exceptions.MethodNotSupportedException;
import exceptions.NotImplementedException;
import exceptions.StringFormatException;
import utilities.FilePath;

/** Представляет HTTP-запрос от клиента серверу.
 *
 */
public class HTTPRequest extends HTTPMessage {
    // --- Поля ---

    private HTTPMethod method; // Метод запроса.
    private String url; // URL запроса.


    // --- Конструкторы ---

    /** Создаёт новый запрос.
     *
     * @param method Метод запроса.
     * @param url URL запроса.
     */
    public HTTPRequest(HTTPMethod method, String url) {
        this.method = method;
        this.url = url;
    }

    private HTTPRequest() {}


    // --- Геттеры и сеттеры ---

    public HTTPMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    // --- Методы:

    @Override
    public String toString() {
        String result = String.format("%s %s %s", method, getURLString(), version);
        if (headers.size() != 0)
            result += "\n" + headers;

        result += "\n\n";

        result += getBodyString();
        return result;
    }

    /** Восстанавливает запрос из строки.
     *
     * @param line Строка.
     * @return Запрос.
     */
    public static HTTPRequest parseString(String line) throws StringFormatException, MethodNotSupportedException {
        line = line.replace("\r", "");
        if (!line.contains("\n\n"))
            throw new StringFormatException("В запросе отсутствует CRLF!");

        HTTPRequest result = new HTTPRequest();
        String[] parts = line.split("\n\n");
        String head = parts[0];

        String firstLine = head.split("\n")[0];
        String[] firstLineParts = firstLine.split(" ");
        result.method = HTTPMethod.parseString(firstLineParts[0]);
        result.url = firstLineParts[1].split("\\?")[0];
        if (firstLineParts[1].split("\\?").length != 1)
            result.params = result.new Params(firstLineParts[1].split("\\?")[1]);
        result.version = firstLineParts[2];

        head = head.replace(firstLine, "");
        if (head.startsWith("\n"))
            head = head.replaceFirst("\n", "");
        if (head.length() != 0)
            result.headers = result.new Headers(head);

        if (parts.length == 1)
            return result;

        String body = parts[1];
        try {
            result.params = result.new Params(body);
        } catch (StringFormatException e) {
            result.body = body.getBytes();
        }

        return result;
    }

    /** Возвращает байтовое представление запроса.
     *
     * @return байтовое представление запроса.
     */
    public byte[] getBytes() {
        return toString().getBytes();
    }

    /** Восстанавливает путь к файлу из URL.
     *
     * @return Путь к файлу.
     */
    public FilePath getPath() {
        return FilePath.parseURL(url);
    }

    /** Возвращает url вместе с параметрами.
     *
     * @return url вместе с параметрами.
     */
    private String getURLString() {
        String result = url;
        if (method == HTTPMethod.GET || method == HTTPMethod.HEAD || method == HTTPMethod.REMOVE) {
            if (params.size() == 0)
                return result;
            return result + "?" + params;
        }
        if (method == HTTPMethod.POST || method == HTTPMethod.PUT)
            return result;

        throw new NotImplementedException();
    }

    /** Возвращает url вместе с параметрами.
     *
     * @return url вместе с параметрами.
     */
    private String getBodyString() {
        if (method == HTTPMethod.POST || method == HTTPMethod.PUT)
            if (params.size() != 0)
                return params.toString();
            else if (body == null)
                return "";
            else
                return new String(body, 0, body.length);

        if (method == HTTPMethod.GET || method == HTTPMethod.HEAD || method == HTTPMethod.REMOVE)
            if (body == null)
                return "";
            else
                return new String(body, 0, body.length);

        throw new NotImplementedException();
    }
}
