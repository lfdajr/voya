package org.voya.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.Converter;

//Construtor de classes de domínio a partir de construtores destas classes que recebem
//inteiro como parâmetro
public class ConversorClasseDominio implements Converter{

    @Override
    public Object convert(Class type, Object o) {
        try 
        {
            Constructor constructor = type.getConstructor(Integer.class);
            return constructor.newInstance(Integer.parseInt((String) o));
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(ConversorClasseDominio.class.getName()).log(Level.SEVERE, "Não foi possível criar a classe", ex);
        } 
        
        try 
        {
            Constructor constructor = type.getConstructor(Long.class);
            return constructor.newInstance(Long.parseLong((String) o));
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(ConversorClasseDominio.class.getName()).log(Level.SEVERE, "Não foi possível criar a classe", ex);
        }         
        
        return null;
    }
    
}
