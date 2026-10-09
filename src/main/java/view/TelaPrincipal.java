package view;

import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal(Usuario usuario) {
        super("Emipy3 - Menu principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel boasVindas = new JLabel("Olá, " + usuario.getNome() + "!", SwingConstants.CENTER);
        boasVindas.setFont(new Font("SansSerif", Font.BOLD, 18));
        boasVindas.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        add(boasVindas, BorderLayout.NORTH);

        JButton btnArtistas = new JButton("Cadastrar artistas / bandas");
        JButton btnAlbuns = new JButton("Cadastrar álbuns");
        JButton btnMusicas = new JButton("Cadastrar músicas");
        JButton btnPesquisar = new JButton("Pesquisar");
        JButton btnPlaylists = new JButton("Minhas playlists");
        JButton btnSair = new JButton("Sair");

        JPanel menu = new JPanel(new GridLayout(6, 1, 8, 8));
        menu.setBorder(BorderFactory.createEmptyBorder(15, 50, 20, 50));
        menu.add(btnArtistas);
        menu.add(btnAlbuns);
        menu.add(btnMusicas);
        menu.add(btnPesquisar);
        menu.add(btnPlaylists);
        menu.add(btnSair);
        add(menu, BorderLayout.CENTER);

        btnArtistas.addActionListener(e -> new TelaArtista().setVisible(true));
        btnAlbuns.addActionListener(e -> new TelaAlbum().setVisible(true));
        btnMusicas.addActionListener(e -> new TelaMusica().setVisible(true));
        btnPesquisar.addActionListener(e -> new TelaPesquisa(usuario).setVisible(true));
        btnPlaylists.addActionListener(e -> new TelaPlaylist(usuario).setVisible(true));
        btnSair.addActionListener(e -> {
            new TelaLogin().setVisible(true);
            dispose();
        });
    }
}
