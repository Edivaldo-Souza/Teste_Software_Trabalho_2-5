import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.dao.UsuarioService;
import org.example.entities.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TesteIntegracao {

    private void deleteUsers(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU_H2");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        String jpql = "DELETE FROM Usuario u WHERE u.login =: login";

        em.createQuery(jpql).setParameter("login","user2").executeUpdate();
        em.createQuery(jpql).setParameter("login","user3").executeUpdate();

        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    @BeforeEach
    public void limparDados() {
        deleteUsers();
    }

    // Teste de integração que verifica se dois usuários inseridos
    // no banco de dados podem ser retornados através de uma busca ao mesmo
    @Test
    public void testCriarUsuario(){
        UsuarioService usuarioService = new UsuarioService();

        Usuario usuario1 = new Usuario();
        usuario1.setLogin("user2");
        usuario1.setSenha("123456");
        usuario1.setAvatar("AvatarPadrao");
        usuario1.setQuantidadeSimulacoesBemSucedidas(10);
        usuario1.setQuantidadeSimulacoes(15);
        usuario1.setMediaSimulacoesBemSucedidas(0.66F);

        Usuario usuario2 = new Usuario();
        usuario2.setLogin("user3");
        usuario2.setSenha("654321");
        usuario2.setAvatar("Avatar");
        usuario2.setQuantidadeSimulacoesBemSucedidas(10);
        usuario2.setQuantidadeSimulacoes(30);
        usuario2.setMediaSimulacoesBemSucedidas(0.33F);

        usuarioService.salvarUsuario(usuario1);

        List<Usuario> usuariosSalvos = usuarioService.buscarTodos();

        assertThat(usuariosSalvos).contains(usuario1);

        usuarioService.salvarUsuario(usuario2);

        List<Usuario> usuariosSalvos2 = usuarioService.buscarTodos();

        assertThat(usuariosSalvos2).contains(usuario1,usuario2);
    }

    // Teste que verifica a presença de um usuário através do seu nome de login
    @Test
    public void testBuscarUsuarioPorLogin(){
        UsuarioService usuarioService = new UsuarioService();

        Usuario usuario1 = new Usuario();
        usuario1.setLogin("user2");
        usuario1.setSenha("123456");
        usuario1.setAvatar("AvatarPadrao");
        usuario1.setQuantidadeSimulacoesBemSucedidas(10);
        usuario1.setQuantidadeSimulacoes(15);
        usuario1.setMediaSimulacoesBemSucedidas(0.66F);

        usuarioService.salvarUsuario(usuario1);

        Usuario usuarioSalvo = usuarioService.buscarPorLogin(usuario1.getLogin());

        assertThat(usuarioSalvo).isEqualTo(usuario1);
    }

    // Teste que verifica a presença de um usuário através do seu número de id
    @Test
    public void testBuscarUsuarioPorId(){
        UsuarioService usuarioService = new UsuarioService();

        Usuario usuario1 = new Usuario();
        usuario1.setLogin("user2");
        usuario1.setSenha("123456");
        usuario1.setAvatar("AvatarPadrao");
        usuario1.setQuantidadeSimulacoesBemSucedidas(10);
        usuario1.setQuantidadeSimulacoes(15);
        usuario1.setMediaSimulacoesBemSucedidas(0.66F);

        Long id = usuarioService.salvarUsuario(usuario1).getId();

        Usuario usuarioSalvo = usuarioService.buscarPorId(id);

        assertThat(usuarioSalvo).isEqualTo(usuario1);
    }
}
