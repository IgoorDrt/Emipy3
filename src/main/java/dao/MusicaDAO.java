package dao;

import model.Album;
import model.Artista;
import model.Musica;
import util.ConexaoFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusicaDAO {

    private static final String BASE =
            "SELECT m.id, m.titulo, m.duracao_seg, m.genero, m.artista_id, a.nome AS artista_nome, "
          + "m.album_id, al.titulo AS album_titulo "
          + "FROM musica m JOIN artista a ON a.id = m.artista_id "
          + "LEFT JOIN album al ON al.id = m.album_id ";

    public void inserir(Musica m) {
        String sql = "INSERT INTO musica (titulo, duracao_seg, genero, artista_id, album_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            preencher(ps, m);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir música: " + e.getMessage(), e);
        }
    }

    public void atualizar(Musica m) {
        String sql = "UPDATE musica SET titulo = ?, duracao_seg = ?, genero = ?, artista_id = ?, album_id = ? WHERE id = ?";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            preencher(ps, m);
            ps.setInt(6, m.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar música: " + e.getMessage(), e);
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM musica WHERE id = ?";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir música: " + e.getMessage(), e);
        }
    }

    public List<Musica> listar() {
        return consultar(BASE + "ORDER BY m.titulo");
    }

    public List<Musica> buscarPorNome(String nome) {
        return consultar(BASE + "WHERE m.titulo LIKE ? ORDER BY m.titulo", "%" + nome + "%");
    }

    public List<Musica> buscarPorArtista(String nomeArtista) {
        return consultar(BASE + "WHERE a.nome LIKE ? ORDER BY a.nome, m.titulo", "%" + nomeArtista + "%");
    }

    public List<Musica> buscarPorAlbum(String tituloAlbum) {
        return consultar(BASE + "WHERE al.titulo LIKE ? ORDER BY al.titulo, m.titulo", "%" + tituloAlbum + "%");
    }

    public List<Musica> listarPorAlbumId(int albumId) {
        return consultar(BASE + "WHERE m.album_id = ? ORDER BY m.titulo", albumId);
    }

    public List<Musica> listarPorArtistaId(int artistaId) {
        return consultar(BASE + "WHERE m.artista_id = ? ORDER BY m.titulo", artistaId);
    }

    public List<Musica> listarPorPlaylist(int playlistId) {
        return consultar(BASE + "JOIN playlist_musica pm ON pm.musica_id = m.id "
                + "WHERE pm.playlist_id = ? ORDER BY m.titulo", playlistId);
    }

    private void preencher(PreparedStatement ps, Musica m) throws SQLException {
        ps.setString(1, m.getTitulo());
        ps.setInt(2, m.getDuracaoSeg());
        ps.setString(3, m.getGenero());
        ps.setInt(4, m.getArtista().getId());
        if (m.getAlbum() == null) ps.setNull(5, Types.INTEGER);
        else ps.setInt(5, m.getAlbum().getId());
    }

    private List<Musica> consultar(String sql, Object... params) {
        List<Musica> lista = new ArrayList<>();
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Musica m = new Musica();
                    m.setId(rs.getInt("id"));
                    m.setTitulo(rs.getString("titulo"));
                    m.setDuracaoSeg(rs.getInt("duracao_seg"));
                    m.setGenero(rs.getString("genero"));

                    Artista a = new Artista();
                    a.setId(rs.getInt("artista_id"));
                    a.setNome(rs.getString("artista_nome"));
                    m.setArtista(a);

                    int albumId = rs.getInt("album_id");
                    if (!rs.wasNull()) {
                        Album al = new Album();
                        al.setId(albumId);
                        al.setTitulo(rs.getString("album_titulo"));
                        al.setArtista(a);
                        m.setAlbum(al);
                    }
                    lista.add(m);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar músicas: " + e.getMessage(), e);
        }
        return lista;
    }
}
