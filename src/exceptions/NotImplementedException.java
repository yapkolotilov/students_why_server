package exceptions;

/** Означает, что часть кода ещё не дописана.
 *
 */
public class NotImplementedException extends RuntimeException {
    public NotImplementedException() {
    }

    public NotImplementedException(String message) {
        super(message);
    }
}
