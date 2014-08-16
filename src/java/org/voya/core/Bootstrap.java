package org.voya.core;

import java.util.Date;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;

public abstract class Bootstrap {

    public abstract void inicializar();
    
    public abstract Class getClassUsuario();
    
    public abstract String getFirstPage();
    
    public void inicializarConverters()
    {
        DateTimeConverter dtConverter = new DateConverter();  
        //dtConverter.setPattern("dd/MM/yyyy"); 
        dtConverter.setPattern("yyyy-MM-dd"); 
        ConvertUtils.deregister(Date.class);  
        ConvertUtils.register(dtConverter, Date.class);  
    }
    
}
