package dao;

import model.Album;
import model.Artista;
import util.ConexaoFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAO {

    private static final String BASE =
            "SELECT al.id, al.titulo, al.ano, al.artista_id, a.nome AS artista_nome "
          + "FROM album al JOIN artista a ON a.id = al.artista_id ";

    public void inserir(Album al) {
        String sql = "INSERT INTO album (titulo, ano, artista_id) VALUES (?, ?, ?)";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, al.getTitulo());
            ps.setInt(2, al.getAno());
            ps.setInt(3, al.getArtista().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir álbum: " + e.getMessage(), e);
        }
    }

    public void atualizar(Album al) {
        String sql = "UPDATE album SET titulo = ?, ano = ?, artista_id = ? WHERE id = ?";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, al.getTitulo());
            ps.setInt(2, al.getAno());
            ps.setInt(3, al.getArtista().getId());
            ps.setInt(4, al.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar álbum: " + e.getMessage(), e);
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM album WHERE id = ?";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir álbum: " + e.getMessage(), e);
        }
    }

    public List<Album> listar() {
        return consultar(BASE + "ORDER BY al.titulo");
    }

    public List<Album> listarPorArtista(int artistaId) {
        return consultar(BASE + "WHERE al.artista_id = ? ORDER BY al.ano, al.titulo", artistaId);
    }

    public List<Album> buscarPorTitulo(String titulo) {
        return consultar(BASE + "WHERE al.titulo LIKE ? ORDER BY al.titulo", "%" + titulo + "%");
    }

    public List<Album> buscarPorArtista(String nomeArtista) {
        return consultar(BASE + "WHERE a.nome LIKE ? ORDER BY al.titulo", "%" + nomeArtista + "%");
    }

    private List<Album> consultar(String sql, Object... params) {
        List<Album> lista = new ArrayList<>();
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Artista a = new Artista();
                    a.setId(rs.getInt("artista_id"));
                    a.setNome(rs.getString("artista_nome"));
                    lista.add(new Album(rs.getInt("id"), rs.getString("titulo"), rs.getInt("ano"), a));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar álbuns: " + e.getMessage(), e);
        }
        return lista;
    }
}
