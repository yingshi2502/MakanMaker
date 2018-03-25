/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author yingshi
 */
public class ManagerNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ManagerNotFoundException</code> without
     * detail message.
     */
    public ManagerNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ManagerNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ManagerNotFoundException(String msg) {
        super(msg);
    }
}
