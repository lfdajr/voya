package org.voya.core;

import java.util.Date;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;

public class Bootstrap {

    public void inicializar()
    {
        
    }
    
    public void inicializarConverters()
    {
        DateTimeConverter dtConverter = new DateConverter();  
        //dtConverter.setPattern("dd/MM/yyyy"); 
        dtConverter.setPattern("yyyy-MM-dd"); 
        ConvertUtils.deregister(Date.class);  
        ConvertUtils.register(dtConverter, Date.class);  
    }
    
}
