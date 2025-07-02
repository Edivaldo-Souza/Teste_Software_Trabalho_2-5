
import org.example.simulationV1.criatura.Criatura;
import org.example.simulationV1.simulation.ProcessamentoCriaturas;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TesteDominio {

    @Test
    public void casoSimples(){
        assertThat(ProcessamentoCriaturas.processamento(5,60).getStatus()).isEqualTo(1);
    }

    @Test
    public void testReceberMoedasComValorValido() {
        Criatura criatura = new Criatura(0, 0, 1, 0, (byte)255, (byte)0, (byte)0, (byte)255, 0.5);
        int moedasAntes = criatura.getMoedas();
        criatura.receiveCoins(500);
        assertEquals(moedasAntes + 500, criatura.getMoedas());
        assertTrue(criatura.getXi() > criatura.getLastXi()); // Verifica o domínio da lógica de xi
    }

    @Test
    public void testPerderMoedasComValorValido() {
        Criatura criatura = new Criatura(0, 0, 1, 0, (byte)255, (byte)0, (byte)0, (byte)255, 0.5);
        int moedasAntes = criatura.getMoedas();
        criatura.giveCoins();
        assertEquals(moedasAntes - moedasAntes/2, criatura.getMoedas());
    }

}
