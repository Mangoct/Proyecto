package com.example.proyec;

import java.util.ArrayList;
import java.util.List;

public class Clientes {
    private List<Cliente> clientes;

    public Clientes() {
        // Inicializa la lista de clientes con algunos clientes
        clientes = new ArrayList<>();
        clientes.add(new Cliente("Juan"));
        clientes.add(new Cliente("María"));
        clientes.add(new Cliente("Pedro"));
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
}
