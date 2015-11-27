package org.voya.core.validator;

import java.util.ArrayList;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.validator.GenericValidator;

public class Validator 
{
    private HttpServletRequest request;
    private ArrayList errors;
    private boolean erroPresente;
    private Properties appMensagens;
    
    public Validator(HttpServletRequest request, Properties appMensagens)
    {
        this.request = request;
        errors = new ArrayList();
        erroPresente = false;
        this.appMensagens = appMensagens;
    }
    
    public boolean hasErrors()
    {
        return !errors.isEmpty();
    }
    
    public ArrayList getErrors()
    {
        return errors;
    }
    
    public boolean required(String field, String chaveOnError)
    {
        erroPresente = GenericValidator.isBlankOrNull(request.getParameter(field));
        
        if (erroPresente)
            errors.add(appMensagens.get(chaveOnError));
        
        return erroPresente;
    }
    
    public boolean date(String field, String pattern, String chaveOnError)
    {
        erroPresente = GenericValidator.isDate(request.getParameter(field), pattern, true);
        
        if (!erroPresente)
            errors.add(appMensagens.get(chaveOnError));
        
        return erroPresente;
    }
                
}
