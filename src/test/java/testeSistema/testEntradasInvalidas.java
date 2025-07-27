package testeSistema;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.usuarioInterface.UsuarioInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class testEntradasInvalidas {

    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;

    private ByteArrayOutputStream outputStream;

    private void deleteUser(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU_H2");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        String jpql = "DELETE FROM Usuario u WHERE u.login = :login";

        em.createQuery(jpql).setParameter("login","user2").executeUpdate();
        em.createQuery(jpql).setParameter("login","user3").executeUpdate();

        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        deleteUser();
    }

    @AfterEach
    public void tearDown() {
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
        deleteUser();
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    // Teste da inserção de uma entrada inválida no primeiro menu da aplicação,
    // inserindo um texto no lugar de um número dentre as opções válidas
    @Test
    public void testEntradasInvalidasNoPrimeiroMenu() {
        String inputStream = "fffff" + System.lineSeparator()
                + "3" + System.lineSeparator();

        provideInput(inputStream);
        UsuarioInterface usuarioInterface = new UsuarioInterface(new Scanner(System.in));

        usuarioInterface.iniciar();

        String saida = outputStream.toString();
        System.out.println(saida);
        assertTrue(saida.contains("Erro: Entrada inválida. Por favor, digite apenas um número de opção válida."));
    }

    // Teste da inserção de uma entrada padrão no primeiro menu, o que
    // ocasiona o sistema continuar aguardando por uma entrada válida
    @Test
    public void testEntradasPadraoNoPrimeiroMenu() {
        String inputStream = "4" + System.lineSeparator()
                + "3" + System.lineSeparator();

        provideInput(inputStream);
        UsuarioInterface usuarioInterface = new UsuarioInterface(new Scanner(System.in));

        usuarioInterface.iniciar();

        String saida = outputStream.toString();
        System.out.println(saida);
        assertTrue(saida.contains("Digite o número da operação: "));
    }

    // Teste da inserção de uma entrada padrão no segundo menu, o que
    // ocasiona o sistema continuar aguardando por uma entrada válida
    @Test
    public void testEntradasPadraoNoSegundoMenu() {
        String inputStream = "2" + System.lineSeparator() +
                "user2" + System.lineSeparator() +
                "123" + System.lineSeparator() +
                "Avatar" + System.lineSeparator() +
                "1" + System.lineSeparator() +
                "user2" + System.lineSeparator() +
                "123" + System.lineSeparator() +
                "4" + System.lineSeparator() +
                "3" + System.lineSeparator()+
                "3" + System.lineSeparator();

        provideInput(inputStream);
        UsuarioInterface usuarioInterface = new UsuarioInterface(new Scanner(System.in));

        usuarioInterface.iniciar();

        String saida = outputStream.toString();
        System.out.println(saida);
        assertTrue(saida.contains("Digite o número da operação: "));
        assertTrue(saida.contains("1 - Criar simulação"));
    }

    // Teste de tentativa de login no sistema quando o usuário não
    // existe no banco de dados
    @Test
    public void testLoginInvalido() {
        String inputStream =
                "1" + System.lineSeparator() +
                "user2" + System.lineSeparator() +
                "123" + System.lineSeparator() +
                "3" + System.lineSeparator();

        provideInput(inputStream);
        UsuarioInterface usuarioInterface = new UsuarioInterface(new Scanner(System.in));

        usuarioInterface.iniciar();

        String saida = outputStream.toString();
        System.out.println(saida);
        assertTrue(saida.contains("Digite o número da operação: "));
        assertTrue(saida.contains("Login falhou!"));
    }

    // Teste de tentativa de login no sistema quando o usuário
    // existe no banco de dados, mas a senha informada é incorreta
    @Test
    public void testLoginComUsuarioExistenteMasSenhaIncorreta() {
        String inputStream ="2" + System.lineSeparator() +
                "user2" + System.lineSeparator() +
                "123" + System.lineSeparator() +
                "Avatar" + System.lineSeparator() +
                "1" + System.lineSeparator() +
                "user2" + System.lineSeparator() +
                "1234" + System.lineSeparator() +
                "3" + System.lineSeparator();

        provideInput(inputStream);
        UsuarioInterface usuarioInterface = new UsuarioInterface(new Scanner(System.in));

        usuarioInterface.iniciar();

        String saida = outputStream.toString();
        System.out.println(saida);
        assertTrue(saida.contains("Digite o número da operação: "));
        assertTrue(saida.contains("Login falhou!"));
    }

    // Teste de uma execução da simulação que não obtem sucesso
    @Test
    public void testSimulacaoSemSucesso() {

        String inputStream = "2" + System.lineSeparator() +
                "user2" + System.lineSeparator() +
                "123" + System.lineSeparator() +
                "Avatar" + System.lineSeparator() +
                "1" + System.lineSeparator() +
                "user2" + System.lineSeparator() +
                "123" + System.lineSeparator() +
                "1" + System.lineSeparator() +
                "10" + System.lineSeparator() +
                "6" + System.lineSeparator() +
                System.lineSeparator()+
                "3" + System.lineSeparator() +
                "3" + System.lineSeparator();


        String usuarioCadastrado = "Usuario user2 :\n" +
                " avatar = 'Avatar'\n" +
                " quantidade de simulações = 0\n" +
                " média de simulações bem sucedidas = 0,0000\n" +
                " pontuação = 0";


        provideInput(inputStream);
        UsuarioInterface usuarioInterface = new UsuarioInterface(new Scanner(System.in));

        usuarioInterface.iniciar();

        String saida = outputStream.toString();
        System.out.println(saida);
        assertTrue(saida.contains("Fazer cadastro de usuario"));
        assertTrue(saida.contains("Informe o login do usuario:"));
        assertTrue(saida.contains(usuarioCadastrado));
        assertTrue(saida.contains("Login realizado com sucesso!"));
        assertTrue(saida.contains("Simulação executada!"));

    }

    // Teste que trata a possibilidade do usuário inserir uma informação
    // inválida a respeito dos dados do usuário, como por exemplo um nome
    // de usuário com menos de 4 caracteres
    @Test
    public void testEntradaInvalidaDuranteCriacaoDeUsuario(){
        String inputStream =
                "2" + System.lineSeparator() +
                " " + System.lineSeparator() +
                "user2" + System.lineSeparator() +
                "123" + System.lineSeparator() +
                "Avatar" + System.lineSeparator() +
                "3" + System.lineSeparator();

        provideInput(inputStream);
        UsuarioInterface usuarioInterface = new UsuarioInterface(new Scanner(System.in));

        usuarioInterface.iniciar();

        String saida = outputStream.toString();
        System.out.println(saida);
        assertTrue(saida.contains("Digite o número da operação: "));
        assertTrue(saida.contains("Essa informação precisa ter no mínimo 4 caracteres"));
    }

    // Teste que trata a tentativa de se criar um usuário com o mesmo nome
    // de outra que já tenha sido salvo no banco de dados
    @Test
    public void testEntradaInvalidaDuranteCriacaoDeUsuarioQueJaExiste(){
        String inputStream =
                "2" + System.lineSeparator() +
                "user2" + System.lineSeparator() +
                "123" + System.lineSeparator() +
                "Avatar" + System.lineSeparator() +
                "2" + System.lineSeparator() +
                "user2" + System.lineSeparator() +
                "user3" + System.lineSeparator() +
                "123" + System.lineSeparator() +
                "Avatar" + System.lineSeparator() +
                "3" + System.lineSeparator();

        provideInput(inputStream);
        UsuarioInterface usuarioInterface = new UsuarioInterface(new Scanner(System.in));

        usuarioInterface.iniciar();

        String saida = outputStream.toString();
        System.out.println(saida);
        assertTrue(saida.contains("Digite o número da operação: "));
        assertTrue(saida.contains("Já existe um usuário com o login = user2"));
    }
}
