package network;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;

import service.ConfiguracaoSistema;

public class ClienteMulticast {
    private static final String GRUPO = ConfiguracaoSistema.GRUPO_MULTICAST;
    private static final int PORTA = ConfiguracaoSistema.PORTA_MULTICAST;

    public static void main(String[] args) {
        System.out.println("Aguardando notificacoes do servidor...");

        try (MulticastSocket socket = new MulticastSocket(PORTA)) {
            InetSocketAddress grupoEndereco = new InetSocketAddress(GRUPO, PORTA);
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            socket.joinGroup(grupoEndereco, networkInterface);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);
                socket.receive(pacote);

                String mensagem = new String(
                        pacote.getData(), 0, pacote.getLength(), StandardCharsets.UTF_8);
                System.out.println("Notificacao recebida: " + mensagem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}