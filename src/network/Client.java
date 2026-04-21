package network;

import model.Medico;
import model.Resposta;
import service.MedicoService;
import stream.MedicoOutputStream;
import stream.RespostaInputStream;
import service.ConfiguracaoSistema;

import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String HOST = ConfiguracaoSistema.HOST;
    private static final int PORTA   = ConfiguracaoSistema.PORTA_TCP;
    private static final MedicoService medicoService = new MedicoService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== SISTEMA MEDICO - CLIENTE ===");

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
        System.out.println("\n--------------------------------------------------------------");
        System.out.println("  __  __          _    _____           _                 ");
        System.out.println(" |  \\/  |        | |  / ____|         | |                ");
        System.out.println(" | \\  / | ___  __| | | (___  _   _ ___| |_ ___ _ __ ___  ");
        System.out.println(" | |\\/| |/ _ \\/ _` |  \\___ \\| | | / __| __/ _ \\ '_ ` _ \\ ");
        System.out.println(" | |  | |  __/ (_| |  ____) | |_| \\__ \\ ||  __/ | | | | |");
        System.out.println(" |_|  |_|\\___|\\__,_| |_____/ \\__, |___/\\__\\___|_| |_| |_|");
        System.out.println("                              __/ |                      ");
        System.out.println("                             |___/                       ");
        System.out.println("\n-----------------------------------------------------------------");
        
        System.out.println("1. Enviar medicos ao servidor");
        System.out.println("2. Listar medicos locais");
        System.out.println("0. Sair");
        System.out.print("Opcao: ");
    }

    private static void processarOpcao(String opcao) {
        switch (opcao) {
            case "1": enviarMedicos();  break;
            case "2": listarLocais();   break;
            case "0":                   break; // só sai do loop
            default:
                System.out.println("Opcao invalida. Tente novamente.");
        }
    }

    private static void enviarMedicos() {
        System.out.println("\n[Conectando ao servidor " + HOST + ":" + PORTA + "...]");

        Medico[] medicos = medicoService.listarMedicosComoArray();

        try (Socket socket = new Socket(HOST, PORTA);
             MedicoOutputStream out =
                 new MedicoOutputStream(medicos, medicos.length, socket.getOutputStream());
             RespostaInputStream in =
                 new RespostaInputStream(socket.getInputStream())) {

            out.enviar();
            System.out.println("[" + medicos.length + " medico(s) enviado(s)]");

            Resposta resposta = in.ler();

            if (resposta.isSucesso()) {
                System.out.println("Enviado com sucesso!");
                System.out.println("Resposta do servidor: " + resposta.getMensagem());
                System.out.println("Especialidades: " + resposta.getEspecialidadesDisponiveis());
            } else {
                System.out.println("Servidor retornou erro: " + resposta.getMensagem());
            }

        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            System.out.println("Verifique se o servidor esta rodando.");
        }
    }

    private static void listarLocais() {
        System.out.println("\n--- Medicos cadastrados localmente ---");
        Medico[] medicos = medicoService.listarMedicosComoArray();

        if (medicos.length == 0) {
            System.out.println("Nenhum medico encontrado.");
            return;
        }

        for (Medico m : medicos) {
            System.out.println("  [" + m.getId() + "] " + m.getNome()
                + " - " + m.getEspecialidade());
        }
        System.out.println("Total: " + medicos.length + " medico(s)");
    }
}