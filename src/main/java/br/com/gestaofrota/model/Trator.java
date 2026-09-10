package br.com.gestaofrota.model;

public class Trator extends Veiculo {
    private int horasUso;

    public Trator(Integer id, String marca, String modelo, int ano, int horasUso) {
        super(id, marca, modelo, ano);
        this.horasUso = horasUso;
    }

    public Trator(String marca, String modelo, int ano, int horasUso) {
        super(marca, modelo, ano);
        this.horasUso = horasUso;
    }

    @Override
    public String exibirFichaTecnica() {
        return super.exibirFichaTecnica() + String.format(" | Horas Uso: %dh", horasUso);
    }

    public int getHorasUso() {
        return horasUso;
    }

    public void setHorasUso(int horasUso) {
        this.horasUso = horasUso;
    }
}
