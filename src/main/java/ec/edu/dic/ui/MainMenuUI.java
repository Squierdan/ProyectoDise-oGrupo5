package ec.edu.dic.ui;

import javax.swing.*;
import java.awt.*;

public class MainMenuUI extends JFrame {

    public MainMenuUI() {
        super("Sistema DIC - Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 250);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Seleccione un modulo", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnWhitelist = new JButton("Whitelist");
        JButton btnVerificador = new JButton("Verificador");

        btnWhitelist.addActionListener(e -> new WhitelistUI().setVisible(true));
        btnVerificador.addActionListener(e -> new VerificadorUI().setVisible(true));

        JPanel center = new JPanel(new GridLayout(2, 1, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        center.add(btnWhitelist);
        center.add(btnVerificador);

        add(title, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenuUI().setVisible(true));
    }
}
