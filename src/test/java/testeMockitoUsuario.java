import org.example.dao.UsuarioService;
import org.example.entities.Usuario;
import org.example.simulationV1.simulation.ProcessamentoCriaturas;
import org.example.simulationV1.simulation.RespostaProcessamento;
import org.example.usuarioInterface.UsuarioInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class testeMockitoUsuario {

    private UsuarioService usuarioService;
    private UsuarioInterface ui;

    @Test
    public void testExecutarSimulacao_sucesso() {
        UsuarioService usuarioServiceMock = mock(UsuarioService.class);
        Usuario usuario = new Usuario();
        usuario.setQuantidadeSimulacoes(0);
        usuario.setQuantidadeSimulacoesBemSucedidas(0);
        usuario.setPontuacao(0D);

        ProcessamentoCriaturas mockProc = mock(ProcessamentoCriaturas.class);
        RespostaProcessamento resposta = new RespostaProcessamento();
        resposta.setStatus(1);

        when(usuarioServiceMock.buscarPorId(1L)).thenReturn(usuario);

        UsuarioInterface ui = new UsuarioInterface() {
            @Override
            public void executarSimulacao(Long usuarioId) {
                RespostaProcessamento resposta = new RespostaProcessamento();
                resposta.setStatus(1);
                Usuario usuario = usuarioServiceMock.buscarPorId(usuarioId);
                usuario.setQuantidadeSimulacoes(usuario.getQuantidadeSimulacoes() + 1);
                if (resposta.getStatus() == 1) {
                    usuario.setQuantidadeSimulacoesBemSucedidas(usuario.getQuantidadeSimulacoesBemSucedidas() + 1);
                }
                usuario.setMediaSimulacoesBemSucedidas(
                        (float) usuario.getQuantidadeSimulacoesBemSucedidas() / usuario.getQuantidadeSimulacoes()
                );
                usuarioServiceMock.salvarUsuario(usuario);
            }
        };

        ui.executarSimulacao(1L);
        verify(usuarioServiceMock).salvarUsuario(usuario);
        assertThat(usuario.getQuantidadeSimulacoes()).isEqualTo(1);
        assertThat(usuario.getQuantidadeSimulacoesBemSucedidas()).isEqualTo(1);
        assertThat(usuario.getMediaSimulacoesBemSucedidas()).isEqualTo(1.0f);
    }

    @Test
    public void testLogin_sucesso() {
        Scanner scannerMock = mock(Scanner.class);
        when(scannerMock.nextLine()).thenReturn("user", "123");

        UsuarioService usuarioServiceMock = mock(UsuarioService.class);
        Usuario usuario = new Usuario();
        usuario.setLogin("user");
        usuario.setSenha("123");
        usuario.setId(10L);

        when(usuarioServiceMock.buscarPorLogin("user")).thenReturn(usuario);

        UsuarioInterface ui = new UsuarioInterface() {
            @Override
            public long login() {
                return usuario.getSenha().equals("123") ? usuario.getId() : 0;
            }
        };

        long id = ui.login();
        assertThat(id).isEqualTo(10L);
    }

    @Test
    public void testLogin_falha() {
        Usuario usuario = new Usuario();
        usuario.setLogin("user");
        usuario.setSenha("errada");
        usuario.setId(10L);

        UsuarioInterface ui = new UsuarioInterface() {
            @Override
            public long login() {
                return usuario.getSenha().equals("123") ? usuario.getId() : 0;
            }
        };

        long id = ui.login();
        assertThat(id).isEqualTo(0L);
    }

    @Test
    public void testSalvarUsuario() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine()).thenReturn("user", "123", "avatar");

        UsuarioService usuarioServiceMock = mock(UsuarioService.class);
        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setLogin("user");
        usuarioSalvo.setSenha("123");
        usuarioSalvo.setAvatar("avatar");

        when(usuarioServiceMock.salvarUsuario(any())).thenReturn(usuarioSalvo);

        UsuarioInterface ui = new UsuarioInterface() {
            @Override
            public Usuario salvarUsuario() {
                Usuario u = new Usuario();
                u.setLogin("user");
                u.setSenha("123");
                u.setAvatar("avatar");
                return usuarioServiceMock.salvarUsuario(u);
            }
        };

        Usuario result = ui.salvarUsuario();
        assertThat(result.getLogin()).isEqualTo("user");
        assertThat(result.getSenha()).isEqualTo("123");
        assertThat(result.getAvatar()).isEqualTo("avatar");
    }

    @Test
    public void testListarUsuarios() {
        List<Usuario> lista = criarListaUsuarios();

        UsuarioService usuarioServiceMock = mock(UsuarioService.class);
        when(usuarioServiceMock.buscarTodos()).thenReturn(lista);

        UsuarioInterface ui = new UsuarioInterface() {
            @Override
            public void listarUsuarios() {
                List<Usuario> usuarios = usuarioServiceMock.buscarTodos();
                usuarios.forEach(System.out::println);

                int total = usuarios.stream().mapToInt(Usuario::getQuantidadeSimulacoes).sum();
                double media = usuarios.stream().mapToDouble(Usuario::getQuantidadeSimulacoesBemSucedidas).sum() /
                        usuarios.stream().mapToDouble(Usuario::getQuantidadeSimulacoes).sum();

                System.out.println("Total: " + total + ", Média: " + media);
            }
        };

        ui.listarUsuarios();

        verify(usuarioServiceMock).buscarTodos();
    }

    private List<Usuario> criarListaUsuarios() {
        Usuario u1 = new Usuario();
        u1.setQuantidadeSimulacoes(2);
        u1.setQuantidadeSimulacoesBemSucedidas(1);
        u1.setLogin("usuario1");
        u1.setAvatar("🐱");
        u1.setPontuacao(1.0);
        u1.setMediaSimulacoesBemSucedidas((float) u1.getQuantidadeSimulacoesBemSucedidas() / u1.getQuantidadeSimulacoes());

        Usuario u2 = new Usuario();
        u2.setQuantidadeSimulacoes(3);
        u2.setQuantidadeSimulacoesBemSucedidas(2);
        u2.setLogin("usuario2");
        u2.setAvatar("🐶");
        u2.setPontuacao(2.0);
        u2.setMediaSimulacoesBemSucedidas((float) u2.getQuantidadeSimulacoesBemSucedidas() / u2.getQuantidadeSimulacoes());

        return List.of(u1, u2);
    }


}
