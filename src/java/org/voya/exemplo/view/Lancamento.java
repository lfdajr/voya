package org.voya.exemplo.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Lancamento {
    
    public String atualizacaoAjax(HttpServletRequest request, HttpServletResponse response)
    {
        return "/WEB-INF/templates/index.vm";
    }
    
    public String salvar(HttpServletRequest request, HttpServletResponse response)
    {
        return "";
    }
    
    public String atualizar(HttpServletRequest request, HttpServletResponse response)
    {
        return "";
    }
}
