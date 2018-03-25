/*
 * Tag name should be unique in the website
 */
package util.exception;

/**
 *
 * @author yingshi
 */
public class TagExisitException extends Exception {

    /**
     * Creates a new instance of <code>TagExisitException</code> without detail
     * message.
     */
    public TagExisitException() {
    }

    /**
     * Constructs an instance of <code>TagExisitException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TagExisitException(String msg) {
        super(msg);
    }
}
