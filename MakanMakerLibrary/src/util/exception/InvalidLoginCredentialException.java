/*
    password not match / no account
 */
package util.exception;

/**
 *
 * @author yingshi
 */
public class InvalidLoginCredentialException extends Exception {

    /**
     * Creates a new instance of <code>InvalidLoginCredentialException</code>
     * without detail message.
     */
    public InvalidLoginCredentialException() {
    }

    /**
     * Constructs an instance of <code>InvalidLoginCredentialException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidLoginCredentialException(String msg) {
        super(msg);
    }
}
