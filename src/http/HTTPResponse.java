package http;

import enums.HTTPCode;
import enums.Result;
import exceptions.CodeNotSupportedException;
import exceptions.StringFormatException;

/** Представляет HTTP-ответ сервера клиенту.
 *
 */
public class HTTPResponse extends HTTPMessage {
    // --- Поля ---
    HTTPCode code; // Код ответа.


    // --- Конструкторы ---

    /** Создаёт новый HTTP-ответ.
     *
     * @param code Код ответа.
     */
    public HTTPResponse(HTTPCode code) {
        this.code = code;
    }

    public HTTPResponse(HTTPCode code, Result result) {
        this.code = code;
        setHeader("Result", result);
    }

    private HTTPResponse() {}


    // --- Методы ---

    /** Устанавливает значение заголовка Result.
     *
     * @param value значение.
     */
    public void setResult(Result value) {
        setHeader("Result", value);
    }

    @Override
    public String toString() {
        String result = String.format("%s %s", version, code);
        if (headers.size() != 0)
            result += "\n" + headers;

        result += "\n\n";

        if (body != null)
            result += new String(body, 0, body.length);
        else
            if (params.size() != 0)
                result += params;
            else
                return result;
        return result;
    }

    /** Возвращает байтовое представление запроса.
     *
     * @return байтовое представление запроса.
     */
    public byte[] getBytes() {
        return toString().getBytes();
    }

    /** Восстанавливает ответ из строки.
     *
     * @param line Строка.
     * @return Ответ.
     */
    public static HTTPResponse parseString(String line) throws StringFormatException, CodeNotSupportedException {
        line = line.replace("\r", "");
        if (!line.contains("\n\n"))
            throw new StringFormatException("В запросе отсутствует CRLF!");

        HTTPResponse result = new HTTPResponse();
        String[] parts = line.split("\n\n");
        String head = parts[0];

        String firstLine = head.split("\n")[0];
        String[] firstLineParts = firstLine.split(" ", 2);
        result.version = firstLineParts[0];
        result.code = HTTPCode.parseString(firstLineParts[1]);

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
}
