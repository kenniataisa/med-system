package stream;

import model.Resposta;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RespostaInputStream extends InputStream {
    private final DataInputStream in;

    public RespostaInputStream(InputStream in) {
        this.in = new DataInputStream(in);
    }

    public Resposta ler() throws IOException {
        boolean sucesso = in.readBoolean();
        String mensagem = in.readUTF();
        int quantidadeMedicos = in.readInt();
        String especialidadesDisponiveis = in.readUTF();
        return new Resposta(sucesso, mensagem, quantidadeMedicos, especialidadesDisponiveis);
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
