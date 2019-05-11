package studentswhyserver.utilities;

import studentswhyserver.debug.Console;
import studentswhyserver.http.HTTPRequest;
import studentswhyserver.http.HTTPResponse;

import java.io.IOException;
import java.io.InputStream;

/** Содержит общие для всех классов статические методы.
 *
 */
public class GlobalMethods {
    /** Считывает строку из входного потока данных.
     *
     * @param inputStream Входной поток данных.
     * @return Строка из входного потока данных.
     */
    public static String readString(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[100 * 1024];
        int length = inputStream.read(bytes);
        return new String(bytes, 0, length);
    }

    /** Считывает строку из входного потока данных.
     *
     * @param inputStream Входной поток данных.
     * @param contentLength Длина тела сообщения.
     * @return Строка из входного потока данных.
     */
    private static String readString(InputStream inputStream, int contentLength) throws IOException {
        byte[] bytes = new byte[contentLength];
        int length = inputStream.read(bytes);
        return new String(bytes, 0, length);
    }


    /** Считывает строковое представление запроса.
     *
     * @param inputStream Входной поток данных
     * @return строковое представление запроса.
     */
    public static String readStrRequest(InputStream inputStream) throws IOException {
        // Получаем сообщение:
        String strRequest = GlobalMethods.readString(inputStream);

        // Докачиваем тело:
        try {
            HTTPRequest result = HTTPRequest.parseString(strRequest);
            if (result.containsHeader("Content-Length") && (result.getBody() == null || result.getBody().length == 0))
                strRequest += GlobalMethods.readString(inputStream, Integer.parseInt(result.getHeader("Content-Length")));
        } catch (Exception e) {}

        return strRequest;
    }

    /** Считывает строковое представление ответа.
     *
     * @param inputStream Входной поток данных.
     * @return Строковое представление запроса.
     */
    public static String readStrResponse(InputStream inputStream) throws IOException {
        // Получаем сообщение:
        String strRequest = GlobalMethods.readString(inputStream);

        // Докачиваем тело:
        try {
            HTTPResponse result = HTTPResponse.parseString(strRequest);
            if (result.containsHeader("Content-Length") && (result.getBody() == null || result.getBody().length == 0))
                strRequest += GlobalMethods.readString(inputStream, Integer.parseInt(result.getHeader("Content-Length")));
        } catch (Exception e) {Console.println("ошибка!"); e.printStackTrace(); }

        return strRequest;
    }

    public static String arrayToString(String[] array) {
        String result = "";
        if (array == null)
            return "null";
        for (int i = 0; i < array.length - 1; i++)
            result += array[i] + ", ";
        if (array.length != 0)
            result += array[array.length - 1];
        return result;
    }

    private GlobalMethods() {}
}
