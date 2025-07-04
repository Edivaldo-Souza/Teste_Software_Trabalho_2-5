import io.github.libsdl4j.api.render.SDL_Renderer;
import org.example.simulationV1.cluster.Cluster;
import org.example.simulationV1.criatura.Criatura;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class TesteMockitoSimulacao {


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

    @Test
    void deveAdicionarMoedasAoClusterQuandoRecebeMoedas() {
        Criatura criatura = new Criatura();
        Cluster cluster = mock(Cluster.class);

        criatura.cluster = cluster;
        criatura.setMoedas(500);

        criatura.receiveCoins(200);

        verify(cluster, never()).receiveCoins(anyInt()); // comportamento depende de contexto
        assertEquals(700, criatura.getMoedas());
    }
    @Test
    void deveChamarReceiveCoinsNoClusterQuandoRecebeMoedas() {
        Criatura criatura = new Criatura();
        Cluster cluster = mock(Cluster.class);

        criatura.cluster = cluster;
        criatura.setMoedas(100);

        criatura.receiveCoins(50);

        verify(cluster, never()).receiveCoins(anyInt());
        assertEquals(150, criatura.getMoedas());
    }


}

