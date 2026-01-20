package visual;

import logic.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class DashboardDirector extends JFrame {
    private DirectorProyecto director;
    private Color primaryColor = new Color(52, 152, 219);
    private Color successColor = new Color(39, 174, 96);
    private Color dangerColor = new Color(231, 76, 60);
    private Color warningColor = new Color(243, 156, 18);
    private Color bgColor = new Color(236, 240, 241);
    private Color cardColor = Color.WHITE;
    private Color textColor = new Color(44, 62, 80);

    public DashboardDirector(DirectorProyecto director) {
        this.director = director;
        setTitle("Dashboard Director - " + director.getNombreCompleto());
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);

        mainPanel.add(createHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(cardColor);
        tabs.setForeground(textColor);

        tabs.addTab("📁 Mis Proyectos", createPanelProyectos());
        tabs.addTab("🎓 Solicitar Ayudante", createPanelSolicitarAyudante());
        tabs.addTab("✉️ Invitaciones", createPanelWhitelist());
        tabs.addTab("📄 Informes", createPanelInformes());

        mainPanel.add(tabs, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(155, 89, 182));
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel title = new JLabel("Panel de Director de Proyecto");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel("👤 " + director.getNombreCompleto() + " | " + director.getDepartamento());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);

        JButton btnLogout = createStyledButton("Cerrar Sesión", dangerColor);
        btnLogout.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });

        userPanel.add(userLabel);
        userPanel.add(Box.createHorizontalStrut(15));
        userPanel.add(btnLogout);
        header.add(userPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createPanelProyectos() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] columns = { "ID", "Nombre", "Estado", "Tipo", "Cupos", "Progreso" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Proyecto pro : director.getProyectos()) {
            int cuposDisp = director.visualizarCantidadCuposDisponibles(pro);
            model.addRow(new Object[] {
                    pro.getId(),
                    pro.getNombre(),
                    pro.getEstado(),
                    pro.getTipoProyecto(),
                    pro.getAyudantesActuales() + "/" + pro.getCantidadAyudantes() + " (Disp: " + cuposDisp + ")",
                    String.format("%.1f%%", pro.calcularProgreso())
            });
        }

        JTable table = new JTable(model);
        styleTable(table);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton btnNuevo = createStyledButton("+ Nuevo Proyecto", successColor);
        btnNuevo.addActionListener(e -> mostrarDialogoNuevoProyecto(model));
        btnPanel.add(btnNuevo);

        p.add(btnPanel, BorderLayout.SOUTH);
        return p;
    }

    private void mostrarDialogoNuevoProyecto(DefaultTableModel model) {
        JTextField txtNombre = new JTextField(20);
        JTextArea txtDescripcion = new JTextArea(3, 20);
        JComboBox<TipoProyecto> cmbTipo = new JComboBox<>(TipoProyecto.values());
        JSpinner spnAyudantes = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        JSpinner spnPresupuesto = new JSpinner(new SpinnerNumberModel(1000.0, 0, 100000, 500));

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Descripción:"));
        panel.add(new JScrollPane(txtDescripcion));
        panel.add(new JLabel("Tipo:"));
        panel.add(cmbTipo);
        panel.add(new JLabel("Cant. Ayudantes:"));
        panel.add(spnAyudantes);
        panel.add(new JLabel("Presupuesto:"));
        panel.add(spnPresupuesto);

        int result = JOptionPane.showConfirmDialog(this, panel, "Nuevo Proyecto",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Proyecto nuevo = director.crearProyecto(
                    txtNombre.getText(),
                    txtDescripcion.getText(),
                    (TipoProyecto) cmbTipo.getSelectedItem(),
                    (int) spnAyudantes.getValue(),
                    ((Double) spnPresupuesto.getValue()).floatValue());
            Sistema.getInstance().agregarProyecto(nuevo);
            model.addRow(new Object[] {
                    nuevo.getId(), nuevo.getNombre(), nuevo.getEstado(),
                    nuevo.getTipoProyecto(),
                    "0/" + nuevo.getCantidadAyudantes() + " (Disp: " + nuevo.getCantidadAyudantes() + ")",
                    "0.0%"
            });
            JOptionPane.showMessageDialog(this, "Proyecto creado exitosamente");
        }
    }

    private JPanel createPanelSolicitarAyudante() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(cardColor);
        formPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Solicitar Agregar Ayudante a Proyecto");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(155, 89, 182));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        JLabel infoLabel = new JLabel("(Las solicitudes deben ser aprobadas por el Jefe DIC)");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(127, 140, 141));
        gbc.gridy = 1;
        formPanel.add(infoLabel, gbc);

        gbc.gridwidth = 1;

        // Proyecto selector
        JLabel lblProyecto = new JLabel("Proyecto:");
        lblProyecto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblProyecto.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lblProyecto, gbc);

        JComboBox<Proyecto> cmbProyecto = new JComboBox<>();
        for (Proyecto pro : director.getProyectos()) {
            cmbProyecto.addItem(pro);
        }
        cmbProyecto.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Proyecto) {
                    setText(((Proyecto) value).getNombre());
                }
                return this;
            }
        });
        gbc.gridx = 1;
        formPanel.add(cmbProyecto, gbc);

        // Ayudante selector
        JLabel lblAyudante = new JLabel("Ayudante Disponible:");
        lblAyudante.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblAyudante.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(lblAyudante, gbc);

        JComboBox<AyudanteInvestigacion> cmbAyudante = new JComboBox<>();
        for (AyudanteInvestigacion ay : Sistema.getInstance().getAyudantesSinProyecto()) {
            cmbAyudante.addItem(ay);
        }
        cmbAyudante.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof AyudanteInvestigacion) {
                    setText(((AyudanteInvestigacion) value).getNombreCompleto());
                }
                return this;
            }
        });
        gbc.gridx = 1;
        formPanel.add(cmbAyudante, gbc);

        JButton btnSolicitar = createStyledButton("📤 Enviar Solicitud al Jefe DIC", primaryColor);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 10, 10);
        formPanel.add(btnSolicitar, gbc);

        btnSolicitar.addActionListener(e -> {
            Proyecto pro = (Proyecto) cmbProyecto.getSelectedItem();
            AyudanteInvestigacion ay = (AyudanteInvestigacion) cmbAyudante.getSelectedItem();

            if (pro == null || ay == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un proyecto y un ayudante", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            director.solicitarAgregarAyudante(ay, pro);
            JOptionPane.showMessageDialog(this, "Solicitud enviada al Jefe DIC para aprobación.\nAyudante: "
                    + ay.getNombreCompleto() + "\nProyecto: " + pro.getNombre());
            cmbAyudante.removeItem(ay);
        });

        p.add(formPanel, BorderLayout.NORTH);
        return p;
    }

    private JPanel createPanelWhitelist() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] columns = { "ID", "Correo Autorizado", "Proyecto", "Fecha Invitación", "Registrado" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Whitelist w : director.getWhitelists()) {
            model.addRow(new Object[] {
                    w.getId(),
                    w.getCorreoAutorizado(),
                    w.getProyectoAsignado() != null ? w.getProyectoAsignado().getNombre() : "-",
                    w.getFechaInvitacion().toLocalDate(),
                    w.isYaRegistrado() ? "Sí" : "No"
            });
        }

        JTable table = new JTable(model);
        styleTable(table);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        JButton btnInvitar = createStyledButton("+ Invitar Estudiante", successColor);
        btnInvitar.addActionListener(e -> {
            if (director.getProyectos().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No tiene proyectos. Cree uno primero.");
                return;
            }

            JTextField txtCorreo = new JTextField(20);
            JComboBox<Proyecto> cmbProyecto = new JComboBox<>();
            for (Proyecto pro : director.getProyectos()) {
                cmbProyecto.addItem(pro);
            }
            cmbProyecto.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Proyecto) {
                        setText(((Proyecto) value).getNombre());
                    }
                    return this;
                }
            });

            JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
            panel.add(new JLabel("Correo del estudiante:"));
            panel.add(txtCorreo);
            panel.add(new JLabel("Proyecto a asignar:"));
            panel.add(cmbProyecto);

            int result = JOptionPane.showConfirmDialog(this, panel, "Invitar Estudiante",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION && !txtCorreo.getText().trim().isEmpty()) {
                String correo = txtCorreo.getText().trim();
                Proyecto pro = (Proyecto) cmbProyecto.getSelectedItem();
                Whitelist w = director.invitarAyudante(correo, pro);
                Sistema.getInstance().agregarWhitelist(w);
                model.addRow(new Object[] { w.getId(), w.getCorreoAutorizado(), pro.getNombre(),
                        w.getFechaInvitacion().toLocalDate(), "No" });
                JOptionPane.showMessageDialog(this, "Invitación enviada a: " + correo + "\nProyecto: " + pro.getNombre()
                        + "\nEl estudiante puede registrarse desde login.");
            }
        });
        btnPanel.add(btnInvitar);
        p.add(btnPanel, BorderLayout.SOUTH);

        return p;
    }

    private JPanel createPanelInformes() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] columns = { "ID", "Tipo", "Fecha", "Estado", "Autor", "Revisado" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Proyecto pro : director.getProyectos()) {
            for (Informe inf : pro.getInformes()) {
                model.addRow(new Object[] {
                        inf.getId(),
                        inf.getTipo(),
                        inf.getFecha(),
                        inf.getEstado(),
                        inf.getAutor() != null ? inf.getAutor().getNombreCompleto() : "Director",
                        inf.isRevisadoPorDirector() ? "✓" : "✗"
                });
            }
        }

        JTable table = new JTable(model);
        styleTable(table);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);

        JButton btnRevisar = createStyledButton("Revisar Informe", primaryColor);
        btnRevisar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String feedback = JOptionPane.showInputDialog(this, "Ingrese feedback:");
                if (feedback != null) {
                    int idx = 0;
                    outer: for (Proyecto pro : director.getProyectos()) {
                        for (Informe inf : pro.getInformes()) {
                            if (idx == row) {
                                director.revisarInforme(inf, feedback);
                                model.setValueAt(inf.getEstado(), row, 3);
                                model.setValueAt("✓", row, 5);
                                break outer;
                            }
                            idx++;
                        }
                    }
                    JOptionPane.showMessageDialog(this, "Informe revisado");
                }
            }
        });

        JButton btnGenerar = createStyledButton("Generar Informe Proyecto", warningColor);
        btnGenerar.addActionListener(e -> {
            if (!director.getProyectos().isEmpty()) {
                Proyecto pro = director.getProyectos().get(0);
                Informe nuevo = director.generarInformeProyecto(pro);
                model.addRow(new Object[] {
                        nuevo.getId(), nuevo.getTipo(), nuevo.getFecha(),
                        nuevo.getEstado(), "Director", "✗"
                });
                JOptionPane.showMessageDialog(this, "Informe de proyecto generado");
            }
        });

        btnPanel.add(btnGenerar);
        btnPanel.add(btnRevisar);
        p.add(btnPanel, BorderLayout.SOUTH);

        return p;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setBackground(cardColor);
        table.setForeground(textColor);
        table.setSelectionBackground(new Color(210, 180, 222));
        table.setSelectionForeground(textColor);
        table.setGridColor(new Color(220, 220, 220));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(142, 68, 173));
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
