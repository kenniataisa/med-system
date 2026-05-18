package model;

public class Paciente {

    private int    id;
    private String nome;
    private String cpf;
    private int    idade;

    public Paciente() {}

    public Paciente(int id, String nome, String cpf, int idade) {
        this.id    = id;
        this.nome  = nome;
        this.cpf   = cpf;
        this.idade = idade;
    }

    public int    getId()    { return id; }
    public String getNome()  { return nome; }
    public String getCpf()   { return cpf; }
    public int    getIdade() { return idade; }

    public void setId(int id)         { this.id = id; }
    public void setNome(String nome)  { this.nome = nome; }
    public void setCpf(String cpf)    { this.cpf = cpf; }
    public void setIdade(int idade)   { this.idade = idade; }

    @Override
    public String toString() {
        return "Paciente{id=" + id + ", nome='" + nome + "', idade=" + idade + "}";
    }
}
