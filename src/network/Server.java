package network;

import model.Medico;
import model.Resposta;
import service.MedicoService;
import stream.MedicoInputStream;
import stream.RespostaOutputStream;
import service.ConfiguracaoSistema;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Server {
    private static final int PORTA = ConfiguracaoSistema.PORTA_TCP;
    private static final MedicoService MEDICO_SERVICE = new MedicoService();
    private static final Scanner scanner = new Scanner(System.in);

    private static final List<Medico> medicosRecebidos =
        Collections.synchronizedList(new ArrayList<>());

    private static boolean rodando = true;

    public static void main(String[] args) {
        System.out.println("=== SISTEMA MEDICO - SERVIDOR ===");

        Thread threadConexoes = new Thread(Server::aguardarConexoes, "thread-conexoes");
        threadConexoes.setDaemon(true);
        threadConexoes.start();

        String opcao;
        do {
            exibirMenu();
            opcao = scanner.nextLine().trim();
            processarOpcao(opcao);
        } while (!opcao.equals("0"));

        System.out.println("Encerrando servidor...");
        rodando = false;
        scanner.close();
    }

    private static void aguardarConexoes() {
        try (ServerSocket servidor = new ServerSocket(PORTA)) {
            System.out.println("[Servidor ouvindo na porta " + PORTA + "...]");

            while (rodando) {
                Socket cliente = servidor.accept();
                Thread handler = new Thread(
                    () -> processarCliente(cliente),
                    "cliente-" + cliente.getPort()
                );
                handler.start();
            }
        } catch (Exception e) {
            if (rodando) e.printStackTrace();
        }
    }

    private static void processarCliente(Socket cliente) {
        try (Socket s = cliente;
             MedicoInputStream in = new MedicoInputStream(s.getInputStream())) {

            Medico[] medicos = in.ler();

            for (Medico m : medicos) {
                medicosRecebidos.add(m); 
                System.out.println("\n[Recebido] " + m.getNome()
                    + " - " + m.getEspecialidade());
            }

            Resposta resposta = MEDICO_SERVICE.processarEnvio(medicos);
            try (RespostaOutputStream out =
                     new RespostaOutputStream(resposta, s.getOutputStream())) {
                out.enviar();
            }


            MulticastNotificador.enviar(
                medicos.length + " novo(s) medico(s) recebido(s) na porta " + cliente.getPort()
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

private static void exibirMenu() {
    System.out.println("\n---------------------------------------------------------------");
    System.out.println("  __  __          _    _____           _                         ");
    System.out.println(" |  \\/  |        | |  / ____|         | |                       ");
    System.out.println(" | \\  / | ___  __| | | (___  _   _ ___| |_ ___ _ __ ___         ");
    System.out.println(" | |\\/| |/ _ \\/ _` |  \\___ \\| | | / __| __/ _ \\ '_ ` _ \\   ");
    System.out.println(" | |  | |  __/ (_| |  ____) | |_| \\__ \\ ||  __/ | | | | |      ");
    System.out.println(" |_|  |_|\\___|\\__,_| |_____/ \\__, |___/\\__\\___|_| |_| |_|   ");
    System.out.println("                              __/ |                              ");
    System.out.println("                             |___/                               ");
    System.out.println("\n---------------------------------------------------------------");

    System.out.println("1. Listar medicos recebidos");
    System.out.println("2. Enviar notificacao multicast");
    System.out.println("0. Encerrar servidor");
    System.out.print("Opcao: ");
}

    private static void processarOpcao(String opcao) {
        switch (opcao) {
            case "1": listarRecebidos();       break;
            case "2": enviarNotificacao();     break;
            case "0":                          break;
            default:
                System.out.println("Opcao invalida.");
        }
    }


    private static void listarRecebidos() {
        System.out.println("\n--- Medicos recebidos via rede ---");

        if (medicosRecebidos.isEmpty()) {
            System.out.println("Nenhum medico recebido ainda.");
            return;
        }

        for (Medico m : medicosRecebidos) {
            System.out.println("  [" + m.getId() + "] " + m.getNome()
                + " - " + m.getEspecialidade());
        }
        System.out.println("Total: " + medicosRecebidos.size() + " medico(s)");
    }

    private static void enviarNotificacao() {
        System.out.print("Digite a mensagem: ");
        String texto = scanner.nextLine().trim();

        if (texto.isEmpty()) {
            System.out.println("Mensagem vazia. Cancelado.");
            return;
        }

        MulticastNotificador.enviar("[ADMIN] " + texto);
        System.out.println("Notificacao enviada!");
    }
}