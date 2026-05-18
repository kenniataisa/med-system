package model;

import java.io.Serializable;

public class Resposta implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean sucesso;
    private String mensagem;
    private int quantidadeMedicos;
    private String especialidadesDisponiveis;

    public Resposta() {
        this(false, "", 0, "");
    }

    public Resposta(boolean sucesso, String mensagem, int quantidadeMedicos, String especialidadesDisponiveis) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.quantidadeMedicos = quantidadeMedicos;
        this.especialidadesDisponiveis = especialidadesDisponiveis;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public int getQuantidadeMedicos() {
        return quantidadeMedicos;
    }

    public String getEspecialidadesDisponiveis() {
        return especialidadesDisponiveis;
    }

    @Override
    public String toString() {
        return "Resposta{sucesso=" + sucesso
            + ", mensagem='" + mensagem + '\''
            + ", quantidadeMedicos=" + quantidadeMedicos
            + ", especialidadesDisponiveis='" + especialidadesDisponiveis + "'}";
    }
}
