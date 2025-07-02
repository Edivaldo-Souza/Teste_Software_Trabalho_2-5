package org.example.simulationV1.criatura;


import io.github.libsdl4j.api.rect.SDL_Rect;
import org.example.simulationV1.cluster.Cluster;

import io.github.libsdl4j.api.render.SDL_Renderer;


import static io.github.libsdl4j.api.render.SdlRender.*;
import static org.example.simulationV1.simulation.ProcessamentoCriaturas.WINDOW_HEIGHT;
import static org.example.simulationV1.simulation.ProcessamentoCriaturas.WINDOW_WIDTH;


public class Criatura {
    public static final int CRIATURA_LARGURA = 50;
    public static final int CRIATURA_ALTURA = 50;
    public static final int MIN_SPACE_BTW_BOXES = 0;
    private SDL_Rect collisionBox;
    public boolean hasCollision;
    public boolean shouldMove;
    private float velX, velY;
    private float posX, posY;
    public byte r, g, b, a;
    private double xi;
    private double lastXi;
    private double random;
    private int moedas;
    public Cluster cluster = null;
    public boolean consumedByCluster;
    public boolean guardiao;

    public void setMoedas(int moedas) {
        this.moedas = moedas;
    }

    public Criatura() {

    }
    public Criatura(float posX, float posY, float velX, float velY, byte r, byte g, byte b, byte a, double random) {
        collisionBox = new SDL_Rect();
        collisionBox.h = CRIATURA_ALTURA;
        collisionBox.w = CRIATURA_LARGURA;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.hasCollision = false;
        this.moedas = 1_000_000;
        this.random = random;
        this.xi = this.random*this.moedas;
    }
    public void render(SDL_Renderer renderer){
        SDL_SetRenderDrawColor(renderer, r,g,b,a);
        if(!hasCollision || consumedByCluster){
            SDL_RenderFillRect(renderer, collisionBox);
        }
        else{
            SDL_RenderDrawRect(renderer,collisionBox);
        }
    }

    public void receiveCoins(int coins) {
        this.moedas += coins;
        this.lastXi = this.xi;
        this.xi += this.random+this.moedas;
    }

    public int giveCoins(){
        this.moedas /= 2;
        this.lastXi = this.xi;
        this.xi += this.random*this.moedas;
        return this.moedas;
    }

    private boolean noChao = false;
    private final float GRAVIDADE = 0.20f;       // menor gravidade = queda mais lenta
    private final float FORCA_PULO = -15.5f;      // mais força de pulo = salto mais alto
    private final float CHAO_Y = WINDOW_HEIGHT - CRIATURA_ALTURA; // piso

    public void move() {

        if(consumedByCluster) return;
        // Movimento horizontal constante
        posX += velX;
        collisionBox.x = (int) posX;

        // Rebate nas bordas laterais
        if (posX < 0 || posX + CRIATURA_LARGURA >= WINDOW_WIDTH) {
            velX = -velX;
            posX += velX;
            collisionBox.x = (int) posX;
        }

        // Gravidade
        velY += GRAVIDADE;
        posY += velY;

        // Colisão com o chão
        if (posY >= CHAO_Y) {
            posY = CHAO_Y;
            velY = FORCA_PULO; // faz a criatura "saltar" novamente
        }

        collisionBox.y = (int) posY;

        if(cluster!=null && cluster.getCriaturas()!=null && !cluster.getCriaturas().isEmpty()){
            for(int i = 0; i < cluster.getCriaturas().size(); i++){
                switch (i) {
                    case 0:
                        cluster.getCriaturas().get(i).setPosX(collisionBox.x+CRIATURA_LARGURA);
                        cluster.getCriaturas().get(i).setPosY(collisionBox.y);
                        break;
                    case 1:
                        cluster.getCriaturas().get(i).setPosX(collisionBox.x);
                        cluster.getCriaturas().get(i).setPosY(collisionBox.y+CRIATURA_ALTURA);
                        break;
                    case 2:
                        cluster.getCriaturas().get(i).setPosX(collisionBox.x-CRIATURA_LARGURA);
                        cluster.getCriaturas().get(i).setPosY(collisionBox.y);
                        break;
                    case 3:
                        cluster.getCriaturas().get(i).setPosX(collisionBox.x);
                        cluster.getCriaturas().get(i).setPosY(collisionBox.y-CRIATURA_ALTURA);
                        break;
                }
            }
        }

    }

    public boolean checkClusterColision(Criatura criaturaA, Criatura criaturaB) {
        if(criaturaA.getCluster()!=null && criaturaB.getCluster()==null){
            for(Criatura criaturaDoClusterA : criaturaA.getCluster().getCriaturas()){
                if(checkCollison(criaturaDoClusterA.getCollisionBox(),criaturaB.getCollisionBox(),0)){
                    return true;
                }
            }
        }
        else if(criaturaA.getCluster()==null && criaturaB.getCluster()!=null){
            for(Criatura criaturaDoClusterB : criaturaB.getCluster().getCriaturas()){
                if(checkCollison(criaturaDoClusterB.getCollisionBox(),criaturaA.getCollisionBox(),0)){
                    return true;
                }
            }
        }
        else if(criaturaA.getCluster()!=criaturaB.getCluster()){
            for(Criatura criaturaDoClusterA : criaturaA.getCluster().getCriaturas()){
                for(Criatura criaturaDoClusterB : criaturaB.getCluster().getCriaturas()){
                    if(checkCollison(criaturaDoClusterA.getCollisionBox(),criaturaDoClusterB.getCollisionBox(),0)){
                        return true;
                    }
                }
            }
        }
        return checkCollison(criaturaA.getCollisionBox(),criaturaB.getCollisionBox(),0);
    }

    public boolean checkCollison(SDL_Rect rectA, SDL_Rect rectB, int min_space_btw_boxes) {
        int leftA,leftB;
        int rightA,rightB;
        int topA,topB;
        int bottomA,bottomB;

        leftA = rectA.x;
        rightA = rectA.x + rectA.w;
        topA = rectA.y;
        bottomA = rectA.y + rectA.h;

        leftB = rectB.x;
        rightB = rectB.x + rectB.w;
        topB = rectB.y;
        bottomB = rectB.y + rectB.h;

        if(bottomA <= topB+min_space_btw_boxes){
            return false;
        }

        if(topA >= bottomB+min_space_btw_boxes){
            return false;
        }

        if(rightA <= leftB+min_space_btw_boxes){
            return false;
        }

        if(leftA >= rightB+min_space_btw_boxes){
            return false;
        }

        return true;
    }

    public SDL_Rect getCollisionBox(){
        return this.collisionBox;
    }

    public float getVelX() {
        return velX;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public float getVelY() {
        return velY;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
        this.collisionBox.y = (int) posY;
    }

    public void setPosX(float posX) {
        this.posX = posX;
        this.collisionBox.x = (int) posX;
    }

    public float getPosX(){
        return this.posX;
    }

    public float getPosY(){
        return this.posY;
    }

    public int getMoedas() {
        return moedas;
    }

    public double getLastXi(){
        return lastXi;
    }

    public double getXi(){
        return xi;
    }

    public double getRandom(){
        return random;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public boolean isShouldMove() {
        return shouldMove;
    }

}