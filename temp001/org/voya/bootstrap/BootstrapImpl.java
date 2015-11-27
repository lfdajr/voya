package org.voya.bootstrap;

import org.apache.commons.beanutils.ConvertUtils;
import org.voya.core.ConversorClasseDominio;

public class BootstrapImpl extends org.voya.core.Bootstrap
{
    @Override
    public void inicializar() 
    {
//        ConvertUtils.register(new ConversorClasseDominio(), Categoria.class);
//        ConvertUtils.register(new ConversorClasseDominio(), Conta.class);
//        ConvertUtils.register(new ConversorClasseDominio(), Lancamento.class);
    }

    @Override
    public Class getClassUsuario() {
        //return Usuario.class;
        return null;
    }

    @Override
    public String getFirstPage() {
        return "/voya/Lancamento/index";
    }    
}
