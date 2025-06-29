import org.example.simulationV1.simulation.ProcessamentoCriaturas;
import org.example.simulationV1.simulation.RespostaProcessamento;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TesteMockito {

    @Test
    public void testProcessamentoComQuantidadeInvalida_DeveRetornarStatusZero() {
        // Cenário com apenas 1 criatura (inválido)
        int quantidadeCriaturas = 1;
        int tempoExecucao = 5;

        RespostaProcessamento resposta = ProcessamentoCriaturas.processamento(quantidadeCriaturas, tempoExecucao);

        assertEquals(0, resposta.getStatus());
        assertNull(resposta.getCriaturas());
    }

    @Test
    public void testProcessamentoComQuantidadeMuitoAlta_DeveRetornarStatusZero() {
        int quantidadeCriaturas = 500; // acima do limite
        int tempoExecucao = 5;

        RespostaProcessamento resposta = ProcessamentoCriaturas.processamento(quantidadeCriaturas, tempoExecucao);

        assertEquals(0, resposta.getStatus());
        assertNull(resposta.getCriaturas());
    }

    @Test
    public void testProcessamentoComQuantidadeValida_DeveRetornarRespostaComCriaturas() {
        int quantidadeCriaturas = 10;
        int tempoExecucao = 1;

        // Você pode mockar SDL aqui se extrair essas dependências para uma interface
        RespostaProcessamento resposta = ProcessamentoCriaturas.processamento(quantidadeCriaturas, tempoExecucao);

        assertTrue(resposta.getStatus() > 0);
        assertNotNull(resposta.getCriaturas());
        assertEquals(quantidadeCriaturas + 1, resposta.getCriaturas().length); // +1 por causa do guardião
    }
}

