package view;

import controller.ArtistaController;
import model.Artista;
import util.ModeloTabela;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaArtista extends JFrame {
    private final ArtistaController controller = new ArtistaController();
    private final JTextField txtNome = new JTextField(20);
    private final JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Cantor", "Banda"});
    private final JTextField txtPais = new JTextField(20);
    private final ModeloTabela modelo = new ModeloTabela("ID", "Nome", "Tipo", "País");
    private final JTable tabela = new JTable(modelo);
    private List<Artista> artistas = new ArrayList<>();
    private int idSelecionado = 0;

    public TelaArtista() {
        super("Emipy3 - Artistas e bandas");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(650, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 8));
        form.setBorder(BorderFactory.createTitledBorder("Dados do artista"));
        form.add(new JLabel("Nome:"));
        form.add(txtNome);
        form.add(new JLabel("Tipo:"));
        form.add(cbTipo);
        form.add(new JLabel("País:"));
        form.add(txtPais);

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

        carregarTabela();
    }

    private void carregarTabela() {
        try {
            artistas = controller.listar();
            modelo.setRowCount(0);
            for (Artista a : artistas) modelo.addRow(new Object[]{a.getId(), a.getNome(), a.getTipo(), a.getPais()});
        } catch (Exception ex) {
            erro(ex);
        }
    }

    private void carregarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        Artista a = artistas.get(linha);
        idSelecionado = a.getId();
        txtNome.setText(a.getNome());
        cbTipo.setSelectedItem(a.getTipo());
        txtPais.setText(a.getPais());
    }

    private void salvar() {
        try {
            controller.salvar(idSelecionado, txtNome.getText(), (String) cbTipo.getSelectedItem(), txtPais.getText());
            limpar();
            carregarTabela();
        } catch (Exception ex) {
            erro(ex);
        }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um artista na tabela.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this,
                "Excluir este artista também exclui seus álbuns e músicas. Continuar?",
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
        txtNome.setText("");
        txtPais.setText("");
        cbTipo.setSelectedIndex(0);
        tabela.clearSelection();
    }

    private void erro(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
