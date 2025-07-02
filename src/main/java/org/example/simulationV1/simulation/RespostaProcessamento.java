package org.example.simulationV1.simulation;


import org.example.simulationV1.criatura.Criatura;

public class RespostaProcessamento {
    private Criatura[] criaturas;
    private int status;

    public void setCriaturas(Criatura[] criaturas) {
        this.criaturas = criaturas;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
