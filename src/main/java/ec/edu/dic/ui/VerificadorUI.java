package ec.edu.dic.ui;

import ec.edu.dic.model.Verificador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VerificadorUI extends JFrame {

    private final List<Verificador> historial = new ArrayList<>();
    private int nextId = 1;

    private final JTextField txtIdProyecto = new JTextField(10);
    private final JCheckBox chkDoc = new JCheckBox("Documentacion completa");
    private final JCheckBox chkAyud = new JCheckBox("Ayudantes confirmados");
    private final JCheckBox chkPres = new JCheckBox("Presupuesto validado");
    private final JCheckBox chkReq = new JCheckBox("Requisitos tecnicos OK");
    private final JTextArea txtObs = new JTextArea(4, 30);

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "ID Proyecto", "Fecha", "Completitud"}, 0
    );
    private final JTable table = new JTable(model);

    public VerificadorUI() {
        super("Modulo Verificador (Basico)");
        setSize(900, 520);
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new GridLayout(3, 1));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("ID Proyecto:"));
        row1.add(txtIdProyecto);

        JButton btnGuardar = new JButton("Guardar verificacion");
        JButton btnReporte = new JButton("Ver reporte (ultimo)");
        JButton btnRefrescar = new JButton("Refrescar");

        row1.add(btnGuardar);
        row1.add(btnReporte);
        row1.add(btnRefrescar);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(chkDoc);
        row2.add(chkAyud);
        row2.add(chkPres);
        row2.add(chkReq);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row3.add(new JLabel("Observaciones:"));
        row3.add(new JScrollPane(txtObs));

        top.add(row1);
        top.add(row2);
        top.add(row3);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardar());
        btnReporte.addActionListener(e -> reporteUltimo());
        btnRefrescar.addActionListener(e -> refrescar());
    }

    private void guardar() {
        int idProyecto;
        try {
            idProyecto = Integer.parseInt(txtIdProyecto.getText().trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ID Proyecto invalido.");
            return;
        }

        Verificador v = new Verificador();
        v.id = nextId++;
        v.id_proyecto = idProyecto;
        v.documentacion_completa = chkDoc.isSelected();
        v.ayudantes_confirmados = chkAyud.isSelected();
        v.presupuesto_validado = chkPres.isSelected();
        v.requisitos_tecnicos_ok = chkReq.isSelected();
        v.fecha_verificacion = LocalDateTime.now();
        v.observaciones = txtObs.getText();

        historial.add(v);
        refrescar();

        // usa tu metodo existente
        boolean completo = v.verificarCompletitud(null); // tu metodo recibe Proyecto, pero como es cascarita pasamos null
        JOptionPane.showMessageDialog(this, completo ? "Verificacion: COMPLETA" : "Verificacion: INCOMPLETA");
    }

    private void reporteUltimo() {
        if (historial.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay verificaciones.");
            return;
        }

        Verificador v = historial.get(historial.size() - 1);

        String reporte =
                "===== REPORTE VERIFICACION =====\n" +
                        "ID Verificacion: " + v.id + "\n" +
                        "ID Proyecto: " + v.id_proyecto + "\n" +
                        "Fecha: " + v.fecha_verificacion + "\n\n" +
                        "Documentacion completa: " + (v.documentacion_completa ? "SI" : "NO") + "\n" +
                        "Ayudantes confirmados: " + (v.ayudantes_confirmados ? "SI" : "NO") + "\n" +
                        "Presupuesto validado: " + (v.presupuesto_validado ? "SI" : "NO") + "\n" +
                        "Requisitos tecnicos OK: " + (v.requisitos_tecnicos_ok ? "SI" : "NO") + "\n\n" +
                        "Observaciones:\n" + (v.observaciones == null ? "" : v.observaciones) + "\n" +
                        "===============================\n";

        JTextArea area = new JTextArea(reporte);
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Reporte", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refrescar() {
        model.setRowCount(0);
        for (Verificador v : historial) {
            boolean completo = v.documentacion_completa && v.ayudantes_confirmados && v.presupuesto_validado && v.requisitos_tecnicos_ok;
            model.addRow(new Object[]{
                    v.id,
                    v.id_proyecto,
                    v.fecha_verificacion,
                    completo ? "SI" : "NO"
            });
        }
    }
}
