/*
 * Meal Kit with the same name already exists
 */
package util.exception;

/**
 *
 * @author Summer
 */
public class MealKitExistException extends Exception {

    /**
     * Creates a new instance of <code>MealKitExistsException</code> without
     * detail message.
     */
    public MealKitExistException() {
    }

    /**
     * Constructs an instance of <code>MealKitExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public MealKitExistException(String msg) {
        super(msg);
    }
}
