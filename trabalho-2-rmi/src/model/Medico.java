package model;

// "Além dos campos herdados, Medico tem crm e especialidade.
// Primeira agregação: Medico tem uma Especialidade."
public class Medico extends Funcionario {
    private String crm;
    private Especialidade especialidade;  

    public Medico() {}

    public Medico(int id, String nome, String cpf, String crm, Especialidade especialidade) {
        super(id, nome, cpf);
        this.crm = crm;
        this.especialidade = especialidade;
    }

    @Override
    public String getCargo() { return "Medico"; }

    public String       getCrm()          { return crm; }
    public Especialidade getEspecialidade() { return especialidade; }

    public void setCrm(String crm)                   { this.crm = crm; }
    public void setEspecialidade(Especialidade esp)  { this.especialidade = esp; }

    @Override
    public String toString() {
        return "Medico{id=" + getId()
            + ", nome='"  + getNome()  + "'"
            + ", crm='"   + crm        + "'"
            + ", especialidade=" + (especialidade != null ? especialidade.getNome() : "N/A")
            + "}";
    }
}
