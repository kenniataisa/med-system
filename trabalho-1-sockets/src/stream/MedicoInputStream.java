package stream;

import model.Medico;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MedicoInputStream extends InputStream {
    private final DataInputStream in;

    public MedicoInputStream(InputStream in) {
        this.in = new DataInputStream(in);
    }

    public Medico[] ler() throws IOException {
        int quantidade = in.readInt();
        Medico[] medicos = new Medico[quantidade];

        for (int i = 0; i < quantidade; i++) {
            in.readInt();
            in.readInt();

            Medico medico = new Medico();
            medico.setId(in.readInt());
            medico.setNome(in.readUTF());
            medico.setEspecialidade(in.readUTF());
            medicos[i] = medico;
        }

        return medicos;
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
