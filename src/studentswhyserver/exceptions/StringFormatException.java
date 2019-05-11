package studentswhyserver.exceptions;

/** Неправильный формат строки.
 *
 */
public class StringFormatException extends Exception {
    public StringFormatException(String message) {
        super(message);
    }
}
