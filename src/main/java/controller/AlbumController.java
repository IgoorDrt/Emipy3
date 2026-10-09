package controller;

import dao.AlbumDAO;
import model.Album;
import model.Artista;

import java.util.List;

public class AlbumController {
    private final AlbumDAO dao = new AlbumDAO();

    public void salvar(int id, String titulo, String anoTexto, Artista artista) {
        if (titulo == null || titulo.isBlank()) throw new IllegalArgumentException("Informe o título do álbum.");
        if (artista == null) throw new IllegalArgumentException("Cadastre e selecione um artista primeiro.");
        int ano;
        try {
            ano = Integer.parseInt(anoTexto.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Ano inválido. Exemplo: 1999");
        }
        if (ano < 1900 || ano > 2100) throw new IllegalArgumentException("Ano fora do intervalo permitido.");

        Album al = new Album(id, titulo.trim(), ano, artista);
        if (id == 0) dao.inserir(al);
        else dao.atualizar(al);
    }

    public void excluir(int id) {
        dao.excluir(id);
    }

    public List<Album> listar() {
        return dao.listar();
    }

    public List<Album> listarPorArtista(int artistaId) {
        return dao.listarPorArtista(artistaId);
    }

    public List<Album> buscarPorTitulo(String titulo) {
        return dao.buscarPorTitulo(titulo.trim());
    }

    public List<Album> buscarPorArtista(String nomeArtista) {
        return dao.buscarPorArtista(nomeArtista.trim());
    }
}
