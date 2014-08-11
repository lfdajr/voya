package org.voya.core;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/** 
 * Utilitário para criaçãoo de Entity Managers para aplicação.
 * @author Lourival Almeida
 */
public class EMUtil
{
    private static EntityManagerFactory emf = null;
    
    public static EntityManager getEntityManager()
    {
        try
        {
            if (emf == null)
                emf = Persistence.createEntityManagerFactory("PersistenceUnit");
        } 
        catch (Exception e)
        {
            //Todo: Criar uma forma de mostrar mensagens de erro na tela
            //Utilitario.mostrarMensagemErro(ErrorMessages.ERRO_FALHA_GERAL);
            e.printStackTrace();
            if (emf != null)
                emf.close();
        }

        return emf.createEntityManager();
    }    
    
    
    /**
     * Executa uma query simples na base de dados.
     */
    public static List query(String queryString)
    {
        EntityManager em = EMUtil.getEntityManager();
        Query query = em.createQuery(queryString);
        List results = query.getResultList();
        em.close();
        return results;
    }    
    
    
}



/*
    public void inserir(Usuario entidade) 
        throws UsuarioEmailUniqueViolationException,Exception
    {
        EntityManager em = EMUtil.getEntityManager();
        EntityTransaction tx = null;
        try
        {
            tx = em.getTransaction();
            tx.begin();
            em.persist(entidade);
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
            throw new UsuarioEmailUniqueViolationException();
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
        
    }*/