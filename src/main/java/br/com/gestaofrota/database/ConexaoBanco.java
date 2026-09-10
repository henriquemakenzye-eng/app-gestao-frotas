package br.com.gestaofrota.database;

import br.com.gestaofrota.excecoes.DadosVeiculoInvalidosException;
import br.com.gestaofrota.model.*;
import br.com.gestaofrota.model.Ambulancia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConexaoBanco {

    // Banco de dados em arquivo local 'frota.db' na raiz do projeto
    private static final String URL_CONEXAO = "jdbc:sqlite:frota.db";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL_CONEXAO);
    }

    public static void inicializarTabela() {
        String sql = """
            CREATE TABLE IF NOT EXISTS veiculos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                tipo TEXT NOT NULL,
                marca TEXT NOT NULL,
                modelo TEXT NOT NULL,
                ano INTEGER NOT NULL,
                detalhe_especifico INTEGER NOT NULL
            );
        """;

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
        }
    }

    public static void salvarVeiculo(Veiculo veiculo)
            throws SQLException, DadosVeiculoInvalidosException {
        if (veiculo.getAno() < 1900 || veiculo.getAno() > 2027) {
            throw new DadosVeiculoInvalidosException("Ano do veículo é inválido: " + veiculo.getAno());
        }
        if (veiculo.getMarca().isBlank() || veiculo.getMarca().isEmpty()) {
            throw new DadosVeiculoInvalidosException("A marca deve ser informado!");
        }
        if (!(veiculo instanceof Ambulancia) && (veiculo.getModelo().isBlank() || veiculo.getModelo().isEmpty())) {
            throw new DadosVeiculoInvalidosException("O modelo deve ser informado!");
        }
        if (veiculo instanceof Carro c) {
            if (c.getQuantidadePortas() < 2 ||  c.getQuantidadePortas() > 6) {
                throw new DadosVeiculoInvalidosException("Quantidade de portas deve ser de 2 a 6!");
            }
        }
        if (veiculo instanceof Moto m) {
            if (m.getCilindradas() < 50 || m.getCilindradas() > 2500) {
                throw new DadosVeiculoInvalidosException("Quantidade de cilindradas deve ser entre 50 a 2500!");
            }
        }
        if (veiculo instanceof Onibus o) {
            if (o.getCapacidadePassageiros() < 10 || o.getCapacidadePassageiros() > 100) {
                throw new DadosVeiculoInvalidosException("A capacidade de passageiros deve ser entre 10 e 100!");
            }
        }
        if (veiculo instanceof Ambulancia a) {
            if (a.getNumeroUTI() <=0) {
                throw new DadosVeiculoInvalidosException("O numero de UTI ser maior que zero!");
            }
        }
        if (veiculo instanceof TricicloEletrico t) {
            if (t.getAutonomiaBateriaKm() < 10 || t.getAutonomiaBateriaKm() > 300) {
                throw new DadosVeiculoInvalidosException("A autonomia de bateria deve ser entre 10km e 300km!");
            }
        }
        if (veiculo instanceof Trator tr) {
            if (tr.getHorasUso() <=0) {
                throw new DadosVeiculoInvalidosException("As horas de uso devem ser maior ou igual a 0!");
            }
        }

        String sql = "INSERT INTO veiculos(tipo, marca, modelo, ano, detalhe_especifico) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, veiculo.getClass().getSimpleName().toUpperCase());
            stmt.setString(2, veiculo.getMarca());
            stmt.setString(3, veiculo.getModelo());
            stmt.setInt(4, veiculo.getAno());

            if (veiculo instanceof Carro c) {
                stmt.setInt(5, c.getQuantidadePortas());
            } else if (veiculo instanceof Moto m) {
                stmt.setInt(5, m.getCilindradas());
            } else if (veiculo instanceof Caminhao cam) {
                stmt.setDouble(5, cam.getCapacidadeCargaToneladas());
            } else if (veiculo instanceof Onibus o) {
                stmt.setInt(5, o.getCapacidadePassageiros());
            } else if (veiculo instanceof Ambulancia a) {
                stmt.setInt(5, a.getNumeroUTI());
            } else if (veiculo instanceof TricicloEletrico t) {
                stmt.setInt(5, t.getAutonomiaBateriaKm());
            }  else if (veiculo instanceof Trator tr) {
                stmt.setInt(5, tr.getHorasUso());
            }

            stmt.executeUpdate();
        }
    }
    public static void excluirVeiculo(int id) throws SQLException {
        String sql = "DELETE FROM veiculos WHERE id = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✅ Veiculo com id" + id + " deletado com sucesso!");
            } else {
                System.out.println("Nenhum veiculo encontrado com id " + id + ".");
            }
        }
    }

    public static List<Veiculo> listarVeiculos() throws SQLException {
        List<Veiculo> frota = new ArrayList<>();
        String sql = "SELECT * FROM veiculos";

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String tipo = rs.getString("tipo");
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                int ano = rs.getInt("ano");
                double detalhe = rs.getDouble("detalhe_especifico");

                if ("CARRO".equalsIgnoreCase(tipo)) {
                    frota.add(new Carro(id, marca, modelo, ano, (int) detalhe));
                } else if ("MOTO".equalsIgnoreCase(tipo)) {
                    frota.add(new Moto(id, marca, modelo, ano, (int) detalhe));
                } else if ("CAMINHAO".equalsIgnoreCase(tipo)) {
                    frota.add(new Caminhao(id, marca, modelo, ano, detalhe));
                }else if ("ONIBUS".equalsIgnoreCase(tipo)) {
                    frota.add(new Onibus(id, marca, modelo, ano,(int) detalhe));
                }else if ("AMBULANCIA".equalsIgnoreCase(tipo)) {
                    frota.add(new Ambulancia(id, marca, modelo, ano, (int) detalhe));
                }else if ("TRICICLOELETRICO".equalsIgnoreCase(tipo)) {
                    frota.add(new TricicloEletrico(id, marca, modelo, ano, (int) detalhe));
                }else if ("TRATOR".equalsIgnoreCase(tipo)) {
                    frota.add(new Trator(id, marca, modelo, ano, (int) detalhe));
                }
            }
        }
        return frota;
    }

}


