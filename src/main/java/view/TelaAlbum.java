package view;

import controller.AlbumController;
import controller.ArtistaController;
import model.Album;
import model.Artista;
import util.ModeloTabela;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaAlbum extends JFrame {
    private final AlbumController controller = new AlbumController();
    private final ArtistaController artistaController = new ArtistaController();
    private final JTextField txtTitulo = new JTextField(20);
    private final JTextField txtAno = new JTextField(6);
    private final JComboBox<Artista> cbArtista = new JComboBox<>();
    private final ModeloTabela modelo = new ModeloTabela("ID", "Título", "Ano", "Artista");
    private final JTable tabela = new JTable(modelo);
    private List<Album> albuns = new ArrayList<>();
    private int idSelecionado = 0;

    public TelaAlbum() {
        super("Emipy3 - Álbuns");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(650, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 8));
        form.setBorder(BorderFactory.createTitledBorder("Dados do álbum"));
        form.add(new JLabel("Título:"));
        form.add(txtTitulo);
        form.add(new JLabel("Ano:"));
        form.add(txtAno);
        form.add(new JLabel("Artista / banda:"));
        form.add(cbArtista);

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
            cbArtista.removeAllItems();
            for (Artista a : artistaController.listar()) cbArtista.addItem(a);
        } catch (Exception ex) {
            erro(ex);
        }
    }

    private void carregarTabela() {
        try {
            albuns = controller.listar();
            modelo.setRowCount(0);
            for (Album a : albuns)
                modelo.addRow(new Object[]{a.getId(), a.getTitulo(), a.getAno(), a.getArtista().getNome()});
        } catch (Exception ex) {
            erro(ex);
        }
    }

    private void carregarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        Album a = albuns.get(linha);
        idSelecionado = a.getId();
        txtTitulo.setText(a.getTitulo());
        txtAno.setText(String.valueOf(a.getAno()));
        for (int i = 0; i < cbArtista.getItemCount(); i++) {
            if (cbArtista.getItemAt(i).getId() == a.getArtista().getId()) {
                cbArtista.setSelectedIndex(i);
                break;
            }
        }
    }

    private void salvar() {
        try {
            controller.salvar(idSelecionado, txtTitulo.getText(), txtAno.getText(),
                    (Artista) cbArtista.getSelectedItem());
            limpar();
            carregarTabela();
        } catch (Exception ex) {
            erro(ex);
        }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um álbum na tabela.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this,
                "Excluir o álbum? As músicas dele continuam cadastradas, mas ficam sem álbum.",
                "Confirmar", JOptionPane.YES_NO_OPTION);
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
        txtAno.setText("");
        tabela.clearSelection();
    }

    private void erro(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
