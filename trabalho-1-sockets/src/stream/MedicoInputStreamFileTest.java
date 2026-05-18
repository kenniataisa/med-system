package stream;

import model.Medico;

import java.io.FileInputStream;

public class MedicoInputStreamFileTest {
    private static final String ARQUIVO = "medicos.bin";

    public static void main(String[] args) {
        try (FileInputStream arquivo = new FileInputStream(ARQUIVO);
             MedicoInputStream in = new MedicoInputStream(arquivo)) {
            imprimir(in.ler());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void imprimir(Medico[] medicos) {
        for (Medico medico : medicos) {
            System.out.println(medico);
        }
    }
}
