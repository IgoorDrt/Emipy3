package dao;

import model.Artista;
import util.ConexaoFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistaDAO {

    public void inserir(Artista a) {
        String sql = "INSERT INTO artista (nome, tipo, pais) VALUES (?, ?, ?)";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getTipo());
            ps.setString(3, a.getPais());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir artista: " + e.getMessage(), e);
        }
    }

    public void atualizar(Artista a) {
        String sql = "UPDATE artista SET nome = ?, tipo = ?, pais = ? WHERE id = ?";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getTipo());
            ps.setString(3, a.getPais());
            ps.setInt(4, a.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar artista: " + e.getMessage(), e);
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM artista WHERE id = ?";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir artista: " + e.getMessage(), e);
        }
    }

    public List<Artista> listar() {
        return consultar("SELECT id, nome, tipo, pais FROM artista ORDER BY nome");
    }

    public List<Artista> buscarPorNome(String nome) {
        return consultar("SELECT id, nome, tipo, pais FROM artista WHERE nome LIKE ? ORDER BY nome",
                "%" + nome + "%");
    }

    private List<Artista> consultar(String sql, Object... params) {
        List<Artista> lista = new ArrayList<>();
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Artista(rs.getInt("id"), rs.getString("nome"),
                            rs.getString("tipo"), rs.getString("pais")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar artistas: " + e.getMessage(), e);
        }
        return lista;
    }
}
