//controlador
package org.voya.exemplo;

import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import org.voya.core.EMUtil;
import org.voya.exemplo.dominio.Conta;

public class Lancamento {
    
    public void index(HttpServletRequest req) throws Exception
    {
        req.setAttribute("objeto", org.voya.exemplo.dominio.Lancamento.findAll());
        req.setAttribute("contas", Conta.findAll());
        
        
    }
    
    public void analitico(HttpServletRequest req) throws Exception
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

        
        
    }
    
    public void atualizar(org.voya.exemplo.dominio.Lancamento l) throws Exception
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
    }
    
    public void salvar(org.voya.exemplo.dominio.Lancamento l) throws Exception
    {
        l.save();
    }
    
    public List categorias() throws Exception
    {
        List<org.voya.exemplo.dominio.Categoria> lista = org.voya.exemplo.dominio.Categoria.findAll();
        return lista;
    }
    
    public List contas() throws Exception
    {
        List<org.voya.exemplo.dominio.Conta> lista = org.voya.exemplo.dominio.Conta.findAll();
        return lista;
    }    
    
}
