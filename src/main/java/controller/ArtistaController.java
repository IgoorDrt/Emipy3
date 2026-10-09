package controller;

import dao.ArtistaDAO;
import model.Artista;

import java.util.List;

public class ArtistaController {
    private final ArtistaDAO dao = new ArtistaDAO();

    public void salvar(int id, String nome, String tipo, String pais) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Informe o nome do artista/banda.");
        Artista a = new Artista(id, nome.trim(), tipo, pais == null ? "" : pais.trim());
        if (id == 0) dao.inserir(a);
        else dao.atualizar(a);
    }

    public void excluir(int id) {
        dao.excluir(id);
    }

    public List<Artista> listar() {
        return dao.listar();
    }

    public List<Artista> buscarPorNome(String nome) {
        return dao.buscarPorNome(nome.trim());
    }
}
