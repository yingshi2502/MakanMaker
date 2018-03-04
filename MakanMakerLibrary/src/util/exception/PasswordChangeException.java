/*
 * YS: When the input old password didn't match.
 */
package util.exception;

/**
 *
 * @author yingshi
 */
public class PasswordChangeException extends Exception {

    /**
     * Creates a new instance of <code>PasswordChangeException</code> without
     * detail message.
     */
    public PasswordChangeException() {
    }

    /**
     * Constructs an instance of <code>PasswordChangeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PasswordChangeException(String msg) {
        super(msg);
    }
}
