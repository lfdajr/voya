package org.voya.core.validator;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.validator.GenericValidator;

public class Validator 
{
    private HttpServletRequest request;
    private ArrayList errors;
    
    public Validator(HttpServletRequest request)
    {
        this.request = request;
        errors = new ArrayList();
    }
    
    public boolean hasErrors()
    {
        if (errors.size() == 0)
            return false;
        else
            return true;
    }
    
    public ArrayList getErrors()
    {
        return errors;
    }
    
    public boolean required(String field)
    {
        return GenericValidator.isBlankOrNull(request.getParameter(field));
    }
    
    public boolean date(String field, String pattern)
    {
        boolean retorno = GenericValidator.isDate(request.getParameter(field), pattern, true);
        
        if (!retorno)
            errors.add(field);
        
        return retorno;
    }
                
}
