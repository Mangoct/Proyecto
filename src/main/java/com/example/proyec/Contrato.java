package com.example.proyec;


import java.util.ArrayList;
import java.util.List;

public class Contrato {

    private int numeroContrato;


    public Contrato(int numeroContrato) {
        this.numeroContrato = numeroContrato;

    }


    public int getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(int numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    @Override
    public String toString() {
        return "Contrato{" +
                "numeroContrato=" + numeroContrato +
                '}';
    }
}



