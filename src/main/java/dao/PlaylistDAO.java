package dao;

import model.Playlist;
import util.ConexaoFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {

    public void inserir(Playlist p) {
        String sql = "INSERT INTO playlist (nome, usuario_id) VALUES (?, ?)";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setInt(2, p.getUsuarioId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar playlist: " + e.getMessage(), e);
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM playlist WHERE id = ?";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir playlist: " + e.getMessage(), e);
        }
    }

    public List<Playlist> listarPorUsuario(int usuarioId) {
        List<Playlist> lista = new ArrayList<>();
        String sql = "SELECT id, nome, usuario_id FROM playlist WHERE usuario_id = ? ORDER BY nome";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Playlist(rs.getInt("id"), rs.getString("nome"), rs.getInt("usuario_id")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar playlists: " + e.getMessage(), e);
        }
        return lista;
    }

    public void adicionarMusica(int playlistId, int musicaId) {
        String sql = "INSERT IGNORE INTO playlist_musica (playlist_id, musica_id) VALUES (?, ?)";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ps.setInt(2, musicaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar música: " + e.getMessage(), e);
        }
    }

    public void removerMusica(int playlistId, int musicaId) {
        String sql = "DELETE FROM playlist_musica WHERE playlist_id = ? AND musica_id = ?";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ps.setInt(2, musicaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover música: " + e.getMessage(), e);
        }
    }
}
