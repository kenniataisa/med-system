package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import service.ConfiguracaoSistema;

public class MulticastNotificador {
    private static final String GRUPO = ConfiguracaoSistema.GRUPO_MULTICAST;
    private static final int PORTA    = ConfiguracaoSistema.PORTA_MULTICAST;

    public static void enviar(String mensagem) {
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] dados = mensagem.getBytes(StandardCharsets.UTF_8);
            InetAddress grupo = InetAddress.getByName(GRUPO);
            DatagramPacket pacote = new DatagramPacket(dados, dados.length, grupo, PORTA);
            socket.send(pacote);
            System.out.println("Multicast enviado: " + mensagem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}