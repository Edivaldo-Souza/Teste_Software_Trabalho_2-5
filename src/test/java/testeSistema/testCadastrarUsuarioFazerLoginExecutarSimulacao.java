package testeSistema;

import org.example.usuarioInterface.UsuarioInterface;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class testCadastrarUsuarioFazerLoginExecutarSimulacao extends TesteSistemaBase{


    // Teste da jornada de usuário que engloba o cadastro de um novo usuário, a realização do login
    // com para esse mesmo usuário e a execução de uma simulação
    @Test
    public void cadastrarUsuarioFazerLoginExecutarSimulacao() {

        String inputStream = "2" + System.lineSeparator() +
                "user2" + System.lineSeparator() +
                "123" + System.lineSeparator() +
                "Avatar" + System.lineSeparator() +
                "1" + System.lineSeparator() +
                "user2" + System.lineSeparator() +
                "123" + System.lineSeparator() +
                "1" + System.lineSeparator() +
                "20" + System.lineSeparator() +
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

        fecharJanelaAutomatico();

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
