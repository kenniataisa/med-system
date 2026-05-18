package stream;

import model.Resposta;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RespostaOutputStream extends OutputStream {
    private final Resposta resposta;
    private final DataOutputStream out;

    public RespostaOutputStream(Resposta resposta, OutputStream out) {
        if (resposta == null) {
            throw new IllegalArgumentException("A resposta nao pode ser nula.");
        }
        this.resposta = resposta;
        this.out = new DataOutputStream(out);
    }

    public void enviar() throws IOException {
        out.writeBoolean(resposta.isSucesso());
        out.writeUTF(resposta.getMensagem());
        out.writeInt(resposta.getQuantidadeMedicos());
        out.writeUTF(resposta.getEspecialidadesDisponiveis());
        out.flush();
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
