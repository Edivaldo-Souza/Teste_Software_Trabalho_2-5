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
        if(!em.isOpen()) {
            this.emf = Persistence.createEntityManagerFactory("meuPU_H2");
            this.em = emf.createEntityManager();
        }

        em.getTransaction().begin();
        em.persist(usuario);
        em.getTransaction().commit();
        Usuario usuarioEncontrado = buscarPorLogin(usuario.getLogin());

        em.close();
        emf.close();

        return usuarioEncontrado;
    }

    public Usuario buscarPorId(Long id){
        if(!em.isOpen()) {
            this.emf = Persistence.createEntityManagerFactory("meuPU_H2");
            this.em = emf.createEntityManager();
        }

        return em.find(Usuario.class, id);
    }

    public Usuario buscarPorLogin(String login) {
        if(!em.isOpen()) {
            this.emf = Persistence.createEntityManagerFactory("meuPU_H2");
            this.em = emf.createEntityManager();
        }

        String jpql = "SELECT u FROM Usuario u WHERE u.login = :login";
        TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
        query.setParameter("login", login); // Define o valor do parâmetro

        try {
            Usuario usuario = query.getSingleResult();

            em.close();
            emf.close();
            return usuario;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Usuario> buscarTodos() {
        if(!em.isOpen()) {
            this.emf = Persistence.createEntityManagerFactory("meuPU_H2");
            this.em = emf.createEntityManager();
        }

        String jpql = "SELECT u FROM Usuario u";

        TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);

        List<Usuario> usuarios = query.getResultList();

        em.close();
        emf.close();
        return usuarios;
    }
}
