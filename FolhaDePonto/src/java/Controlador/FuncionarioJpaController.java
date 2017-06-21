/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Frequencia;
import Entidades.Funcionario;
import Entidades.FuncionarioPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Tássio
 */
public class FuncionarioJpaController implements Serializable {

    public FuncionarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Funcionario funcionario) throws PreexistingEntityException, Exception {
        if (funcionario.getFuncionarioPK() == null) {
            funcionario.setFuncionarioPK(new FuncionarioPK());
        }
        funcionario.getFuncionarioPK().setFrequenciaId(funcionario.getFrequencia().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Frequencia frequencia = funcionario.getFrequencia();
            if (frequencia != null) {
                frequencia = em.getReference(frequencia.getClass(), frequencia.getId());
                funcionario.setFrequencia(frequencia);
            }
            em.persist(funcionario);
            if (frequencia != null) {
                frequencia.getFuncionarioCollection().add(funcionario);
                frequencia = em.merge(frequencia);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFuncionario(funcionario.getFuncionarioPK()) != null) {
                throw new PreexistingEntityException("Funcionario " + funcionario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Funcionario funcionario) throws NonexistentEntityException, Exception {
        funcionario.getFuncionarioPK().setFrequenciaId(funcionario.getFrequencia().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Funcionario persistentFuncionario = em.find(Funcionario.class, funcionario.getFuncionarioPK());
            Frequencia frequenciaOld = persistentFuncionario.getFrequencia();
            Frequencia frequenciaNew = funcionario.getFrequencia();
            if (frequenciaNew != null) {
                frequenciaNew = em.getReference(frequenciaNew.getClass(), frequenciaNew.getId());
                funcionario.setFrequencia(frequenciaNew);
            }
            funcionario = em.merge(funcionario);
            if (frequenciaOld != null && !frequenciaOld.equals(frequenciaNew)) {
                frequenciaOld.getFuncionarioCollection().remove(funcionario);
                frequenciaOld = em.merge(frequenciaOld);
            }
            if (frequenciaNew != null && !frequenciaNew.equals(frequenciaOld)) {
                frequenciaNew.getFuncionarioCollection().add(funcionario);
                frequenciaNew = em.merge(frequenciaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                FuncionarioPK id = funcionario.getFuncionarioPK();
                if (findFuncionario(id) == null) {
                    throw new NonexistentEntityException("The funcionario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(FuncionarioPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Funcionario funcionario;
            try {
                funcionario = em.getReference(Funcionario.class, id);
                funcionario.getFuncionarioPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The funcionario with id " + id + " no longer exists.", enfe);
            }
            Frequencia frequencia = funcionario.getFrequencia();
            if (frequencia != null) {
                frequencia.getFuncionarioCollection().remove(funcionario);
                frequencia = em.merge(frequencia);
            }
            em.remove(funcionario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    
    //REVER ESSE MÉTODO DE LOGIN
    public Funcionario findLogin(String login, String senha) {
        List<Funcionario> fs = null;
            Funcionario f = null;
        
        EntityManager em = getEntityManager();
        try {
            fs = (List<Funcionario>) em.createNamedQuery("Usuarios.findByLogin").setParameter("login", login).setParameter("senha", senha).getResultList();
            if(fs.size() > 0 )
            f = fs.get(0);
            return f;
        } finally {
//            em.close();
        }
    }

    public List<Funcionario> findFuncionarioEntities() {
        return findFuncionarioEntities(true, -1, -1);
    }

    public List<Funcionario> findFuncionarioEntities(int maxResults, int firstResult) {
        return findFuncionarioEntities(false, maxResults, firstResult);
    }

    private List<Funcionario> findFuncionarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Funcionario.class));
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

    public Funcionario findFuncionario(FuncionarioPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Funcionario.class, id);
        } finally {
            em.close();
        }
    }

    public int getFuncionarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Funcionario> rt = cq.from(Funcionario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
