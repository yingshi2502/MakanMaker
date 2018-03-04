/*
 * YS: Throw when indending to retrieve a list of data from database but there's no record 
 *
 */
package util.exception;

/**
 *
 * @author yingshi
 */
public class EmptyListException extends Exception {

    /**
     * Creates a new instance of <code>EmptyListException</code> without detail
     * message.
     */
    public EmptyListException() {
    }

    /**
     * Constructs an instance of <code>EmptyListException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EmptyListException(String msg) {
        super(msg);
    }
}
