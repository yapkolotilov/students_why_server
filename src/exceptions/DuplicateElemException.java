package exceptions;

/** Означает, что пользовал потребовал создать или добавить элемент, который уже существует.
 *
 */
public class DuplicateElemException extends Exception {
    public DuplicateElemException(String message) {
        super(message);
    }
}
