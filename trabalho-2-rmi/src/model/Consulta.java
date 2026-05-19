package model;

// Consulta tem medico, paciente, data e status.
// Temos mais duas agregações: Consulta tem um Medico e tem um Paciente.
// O status já começa como AGENDADA no construtor."

public class Consulta {

    private int     id;
    private Medico  medico;    
    private Paciente paciente; 
    private String  data;      
    private String  status;    

    public Consulta() {}

    public Consulta(int id, Medico medico, Paciente paciente, String data) {
        this.id       = id;
        this.medico   = medico;
        this.paciente = paciente;
        this.data     = data;
        this.status   = "AGENDADA";
    }

    public int     getId()       { return id; }
    public Medico  getMedico()   { return medico; }
    public Paciente getPaciente() { return paciente; }
    public String  getData()     { return data; }
    public String  getStatus()   { return status; }

    public void setId(int id)              { this.id = id; }
    public void setMedico(Medico m)        { this.medico = m; }
    public void setPaciente(Paciente p)    { this.paciente = p; }
    public void setData(String data)       { this.data = data; }
    public void setStatus(String status)   { this.status = status; }

    @Override
    public String toString() {
        return "Consulta{id=" + id
            + ", medico="   + (medico   != null ? medico.getNome()   : "N/A")
            + ", paciente=" + (paciente != null ? paciente.getNome() : "N/A")
            + ", data='"    + data + "'"
            + ", status='"  + status + "'"
            + "}";
    }
}
