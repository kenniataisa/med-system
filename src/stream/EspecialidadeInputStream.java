// EspecialidadeInputStream.java
package stream;

import model.Especialidade;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class EspecialidadeInputStream extends InputStream {
    private final DataInputStream in;

    public EspecialidadeInputStream(InputStream in) {
        this.in = new DataInputStream(in);
    }

    public Especialidade[] ler() throws IOException {
        int quantidade = in.readInt();
        Especialidade[] especialidades = new Especialidade[quantidade];

        for (int i = 0; i < quantidade; i++) {
            in.readInt(); 
            in.readInt(); 

            Especialidade e = new Especialidade(in.readInt(), in.readUTF());
            especialidades[i] = e;

            
        }

        return especialidades;
    }

    @Override public int read() throws IOException  { return in.read(); }
    @Override public void close() throws IOException { in.close(); }
}