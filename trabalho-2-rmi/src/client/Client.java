package client;

import common.Marshaller;
import common.RemoteObjectRef;
import model.Consulta;
import model.Especialidade;
import model.Medico;
import model.Paciente;

import java.util.List;
import java.util.Scanner;

public class Client {

    private static final String HOST = "localhost";
    private static final int    PORTA_RMI = 1099;
    private static final String NOME_SERVICO = "ServicoMedico"; 

    private static Proxy   proxy;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        exibirBanner();

        RemoteObjectRef ref = new RemoteObjectRef(NOME_SERVICO, HOST, PORTA_RMI);
        proxy = new Proxy(ref);

        try {
            proxy.conectar();
        } catch (Exception e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
            System.err.println("Verifique se o servidor esta rodando.");
            return;
        }

        String opcao;
        do {
            exibirMenu();
            opcao = scanner.nextLine().trim();
            processarOpcao(opcao);
        } while (!opcao.equals("0"));

        System.out.println("Encerrando cliente...");
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n---------------------------------------------------------------");
        System.out.println(" Operacoes disponiveis (4 metodos remotos):");
        System.out.println("  1. Cadastrar medico        [cadastrarMedico]");
        System.out.println("  2. Buscar medico por ID    [buscarMedico]");
        System.out.println("  3. Listar todos os medicos [listarMedicos]");
        System.out.println("  4. Agendar consulta        [agendarConsulta]");
        System.out.println("  0. Sair");
        System.out.println("---------------------------------------------------------------");
        System.out.print("Opcao: ");
    }

    private static void processarOpcao(String opcao) {
        try {
            switch (opcao) {
                case "1" -> cadastrarMedico();
                case "2" -> buscarMedico();
                case "3" -> listarMedicos();
                case "4" -> agendarConsulta();
                case "0" -> {}
                default  -> System.out.println("Opcao invalida.");
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    private static void cadastrarMedico() throws Exception {
        System.out.println("\n--- [1] cadastrarMedico ---");
        System.out.print("Nome: ");   String nome = scanner.nextLine().trim();
        System.out.print("CPF: ");    String cpf  = scanner.nextLine().trim();
        System.out.print("CRM: ");    String crm  = scanner.nextLine().trim();
        System.out.print("Especialidade (1-Cardiologia  2-Pediatria  3-Neurologia): ");
        int espId = Integer.parseInt(scanner.nextLine().trim());

        String[] nomes = {"", "Cardiologia", "Pediatria", "Neurologia"};
        String[] descs = {"", "Doencas do coracao", "Saude infantil", "Sistema nervoso"};
        if (espId < 1 || espId >= nomes.length) {
            throw new IllegalArgumentException("Especialidade invalida.");
        }
        Especialidade esp = new Especialidade(espId, nomes[espId], descs[espId]);

        Medico medico = new Medico(0, nome, cpf, crm, esp);

        byte[] argBytes = Marshaller.marshal(medico);
        byte[] respBytes = proxy.doOperation("cadastrarMedico", argBytes);
        Medico cadastrado = lerResposta(respBytes, Medico.class);

        System.out.println("[OK] Medico cadastrado: " + cadastrado);
    }

    private static void buscarMedico() throws Exception {
        System.out.println("\n--- [2] buscarMedico ---");
        System.out.print("ID: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        byte[] argBytes = Marshaller.marshal(id);
        byte[] respBytes = proxy.doOperation("buscarMedico", argBytes);

        if (respBytes == null || respBytes.length == 0) {
            System.out.println("[INFO] Medico id=" + id + " nao encontrado.");
        } else {
            Medico m = lerResposta(respBytes, Medico.class);
            System.out.println("[OK] Medico encontrado: " + m);
        }
    }

    
    @SuppressWarnings("unchecked")
    private static void listarMedicos() throws Exception {
        System.out.println("\n--- [3] listarMedicos ---");

        byte[] respBytes = proxy.doOperation("listarMedicos", new byte[0]);
        List<Medico> lista = (List<Medico>) lerRespostaObjeto(respBytes);

        if (lista == null || lista.isEmpty()) {
            System.out.println("Nenhum medico cadastrado.");
            return;
        }
        System.out.println("[OK] " + lista.size() + " medico(s):");
        for (Medico m : lista) {
            System.out.printf("  [%d] %-25s CRM: %-12s Esp: %s%n",
                    m.getId(), m.getNome(), m.getCrm(),
                    m.getEspecialidade() != null ? m.getEspecialidade().getNome() : "N/A");
        }
    }

    private static void agendarConsulta() throws Exception {
        System.out.println("\n--- [4] agendarConsulta ---");
        System.out.print("ID do medico: ");
        int medicoId = Integer.parseInt(scanner.nextLine().trim());

        byte[] respMedico = proxy.doOperation("buscarMedico", Marshaller.marshal(medicoId));
        if (respMedico == null || respMedico.length == 0) {
            System.out.println("[ERRO] Medico id=" + medicoId + " nao encontrado.");
            return;
        }
        Medico medico = lerResposta(respMedico, Medico.class);

        System.out.print("Nome do paciente: "); String pacNome = scanner.nextLine().trim();
        System.out.print("CPF do paciente: ");  String pacCpf  = scanner.nextLine().trim();
        System.out.print("Idade do paciente: "); int pacIdade  = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Data (dd/MM/yyyy HH:mm): "); String data = scanner.nextLine().trim();

        Paciente paciente = new Paciente(0, pacNome, pacCpf, pacIdade);
        Consulta consulta = new Consulta(0, medico, paciente, data);

        byte[] respBytes = proxy.doOperation("agendarConsulta", Marshaller.marshal(consulta));
        Consulta agendada = lerResposta(respBytes, Consulta.class);

        System.out.println("[OK] Consulta agendada:");
        System.out.println("  ID      : " + agendada.getId());
        System.out.println("  Medico  : " + agendada.getMedico().getNome());
        System.out.println("  Paciente: " + agendada.getPaciente().getNome());
        System.out.println("  Data    : " + agendada.getData());
        System.out.println("  Status  : " + agendada.getStatus());
    }

    private static void exibirBanner() {
        System.out.println("=== SISTEMA MEDICO - CLIENTE RMI ===\n");
        System.out.println("---------------------------------------------------------------");
        System.out.println("  __  __          _    _____           _                      ");
        System.out.println(" |  \\/  |        | |  / ____|         | |                     ");
        System.out.println(" | \\  / | ___  __| | | (___  _   _ ___| |_ ___ _ __ ___      ");
        System.out.println(" | |\\/| |/ _ \\/ _` |  \\___ \\| | | / __| __/ _ \\ '_ ` _ \\  ");
        System.out.println(" | |  | |  __/ (_| |  ____) | |_| \\__ \\ ||  __/ | | | | |   ");
        System.out.println(" |_|  |_|\\___|\\__,_| |_____/ \\__, |___/\\__\\___|_| |_| |_|  ");
        System.out.println("                              __/ |                           ");
        System.out.println("                             |___/                            ");
        System.out.println("---------------------------------------------------------------\n");
    }

    private static <T> T lerResposta(byte[] respBytes, Class<T> clazz) {
        Object obj = lerRespostaObjeto(respBytes);
        return clazz.cast(obj);
    }

    private static Object lerRespostaObjeto(byte[] respBytes) {
        Object obj = Marshaller.unmarshal(respBytes, Object.class);
        if (obj instanceof String) {
            throw new IllegalStateException((String) obj);
        }
        return obj;
    }
}
