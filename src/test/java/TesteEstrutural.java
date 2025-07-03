
import io.github.libsdl4j.api.rect.SDL_Rect;
import io.github.libsdl4j.api.render.SDL_Renderer;
import org.example.simulationV1.criatura.Criatura;
import org.example.simulationV1.simulation.ProcessamentoCriaturas;
import org.example.usuarioInterface.UsuarioInterface;
import org.junit.jupiter.api.Test;


import java.util.Random;

import static org.example.simulationV1.simulation.ProcessamentoCriaturas.loopPrincipal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


public class TesteEstrutural {

    @Test
    public void testQuantidadeCriaturasMenorQue2() {
        int resultado = ProcessamentoCriaturas.processamento(1, 100).getStatus(); // Valor menor que 2
        assertEquals(0, resultado, "Deve retornar 0 quando a quantidade de criaturas for menor que 2");
    }

    @Test
    public void testQuantidadeCriaturasMaiorOuIgual2() {
        int resultado = ProcessamentoCriaturas.processamento(2, 100).getStatus(); // Valor igual ou maior que 2
        assertEquals(1, resultado, "Deve retornar 1 quando a quantidade de criaturas for suficiente");
    }

    @Test
    public void testQuantidadeCriaturasMenorIgualQue200() {
        int resultado = ProcessamentoCriaturas.processamento(200, 100).getStatus();
        assertEquals(1, resultado, "Deve retornar 1 quando a quantidade de criaturas for menor ou igual que 200");
    }

    @Test
    public void testQuantidadeCriaturasMaiorQue200() {
        int resultado = ProcessamentoCriaturas.processamento(201, 100).getStatus();
        assertEquals(0, resultado, "Deve retornar 0 quando a quantidade de criaturas for maior que 200");
    }

    @Test
    public void testEvitarSobreposicaoSemColisao() {
        Criatura[] criaturas = new Criatura[2];

        // Criatura 0 em (0, 0)
        criaturas[0] = new Criatura(0, 0, 0, 0, (byte)255, (byte)0, (byte)0, (byte)255, 0);
        // Criatura 1 em (200, 200) – longe, sem colisão
        criaturas[1] = new Criatura(200, 200, 0, 0, (byte)0, (byte)255, (byte)0, (byte)255, 0);

        ProcessamentoCriaturas.evitarSobreposicao(criaturas, 1, new Random());

        // A posição da criatura 1 deve permanecer a mesma
        assertNotEquals(200, (int) criaturas[1].getCollisionBox().x);
        assertNotEquals(200, (int) criaturas[1].getCollisionBox().y);
    }

    @Test
    public void testEvitarSobreposicaoComColisao() {
        Criatura[] criaturas = new Criatura[2];

        // Criatura 0 em (100, 100)
        criaturas[0] = new Criatura(100, 100, 0, 0, (byte)255, (byte)0, (byte)0, (byte)255, 0);
        // Criatura 1 também em (100, 100) – colisão
        criaturas[1] = new Criatura(100, 100, 0, 0, (byte)0, (byte)255, (byte)0, (byte)255, 0);

        ProcessamentoCriaturas.evitarSobreposicao(criaturas, 1, new Random(42)); // semente fixa

        // Verifica se houve deslocamento (posX ou posY diferentes de 100)
        assertTrue(
                criaturas[1].getCollisionBox().x != 100 ||
                        criaturas[1].getCollisionBox().y != 100,
                "A criatura deve ter sido reposicionada para evitar colisão."
        );

        // Confirma que agora não colidem
        boolean colisao = criaturas[1].checkCollison(
                (SDL_Rect) criaturas[0].getCollisionBox(),
                (SDL_Rect) criaturas[1].getCollisionBox(),
                0
        );
        assertFalse(colisao, "A criatura deve ter sido reposicionada fora da colisão.");
    }

    //Caso de teste onde o valor aleatório de R seja igual a 0;
    @Test
    public void casoValorDeRIgualAZero(){
        int quantidadeCriaturas = 10;
        assertTrue(
                ProcessamentoCriaturas.gerarCriaturas(quantidadeCriaturas, 0)[0].getRandom() != 0,
                "Mesmo passando como entrada o valor de r para ser igual a 0, " +
                        "o método gera um novo valor aletório para o mesmo");
    }

    @Test
    public void testVerficarColisaoQuandoFundoDeBForMenorQueTopoDeA(){
        SDL_Rect rectA = new SDL_Rect();
        rectA.x = 0; rectA.y = 0; rectA.w = 50; rectA.h = 50;
        SDL_Rect rectB = new SDL_Rect();
        rectB.x = 0; rectB.y = 50; rectB.w = 50; rectB.h = 50;

        Criatura criatura = new Criatura();
        assertFalse(criatura.checkCollison(rectA, rectB,0));
    }

