package org.voya.core;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Filtro implements Filter
{
    private int contador;

    @Override
    public void destroy()
    {
    }

    
    /*public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        req.setCharacterEncoding("UTF8");
        
        
            if (req.getSession(false).getAttribute(Globals.USUARIO) != null)
            {
                chain.doFilter(request, response);
            }
            else
            {
                ((HttpServletResponse) response).sendRedirect(req.getContextPath() + "/logout.do");
            }
       
        
        
    }*/
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        req.setCharacterEncoding("UTF8");
        chain.doFilter(request, response);
    }    

    @Override
    public void init(FilterConfig filterConfig)
        throws ServletException
    {
    }
}
