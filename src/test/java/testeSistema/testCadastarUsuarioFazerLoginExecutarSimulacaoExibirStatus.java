package testeSistema;

import io.github.libsdl4j.api.event.SDL_Event;
import io.github.libsdl4j.api.event.events.SDL_KeyboardEvent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.simulationV1.simulation.ProcessamentoCriaturas;
import org.example.usuarioInterface.UsuarioInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static io.github.libsdl4j.api.event.SDL_EventType.SDL_KEYDOWN;
import static io.github.libsdl4j.api.event.SdlEvents.SDL_PushEvent;
import static io.github.libsdl4j.api.event.SdlEventsConst.SDL_PRESSED;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_RETURN;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class testCadastarUsuarioFazerLoginExecutarSimulacaoExibirStatus extends TesteSistemaBase{


    // Teste da jornada de usuário que engloba o cadastro de um novo usuário, a realização do login
    // com para esse mesmo usuário, a execução de uma simulação e a busca pelas estatísticas de todos os usuários
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
                "2" + System.lineSeparator() +
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
        assertTrue(saida.contains("Usuario user2 :\n" + " avatar = 'Avatar'\n" + " quantidade de simulações = 1\n"));
    }
}
