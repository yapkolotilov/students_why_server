package enums;

import com.sun.org.apache.regexp.internal.RE;
import exceptions.MethodNotSupportedException;
import exceptions.NotImplementedException;

/** Методы HTTP-запроса.
 */
public enum HTTPMethod {
    GET("GET"), HEAD("HEAD"), POST("POST"), PUT("PUT"), REMOVE("REMOVE");


    HTTPMethod(String value) {
        this.value = value;
    }

    private String value;
    public String toString() {return value;}

    /** Получает метод по его текстовому представлению.
     *
     * @param line Текстовое представление запроса.
     * @return Метод.
     */
    public static HTTPMethod parseString(String line) throws MethodNotSupportedException {
        switch (line) {
            case "GET":
                return GET;
            case "HEAD":
                return HEAD;
            case "POST":
                return POST;
            case "PUT":
                return PUT;
            case "REMOVE":
                return REMOVE;
            default:
                throw new MethodNotSupportedException(String.format("Метод %s не поддерживается!", line));
        }
    }
}
