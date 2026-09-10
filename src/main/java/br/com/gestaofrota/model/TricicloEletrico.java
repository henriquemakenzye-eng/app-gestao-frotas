package br.com.gestaofrota.model;

public class TricicloEletrico extends Veiculo {
    private int autonomiaBateriaKm;

    public TricicloEletrico(Integer id, String marca, String modelo, int ano, int autonomiaBateriaKm) {
        super(id, marca, modelo, ano);
        this.autonomiaBateriaKm = autonomiaBateriaKm;
    }
    public TricicloEletrico(String marca, String modelo, int ano, int autonomiaBateriaKm) {
        super(marca, modelo, ano);
        this.autonomiaBateriaKm = autonomiaBateriaKm;
    }

    @Override
    public String exibirFichaTecnica() {
        return super.exibirFichaTecnica() + String.format(" | Autonomia = %dkm", autonomiaBateriaKm);
    }

    public int getAutonomiaBateriaKm() {
        return autonomiaBateriaKm;
    }
    public void setAutonomiaBateriaKm(int autonomiaBateriaKm) {
        this.autonomiaBateriaKm = autonomiaBateriaKm;
    }
}
