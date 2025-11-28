package view;
import javax.swing.*;
import java.awt.*;

public class ManageStudioPanel extends JPanel {
    public ManageStudioPanel(CardLayout cardLayout, JPanel mainPanel) {
        add(new JLabel("Halaman Kelola Studio (Coming Soon)"));
        JButton btnBack = new JButton("Kembali");
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
        add(btnBack);
    }
    public void refreshTable() {}
}