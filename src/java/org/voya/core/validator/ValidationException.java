/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.voya.core.validator;

/**
 *
 * @author 99282895491
 */
public class ValidationException extends Exception {

    /**
     * Creates a new instance of <code>ValidationException</code> without detail
     * message.
     */
    public ValidationException() {
    }

    /**
     * Constructs an instance of <code>ValidationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ValidationException(String msg) {
        super(msg);
    }
}
