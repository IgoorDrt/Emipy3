package view;

import controller.AlbumController;
import controller.ArtistaController;
import controller.MusicaController;
import controller.PlaylistController;
import model.Album;
import model.Artista;
import model.Musica;
import model.Playlist;
import model.Usuario;
import util.ModeloTabela;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaPesquisa extends JFrame {
    private final MusicaController musicaController = new MusicaController();
    private final AlbumController albumController = new AlbumController();
    private final ArtistaController artistaController = new ArtistaController();
    private final PlaylistController playlistController = new PlaylistController();
    private final Usuario usuario;

    private final JComboBox<String> cbEntidade = new JComboBox<>(new String[]{"Músicas", "Álbuns", "Artistas"});
    private final JComboBox<String> cbCampo = new JComboBox<>();
    private final JTextField txtBusca = new JTextField(20);

    private final ModeloTabela modeloResultado = new ModeloTabela("Resultado");
    private final JTable tabelaResultado = new JTable(modeloResultado);
    private final ModeloTabela modeloDetalhe = new ModeloTabela("Música", "Artista", "Álbum", "Duração", "Gênero");
    private final JTable tabelaDetalhe = new JTable(modeloDetalhe);
    private final JLabel lblDetalhe = new JLabel("Faixas");

    private List<Musica> musicasTopo = new ArrayList<>();
    private List<Album> albunsTopo = new ArrayList<>();
    private List<Artista> artistasTopo = new ArrayList<>();
    private List<Musica> detalhes = new ArrayList<>();

    public TelaPesquisa(Usuario usuario) {
        super("Emipy3 - Pesquisar");
        this.usuario = usuario;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(820, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        JButton btnBuscar = new JButton("Buscar");
        JPanel busca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        busca.setBorder(BorderFactory.createTitledBorder("Pesquisa"));
        busca.add(new JLabel("Pesquisar:"));
        busca.add(cbEntidade);
        busca.add(new JLabel("por:"));
        busca.add(cbCampo);
        busca.add(txtBusca);
        busca.add(btnBuscar);
        add(busca, BorderLayout.NORTH);

        JPanel detalhePanel = new JPanel(new BorderLayout());
        detalhePanel.add(lblDetalhe, BorderLayout.NORTH);
        detalhePanel.add(new JScrollPane(tabelaDetalhe), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tabelaResultado), detalhePanel);
        split.setResizeWeight(0.5);
        add(split, BorderLayout.CENTER);

        JButton btnPlaylist = new JButton("Adicionar música selecionada à playlist");
        JPanel rodape = new JPanel();
        rodape.add(btnPlaylist);
        add(rodape, BorderLayout.SOUTH);

        cbEntidade.addActionListener(e -> atualizarCampos());
        btnBuscar.addActionListener(e -> buscar());
        txtBusca.addActionListener(e -> buscar());
        btnPlaylist.addActionListener(e -> adicionarNaPlaylist());
        tabelaResultado.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) carregarDetalhes();
        });

        atualizarCampos();
    }

    private void atualizarCampos() {
        cbCampo.removeAllItems();
        switch (cbEntidade.getSelectedIndex()) {
            case 0: // músicas
                cbCampo.addItem("Nome");
                cbCampo.addItem("Artista");
                cbCampo.addItem("Álbum");
                break;
            case 1: // álbuns
                cbCampo.addItem("Título");
                cbCampo.addItem("Artista");
                break;
            default: // artistas
                cbCampo.addItem("Nome");
        }
    }

    private void buscar() {
        String termo = txtBusca.getText();
        int campo = cbCampo.getSelectedIndex();
        try {
            modeloDetalhe.setRowCount(0);
            detalhes = new ArrayList<>();
            switch (cbEntidade.getSelectedIndex()) {
                case 0:
                    musicasTopo = campo == 0 ? musicaController.buscarPorNome(termo)
                            : campo == 1 ? musicaController.buscarPorArtista(termo)
                            : musicaController.buscarPorAlbum(termo);
                    modeloResultado.setColumnIdentifiers(new String[]{"Música", "Artista", "Álbum", "Duração", "Gênero"});
                    modeloResultado.setRowCount(0);
                    for (Musica m : musicasTopo) {
                        modeloResultado.addRow(new Object[]{m.getTitulo(), m.getArtista().getNome(),
                                m.getAlbum() == null ? "-" : m.getAlbum().getTitulo(),
                                m.getDuracaoFormatada(), m.getGenero()});
                    }
                    lblDetalhe.setText("Faixas");
                    break;
                case 1:
                    albunsTopo = campo == 0 ? albumController.buscarPorTitulo(termo)
                            : albumController.buscarPorArtista(termo);
                    modeloResultado.setColumnIdentifiers(new String[]{"Álbum", "Artista", "Ano"});
                    modeloResultado.setRowCount(0);
                    for (Album a : albunsTopo) {
                        modeloResultado.addRow(new Object[]{a.getTitulo(), a.getArtista().getNome(), a.getAno()});
                    }
                    lblDetalhe.setText("Faixas do álbum selecionado");
                    break;
                default:
                    artistasTopo = artistaController.buscarPorNome(termo);
                    modeloResultado.setColumnIdentifiers(new String[]{"Artista", "Tipo", "País"});
                    modeloResultado.setRowCount(0);
                    for (Artista a : artistasTopo) {
                        modeloResultado.addRow(new Object[]{a.getNome(), a.getTipo(), a.getPais()});
                    }
                    lblDetalhe.setText("Músicas do artista selecionado");
            }
            if (modeloResultado.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Nenhum resultado encontrado.");
            }
        } catch (Exception ex) {
            erro(ex);
        }
    }

    /** Ao selecionar um álbum ou artista, lista as músicas dele na tabela de baixo. */
    private void carregarDetalhes() {
        int linha = tabelaResultado.getSelectedRow();
        if (linha < 0) return;
        try {
            int entidade = cbEntidade.getSelectedIndex();
            if (entidade == 1 && linha < albunsTopo.size()) {
                detalhes = musicaController.listarPorAlbum(albunsTopo.get(linha).getId());
            } else if (entidade == 2 && linha < artistasTopo.size()) {
                detalhes = musicaController.listarPorArtista(artistasTopo.get(linha).getId());
            } else {
                return;
            }
            modeloDetalhe.setRowCount(0);
            for (Musica m : detalhes) {
                modeloDetalhe.addRow(new Object[]{m.getTitulo(), m.getArtista().getNome(),
                        m.getAlbum() == null ? "-" : m.getAlbum().getTitulo(),
                        m.getDuracaoFormatada(), m.getGenero()});
            }
        } catch (Exception ex) {
            erro(ex);
        }
    }

    private Musica musicaSelecionada() {
        int d = tabelaDetalhe.getSelectedRow();
        if (d >= 0 && d < detalhes.size()) return detalhes.get(d);
        int t = tabelaResultado.getSelectedRow();
        if (cbEntidade.getSelectedIndex() == 0 && t >= 0 && t < musicasTopo.size()) return musicasTopo.get(t);
        return null;
    }

    private void adicionarNaPlaylist() {
        Musica m = musicaSelecionada();
        if (m == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma música nos resultados.");
            return;
        }
        try {
            List<Playlist> playlists = playlistController.listar(usuario);
            if (playlists.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Você ainda não tem playlists. Crie uma em \"Minhas playlists\".");
                return;
            }
            Playlist escolhida = (Playlist) JOptionPane.showInputDialog(this, "Escolha a playlist:",
                    "Adicionar \"" + m.getTitulo() + "\"", JOptionPane.PLAIN_MESSAGE, null,
                    playlists.toArray(), playlists.get(0));
            if (escolhida != null) {
                playlistController.adicionarMusica(escolhida, m);
                JOptionPane.showMessageDialog(this, "Música adicionada!");
            }
        } catch (Exception ex) {
            erro(ex);
        }
    }

    private void erro(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
