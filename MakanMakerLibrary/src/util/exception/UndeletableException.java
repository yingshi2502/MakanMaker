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
public class UndeletableException extends Exception {

    /**
     * Creates a new instance of <code>UndeletableException</code> without
     * detail message.
     */
    public UndeletableException() {
    }

    /**
     * Constructs an instance of <code>UndeletableException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UndeletableException(String msg) {
        super(msg);
    }
}
