package exceptions;

/** Означает, что HTTP-метод не поддерживается.
 *
 */
public class MethodNotSupportedException extends Exception {
    public MethodNotSupportedException(String message) {
        super(message);
    }
}
