package model;

public class Recepcionista extends Funcionario {
    private String setor;

    public Recepcionista() {}

    public Recepcionista(int id, String nome, String cpf, String setor) {
        super(id, nome, cpf);
        this.setor = setor;
    }

    @Override
    public String getCargo() {
        return "Recepcionista";
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    @Override
    public String toString() {
        return "Recepcionista{id=" + getId()
            + ", nome='" + getNome() + "'"
            + ", setor='" + setor + "'"
            + "}";
    }
}
