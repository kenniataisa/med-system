package service;

import model.Especialidade;

import java.util.ArrayList;
import java.util.List;

public class EspecialidadeService {
    private final List<Especialidade> especialidades;

    public EspecialidadeService() {
        especialidades = new ArrayList<>();
        especialidades.add(new Especialidade(1, "Cardiologia"));
        especialidades.add(new Especialidade(2, "Pediatria"));
    }

    public List<Especialidade> listarEspecialidades() {
        return new ArrayList<>(especialidades);
    }

    public String buscarNomePorId(int id) {
        for (Especialidade especialidade : listarEspecialidades()) {
            if (especialidade.getId() == id) {
                return especialidade.getNome();
            }
        }

        return "";
    }

    public String listarEspecialidadesEmTexto() {
        StringBuilder texto = new StringBuilder();
        for (Especialidade especialidade : listarEspecialidades()) {
            if (texto.length() > 0) {
                texto.append(", ");
            }
            texto.append(especialidade.getNome());
        }
        return texto.toString();
    }
}