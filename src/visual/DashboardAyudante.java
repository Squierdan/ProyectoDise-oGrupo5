package visual;

import logic.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class DashboardAyudante extends JFrame {
    private AyudanteInvestigacion ayudante;
    private Color primaryColor = new Color(52, 152, 219);
    private Color successColor = new Color(39, 174, 96);
    private Color dangerColor = new Color(231, 76, 60);
    private Color bgColor = new Color(236, 240, 241);
    private Color cardColor = Color.WHITE;
    private Color textColor = new Color(44, 62, 80);

    public DashboardAyudante(AyudanteInvestigacion ayudante) {
        this.ayudante = ayudante;
        setTitle("Dashboard Ayudante - " + ayudante.getNombreCompleto());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);

        mainPanel.add(createHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(cardColor);
        tabs.setForeground(textColor);

        tabs.addTab("🏠 Mi Dashboard", createDashboardPanel());
        tabs.addTab("📄 Mis Informes", createPanelInformes());
        tabs.addTab("📝 Registrar Informe", createPanelNuevoInforme());
        tabs.addTab("⚙️ Configuración", createPanelConfiguracion());

        mainPanel.add(tabs, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(successColor);
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel title = new JLabel("Panel de Ayudante de Investigación");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);

        JLabel statusLabel = new JLabel(
                ayudante.getEstadoAyudante() == EstadoAyudante.ACTIVO ? "🟢 ACTIVO" : "🔴 INACTIVO");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel("👤 " + ayudante.getNombreCompleto());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);

        JButton btnLogout = createStyledButton("Cerrar Sesión", dangerColor);
        btnLogout.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });

        userPanel.add(statusLabel);
        userPanel.add(Box.createHorizontalStrut(15));
        userPanel.add(userLabel);
        userPanel.add(Box.createHorizontalStrut(15));
        userPanel.add(btnLogout);
        header.add(userPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createDashboardPanel() {
        JPanel p = new JPanel(new BorderLayout(20, 20));
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setOpaque(false);

        cardsPanel.add(createInfoCard("👤 Datos Personales",
                "Nombre: " + ayudante.getNombreCompleto() + "\n" +
                        "Carrera: " + ayudante.getCarrera() + "\n" +
                        "Semestre: " + ayudante.getSemestre()));

        Proyecto pro = ayudante.getProyectoAsignado();
        cardsPanel.add(createInfoCard("📁 Proyecto Asignado",
                pro != null ? "Proyecto: " + pro.getNombre() + "\n" +
                        "Director: " + (pro.getDirector() != null ? pro.getDirector().getNombreCompleto() : "N/A")
                        + "\n" +
                        "Estado: " + pro.getEstado()
                        : "No tienes proyecto asignado"));

        cardsPanel.add(createInfoCard("📊 Estadísticas",
                "Informes entregados: " + ayudante.getInformes().size() + "\n" +
                        "Visibilidad: " + (ayudante.isVisiblePublicamente() ? "Público" : "Privado") + "\n" +
                        "Estado: " + ayudante.getEstadoAyudante()));

        p.add(cardsPanel, BorderLayout.NORTH);

        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(cardColor);
        welcomePanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel welcomeTitle = new JLabel("¡Bienvenido, " + ayudante.getNombres() + "!");
        welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeTitle.setForeground(successColor);
        welcomePanel.add(welcomeTitle, BorderLayout.NORTH);

        JTextArea welcomeText = new JTextArea(
                "\nDesde este panel puedes:\n\n" +
                        "• Ver información de tu proyecto asignado\n" +
                        "• Registrar y enviar informes de actividades\n" +
                        "• Consultar el estado de tus informes anteriores\n" +
                        "• Configurar tu visibilidad y preferencias\n\n" +
                        "Recuerda enviar tus informes antes de la fecha límite.");
        welcomeText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeText.setBackground(cardColor);
        welcomeText.setForeground(textColor);
        welcomeText.setEditable(false);
        welcomePanel.add(welcomeText, BorderLayout.CENTER);

        p.add(welcomePanel, BorderLayout.CENTER);

        return p;
    }

    private JPanel createPanelInformes() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] columns = { "ID", "Fecha", "Estado", "Revisado Director", "Revisado Jefe", "Feedback" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Informe inf : ayudante.getInformes()) {
            model.addRow(new Object[] {
                    inf.getId(),
                    inf.getFecha(),
                    inf.getEstado(),
                    inf.isRevisadoPorDirector() ? "✓" : "✗",
                    inf.isRevisadoPorJefe() ? "✓" : "✗",
                    inf.getFeedbackDirector().isEmpty() ? "-" : inf.getFeedbackDirector()
            });
        }

        JTable table = new JTable(model);
        styleTable(table);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        return p;
    }

    private JPanel createPanelNuevoInforme() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(30, 50, 30, 50));

        if (ayudante.getProyectoAsignado() == null) {
            JLabel noProject = new JLabel("No tienes proyecto asignado. No puedes registrar informes.",
                    SwingConstants.CENTER);
            noProject.setFont(new Font("Segoe UI", Font.BOLD, 16));
            noProject.setForeground(dangerColor);
            p.add(noProject, BorderLayout.CENTER);
            return p;
        }

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(cardColor);
        formPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Registrar Nuevo Informe de Actividades");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(successColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        JLabel lblActividades = new JLabel("Descripción de Actividades:");
        lblActividades.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblActividades.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblActividades, gbc);

        JTextArea txtActividades = new JTextArea(5, 30);
        txtActividades.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtActividades.setForeground(textColor);
        txtActividades.setLineWrap(true);
        txtActividades.setWrapStyleWord(true);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(new JScrollPane(txtActividades), gbc);

        JLabel lblArchivo = new JLabel("URL del Archivo (opcional):");
        lblArchivo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblArchivo.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        formPanel.add(lblArchivo, gbc);

        JTextField txtArchivo = new JTextField(30);
        txtArchivo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtArchivo.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(txtArchivo, gbc);

        JButton btnEnviar = createStyledButton("📤 Enviar Informe", successColor);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 10, 10);
        formPanel.add(btnEnviar, gbc);

        btnEnviar.addActionListener(e -> {
            String actividades = txtActividades.getText().trim();
            if (actividades.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar la descripción de actividades", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Informe nuevoInforme = new Informe(TipoInforme.ACTIVIDADES_AYUDANTE, actividades, ayudante,
                    ayudante.getProyectoAsignado());
            nuevoInforme.setArchivoUrl(txtArchivo.getText().trim());
            nuevoInforme.setEstado(EstadoInforme.PENDIENTE_DIRECTOR);
            ayudante.registrarInforme(nuevoInforme);

            JOptionPane.showMessageDialog(this, "¡Informe enviado exitosamente!", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            txtActividades.setText("");
            txtArchivo.setText("");
        });

        p.add(formPanel, BorderLayout.CENTER);
        return p;
    }

    private JPanel createPanelConfiguracion() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(30, 50, 30, 50));

        JPanel configPanel = new JPanel(new GridLayout(0, 1, 10, 15));
        configPanel.setBackground(cardColor);
        configPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Configuración de Cuenta");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(successColor);
        configPanel.add(titleLabel);

        JCheckBox chkVisibilidad = new JCheckBox("Perfil visible públicamente");
        chkVisibilidad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkVisibilidad.setForeground(textColor);
        chkVisibilidad.setBackground(cardColor);
        chkVisibilidad.setSelected(ayudante.isVisiblePublicamente());
        chkVisibilidad.addActionListener(e -> {
            ayudante.cambiarVisibilidad();
            JOptionPane.showMessageDialog(this,
                    "Visibilidad actualizada: " + (ayudante.isVisiblePublicamente() ? "Público" : "Privado"));
        });
        configPanel.add(chkVisibilidad);

        JButton btnBaja = createStyledButton("Solicitar Baja del Proyecto", dangerColor);
        btnBaja.addActionListener(e -> {
            if (ayudante.getProyectoAsignado() == null) {
                JOptionPane.showMessageDialog(this, "No tienes proyecto asignado");
                return;
            }
            String motivo = JOptionPane.showInputDialog(this, "Ingrese el motivo de la baja:");
            if (motivo != null && !motivo.isEmpty()) {
                Proyecto pro = ayudante.getProyectoAsignado();
                ayudante.darseDeBaja(pro, motivo);
                JOptionPane.showMessageDialog(this, "Solicitud de baja procesada. Estado: INACTIVO");
            }
        });
        configPanel.add(btnBaja);

        p.add(configPanel, BorderLayout.NORTH);
        return p;
    }

    private JPanel createInfoCard(String title, String content) {
        JPanel card = new JPanel(new BorderLayout(5, 10));
        card.setBackground(cardColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(successColor);
        card.add(lblTitle, BorderLayout.NORTH);

        JTextArea txtContent = new JTextArea(content);
        txtContent.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtContent.setBackground(cardColor);
        txtContent.setForeground(textColor);
        txtContent.setEditable(false);
        card.add(txtContent, BorderLayout.CENTER);

        return card;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setBackground(cardColor);
        table.setForeground(textColor);
        table.setSelectionBackground(new Color(171, 235, 198));
        table.setSelectionForeground(textColor);
        table.setGridColor(new Color(220, 220, 220));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(30, 132, 73));
        table.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bgColor);
        btn.setForeground(textColor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
