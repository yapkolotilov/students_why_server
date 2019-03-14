package exceptions;

/** Означает, что запрашиваемый элемент не существует.
 *
 */
public class NoSuchElemException extends Exception {
    public NoSuchElemException(String message) {
        super(message);
    }
}
