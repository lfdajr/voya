package org.voya.core.security;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.voya.core.EMUtil;
import org.voya.core.Globals;
import org.voya.core.VoyaServlet;
import org.voya.core.validator.ValidatedController;
import org.voya.core.validator.Validator;

public class LoginLogoutController implements ValidatedController{
    
    private HttpServletRequest req;
    private HttpServletResponse resp;
    
    public LoginLogoutController(HttpServletRequest req, HttpServletResponse resp)
    {
        this.req = req;
        this.resp = resp;
    }
    
    public String login(HttpServletRequest request) 
    {
        return "/WEB-INF/templates/login.vm";
    }
    
    public String loginDo(HttpServletRequest request)
            throws NoResultException
    {
        EntityManager em = EMUtil.getEntityManager();
        EntityTransaction tx = null;
        try
        {
            tx = em.getTransaction();
            tx.begin();
            Query q = em.createNamedQuery("usuarioLogin");
            q.setParameter("login", request.getParameter("login"));
            q.setParameter("senha", request.getParameter("senha"));
            Usuario u = (Usuario) q.getSingleResult();
            request.getSession().setAttribute(Globals.SESSAO_USUARIO, u);
            tx.commit();
        }
        catch (NoResultException e)
        {
            Logger.getLogger(LoginLogoutController.class.getName()).log(Level.INFO, "Login/senha passados não conferem com nenhum usuário", e);
            
            if ( tx != null && tx.isActive() )
                tx.rollback();
            return "/WEB-INF/templates/login.vm";
        }
        catch (Exception e)
        {
            Logger.getLogger(LoginLogoutController.class.getName()).log(Level.SEVERE, "Erro geral no login.", e);
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

    @Override
    public String validate(Validator v, String metodo) {
        if (metodo.equals("loginDo"))
        {
            v.required("login", "erro.required.login");
            v.required("senha", "erro.required.senha");
            if (v.hasErrors()) 
                return "login";
        }
        
        return "redirect:" + VoyaServlet.getBootstrap().getFirstPage();
    }
    
    
}
