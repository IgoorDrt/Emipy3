package view;

import controller.UsuarioController;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {
    private final UsuarioController controller = new UsuarioController();
    private final JTextField txtEmail = new JTextField(20);
    private final JPasswordField txtSenha = new JPasswordField(20);

    public TelaLogin() {
        super("Emipy3 - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(380, 260);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Emipy3", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        add(titulo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 2, 5, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        form.add(new JLabel("E-mail:"));
        form.add(txtEmail);
        form.add(new JLabel("Senha:"));
        form.add(txtSenha);
        add(form, BorderLayout.CENTER);

        JButton btnEntrar = new JButton("Entrar");
        JButton btnCadastrar = new JButton("Criar conta");
        JPanel botoes = new JPanel();
        botoes.add(btnEntrar);
        botoes.add(btnCadastrar);
        add(botoes, BorderLayout.SOUTH);

        btnEntrar.addActionListener(e -> entrar());
        btnCadastrar.addActionListener(e -> new TelaCadastroUsuario().setVisible(true));
        getRootPane().setDefaultButton(btnEntrar);
    }

    private void entrar() {
        try {
            Usuario u = controller.login(txtEmail.getText(), new String(txtSenha.getPassword()));
            new TelaPrincipal(u).setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
