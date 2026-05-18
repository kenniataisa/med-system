package stream;

import model.Medico;
import service.MedicoService;

public class MedicoOutputStreamConsoleTest {

    public static void main(String[] args) {
        MedicoService service = new MedicoService();
        Medico[] medicos = service.listarMedicosComoArray();

        try (MedicoOutputStream out = new MedicoOutputStream(medicos, medicos.length, System.out)) {
            out.enviar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
