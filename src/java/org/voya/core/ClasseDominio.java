package org.voya.core;

import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author Lourival Almeida
 */
public abstract class ClasseDominio<T> 
{
    public void save() throws Exception
    {
        EntityManager em = EMUtil.getEntityManager();
        EntityTransaction tx = null;
        try
        {
            tx = em.getTransaction();
            tx.begin();
            em.persist(this);
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
    }
    
    public void update() throws Exception
    {
        EntityManager em = EMUtil.getEntityManager();
        EntityTransaction tx = null;
        try
        {
            tx = em.getTransaction();
            tx.begin();
            em.merge(this);
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
    }
    
    public void remove() throws Exception
    {
        EntityManager em = EMUtil.getEntityManager();
        EntityTransaction tx = null;
        try
        {
            tx = em.getTransaction();
            tx.begin();
            em.remove(this);
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
    }
    
    public static Object findById(Class classe, Object id) throws Exception
    {
        EntityManager em = EMUtil.getEntityManager();
        EntityTransaction tx = null;
        Object retorno = null;
        try
        {
            tx = em.getTransaction();
            tx.begin();
            retorno = em.find(classe, id);
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
        
        return retorno;
    }
    
    /**
     * Retorna uma lista a partir do named query
     * @param classe
     * @param queryStr
     * @return
     * @throws Exception 
     */
    public static List findAll(Class classe) throws Exception
    {
        EntityManager em = EMUtil.getEntityManager();
        EntityTransaction tx = null;
        List retorno = null;
        try
        {
            tx = em.getTransaction();
            tx.begin();
            Query q = em.createQuery("from " + classe.getSimpleName());
            retorno = q.getResultList();
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
        
        return retorno;
    }
    
}
