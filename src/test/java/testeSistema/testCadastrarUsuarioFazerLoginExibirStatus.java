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

public class testCadastrarUsuarioFazerLoginExibirStatus extends TesteSistemaBase{

    // Teste da jornada de usuário que engloba o cadastro de um novo usuário, a realização do login
    // com para esse mesmo usuário e a busca pelas estatísticas de todos os usuários, que nesse caso
    // retorna apenas o usuário que foi criado para o teste, cuja quantidade de simulações é igual
    // a zero
    @Test
    public void mainTest() {

        String inputStream = "2" + System.lineSeparator() +
                "user2" + System.lineSeparator() +
                "123" + System.lineSeparator() +
                "Avatar" + System.lineSeparator() +
                "1" + System.lineSeparator() +
                "user2" + System.lineSeparator() +
                "123" + System.lineSeparator() +
                "2" + System.lineSeparator() +
                "3" + System.lineSeparator() +
                "3" + System.lineSeparator();


        provideInput(inputStream);
        UsuarioInterface usuarioInterface = new UsuarioInterface(new Scanner(System.in));

        usuarioInterface.iniciar();

        String saida = outputStream.toString();
        System.out.println(saida);
        assertTrue(saida.contains("Fazer cadastro de usuario"));
        assertTrue(saida.contains("Informe o login do usuario:"));
        assertTrue(saida.contains("Login realizado com sucesso!"));
        assertTrue(saida.contains("Usuario user2 :\n" + " avatar = 'Avatar'\n" + " quantidade de simulações = 0\n"));
    }
}
