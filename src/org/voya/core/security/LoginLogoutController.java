package org.voya.core.security;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import org.voya.core.EMUtil;
import org.voya.core.Globals;
import org.voya.core.VoyaServlet;

public class LoginLogoutController {
    
    public String login(HttpServletRequest request) 
    {
        return "/WEB-INF/templates/login.vm";
    }
    
    public String loginDo(HttpServletRequest request)
    {
        EntityManager em = EMUtil.getEntityManager();
        EntityTransaction tx = null;
        try
        {
            tx = em.getTransaction();
            tx.begin();
            Query q = em.createQuery("from " + VoyaServlet.getBootstrap().getClassUsuario().getName() + " where login = :login and senha = SHA1(:senha)");
            q.setParameter("login", request.getParameter("login"));
            q.setParameter("senha", request.getParameter("senha"));
            Usuario u = (Usuario) q.getSingleResult();
            request.getSession().setAttribute(Globals.SESSAO_USUARIO, u);
            tx.commit();
        }
        catch (Exception e)
        {
            if ( tx != null && tx.isActive() )
                tx.rollback();
            return "/WEB-INF/templates/login.vm";
        }
        finally
        {
            em.close();
        }
        
        return "redirect:" + VoyaServlet.getBootstrap().getFirstPage();
    }
    
    public String logout(HttpServletRequest request)
    {
        request.getSession().removeAttribute(Globals.SESSAO_USUARIO);
        request.getSession().invalidate();
        return "redirect:" + request.getContextPath() + "/login";
    }
    
    
}
