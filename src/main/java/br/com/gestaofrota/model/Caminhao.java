package br.com.gestaofrota.model;

public class Caminhao extends Veiculo {
    private int capacidadeCargaToneladas;

    public Caminhao(Integer id, String marca, String modelo, int ano, int capacidadeCargaToneladas) {
        super(id, marca, modelo, ano);
        this.capacidadeCargaToneladas = capacidadeCargaToneladas;
    }

    public Caminhao(String marca, String modelo, int ano, int capacidadeCargaToneladas) {
        super(marca, modelo, ano);
        this.capacidadeCargaToneladas = capacidadeCargaToneladas;
    }

    @Override
    public String exibirFichaTecnica() {
        return super.exibirFichaTecnica() + String.format(" | capacidadeCargaToneladas: %dt", capacidadeCargaToneladas);
    }

    public int getCapacidadeCargaToneladas() {
        return capacidadeCargaToneladas;
    }

    public void setCapacidadeCargaToneladas(int capacidadeCargaToneladas) {
        this.capacidadeCargaToneladas = capacidadeCargaToneladas;
    }
}
