package server;

import common.RemoteObjectRef;
import common.interfaces.ServicoMedico;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Server {

    private static final int    PORTA_RMI    = 1099;
    private static final String NOME_SERVICO = "ServicoMedico";

    public static void main(String[] args) {
        exibirBanner();

        try {
            
            RequestHandler handler = new RequestHandler();

            
            Registry registry = LocateRegistry.createRegistry(PORTA_RMI);
            registry.rebind(NOME_SERVICO, handler);

            
            RemoteObjectRef ref = new RemoteObjectRef(NOME_SERVICO, "localhost", PORTA_RMI);

            System.out.println("[Servidor RMI ouvindo na porta " + PORTA_RMI + "]");
            System.out.println("[Objeto registrado: " + ref.toLookupUrl() + "]");
            System.out.println("[Aguardando requisicoes... pressione ENTER para encerrar]\n");

            try {
                new Scanner(System.in).nextLine();
            } catch (Exception ignored) {
                
                Thread.currentThread().join();
            }

            System.out.println("Encerrando servidor...");
            System.exit(0);

        } catch (Exception e) {
            System.err.println("Erro ao iniciar servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void exibirBanner() {
        System.out.println("=== SISTEMA MEDICO - SERVIDOR RMI ===\n");
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
}
