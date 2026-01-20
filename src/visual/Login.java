package visual;

import logic.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class Login extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JLabel lblStatus;

    public Login() {
        setTitle("Sistema de Gestión DIC - Inicio de Sesión");
        setSize(480, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with light background for better contrast
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 243, 247));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Header
        JLabel lblTitle = new JLabel("Sistema de Gestión DIC", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setBorder(new EmptyBorder(0, 0, 25, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // User Label
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUser.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblUser, gbc);

        // User Field
        txtUser = new JTextField(22);
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUser.setBackground(Color.WHITE);
        txtUser.setForeground(new Color(44, 62, 80));
        txtUser.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(txtUser, gbc);

        // Password Label
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPass.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lblPass, gbc);

        // Password Field
        txtPass = new JPasswordField(22);
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPass.setBackground(Color.WHITE);
        txtPass.setForeground(new Color(44, 62, 80));
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(txtPass, gbc);

        // Login Button
        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(new Color(44, 62, 80));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 8, 8, 8);
        formPanel.add(btnLogin, gbc);

        // Register Link
        JButton btnRegistrar = new JButton("¿Fuiste invitado? Regístrate aquí");
        btnRegistrar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRegistrar.setForeground(new Color(52, 152, 219));
        btnRegistrar.setBackground(null);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setContentAreaFilled(false);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 8, 8, 8);
        formPanel.add(btnRegistrar, gbc);

        // Status Label
        lblStatus = new JLabel(" ", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setForeground(new Color(231, 76, 60));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 8, 0, 8);
        formPanel.add(lblStatus, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Footer
        JLabel lblFooter = new JLabel("© 2026 Departamento de Informática y Computación", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblFooter.setForeground(new Color(127, 140, 141));
        lblFooter.setBorder(new EmptyBorder(20, 0, 0, 0));
        mainPanel.add(lblFooter, BorderLayout.SOUTH);

        add(mainPanel);

        // Action Listeners
        btnLogin.addActionListener(e -> autenticar());
        txtPass.addActionListener(e -> autenticar());
        btnRegistrar.addActionListener(e -> mostrarRegistro());
    }

    private void autenticar() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            lblStatus.setText("Complete todos los campos");
            return;
        }

        Usuario usuario = Sistema.getInstance().login(user, pass);

        if (usuario != null) {
            lblStatus.setForeground(new Color(39, 174, 96));
            lblStatus.setText("¡Bienvenido " + usuario.getNombreCompleto() + "!");

            Timer timer = new Timer(500, e -> abrirDashboard(usuario));
            timer.setRepeats(false);
            timer.start();
        } else {
            lblStatus.setForeground(new Color(231, 76, 60));
            lblStatus.setText("Credenciales incorrectas");
            txtPass.setText("");
        }
    }

    private void mostrarRegistro() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        JTextField txtCorreo = new JTextField(20);
        JTextField txtNombres = new JTextField(20);
        JTextField txtApellidos = new JTextField(20);
        JTextField txtCarrera = new JTextField(20);
        JSpinner spnSemestre = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        JTextField txtUsuario = new JTextField(20);
        JPasswordField txtClave = new JPasswordField(20);

        panel.add(new JLabel("Correo Institucional (invitación):"));
        panel.add(txtCorreo);
        panel.add(new JLabel("Nombres:"));
        panel.add(txtNombres);
        panel.add(new JLabel("Apellidos:"));
        panel.add(txtApellidos);
        panel.add(new JLabel("Carrera:"));
        panel.add(txtCarrera);
        panel.add(new JLabel("Semestre:"));
        panel.add(spnSemestre);
        panel.add(new JLabel("Usuario:"));
        panel.add(txtUsuario);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtClave);

        int result = JOptionPane.showConfirmDialog(this, panel, "Registro de Nuevo Ayudante",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String correo = txtCorreo.getText().trim();

            // Check if email is in whitelist
            Whitelist whitelist = null;
            for (Whitelist w : Sistema.getInstance().getWhitelists()) {
                if (w.getCorreoAutorizado().equalsIgnoreCase(correo) && !w.isYaRegistrado()) {
                    whitelist = w;
                    break;
                }
            }

            if (whitelist == null) {
                JOptionPane.showMessageDialog(this,
                        "El correo no está en la lista de invitados o ya fue utilizado.\nContacte a un director para recibir una invitación.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate fields
            if (txtNombres.getText().trim().isEmpty() || txtApellidos.getText().trim().isEmpty() ||
                    txtUsuario.getText().trim().isEmpty() || txtClave.getPassword().length == 0) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create new student
            AyudanteInvestigacion nuevoAyudante = new AyudanteInvestigacion(
                    txtUsuario.getText().trim(),
                    new String(txtClave.getPassword()),
                    txtNombres.getText().trim(),
                    txtApellidos.getText().trim(),
                    txtCarrera.getText().trim(),
                    (int) spnSemestre.getValue(),
                    correo);

            // Register in system
            Sistema.getInstance().getUsuarios().add(nuevoAyudante);
            whitelist.registrarAyudante(nuevoAyudante);

            // Save to file
            guardarUsuarioEnArchivo(txtUsuario.getText().trim(), new String(txtClave.getPassword()),
                    "AYUDANTE", txtNombres.getText().trim(), txtApellidos.getText().trim());

            JOptionPane.showMessageDialog(this,
                    "¡Registro exitoso!\nYa puede iniciar sesión con su usuario y contraseña.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void guardarUsuarioEnArchivo(String usuario, String clave, String rol, String nombres, String apellidos) {
        try (FileWriter fw = new FileWriter("users.txt", true)) {
            fw.write("\n" + usuario + "," + clave + "," + rol + "," + nombres + "," + apellidos);
        } catch (IOException e) {
            System.err.println("Error guardando usuario: " + e.getMessage());
        }
    }

    private void abrirDashboard(Usuario usuario) {
        this.dispose();
        if (usuario instanceof JefeDIC) {
            new DashboardJefe((JefeDIC) usuario).setVisible(true);
        } else if (usuario instanceof DirectorProyecto) {
            new DashboardDirector((DirectorProyecto) usuario).setVisible(true);
        } else if (usuario instanceof AyudanteInvestigacion) {
            new DashboardAyudante((AyudanteInvestigacion) usuario).setVisible(true);
        }
    }
}
