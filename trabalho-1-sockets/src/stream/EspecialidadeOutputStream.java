package stream;

import model.Especialidade;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class EspecialidadeOutputStream extends OutputStream {
    private static final int ATRIBUTOS_SERIALIZADOS = 2;

    private final Especialidade[] especialidades;
    private final int quantidade;
    private final DataOutputStream out;

    public EspecialidadeOutputStream(Especialidade[] especialidades,
                                     int quantidade,
                                     OutputStream out) {
        if (especialidades == null)
            throw new IllegalArgumentException("Array nao pode ser nulo.");
        if (quantidade < 0 || quantidade > especialidades.length)
            throw new IllegalArgumentException("Quantidade invalida.");

        this.especialidades = especialidades;
        this.quantidade = quantidade;
        this.out = new DataOutputStream(out);
    }

    public void enviar() throws IOException {
        out.writeInt(quantidade);

        for (int i = 0; i < quantidade; i++) {
            Especialidade e = especialidades[i];
            out.writeInt(calcularBytes(e));
            out.writeInt(ATRIBUTOS_SERIALIZADOS);
            out.writeInt(e.getId());
            out.writeUTF(e.getNome());
        }

        out.flush();
    }

    private int calcularBytes(Especialidade e) {
        return Integer.BYTES
            + Short.BYTES + e.getNome().getBytes(StandardCharsets.UTF_8).length;
    }

    @Override public void write(int b) throws IOException { out.write(b); }
    @Override public void close() throws IOException      { out.close(); }
}