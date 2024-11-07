package dao;


import model.Cliente;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ClienteDAO {

    public void salvar(Cliente cliente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (cliente.getId() == null) {
                em.persist(cliente);
            } else {
                em.merge(cliente);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void excluir(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Cliente cliente = em.find(Cliente.class, id);
            if (cliente != null) {
                em.remove(cliente);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Cliente buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public List<Cliente> buscarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Cliente> buscarPorNome(String nome) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT c FROM Cliente c WHERE LOWER(c.nome) LIKE LOWER(:nome)";
            TypedQuery<Cliente> query = em.createQuery(jpql, Cliente.class);
            query.setParameter("nome", "%" + nome + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public boolean emailJaExiste(String email, Long clienteId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(c) FROM Cliente c WHERE c.email = :email";
            if (clienteId != null) {
                jpql += " AND c.id != :id";
            }
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("email", email);
            if (clienteId != null) {
                query.setParameter("id", clienteId);
            }
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
}
