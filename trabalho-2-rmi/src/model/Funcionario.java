package model;

public abstract class Funcionario {

    private int id;
    private String nome;
    private String cpf;

    public Funcionario() {}

    public Funcionario(int id, String nome, String cpf) {
        this.id   = id;
        this.nome = nome;
        this.cpf  = cpf;
    }

    public int getId()          { return id; }
    public String getNome()     { return nome; }
    public String getCpf()      { return cpf; }

    public void setId(int id)           { this.id = id; }
    public void setNome(String nome)    { this.nome = nome; }
    public void setCpf(String cpf)      { this.cpf = cpf; }

    
    public abstract String getCargo();

    @Override
    public String toString() {
        return getCargo() + "{id=" + id + ", nome='" + nome + "', cpf='" + cpf + "'}";
    }
}
