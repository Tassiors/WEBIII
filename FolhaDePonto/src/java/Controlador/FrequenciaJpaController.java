/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Data;
import Entidades.Frequencia;
import java.util.ArrayList;
import java.util.Collection;
import Entidades.Funcionario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Tássio
 */
public class FrequenciaJpaController implements Serializable {

    public FrequenciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Frequencia frequencia) {
        if (frequencia.getDataCollection() == null) {
            frequencia.setDataCollection(new ArrayList<Data>());
        }
        if (frequencia.getFuncionarioCollection() == null) {
            frequencia.setFuncionarioCollection(new ArrayList<Funcionario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Data> attachedDataCollection = new ArrayList<Data>();
            for (Data dataCollectionDataToAttach : frequencia.getDataCollection()) {
                dataCollectionDataToAttach = em.getReference(dataCollectionDataToAttach.getClass(), dataCollectionDataToAttach.getId());
                attachedDataCollection.add(dataCollectionDataToAttach);
            }
            frequencia.setDataCollection(attachedDataCollection);
            Collection<Funcionario> attachedFuncionarioCollection = new ArrayList<Funcionario>();
            for (Funcionario funcionarioCollectionFuncionarioToAttach : frequencia.getFuncionarioCollection()) {
                funcionarioCollectionFuncionarioToAttach = em.getReference(funcionarioCollectionFuncionarioToAttach.getClass(), funcionarioCollectionFuncionarioToAttach.getFuncionarioPK());
                attachedFuncionarioCollection.add(funcionarioCollectionFuncionarioToAttach);
            }
            frequencia.setFuncionarioCollection(attachedFuncionarioCollection);
            em.persist(frequencia);
            for (Data dataCollectionData : frequencia.getDataCollection()) {
                dataCollectionData.getFrequenciaCollection().add(frequencia);
                dataCollectionData = em.merge(dataCollectionData);
            }
            for (Funcionario funcionarioCollectionFuncionario : frequencia.getFuncionarioCollection()) {
                Frequencia oldFrequenciaOfFuncionarioCollectionFuncionario = funcionarioCollectionFuncionario.getFrequencia();
                funcionarioCollectionFuncionario.setFrequencia(frequencia);
                funcionarioCollectionFuncionario = em.merge(funcionarioCollectionFuncionario);
                if (oldFrequenciaOfFuncionarioCollectionFuncionario != null) {
                    oldFrequenciaOfFuncionarioCollectionFuncionario.getFuncionarioCollection().remove(funcionarioCollectionFuncionario);
                    oldFrequenciaOfFuncionarioCollectionFuncionario = em.merge(oldFrequenciaOfFuncionarioCollectionFuncionario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Frequencia frequencia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Frequencia persistentFrequencia = em.find(Frequencia.class, frequencia.getId());
            Collection<Data> dataCollectionOld = persistentFrequencia.getDataCollection();
            Collection<Data> dataCollectionNew = frequencia.getDataCollection();
            Collection<Funcionario> funcionarioCollectionOld = persistentFrequencia.getFuncionarioCollection();
            Collection<Funcionario> funcionarioCollectionNew = frequencia.getFuncionarioCollection();
            List<String> illegalOrphanMessages = null;
            for (Funcionario funcionarioCollectionOldFuncionario : funcionarioCollectionOld) {
                if (!funcionarioCollectionNew.contains(funcionarioCollectionOldFuncionario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Funcionario " + funcionarioCollectionOldFuncionario + " since its frequencia field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Data> attachedDataCollectionNew = new ArrayList<Data>();
            for (Data dataCollectionNewDataToAttach : dataCollectionNew) {
                dataCollectionNewDataToAttach = em.getReference(dataCollectionNewDataToAttach.getClass(), dataCollectionNewDataToAttach.getId());
                attachedDataCollectionNew.add(dataCollectionNewDataToAttach);
            }
            dataCollectionNew = attachedDataCollectionNew;
            frequencia.setDataCollection(dataCollectionNew);
            Collection<Funcionario> attachedFuncionarioCollectionNew = new ArrayList<Funcionario>();
            for (Funcionario funcionarioCollectionNewFuncionarioToAttach : funcionarioCollectionNew) {
                funcionarioCollectionNewFuncionarioToAttach = em.getReference(funcionarioCollectionNewFuncionarioToAttach.getClass(), funcionarioCollectionNewFuncionarioToAttach.getFuncionarioPK());
                attachedFuncionarioCollectionNew.add(funcionarioCollectionNewFuncionarioToAttach);
            }
            funcionarioCollectionNew = attachedFuncionarioCollectionNew;
            frequencia.setFuncionarioCollection(funcionarioCollectionNew);
            frequencia = em.merge(frequencia);
            for (Data dataCollectionOldData : dataCollectionOld) {
                if (!dataCollectionNew.contains(dataCollectionOldData)) {
                    dataCollectionOldData.getFrequenciaCollection().remove(frequencia);
                    dataCollectionOldData = em.merge(dataCollectionOldData);
                }
            }
            for (Data dataCollectionNewData : dataCollectionNew) {
                if (!dataCollectionOld.contains(dataCollectionNewData)) {
                    dataCollectionNewData.getFrequenciaCollection().add(frequencia);
                    dataCollectionNewData = em.merge(dataCollectionNewData);
                }
            }
            for (Funcionario funcionarioCollectionNewFuncionario : funcionarioCollectionNew) {
                if (!funcionarioCollectionOld.contains(funcionarioCollectionNewFuncionario)) {
                    Frequencia oldFrequenciaOfFuncionarioCollectionNewFuncionario = funcionarioCollectionNewFuncionario.getFrequencia();
                    funcionarioCollectionNewFuncionario.setFrequencia(frequencia);
                    funcionarioCollectionNewFuncionario = em.merge(funcionarioCollectionNewFuncionario);
                    if (oldFrequenciaOfFuncionarioCollectionNewFuncionario != null && !oldFrequenciaOfFuncionarioCollectionNewFuncionario.equals(frequencia)) {
                        oldFrequenciaOfFuncionarioCollectionNewFuncionario.getFuncionarioCollection().remove(funcionarioCollectionNewFuncionario);
                        oldFrequenciaOfFuncionarioCollectionNewFuncionario = em.merge(oldFrequenciaOfFuncionarioCollectionNewFuncionario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = frequencia.getId();
                if (findFrequencia(id) == null) {
                    throw new NonexistentEntityException("The frequencia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Frequencia frequencia;
            try {
                frequencia = em.getReference(Frequencia.class, id);
                frequencia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The frequencia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Funcionario> funcionarioCollectionOrphanCheck = frequencia.getFuncionarioCollection();
            for (Funcionario funcionarioCollectionOrphanCheckFuncionario : funcionarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Frequencia (" + frequencia + ") cannot be destroyed since the Funcionario " + funcionarioCollectionOrphanCheckFuncionario + " in its funcionarioCollection field has a non-nullable frequencia field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Data> dataCollection = frequencia.getDataCollection();
            for (Data dataCollectionData : dataCollection) {
                dataCollectionData.getFrequenciaCollection().remove(frequencia);
                dataCollectionData = em.merge(dataCollectionData);
            }
            em.remove(frequencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Frequencia> findFrequenciaEntities() {
        return findFrequenciaEntities(true, -1, -1);
    }

    public List<Frequencia> findFrequenciaEntities(int maxResults, int firstResult) {
        return findFrequenciaEntities(false, maxResults, firstResult);
    }

    private List<Frequencia> findFrequenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Frequencia.class));
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

    public Frequencia findFrequencia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Frequencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getFrequenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Frequencia> rt = cq.from(Frequencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
