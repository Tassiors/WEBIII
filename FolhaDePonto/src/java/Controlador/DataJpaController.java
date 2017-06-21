/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import Entidades.Data;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Frequencia;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Tássio
 */
public class DataJpaController implements Serializable {

    public DataJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Data data) {
        if (data.getFrequenciaCollection() == null) {
            data.setFrequenciaCollection(new ArrayList<Frequencia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Frequencia> attachedFrequenciaCollection = new ArrayList<Frequencia>();
            for (Frequencia frequenciaCollectionFrequenciaToAttach : data.getFrequenciaCollection()) {
                frequenciaCollectionFrequenciaToAttach = em.getReference(frequenciaCollectionFrequenciaToAttach.getClass(), frequenciaCollectionFrequenciaToAttach.getId());
                attachedFrequenciaCollection.add(frequenciaCollectionFrequenciaToAttach);
            }
            data.setFrequenciaCollection(attachedFrequenciaCollection);
            em.persist(data);
            for (Frequencia frequenciaCollectionFrequencia : data.getFrequenciaCollection()) {
                frequenciaCollectionFrequencia.getDataCollection().add(data);
                frequenciaCollectionFrequencia = em.merge(frequenciaCollectionFrequencia);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Data data) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Data persistentData = em.find(Data.class, data.getId());
            Collection<Frequencia> frequenciaCollectionOld = persistentData.getFrequenciaCollection();
            Collection<Frequencia> frequenciaCollectionNew = data.getFrequenciaCollection();
            Collection<Frequencia> attachedFrequenciaCollectionNew = new ArrayList<Frequencia>();
            for (Frequencia frequenciaCollectionNewFrequenciaToAttach : frequenciaCollectionNew) {
                frequenciaCollectionNewFrequenciaToAttach = em.getReference(frequenciaCollectionNewFrequenciaToAttach.getClass(), frequenciaCollectionNewFrequenciaToAttach.getId());
                attachedFrequenciaCollectionNew.add(frequenciaCollectionNewFrequenciaToAttach);
            }
            frequenciaCollectionNew = attachedFrequenciaCollectionNew;
            data.setFrequenciaCollection(frequenciaCollectionNew);
            data = em.merge(data);
            for (Frequencia frequenciaCollectionOldFrequencia : frequenciaCollectionOld) {
                if (!frequenciaCollectionNew.contains(frequenciaCollectionOldFrequencia)) {
                    frequenciaCollectionOldFrequencia.getDataCollection().remove(data);
                    frequenciaCollectionOldFrequencia = em.merge(frequenciaCollectionOldFrequencia);
                }
            }
            for (Frequencia frequenciaCollectionNewFrequencia : frequenciaCollectionNew) {
                if (!frequenciaCollectionOld.contains(frequenciaCollectionNewFrequencia)) {
                    frequenciaCollectionNewFrequencia.getDataCollection().add(data);
                    frequenciaCollectionNewFrequencia = em.merge(frequenciaCollectionNewFrequencia);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = data.getId();
                if (findData(id) == null) {
                    throw new NonexistentEntityException("The data with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Data data;
            try {
                data = em.getReference(Data.class, id);
                data.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The data with id " + id + " no longer exists.", enfe);
            }
            Collection<Frequencia> frequenciaCollection = data.getFrequenciaCollection();
            for (Frequencia frequenciaCollectionFrequencia : frequenciaCollection) {
                frequenciaCollectionFrequencia.getDataCollection().remove(data);
                frequenciaCollectionFrequencia = em.merge(frequenciaCollectionFrequencia);
            }
            em.remove(data);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Data> findDataEntities() {
        return findDataEntities(true, -1, -1);
    }

    public List<Data> findDataEntities(int maxResults, int firstResult) {
        return findDataEntities(false, maxResults, firstResult);
    }

    private List<Data> findDataEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Data.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Data findData(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Data.class, id);
        } finally {
            em.close();
        }
    }

    public int getDataCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Data> rt = cq.from(Data.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
