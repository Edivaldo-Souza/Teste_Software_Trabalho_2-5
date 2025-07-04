package org.example.dao;

import jakarta.persistence.*;
import org.example.entities.Usuario;

import java.util.List;

public class UsuarioService {

    private EntityManager em;
    private EntityManagerFactory emf;

    public UsuarioService() {
        this.emf = Persistence.createEntityManagerFactory("meuPU_H2");
        this.em = emf.createEntityManager();
    }

    public Usuario salvarUsuario(Usuario usuario) {
        em.getTransaction().begin();
        em.persist(usuario);
        em.getTransaction().commit();
        Usuario usuarioEncontrado = buscarPorLogin(usuario.getLogin());

        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }

        return usuarioEncontrado;
    }

    public Usuario buscarPorId(Long id){
        return em.find(Usuario.class, id);
    }

    public Usuario buscarPorLogin(String login) {
        String jpql = "SELECT u FROM Usuario u WHERE u.login = :login";
        TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
        query.setParameter("login", login); // Define o valor do parâmetro

        try {
            Usuario usuario = query.getSingleResult();
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
            return usuario;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Usuario> buscarTodos() {
        String jpql = "SELECT u FROM Usuario u";

        TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);

        List<Usuario> usuarios = query.getResultList();
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        return usuarios;
    }

    public Usuario delete(Long id) {
        Usuario usuario = em.find(Usuario.class, id);
        if(usuario != null){
            em.remove(usuario);
        }
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        return usuario;
    }
}
