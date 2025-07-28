package testeIntegracao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.dao.UsuarioService;
import org.example.entities.Usuario;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioServiceIntegrationTest {

    private static EntityManagerFactory emf;
    private UsuarioService usuarioService;

    @BeforeAll
    static void setUpBeforeAll() {
        // Configura a EntityManagerFactory uma vez para todos os testes
        emf = Persistence.createEntityManagerFactory("meuPU_H2");
    }

    @BeforeEach
    void setUp() {
        // Cria um novo UsuarioService com um EntityManager fresco para cada teste
        usuarioService = new UsuarioService();
    }

    @AfterEach
    void tearDown() {
        // Limpa o banco de dados após cada teste
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Usuario").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    @AfterAll
    static void tearDownAfterAll() {
        // Fecha a EntityManagerFactory no final de todos os testes
        if (emf != null) {
            emf.close();
        }
    }

    // --- TESTES DE INTEGRAÇÃO ---

    @Test
    void deveSalvarUsuarioERecuperarPorId() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setLogin("teste");
        usuario.setSenha("123");

        // Persiste o usuário (usando um UsuarioService dedicado)
        UsuarioService serviceParaSalvar = new UsuarioService();
        Usuario usuarioSalvo = serviceParaSalvar.salvarUsuario(usuario);
        Long idSalvo = usuarioSalvo.getId(); // Guarda o ID para busca posterior

        // Recupera o usuário (usando um NOVO UsuarioService)
        UsuarioService serviceParaBuscar = new UsuarioService();
        Usuario usuarioRecuperado = serviceParaBuscar.buscarPorId(idSalvo);

        // Assert
        assertNotNull(usuarioRecuperado);
        assertEquals("teste", usuarioRecuperado.getLogin());
    }

    @Test
    void deveRetornarNullAoBuscarPorLoginInexistente() {
        // Act
        Usuario usuario = usuarioService.buscarPorLogin("nao_existe");

        // Assert
        assertNull(usuario);
    }

    @Test
    void deveBuscarTodosUsuarios() {
        // Cria um UsuarioService específico para este teste
        UsuarioService serviceParaTeste = new UsuarioService();

        // Cria e persiste os usuários em uma única operação
        Usuario u1 = new Usuario();
        u1.setLogin("user1");
        u1.setSenha("123");

        Usuario u2 = new Usuario();
        u2.setLogin("user2");
        u2.setSenha("456");

        // Persiste ambos os usuários em uma única transação
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(u1);
        em.persist(u2);
        em.getTransaction().commit();
        em.close();

        // Act: Busca todos (usando um novo UsuarioService)
        UsuarioService serviceParaBusca = new UsuarioService();
        List<Usuario> usuarios = serviceParaBusca.buscarTodos();

        // Assert
        assertEquals(2, usuarios.size());
        assertTrue(usuarios.stream().anyMatch(u -> u.getLogin().equals("user1")));
        assertTrue(usuarios.stream().anyMatch(u -> u.getLogin().equals("user2")));

        // Fecha os recursos do serviceParaBusca (implícito, pois o método buscarTodos() já fecha)
    }

    @Test
    void deveAtualizarUsuarioComSimulacao() {
        // Configuração inicial
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU_H2");
        EntityManager em = emf.createEntityManager();

        try {
            // Persistência inicial do usuário
            em.getTransaction().begin();
            Usuario usuario = new Usuario();
            usuario.setLogin("simulador");
            usuario.setSenha("123");
            usuario.setQuantidadeSimulacoes(0);
            usuario.setQuantidadeSimulacoesBemSucedidas(0);
            em.persist(usuario);
            em.getTransaction().commit();

            // Atualização dos dados
            em.getTransaction().begin();
            Usuario usuarioAtualizado = em.find(Usuario.class, usuario.getId());
            usuarioAtualizado.setQuantidadeSimulacoes(1);
            usuarioAtualizado.setQuantidadeSimulacoesBemSucedidas(1);
            usuarioAtualizado.setMediaSimulacoesBemSucedidas(1.0f);
            em.getTransaction().commit();

            // Verificação
            em.clear(); // Limpa o cache para forçar busca do banco
            Usuario usuarioVerificado = em.find(Usuario.class, usuario.getId());

            assertEquals(1, usuarioVerificado.getQuantidadeSimulacoes());
            assertEquals(1.0f, usuarioVerificado.getMediaSimulacoesBemSucedidas());
        } finally {
            em.close();
            emf.close();
        }
    }
}
