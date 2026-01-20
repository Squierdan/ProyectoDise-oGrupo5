package ec.edu.dic.ui;

import ec.edu.dic.model.Whitelist;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WhitelistUI extends JFrame {

    // Lista en memoria (cascarita) - luego se conecta a BD/servicio real
    private final List<Whitelist> lista = new ArrayList<>();
    private int nextId = 1;

    private final JTextField txtCorreo = new JTextField(25);
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Correo", "Fecha invitacion", "Ya registrado"}, 0
    );
    private final JTable table = new JTable(model);

    public WhitelistUI() {
        super("Whitelist");
        setSize(850, 420);
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Correo autorizado:"));
        top.add(txtCorreo);

        JButton btnAutorizar = new JButton("Autorizar");
        JButton btnPermite = new JButton("Permite registro?");
        JButton btnMarcar = new JButton("Marcar registrado");
        JButton btnRefrescar = new JButton("Refrescar");

        top.add(btnAutorizar);
        top.add(btnPermite);
        top.add(btnMarcar);
        top.add(btnRefrescar);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnAutorizar.addActionListener(e -> autorizarCorreo());
        btnPermite.addActionListener(e -> permiteRegistro());
        btnMarcar.addActionListener(e -> marcarRegistrado());
        btnRefrescar.addActionListener(e -> refrescar());

        // demo inicial
        agregarWhitelist("demo@epn.edu.ec");
        refrescar();
    }

    private void autorizarCorreo() {
        String correo = txtCorreo.getText().trim().toLowerCase();
        if (correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un correo.");
            return;
        }
        if (buscarPorCorreo(correo) != null) {
            JOptionPane.showMessageDialog(this, "Ese correo ya esta autorizado.");
            return;
        }

        agregarWhitelist(correo);
        refrescar();
        JOptionPane.showMessageDialog(this, "Correo autorizado.");
    }

    private void permiteRegistro() {
        String correo = txtCorreo.getText().trim().toLowerCase();
        Whitelist w = buscarPorCorreo(correo);
        boolean ok = (w != null && !w.ya_registrado);

        JOptionPane.showMessageDialog(this, ok ? "SI permite registro" : "NO permite registro");
    }

    private void marcarRegistrado() {
        String correo = txtCorreo.getText().trim().toLowerCase();
        Whitelist w = buscarPorCorreo(correo);
        if (w == null) {
            JOptionPane.showMessageDialog(this, "Correo no encontrado en whitelist.");
            return;
        }
        if (w.ya_registrado) {
            JOptionPane.showMessageDialog(this, "Ya estaba marcado como registrado.");
            return;
        }

        w.ya_registrado = true;
        refrescar();
        JOptionPane.showMessageDialog(this, "Marcado como registrado.");
    }

    private void agregarWhitelist(String correo) {
        Whitelist w = new Whitelist();
        w.id = nextId++;
        w.correo_autorizado = correo;
        w.fecha_invitacion = LocalDateTime.now();
        w.ya_registrado = false;
        lista.add(w);
    }

    private Whitelist buscarPorCorreo(String correo) {
        for (Whitelist w : lista) {
            if (w.correo_autorizado != null && w.correo_autorizado.equalsIgnoreCase(correo)) {
                return w;
            }
        }
        return null;
    }

    private void refrescar() {
        model.setRowCount(0);
        for (Whitelist w : lista) {
            model.addRow(new Object[]{
                    w.id,
                    w.correo_autorizado,
                    w.fecha_invitacion,
                    w.ya_registrado ? "SI" : "NO"
            });
        }
    }
}
