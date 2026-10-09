package controller;

import dao.MusicaDAO;
import model.Album;
import model.Artista;
import model.Musica;

import java.util.List;

public class MusicaController {
    private final MusicaDAO dao = new MusicaDAO();

    /** duracao no formato mm:ss (ex.: 3:45). album pode ser null (single). */
    public void salvar(int id, String titulo, String duracao, String genero, Artista artista, Album album) {
        if (titulo == null || titulo.isBlank()) throw new IllegalArgumentException("Informe o nome da música.");
        if (artista == null) throw new IllegalArgumentException("Cadastre e selecione um artista primeiro.");
        if (album != null && album.getArtista() != null && album.getArtista().getId() != artista.getId())
            throw new IllegalArgumentException("O álbum selecionado não pertence a esse artista.");

        Musica m = new Musica();
        m.setId(id);
        m.setTitulo(titulo.trim());
        m.setDuracaoSeg(converterDuracao(duracao));
        m.setGenero(genero == null ? "" : genero.trim());
        m.setArtista(artista);
        m.setAlbum(album);

        if (id == 0) dao.inserir(m);
        else dao.atualizar(m);
    }

    private int converterDuracao(String texto) {
        try {
            String[] partes = texto.trim().split(":");
            if (partes.length != 2) throw new NumberFormatException();
            int min = Integer.parseInt(partes[0]);
            int seg = Integer.parseInt(partes[1]);
            if (min < 0 || seg < 0 || seg > 59) throw new NumberFormatException();
            int total = min * 60 + seg;
            if (total == 0) throw new NumberFormatException();
            return total;
        } catch (Exception e) {
            throw new IllegalArgumentException("Duração inválida. Use o formato mm:ss (ex.: 3:45).");
        }
    }

    public void excluir(int id) {
        dao.excluir(id);
    }

    public List<Musica> listar() {
        return dao.listar();
    }

    public List<Musica> buscarPorNome(String t) {
        return dao.buscarPorNome(t.trim());
    }

    public List<Musica> buscarPorArtista(String t) {
        return dao.buscarPorArtista(t.trim());
    }

    public List<Musica> buscarPorAlbum(String t) {
        return dao.buscarPorAlbum(t.trim());
    }

    public List<Musica> listarPorAlbum(int albumId) {
        return dao.listarPorAlbumId(albumId);
    }

    public List<Musica> listarPorArtista(int artistaId) {
        return dao.listarPorArtistaId(artistaId);
    }
}
