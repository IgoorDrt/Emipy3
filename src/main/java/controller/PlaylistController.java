package controller;

import dao.MusicaDAO;
import dao.PlaylistDAO;
import model.Musica;
import model.Playlist;
import model.Usuario;

import java.util.List;

public class PlaylistController {
    private final PlaylistDAO playlistDAO = new PlaylistDAO();
    private final MusicaDAO musicaDAO = new MusicaDAO();

    public void criar(String nome, Usuario usuario) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Informe o nome da playlist.");
        playlistDAO.inserir(new Playlist(0, nome.trim(), usuario.getId()));
    }

    public void excluir(Playlist p) {
        playlistDAO.excluir(p.getId());
    }

    public List<Playlist> listar(Usuario usuario) {
        return playlistDAO.listarPorUsuario(usuario.getId());
    }

    public void adicionarMusica(Playlist p, Musica m) {
        playlistDAO.adicionarMusica(p.getId(), m.getId());
    }

    public void removerMusica(Playlist p, Musica m) {
        playlistDAO.removerMusica(p.getId(), m.getId());
    }

    public List<Musica> listarMusicas(Playlist p) {
        return musicaDAO.listarPorPlaylist(p.getId());
    }
}