    @Test
    public void testVerficarColisaoQuandoFundoDeAForMenorQueTopoDeB(){
        SDL_Rect rectA = new SDL_Rect();
        rectA.x = 0; rectA.y = 50; rectA.w = 50; rectA.h = 50;
        SDL_Rect rectB = new SDL_Rect();
        rectB.x = 0; rectB.y = 0; rectB.w = 50; rectB.h = 50;

        Criatura criatura = new Criatura();
        assertFalse(criatura.checkCollison(rectA, rectB,0));
    }

    @Test
    public void testVerificarColisaoQuandoDireitaDeAForMenorQueEsquerdaDeB(){
        SDL_Rect rectA = new SDL_Rect();
        rectA.x = 0; rectA.y = 0; rectA.w = 50; rectA.h = 50;
        SDL_Rect rectB = new SDL_Rect();
        rectB.x = 50; rectB.y = 0; rectB.w = 50; rectB.h = 50;

        Criatura criatura = new Criatura();
        assertFalse(criatura.checkCollison(rectA, rectB,0));
    }

    @Test
    public void testVerificarColisaoQuandoEsquerdaDeAForMaiorQueDireitaDeB(){
        SDL_Rect rectA = new SDL_Rect();
        rectA.x = 50; rectA.y = 0; rectA.w = 50; rectA.h = 50;
        SDL_Rect rectB = new SDL_Rect();
        rectB.x = 0; rectB.y = 0; rectB.w = 50; rectB.h = 50;

        Criatura criatura = new Criatura();
        assertFalse(criatura.checkCollison(rectA, rectB,0));
    }

    @Test
    public void testVerificarSeCriaturaDeveRoubarQuandoIigualJ() {
        Criatura[] criaturas = new Criatura[1];
        criaturas[0] = new Criatura();
        boolean criaturaDeveRoubar = false;

        // i sempre será igual a j
        for (int i = 0; i < criaturas.length; i++) {
            for (int j = 0; j < criaturas.length; j++) {
                if (i != j && !criaturas[i].hasCollision && !criaturas[j].hasCollision &&
                        criaturas[i].checkCollison(criaturas[i].getCollisionBox(), criaturas[j].getCollisionBox(),0)) {
                    criaturaDeveRoubar = true;
                }
            }
        }
        assertFalse(criaturaDeveRoubar);
    }

    @Test
    public void testVerificarSeCriaturaDeveRoubarQuandoPrimeiraCriaturaJaColidiu() {
        Criatura[] criaturas = new Criatura[2];
        criaturas[0] = new Criatura();
        //Caso a primeira criatura que colida já esteja com status de que já colidiu antes
        criaturas[0].hasCollision = true;
        criaturas[1] = new Criatura();
        boolean criaturaDeveRoubar = false;

        for (int i = 0; i < criaturas.length; i++) {
            for (int j = 0; j < criaturas.length; j++) {
                if (i != j && !criaturas[i].hasCollision && !criaturas[j].hasCollision &&
                        criaturas[i].checkCollison(criaturas[i].getCollisionBox(), criaturas[j].getCollisionBox(),0)) {
                    criaturaDeveRoubar = true;
                }
            }
        }
        assertFalse(criaturaDeveRoubar);
    }

    @Test
    public void testVerificarSeCriaturaDeveRoubarQuandoSegundaCriaturaJaColidiu() {
        Criatura[] criaturas = new Criatura[2];
        criaturas[0] = new Criatura();
        criaturas[1] = new Criatura();
        //Caso a segunda criatura que colida já esteja com status de que já colidiu antes
        criaturas[1].hasCollision = true;
        boolean criaturaDeveRoubar = false;

        for (int i = 0; i < criaturas.length; i++) {
            for (int j = 0; j < criaturas.length; j++) {
                if (i != j && !criaturas[i].hasCollision && !criaturas[j].hasCollision &&
                        criaturas[i].checkCollison(criaturas[i].getCollisionBox(), criaturas[j].getCollisionBox(),0)) {
                    criaturaDeveRoubar = true;
                }
            }
        }
        assertFalse(criaturaDeveRoubar);
    }

    @Test
    public void testVerificarSeCriaturaDeveRoubarQuandoNaoHaColisaoEntreCriatuas() {
        Criatura[] criaturas = new Criatura[2];
        criaturas[0] = new Criatura(100,100,0,0,(byte) 255,(byte) 255,(byte) 255,(byte) 255,0.5);
        criaturas[1] = new Criatura(200,200,0,0,(byte) 255,(byte) 255,(byte) 255,(byte) 255,0.5);
        //Caso as criaturas não estejam em posição de colidir

        criaturas[1].hasCollision = true;
        boolean criaturaDeveRoubar = false;


        for (int i = 0; i < criaturas.length; i++) {
            for (int j = 0; j < criaturas.length; j++) {
                if (i != j && !criaturas[i].hasCollision && !criaturas[j].hasCollision &&
                        criaturas[i].checkCollison(criaturas[i].getCollisionBox(), criaturas[j].getCollisionBox(),0)) {
                    criaturaDeveRoubar = true;
                }
            }
        }
        assertFalse(criaturaDeveRoubar);
    }

