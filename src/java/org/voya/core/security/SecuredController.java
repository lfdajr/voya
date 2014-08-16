package org.voya.core.security;

public interface SecuredController {
    
    //Retorna se a execução está permitida para um determinado papel ou sistema
    public boolean acessoPermitido(Usuario usuario, String metodo);
    
}
