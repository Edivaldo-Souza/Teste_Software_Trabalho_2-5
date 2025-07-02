
import io.github.libsdl4j.api.rect.SDL_Rect;
import org.example.simulationV1.criatura.Criatura;
import org.example.simulationV1.simulation.ProcessamentoCriaturas;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.example.simulationV1.criatura.Criatura.CRIATURA_LARGURA;
import static org.example.simulationV1.simulation.ProcessamentoCriaturas.WINDOW_WIDTH;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TesteFronteira {

    //Caso de teste onde a quantidade de criaturas excede o valor máximo ( n <= 200 criaturas)
    // O valor de N é redefinido para 200
    @Test
    public void casoMaisDe200Criaturas() {
        assertThat(ProcessamentoCriaturas.processamento(300,60).getStatus()).isEqualTo(0);
    }

    //Caso de teste onde a quantidade de criaturas é inferior ao valor mínimo ( n >= 2 criaturas)
    @Test
    public void casoMenosDeDuasCriaturas() {
        assertThat(ProcessamentoCriaturas.processamento(1,60).getStatus()).isEqualTo(0);
    }

    // Caso de teste onde a quantidade de criaturas é igual mínimo possível (2 criaturas)
    @Test
    public void casoApenasDuasCriaturas(){
        assertThat(ProcessamentoCriaturas.processamento(2,60).getStatus()).isEqualTo(1);
    }

    //Caso de teste onde o tempo de execução é inferior ao necessário para terminar uma simulação (1 segundo)
    @Test
    public void casoMenorTempoDeExecucao() {
        assertThat(ProcessamentoCriaturas.processamento(2, 1).getStatus()).isEqualTo(0);
    }


    @Test
    public void testMovimentoNaFronteiraDireita() {
        Criatura criatura = new Criatura(WINDOW_WIDTH - CRIATURA_LARGURA - 1, 0, 2, 0, (byte)255, (byte)0, (byte)0, (byte)255, 1.0);
        float velAntes = criatura.getVelX();
        criatura.move();
        assertTrue(criatura.getVelX() == -velAntes); // Deve inverter a direção ao bater na borda
    }

    @Test
    public void testColisaoNoLimite() {
        SDL_Rect a = new SDL_Rect();
        a.x = 0; a.y = 0; a.w = 50; a.h = 50;

        SDL_Rect b = new SDL_Rect();
        b.x = 50; b.y = 0; b.w = 50; b.h = 50;

        Criatura criatura = new Criatura(0, 0, 1, 0, (byte)255, (byte)255, (byte)255, (byte)255, 1.0);
        boolean colidiu = criatura.checkCollison(a, b,0);
        assertFalse(colidiu); // Fronteira exata: sem sobreposição
    }

}
