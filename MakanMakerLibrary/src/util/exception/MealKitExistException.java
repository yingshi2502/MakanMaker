/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
