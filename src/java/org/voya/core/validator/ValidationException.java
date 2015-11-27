package org.voya.core.validator;

public class ValidationException extends Exception {
    
    private Validator validador;

    public ValidationException(Validator validador) {
        this.validador = validador;
    }

    public ValidationException(String msg) {
        super(msg);
    }
    
    public Validator getValidador()
    {
        return validador;
    }
}
