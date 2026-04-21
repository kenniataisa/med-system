package model;

import java.io.Serializable;

public class Especialidade implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;

    public Especialidade() {
        this(0, "");
    }

    public Especialidade(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Especialidade{id=" + id + ", nome='" + nome + "'}";
    }
}
