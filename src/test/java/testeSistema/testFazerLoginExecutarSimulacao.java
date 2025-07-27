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


public class testFazerLoginExecutarSimulacao {

    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;

    private ByteArrayOutputStream outputStream;

    private void deleteUser(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU_H2");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        String jpql = "DELETE FROM Usuario u WHERE u.login = :login";

        int query = em.createQuery(jpql).setParameter("login","user2").executeUpdate();

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

    // Teste da jornada de usuário que engloba o cadastro de um novo usuário, a realização do login
    // com para esse mesmo usuário e a execução de uma simulação
    @Test
    public void mainTest() {

        String inputStream = "2" + System.lineSeparator() +
                "user2" + System.lineSeparator() +
                "123" + System.lineSeparator() +
                "Avatar" + System.lineSeparator() +
                "1" + System.lineSeparator() +
                "user2" + System.lineSeparator() +
                "123" + System.lineSeparator() +
                "1" + System.lineSeparator() +
                "10" + System.lineSeparator() +
                "60" + System.lineSeparator() +
                System.lineSeparator() +
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


}
