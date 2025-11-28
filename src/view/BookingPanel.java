package view;
import javax.swing.*;
import java.awt.*;

public class BookingPanel extends JPanel {
    public BookingPanel(CardLayout cardLayout, JPanel mainPanel) {
        add(new JLabel("Halaman Booking (Coming Soon)"));
        JButton btnBack = new JButton("Kembali");
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
        add(btnBack);
    }
    public void refreshData() {}
}