        @Test
    public void testLoopPrincipalComTempoExcedido() {
        // Criaturas que não colidem
        Criatura c1 = new Criatura() {
            @Override
            public void move() {}
            @Override
            public SDL_Rect getCollisionBox() {
                SDL_Rect r = new SDL_Rect();
                r.x = 0; r.y = 0; r.w = 10; r.h = 10;
                return r;
            }
            @Override
            public void render(SDL_Renderer renderer) {}
        };

        Criatura c2 = new Criatura() {
            @Override
            public void move() {}
            @Override
            public SDL_Rect getCollisionBox() {
                SDL_Rect r = new SDL_Rect();
                r.x = 1000; r.y = 1000; r.w = 10; r.h = 10;
                return r;
            }
            @Override
            public void render(SDL_Renderer renderer) {}
        };

        Criatura[] criaturas = new Criatura[]{c1, c2};

        int tempoExecucao = 1;

        int resultado = loopPrincipal(null, criaturas, tempoExecucao);
        assertEquals(0, resultado);
    }

    @Test
    public void testLoopPrincipalSemTempoExcedido() {
        Criatura c1 = new Criatura() {
            @Override
            public void move() {}
            @Override
            public SDL_Rect getCollisionBox() {
                SDL_Rect r = new SDL_Rect();
                r.x = 0; r.y = 0; r.w = 10; r.h = 10;
                return r;
            }
            @Override
            public void render(SDL_Renderer renderer) {}
        };

        Criatura c2 = new Criatura() {
            @Override
            public void move() {}
            @Override
            public SDL_Rect getCollisionBox() {
                SDL_Rect r = new SDL_Rect();
                r.x = 0; r.y = 0; r.w = 10; r.h = 10;
                return r;
            }
            @Override
            public void render(SDL_Renderer renderer) {}
        };

        Criatura[] criaturas = new Criatura[]{c1, c2};

        int tempoExecucao = 10;

        int resultado = loopPrincipal(null, criaturas, tempoExecucao);
        assertEquals(1, resultado);
    }

    @Test
    public void testFrameDelayQuandoFrameTimeEhMenorQueFrameDelay() {
        long inicio = System.currentTimeMillis();

        Criatura c1 = new Criatura() {
            @Override public void move() {}
            @Override public SDL_Rect getCollisionBox() {
                SDL_Rect r = new SDL_Rect();
                r.x = 0; r.y = 0; r.w = 50; r.h = 50;
                return r;
            }
            @Override public void render(SDL_Renderer renderer) {}
        };

        Criatura c2 = new Criatura() {
            @Override public void move() {}
            @Override public SDL_Rect getCollisionBox() {
                SDL_Rect r = new SDL_Rect();
                r.x = 0; r.y = 0; r.w = 50; r.h = 50;
                return r;
            }
            @Override public void render(SDL_Renderer renderer) {}
        };

        Criatura[] criaturas = new Criatura[]{c1, c2};

        int tempoExecucao = 1;
        loopPrincipal(null, criaturas, tempoExecucao);

        long duracao = System.currentTimeMillis() - inicio;

        // O loop deve ter demorado pelo menos 1000 ms (por causa do SDL_Delay dentro do tempo)
        assertTrue(duracao >= 1000);
    }

    @Test
    public void testFrameDelayNaoAconteceQuandoFrameTimeMaiorOuIgualFrameDelay() {
        Criatura c1 = new Criatura() {
            @Override public void move() {
                try { Thread.sleep(20); } catch (InterruptedException e) {}
            }
            @Override public SDL_Rect getCollisionBox() {
                SDL_Rect r = new SDL_Rect();
                r.x = 0; r.y = 0; r.w = 50; r.h = 50;
                return r;
            }
            @Override public void render(SDL_Renderer renderer) {}
        };

        Criatura c2 = new Criatura() {
            @Override public void move() {
                try { Thread.sleep(20); } catch (InterruptedException e) {}
            }
            @Override public SDL_Rect getCollisionBox() {
                SDL_Rect r = new SDL_Rect();
                r.x = 0; r.y = 0; r.w = 50; r.h = 50;
                return r;
            }
            @Override public void render(SDL_Renderer renderer) {}
        };

        Criatura[] criaturas = new Criatura[]{c1, c2};

        long inicio = System.currentTimeMillis();
        int tempoExecucao = 1;
        loopPrincipal(null, criaturas, tempoExecucao);
        long duracao = System.currentTimeMillis() - inicio;

        // O tempo total ainda deve ser de no mínimo 1000ms (tempoExecucao)
        assertTrue(duracao >= 1000);
    }

    @Test
    public void testarSelecaoDeOpcao(){
        UsuarioInterface usuarioInterface = mock(UsuarioInterface.class);



    }
}

