package org.voya.bootstrap;

import org.apache.commons.beanutils.ConvertUtils;
import org.voya.core.ConversorClasseDominio;
import org.voya.exemplo.dominio.Usuario;
import org.voya.exemplo.dominio.Categoria;
import org.voya.exemplo.dominio.Conta;
import org.voya.exemplo.dominio.Lancamento;

public class BootstrapImpl extends org.voya.core.Bootstrap
{
    @Override
    public void inicializar() 
    {
        ConvertUtils.register(new ConversorClasseDominio(), Categoria.class);
        ConvertUtils.register(new ConversorClasseDominio(), Conta.class);
        ConvertUtils.register(new ConversorClasseDominio(), Lancamento.class);
    }

    @Override
    public Class getClassUsuario() {
        return Usuario.class;
    }

    @Override
    public String getFirstPage() {
        return "/voya/Lancamento/index";
    }    
}
