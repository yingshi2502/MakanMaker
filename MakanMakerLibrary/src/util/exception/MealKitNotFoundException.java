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
public class MealKitNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>MealKitNotFoundException</code> without
     * detail message.
     */
    public MealKitNotFoundException() {
    }

    /**
     * Constructs an instance of <code>MealKitNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public MealKitNotFoundException(String msg) {
        super(msg);
    }
}
