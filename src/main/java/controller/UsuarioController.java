package controller;

import dao.UsuarioDAO;
import model.Usuario;
import util.Senha;

public class UsuarioController {
    private final UsuarioDAO dao = new UsuarioDAO();

    public void cadastrar(String nome, String email, String senha, String confirmacao) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Informe o nome.");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Informe um e-mail válido.");
        if (senha == null || senha.length() < 4) throw new IllegalArgumentException("A senha deve ter ao menos 4 caracteres.");
        if (!senha.equals(confirmacao)) throw new IllegalArgumentException("As senhas não conferem.");
        if (dao.emailExiste(email.trim())) throw new IllegalArgumentException("Este e-mail já está cadastrado.");

        Usuario u = new Usuario();
        u.setNome(nome.trim());
        u.setEmail(email.trim());
        u.setSenha(Senha.hash(senha));
        dao.inserir(u);
    }

    public Usuario login(String email, String senha) {
        if (email == null || email.isBlank() || senha == null || senha.isEmpty())
            throw new IllegalArgumentException("Informe e-mail e senha.");
        Usuario u = dao.autenticar(email.trim(), Senha.hash(senha));
        if (u == null) throw new IllegalArgumentException("E-mail ou senha incorretos.");
        return u;
    }
}
