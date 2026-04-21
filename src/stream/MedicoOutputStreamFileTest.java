package stream;

import model.Medico;
import service.MedicoService;

import java.io.FileOutputStream;

public class MedicoOutputStreamFileTest {
    private static final String ARQUIVO = "medicos.bin";

    public static void main(String[] args) {
        MedicoService service = new MedicoService();
        Medico[] medicos = service.listarMedicosComoArray();

        try (FileOutputStream arquivo = new FileOutputStream(ARQUIVO);
             MedicoOutputStream out = new MedicoOutputStream(medicos, medicos.length, arquivo)) {
            out.enviar();
            System.out.println("Arquivo gerado com sucesso em " + ARQUIVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
