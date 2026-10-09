package view;

import controller.UsuarioController;

import javax.swing.*;
import java.awt.*;

public class TelaCadastroUsuario extends JFrame {
    private final UsuarioController controller = new UsuarioController();
    private final JTextField txtNome = new JTextField(20);
    private final JTextField txtEmail = new JTextField(20);
    private final JPasswordField txtSenha = new JPasswordField(20);
    private final JPasswordField txtConfirma = new JPasswordField(20);

    public TelaCadastroUsuario() {
        super("Emipy3 - Criar conta");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        form.add(new JLabel("Nome:"));
        form.add(txtNome);
        form.add(new JLabel("E-mail:"));
        form.add(txtEmail);
        form.add(new JLabel("Senha:"));
        form.add(txtSenha);
        form.add(new JLabel("Confirmar senha:"));
        form.add(txtConfirma);
        add(form, BorderLayout.CENTER);

        JButton btnSalvar = new JButton("Cadastrar");
        JPanel botoes = new JPanel();
        botoes.add(btnSalvar);
        add(botoes, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> {
            try {
                controller.cadastrar(txtNome.getText(), txtEmail.getText(),
                        new String(txtSenha.getPassword()), new String(txtConfirma.getPassword()));
                JOptionPane.showMessageDialog(this, "Conta criada! Faça login.");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
