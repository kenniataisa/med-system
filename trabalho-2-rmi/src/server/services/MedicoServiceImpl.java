package server.services;

import common.Marshaller;
import common.Message;
import model.Consulta;
import model.Especialidade;
import model.Medico;
import model.Paciente;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MedicoServiceImpl {

    
    private final Map<Integer, Medico>   medicos   = new ConcurrentHashMap<>();
    private final Map<Integer, Consulta> consultas = new ConcurrentHashMap<>();

    private final AtomicInteger medicoSeq   = new AtomicInteger(1);
    private final AtomicInteger consultaSeq = new AtomicInteger(1);

    public MedicoServiceImpl() {
        carregarDadosIniciais();
    }


    public byte[] processar(Message req) {
        System.out.println("  [Dispatcher] Roteando para: " + req.getMethodId());
        try {
            return switch (req.getMethodId()) {
                case "cadastrarMedico" -> cadastrarMedico(req.getArguments());
                case "buscarMedico"    -> buscarMedico(req.getArguments());
                case "listarMedicos"   -> listarMedicos();
                case "agendarConsulta" -> agendarConsulta(req.getArguments());
                default -> throw new IllegalArgumentException(
                        "Metodo desconhecido: " + req.getMethodId());
            };

        } catch (Exception e) {
            System.err.println("  [Dispatcher] Erro: " + e.getMessage());
            String msg = e.getMessage() != null ? e.getMessage() : "Erro interno no servidor";
            return Marshaller.marshal(msg);
        }
    }

    

    
    private byte[] cadastrarMedico(byte[] args) {
        Medico medico = Marshaller.unmarshal(args, Medico.class);  
        medico.setId(medicoSeq.getAndIncrement());
        medicos.put(medico.getId(), medico);
        System.out.println("  [Service] Medico cadastrado: " + medico);
        return Marshaller.marshal(medico);  
    }

    
    private byte[] buscarMedico(byte[] args) {
        Integer id = Marshaller.unmarshal(args, Integer.class);  
        Medico medico = medicos.get(id);
        System.out.println("  [Service] Busca id=" + id
                + " -> " + (medico != null ? medico.getNome() : "nao encontrado"));
        return medico != null ? Marshaller.marshal(medico) : new byte[0];  
    }

    
    private byte[] listarMedicos() {
        List<Medico> lista = new ArrayList<>(medicos.values());
        System.out.println("  [Service] Listando " + lista.size() + " medico(s)");
        return Marshaller.marshal(lista);  
    }

    
    private byte[] agendarConsulta(byte[] args) {
        Consulta consulta = Marshaller.unmarshal(args, Consulta.class);  
        consulta.setId(consultaSeq.getAndIncrement());
        consulta.setStatus("AGENDADA");
        consultas.put(consulta.getId(), consulta);
        System.out.println("  [Service] Consulta agendada: " + consulta);
        return Marshaller.marshal(consulta);  
    }

    

    private void carregarDadosIniciais() {
        Especialidade cardio  = new Especialidade(1, "Cardiologia",  "Doencas do coracao");
        Especialidade pediatr = new Especialidade(2, "Pediatria",    "Saude infantil");
        Especialidade neuro   = new Especialidade(3, "Neurologia",   "Sistema nervoso");

        Medico m1 = new Medico(medicoSeq.getAndIncrement(),
                "Dra. Ana Lima",      "111.111.111-11", "CRM-12345", cardio);
        Medico m2 = new Medico(medicoSeq.getAndIncrement(),
                "Dr. Carlos Melo",    "222.222.222-22", "CRM-67890", pediatr);
        Medico m3 = new Medico(medicoSeq.getAndIncrement(),
                "Dra. Beatriz Sousa", "333.333.333-33", "CRM-11223", neuro);

        medicos.put(m1.getId(), m1);
        medicos.put(m2.getId(), m2);
        medicos.put(m3.getId(), m3);
        System.out.println("[Service] " + medicos.size() + " medicos pre-carregados.");
    }
}
