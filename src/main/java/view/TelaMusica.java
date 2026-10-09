package view;

import controller.AlbumController;
import controller.ArtistaController;
import controller.MusicaController;
import model.Album;
import model.Artista;
import model.Musica;
import util.ModeloTabela;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaMusica extends JFrame {
    private final MusicaController controller = new MusicaController();
    private final ArtistaController artistaController = new ArtistaController();
    private final AlbumController albumController = new AlbumController();

    private final JTextField txtTitulo = new JTextField(20);
    private final JTextField txtDuracao = new JTextField(6);
    private final JTextField txtGenero = new JTextField(15);
    private final JComboBox<Artista> cbArtista = new JComboBox<>();
    private final JComboBox<Album> cbAlbum = new JComboBox<>();
    private final Album semAlbum = new Album(0, "(sem álbum)", 0, null);

    private final ModeloTabela modelo = new ModeloTabela("ID", "Música", "Artista", "Álbum", "Duração", "Gênero");
    private final JTable tabela = new JTable(modelo);
    private List<Musica> musicas = new ArrayList<>();
    private int idSelecionado = 0;
    private boolean carregando = false;

    public TelaMusica() {
        super("Emipy3 - Músicas");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(780, 540);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 8));
        form.setBorder(BorderFactory.createTitledBorder("Dados da música"));
        form.add(new JLabel("Nome da música:"));
        form.add(txtTitulo);
        form.add(new JLabel("Duração (mm:ss):"));
        form.add(txtDuracao);
        form.add(new JLabel("Gênero:"));
        form.add(txtGenero);
        form.add(new JLabel("Artista / banda:"));
        form.add(cbArtista);
        form.add(new JLabel("Álbum:"));
        form.add(cbAlbum);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnNovo = new JButton("Novo");
        JButton btnExcluir = new JButton("Excluir");
        JPanel botoes = new JPanel();
        botoes.add(btnSalvar);
        botoes.add(btnNovo);
        botoes.add(btnExcluir);

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);
        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // ao trocar o artista, mostra só os álbuns dele
        cbArtista.addActionListener(e -> {
            if (!carregando) carregarAlbuns();
        });
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) carregarSelecionado();
        });
        btnSalvar.addActionListener(e -> salvar());
        btnNovo.addActionListener(e -> limpar());
        btnExcluir.addActionListener(e -> excluir());

        carregarArtistas();
        carregarTabela();
    }

    private void carregarArtistas() {
        try {
            carregando = true;
            cbArtista.removeAllItems();
            for (Artista a : artistaController.listar()) cbArtista.addItem(a);
            carregando = false;
            carregarAlbuns();
        } catch (Exception ex) {
            carregando = false;
            erro(ex);
        }
    }

    private void carregarAlbuns() {
        try {
            cbAlbum.removeAllItems();
            cbAlbum.addItem(semAlbum);
            Artista a = (Artista) cbArtista.getSelectedItem();
            if (a != null) {
                for (Album al : albumController.listarPorArtista(a.getId())) cbAlbum.addItem(al);
            }
        } catch (Exception ex) {
            erro(ex);
        }
    }

    private void carregarTabela() {
        try {
            musicas = controller.listar();
            modelo.setRowCount(0);
            for (Musica m : musicas) {
                modelo.addRow(new Object[]{m.getId(), m.getTitulo(), m.getArtista().getNome(),
                        m.getAlbum() == null ? "-" : m.getAlbum().getTitulo(),
                        m.getDuracaoFormatada(), m.getGenero()});
            }
        } catch (Exception ex) {
            erro(ex);
        }
    }

    private void carregarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        Musica m = musicas.get(linha);
        idSelecionado = m.getId();
        txtTitulo.setText(m.getTitulo());
        txtDuracao.setText(m.getDuracaoFormatada());
        txtGenero.setText(m.getGenero());

        for (int i = 0; i < cbArtista.getItemCount(); i++) {
            if (cbArtista.getItemAt(i).getId() == m.getArtista().getId()) {
                cbArtista.setSelectedIndex(i); // dispara o recarregamento dos álbuns
                break;
            }
        }
        cbAlbum.setSelectedItem(semAlbum);
        if (m.getAlbum() != null) {
            for (int i = 0; i < cbAlbum.getItemCount(); i++) {
                if (cbAlbum.getItemAt(i).getId() == m.getAlbum().getId()) {
                    cbAlbum.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void salvar() {
        try {
            Album album = (Album) cbAlbum.getSelectedItem();
            if (album != null && album.getId() == 0) album = null;
            controller.salvar(idSelecionado, txtTitulo.getText(), txtDuracao.getText(), txtGenero.getText(),
                    (Artista) cbArtista.getSelectedItem(), album);
            limpar();
            carregarTabela();
        } catch (Exception ex) {
            erro(ex);
        }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma música na tabela.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "Excluir esta música?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;
        try {
            controller.excluir(idSelecionado);
            limpar();
            carregarTabela();
        } catch (Exception ex) {
            erro(ex);
        }
    }

    private void limpar() {
        idSelecionado = 0;
        txtTitulo.setText("");
        txtDuracao.setText("");
        txtGenero.setText("");
        tabela.clearSelection();
    }

    private void erro(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
