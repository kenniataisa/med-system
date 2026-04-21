package stream;

import model.Medico;

public class MedicoInputStreamConsoleTest {

    public static void main(String[] args) {
        System.out.println("Lendo medicos da entrada padrao.");

        try (MedicoInputStream in = new MedicoInputStream(System.in)) {
            Medico[] medicos = in.ler();
            for (Medico medico : medicos) {
                System.out.println(medico);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
