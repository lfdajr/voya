//controlador
package org.voya.exemplo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.voya.core.EMUtil;
import org.voya.core.FileUploadUtil;
import org.voya.core.security.SecuredController;
import org.voya.core.validator.ValidatedController;
import org.voya.core.validator.Validator;
import org.voya.exemplo.dominio.Conta;

public class Lancamento implements ValidatedController
{
    private HttpServletRequest request;
    private HttpServletResponse response;
    
    public Lancamento(HttpServletRequest req, HttpServletResponse res)
    {
        this.request = req;
        this.response = res;
    }
    
    public String index(HttpServletRequest req) throws Exception
    {
        req.setAttribute("objeto", org.voya.exemplo.dominio.Lancamento.findAll());
        req.setAttribute("contas", Conta.findAll());
        
        return "/WEB-INF/templates/index.vm";
    }
    
    public String analitico(HttpServletRequest req) throws Exception
    {
        EntityManager em = EMUtil.getEntityManager();
        EntityTransaction tx = null;
        List retorno = null, retorno2 = null, retorno3 = null;
        try
        {
            tx = em.getTransaction();
            tx.begin();
            //seleciona para a conta 9 - cartão de crédito, todos os somatórios por mês
            Query q = em.createNativeQuery("SELECT SUM(VALOR), CONCAT(YEAR(DATA), '-', LPAD(MONTH(DATA),2,'0')) AS DATAANO FROM LANCAMENTO WHERE CONTA_ID = 9 GROUP BY DATAANO");
            retorno = q.getResultList();
            
            //Somatório dos valores para cada conta no mês
            q = em.createNativeQuery("SELECT SUM(L.VALOR) AS SOMATORIO, C.DESCRICAO FROM LANCAMENTO L, CONTA C WHERE C.ID = L.CONTA_ID AND MONTH(L.DATA) <= MONTH(NOW()) AND YEAR(L.DATA) <= YEAR(NOW()) GROUP BY CONTA_ID");
            retorno2 = q.getResultList();
            
            q = em.createNativeQuery("SELECT SUM(L.VALOR) AS SOMATORIO, C.DESCRICAO FROM LANCAMENTO L, CONTA C WHERE C.ID = L.CONTA_ID AND MONTH(L.DATA) <= MONTH(NOW() + interval 1 month) AND YEAR(L.DATA) <= YEAR(NOW() + interval 1 month) GROUP BY CONTA_ID");
            retorno3 = q.getResultList();
            
            
            tx.commit();
        }
        catch (EntityExistsException e)
        {
            if ( tx != null && tx.isActive() )
                tx.rollback();
            throw e;
        }
        catch (PersistenceException e)
        {
            if ( tx != null && tx.isActive() )
                tx.rollback();
            throw e;
        }
        catch (Exception e)
        {
            if ( tx != null && tx.isActive() )
                tx.rollback();
            throw e;
        }
        finally
        {
            em.close();
        }
        
        req.setAttribute("cartao", retorno);
        req.setAttribute("mensal", retorno2);
        req.setAttribute("proximoMensal", retorno3);
        
        return "/WEB-INF/templates/analitico.vm";
    }
    
    public String atualizar(org.voya.exemplo.dominio.Lancamento l) throws Exception
    {
        org.voya.exemplo.dominio.Lancamento lanc = org.voya.exemplo.dominio.Lancamento.findById(l.getId());
        if (l.getData() == null)
            l.setData(lanc.getData());
        if (l.getDescricao() == null)
            l.setDescricao(lanc.getDescricao());
        if (l.getCategoria() == null)
            l.setCategoria(lanc.getCategoria());
        if (l.getConta() == null)
            l.setConta(lanc.getConta());
        if (l.getValor() == null)
            l.setValor(lanc.getValor());        
        
        l.update();
        return "";
    }
    
    public String salvar(org.voya.exemplo.dominio.Lancamento l) throws Exception
    {
        l.save();
        return "";
    }
    
    public String categorias() throws Exception
    {
        List<org.voya.exemplo.dominio.Categoria> lista = org.voya.exemplo.dominio.Categoria.findAll();
        request.setAttribute("objeto", lista);
        return "/WEB-INF/templates/categorias.vm";
    }
    
    public String contas() throws Exception
    {
        List<org.voya.exemplo.dominio.Conta> lista = org.voya.exemplo.dominio.Conta.findAll();
        request.setAttribute("objeto", lista);
        return "/WEB-INF/templates/contas.vm";
    }    

    //@Override
    public boolean acessoPermitido(org.voya.core.security.Usuario usuario, String metodo) {
        return true;
        //return usuario.getPerfil().contains("manager");
    }

    @Override
    public String validate(Validator valida, String metodo) 
    {
        if (metodo.equals("atualizar"))
        {
            valida.required("data");
            valida.date("data", "dd/MM/YYYY");
            //return "/WEB-INF/templates/contas.vm";
        }
        
        return null;
    }
    
    public String upload()
    {
        try 
        {
            String fileName = request.getPart("upfile").getName();
            FileUploadUtil.processarUpload(request, "/home/99282895491/projetos/voya/build/" + fileName, 1000000);
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
        
        return "";
    }
}
