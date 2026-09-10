package br.com.gestaofrota.model;

    public class Ambulancia extends Veiculo  {
        private int numeroUTI;

        public Ambulancia(Integer id, String marca, String modelo, int ano, int numeroUTI) {
            super(id, marca, modelo, ano);
            this.numeroUTI = numeroUTI;
        }

        @Override
        public String exibirFichaTecnica() {
            return super.exibirFichaTecnica() + String.format(" Leito UTI: #%d", numeroUTI);
        }

        public Ambulancia(String marca, String modelo, int ano, int numeroUTI) {
            super(marca, modelo, ano);
            this.numeroUTI = numeroUTI;
        }

        public int getNumeroUTI() {
            return numeroUTI;
        }

        public void setNumeroUTI(int numeroUTI) {
            this.numeroUTI = numeroUTI;
        }

    }
