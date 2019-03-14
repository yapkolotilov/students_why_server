package enums;

import exceptions.NotImplementedException;

/** Код ответа сервера.
 *
 */
public enum  HTTPCode {
    OK_200("200 OK"), BAD_REQUEST_400("400 Bad Request"), NOT_FOUND_404("404 Not Found"),
    METHOD_NOT_ALLOWED_405("405 Method Not Allowed"), INTERNAL_SERVER_ERROR_500("500 Internal Server Error");

    private HTTPCode(String value) {
        this.value = value;
    }

    private String value;

    public String toString() {
        return value;
    }

    /** Парсит строку.
     *
     * @param line Строка.
     * @return Код.
     */
    public static HTTPCode parseString(String line) {
        switch (line) {
            case "200 OK":
                return OK_200;
            case "400 Bad Request":
                return BAD_REQUEST_400;
            case "404 Not Found":
                return NOT_FOUND_404;
            case "405 Method Not Allowed":
                return METHOD_NOT_ALLOWED_405;
            case "500 Internal Server Error":
                return INTERNAL_SERVER_ERROR_500;
            default:
                throw new NotImplementedException();
        }
    }
}
