package studentswhyserver.exceptions;

/** Означает, что код ответа не поддерживается.
 *
 */
public class CodeNotSupportedException extends Exception {
    public CodeNotSupportedException(String message) {
        super(message);
    }
}
