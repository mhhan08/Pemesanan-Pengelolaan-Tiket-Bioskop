package view;
import javax.swing.*;
import java.awt.*;

public class ManageMoviePanel extends JPanel {
    public ManageMoviePanel(CardLayout cardLayout, JPanel mainPanel) {
        add(new JLabel("Halaman Kelola Film (Coming Soon)"));
        JButton btnBack = new JButton("Kembali");
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
        add(btnBack);
    }
    public void refreshTable() {} // dummy method
}