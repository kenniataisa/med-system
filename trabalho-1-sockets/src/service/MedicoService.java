package service;

import model.Medico;
import model.Resposta;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MedicoService {
    private final EspecialidadeService especialidadeService;

    public MedicoService() {
        this.especialidadeService = new EspecialidadeService();
    }

    public List<Medico> listarMedicos() {
    List<Medico> lista = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader("medicos.txt"))) {
        String linha;
        while ((linha = br.readLine()) != null) {
            String[] partes = linha.split(",");
            int id = Integer.parseInt(partes[0].trim());
            String nome = partes[1].trim();
            String especialidade = partes[2].trim();
            lista.add(new Medico(id, nome, especialidade));
        }
    } catch (IOException e) {
        System.out.println("Erro ao ler medicos.txt: " + e.getMessage());
    }
    return lista;
}

    public Medico[] listarMedicosComoArray() {
        List<Medico> medicos = listarMedicos();
        return medicos.toArray(new Medico[0]);
    }

    public synchronized Resposta processarEnvio(Medico[] medicosRecebidos) {
        int quantidade = medicosRecebidos == null ? 0 : medicosRecebidos.length;
        String mensagem = "Servidor recebeu " + quantidade + " medico(s) com sucesso.";
        String especialidades = especialidadeService.listarEspecialidadesEmTexto();
        return new Resposta(true, mensagem, quantidade, especialidades);
    }
}