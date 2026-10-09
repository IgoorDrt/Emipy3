package view;

import controller.PlaylistController;
import model.Musica;
import model.Playlist;
import model.Usuario;
import util.ModeloTabela;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaPlaylist extends JFrame {
    private final PlaylistController controller = new PlaylistController();
    private final Usuario usuario;

    private final DefaultListModel<Playlist> modeloLista = new DefaultListModel<>();
    private final JList<Playlist> lista = new JList<>(modeloLista);
    private final ModeloTabela modeloMusicas = new ModeloTabela("Música", "Artista", "Álbum", "Duração");
    private final JTable tabela = new JTable(modeloMusicas);
    private List<Musica> musicas = new ArrayList<>();

    public TelaPlaylist(Usuario usuario) {
        super("Emipy3 - Minhas playlists");
        this.usuario = usuario;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(780, 460);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        JButton btnNova = new JButton("Nova");
        JButton btnExcluir = new JButton("Excluir");
        JPanel botoesLista = new JPanel();
        botoesLista.add(btnNova);
        botoesLista.add(btnExcluir);

        JPanel esquerda = new JPanel(new BorderLayout());
        esquerda.setBorder(BorderFactory.createTitledBorder("Playlists"));
        esquerda.add(new JScrollPane(lista), BorderLayout.CENTER);
        esquerda.add(botoesLista, BorderLayout.SOUTH);
        esquerda.setPreferredSize(new Dimension(240, 0));
        add(esquerda, BorderLayout.WEST);

        JButton btnRemover = new JButton("Remover música da playlist");
        JPanel direita = new JPanel(new BorderLayout());
        direita.setBorder(BorderFactory.createTitledBorder("Músicas"));
        direita.add(new JScrollPane(tabela), BorderLayout.CENTER);
        JPanel rodape = new JPanel();
        rodape.add(btnRemover);
        direita.add(rodape, BorderLayout.SOUTH);
        add(direita, BorderLayout.CENTER);

        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) carregarMusicas();
        });
        btnNova.addActionListener(e -> criar());
        btnExcluir.addActionListener(e -> excluir());
        btnRemover.addActionListener(e -> remover());

        carregarPlaylists();
    }

    private void carregarPlaylists() {
        try {
            modeloLista.clear();
            for (Playlist p : controller.listar(usuario)) modeloLista.addElement(p);
            modeloMusicas.setRowCount(0);
        } catch (Exception ex) {
            erro(ex);
        }
    }

    private void carregarMusicas() {
        Playlist p = lista.getSelectedValue();
        modeloMusicas.setRowCount(0);
        if (p == null) return;
        try {
            musicas = controller.listarMusicas(p);
            for (Musica m : musicas) {
                modeloMusicas.addRow(new Object[]{m.getTitulo(), m.getArtista().getNome(),
                        m.getAlbum() == null ? "-" : m.getAlbum().getTitulo(), m.getDuracaoFormatada()});
            }
        } catch (Exception ex) {
            erro(ex);
        }
    }

    private void criar() {
        String nome = JOptionPane.showInputDialog(this, "Nome da nova playlist:");
        if (nome == null) return;
        try {
            controller.criar(nome, usuario);
            carregarPlaylists();
        } catch (Exception ex) {
            erro(ex);
        }
    }

    private void excluir() {
        Playlist p = lista.getSelectedValue();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma playlist.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "Excluir a playlist \"" + p.getNome() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;
        try {
            controller.excluir(p);
            carregarPlaylists();
        } catch (Exception ex) {
            erro(ex);
        }
    }

    private void remover() {
        Playlist p = lista.getSelectedValue();
        int linha = tabela.getSelectedRow();
        if (p == null || linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma playlist e uma música.");
            return;
        }
        try {
            controller.removerMusica(p, musicas.get(linha));
            carregarMusicas();
        } catch (Exception ex) {
            erro(ex);
        }
    }

    private void erro(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
