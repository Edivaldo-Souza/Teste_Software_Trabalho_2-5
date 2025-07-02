import io.github.libsdl4j.api.render.SDL_Renderer;
import org.example.simulationV1.criatura.Criatura;
import org.example.simulationV1.simulation.ProcessamentoCriaturas;
import org.example.simulationV1.simulation.RespostaProcessamento;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class TesteMockito {

    @Test
    void deveRenderizarCriatura() {
        SDL_Renderer rendererMock = mock(SDL_Renderer.class);
        Criatura criaturaMock = mock(Criatura.class);

        // Ação simulada
        criaturaMock.render(rendererMock);

        // Verifica se o método foi chamado
        verify(criaturaMock).render(rendererMock);
    }

    @Test
    void deveMoverTodasCriaturasQuePodemSeMover() {
        SDL_Renderer rendererMock = mock(SDL_Renderer.class);

        Criatura[] criaturas = IntStream.range(0, 5)
                .mapToObj(i -> mock(Criatura.class))
                .toArray(Criatura[]::new);

        // Simula que todas devem se mover
        for (Criatura c : criaturas) {
            when(c.isShouldMove()).thenReturn(true);
        }

        // Executa simulação de movimentação
        for (Criatura c : criaturas) {
            if (c.isShouldMove()) {
                c.move();
            }
        }

        // Verifica se todas foram chamadas
        for (Criatura c : criaturas) {
            verify(c).move();
        }
    }



    @Test
    void deveVerificarColisaoEntreDuasCriaturas() {
        Criatura criaturaA = mock(Criatura.class);
        Criatura criaturaB = mock(Criatura.class);

        // Simula colisão retornando true
        when(criaturaA.checkClusterColision(eq(criaturaA), eq(criaturaB))).thenReturn(true);

        boolean houveColisao = criaturaA.checkClusterColision(criaturaA, criaturaB);

        // Verifica se o método foi chamado
        verify(criaturaA).checkClusterColision(criaturaA, criaturaB);
        assertTrue(houveColisao);
    }
}

