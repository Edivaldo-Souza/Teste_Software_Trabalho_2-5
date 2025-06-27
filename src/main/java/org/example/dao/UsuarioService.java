package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.example.entities.Usuario;

import java.util.List;

public class UsuarioService {

    private EntityManager em;

    public UsuarioService(EntityManager em) {
        this.em = em;
    }

    public Usuario buscarPorLogin(String login) {
        String jpql = "SELECT u FROM Usuario u WHERE u.login = :login";
        TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
        query.setParameter("login", login); // Define o valor do parâmetro

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Usuario> buscarTodos() {
        String jpql = "SELECT u FROM Usuario u";

        TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);

        return query.getResultList();
    }
}
