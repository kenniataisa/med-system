package stream;

import model.Medico;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class MedicoOutputStream extends OutputStream {
    private static final int ATRIBUTOS_SERIALIZADOS = 3;

    private final Medico[] medicos;
    private final int quantidade;
    private final DataOutputStream out;

    public MedicoOutputStream(Medico[] medicos, int quantidade, OutputStream out) {
        if (medicos == null) {
            throw new IllegalArgumentException("O array de medicos nao pode ser nulo.");
        }
        if (quantidade < 0 || quantidade > medicos.length) {
            throw new IllegalArgumentException("Quantidade de medicos invalida.");
        }

        this.medicos = medicos;
        this.quantidade = quantidade;
        this.out = new DataOutputStream(out);
    }

    public void enviar() throws IOException {
        out.writeInt(quantidade);

        for (int i = 0; i < quantidade; i++) {
            Medico medico = medicos[i];
            out.writeInt(calcularBytes(medico));
            out.writeInt(ATRIBUTOS_SERIALIZADOS);
            out.writeInt(medico.getId());
            out.writeUTF(medico.getNome());
            out.writeUTF(medico.getEspecialidade());
        }

        out.flush();
    }

    private int calcularBytes(Medico medico) {
        int bytesId = Integer.BYTES;
        int bytesNome = Short.BYTES + medico.getNome().getBytes(StandardCharsets.UTF_8).length;
        int bytesEspecialidade = Short.BYTES + medico.getEspecialidade().getBytes(StandardCharsets.UTF_8).length;
        return bytesId + bytesNome + bytesEspecialidade;
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
