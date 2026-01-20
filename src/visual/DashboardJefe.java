package visual;

import logic.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class DashboardJefe extends JFrame {
    private JefeDIC jefe;
    private Color primaryColor = new Color(52, 152, 219);
    private Color successColor = new Color(39, 174, 96);
    private Color dangerColor = new Color(231, 76, 60);
    private Color bgColor = new Color(236, 240, 241);
    private Color cardColor = Color.WHITE;
    private Color textColor = new Color(44, 62, 80);

    public DashboardJefe(JefeDIC jefe) {
        this.jefe = jefe;
        setTitle("Dashboard Jefe DIC - " + jefe.getNombreCompleto());
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);

        mainPanel.add(createHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(cardColor);
        tabs.setForeground(textColor);

        tabs.addTab("📊 Proyectos", createPanelProyectos());
        tabs.addTab("📋 Solicitudes", createPanelSolicitudes());
        tabs.addTab("👥 Directores", createPanelDirectores());
        tabs.addTab("🎓 Ayudantes", createPanelAyudantes());
        tabs.addTab("📄 Informes", createPanelInformes());

        mainPanel.add(tabs, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(primaryColor);
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel title = new JLabel("Panel de Control - Jefe DIC");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel("👤 " + jefe.getNombreCompleto());
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

        String[] columns = { "ID", "Nombre", "Director", "Estado", "Tipo", "Ayudantes", "Progreso" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Proyecto> proyectos = Sistema.getInstance().getProyectos();
        for (Proyecto pro : proyectos) {
            model.addRow(new Object[] {
                    pro.getId(),
                    pro.getNombre(),
                    pro.getDirector() != null ? pro.getDirector().getNombreCompleto() : "Sin asignar",
                    pro.getEstado(),
                    pro.getTipoProyecto(),
                    pro.getAyudantesActuales() + "/" + pro.getCantidadAyudantes(),
                    String.format("%.1f%%", pro.calcularProgreso())
            });
        }

        JTable table = new JTable(model);
        styleTable(table);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        statsPanel.setOpaque(false);
        statsPanel.add(createStatCard("Total Proyectos", String.valueOf(proyectos.size())));
        statsPanel.add(createStatCard("Activos", String.valueOf(jefe.consultarProyectosActivos().size())));
        int pendientes = Sistema.getInstance().getSolicitudesPendientes().size();
        statsPanel.add(createStatCard("Solicitudes Pend.", String.valueOf(pendientes)));
        p.add(statsPanel, BorderLayout.NORTH);

        return p;
    }

    private JPanel createPanelSolicitudes() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] columns = { "ID", "Director", "Ayudante", "Proyecto", "Fecha", "Estado" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<SolicitudAyudante> solicitudes = Sistema.getInstance().getSolicitudes();
        for (SolicitudAyudante s : solicitudes) {
            model.addRow(new Object[] {
                    s.getId(),
                    s.getDirector().getNombreCompleto(),
                    s.getAyudante().getNombreCompleto(),
                    s.getProyecto().getNombre(),
                    s.getFechaSolicitud().toLocalDate(),
                    s.isProcesada() ? (s.isAprobada() ? "APROBADA" : "RECHAZADA") : "PENDIENTE"
            });
        }

        JTable table = new JTable(model);
        styleTable(table);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton btnAprobar = createStyledButton("✓ Aprobar", successColor);
        btnAprobar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                SolicitudAyudante sol = solicitudes.get(row);
                if (!sol.isProcesada()) {
                    sol.aprobar();
                    model.setValueAt("APROBADA", row, 5);
                    JOptionPane.showMessageDialog(this, "Solicitud aprobada. Ayudante agregado al proyecto.");
                } else {
                    JOptionPane.showMessageDialog(this, "Esta solicitud ya fue procesada.");
                }
            }
        });

        JButton btnRechazar = createStyledButton("✗ Rechazar", dangerColor);
        btnRechazar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                SolicitudAyudante sol = solicitudes.get(row);
                if (!sol.isProcesada()) {
                    String motivo = JOptionPane.showInputDialog(this, "Motivo del rechazo:");
                    if (motivo != null) {
                        sol.rechazar(motivo);
                        model.setValueAt("RECHAZADA", row, 5);
                        JOptionPane.showMessageDialog(this, "Solicitud rechazada.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Esta solicitud ya fue procesada.");
                }
            }
        });

        btnPanel.add(btnAprobar);
        btnPanel.add(btnRechazar);
        p.add(btnPanel, BorderLayout.SOUTH);

        return p;
    }

    private JPanel createPanelDirectores() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] columns = { "Nombre", "Correo", "Departamento", "Proyectos" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (DirectorProyecto d : jefe.visualizarDirectores()) {
            model.addRow(new Object[] {
                    d.getNombreCompleto(),
                    d.getCorreoInstitucional(),
                    d.getDepartamento(),
                    d.getProyectos().size()
            });
        }

        JTable table = new JTable(model);
        styleTable(table);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private JPanel createPanelAyudantes() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] columns = { "Nombre", "Carrera", "Semestre", "Estado", "Proyecto" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (AyudanteInvestigacion a : jefe.visualizarAyudantes()) {
            model.addRow(new Object[] {
                    a.getNombreCompleto(),
                    a.getCarrera(),
                    a.getSemestre(),
                    a.getEstadoAyudante(),
                    a.getProyectoAsignado() != null ? a.getProyectoAsignado().getNombre() : "Sin asignar"
            });
        }

        JTable table = new JTable(model);
        styleTable(table);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private JPanel createPanelInformes() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] columns = { "ID", "Tipo", "Fecha", "Estado", "Autor", "Proyecto" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Informe inf : jefe.visualizarInformes()) {
            model.addRow(new Object[] {
                    inf.getId(),
                    inf.getTipo(),
                    inf.getFecha(),
                    inf.getEstado(),
                    inf.getAutor() != null ? inf.getAutor().getNombreCompleto() : "N/A",
                    inf.getProyecto().getNombre()
            });
        }

        JTable table = new JTable(model);
        styleTable(table);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        JButton btnFirmar = createStyledButton("Firmar Seleccionado", successColor);
        btnFirmar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Informe inf = jefe.visualizarInformes().get(row);
                jefe.firmarInforme(inf);
                model.setValueAt(inf.getEstado(), row, 3);
                JOptionPane.showMessageDialog(this, "Informe firmado correctamente");
            }
        });
        btnPanel.add(btnFirmar);
        p.add(btnPanel, BorderLayout.SOUTH);

        return p;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setBackground(cardColor);
        table.setForeground(textColor);
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(textColor);
        table.setGridColor(new Color(220, 220, 220));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(cardColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 25, 15, 25)));

        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setForeground(primaryColor);
        card.add(lblValue, BorderLayout.CENTER);

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitle.setForeground(new Color(127, 140, 141));
        card.add(lblTitle, BorderLayout.SOUTH);

        return card;
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
