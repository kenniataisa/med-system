package model;

public class Especialidade {

    private int id;
    private String nome;
    private String descricao;

    public Especialidade() {}

    public Especialidade(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public int    getId()         { return id; }
    public String getNome()       { return nome; }
    public String getDescricao()  { return descricao; }

    public void setId(int id)                { this.id = id; }
    public void setNome(String nome)         { this.nome = nome; }
    public void setDescricao(String desc)    { this.descricao = desc; }

    @Override
    public String toString() {
        return "Especialidade{id=" + id + ", nome='" + nome + "'}";
    }
}
