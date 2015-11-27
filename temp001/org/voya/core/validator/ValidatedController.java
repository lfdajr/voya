package org.voya.core.validator;

public interface ValidatedController {
    
    //Retorna se a execução está permitida para um determinado papel ou sistema
    public String validate(Validator validador, String metodo);
    
}
