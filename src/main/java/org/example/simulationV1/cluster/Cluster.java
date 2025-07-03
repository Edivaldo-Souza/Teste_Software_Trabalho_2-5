package org.example.simulationV1.cluster;


import org.example.simulationV1.criatura.Criatura;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private List<Criatura> criaturas;
    private float velX, velY;
    private int moedasDoCluster;

    public Cluster() {
        criaturas = new ArrayList<>();
    }

    public void addCriatura(Criatura criatura){
        criaturas.add(criatura);
    }

    public int getTotalDeCriaturas() {
        return criaturas.size()+1;
    }

    public int getMoedasDoCluster() {
        return moedasDoCluster;
    }

    public void setMoedasDoCluster(int moedasDoCluster) {
        this.moedasDoCluster = moedasDoCluster;
    }

    public void receiveCoins(int quantidadeMoedas){
        this.moedasDoCluster += quantidadeMoedas;
    }

    public int giveCoins(boolean giveToGuardiao){
        if(!giveToGuardiao) {
            this.moedasDoCluster /= 2;
            return moedasDoCluster;
        }
        else{
            int quantidadeMoedas = moedasDoCluster;
            this.moedasDoCluster = 0;
            return quantidadeMoedas;
        }
    }

    public List<Criatura> getCriaturas() {
        return criaturas;
    }
}
