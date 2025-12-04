package view;

import controller.CinemaSystemFacade;
import java.awt.*;
import javax.swing.*;
import models.Studio;
import models.Ticket;


public class BookingPanel extends JPanel {
    private CinemaSystemFacade facade;
    private JComboBox<String> cbStudio;
    private JTextField txtTime;
    private JPanel seatPanel;
    private JLabel lblStatus;

    public BookingPanel(CinemaSystemFacade facade, CardLayout cardLayout, JPanel mainPanel) {
        this.facade = facade;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //HEADER
        JPanel topPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Pilih Jadwal"));
        
        cbStudio = new JComboBox<>();
        txtTime = new JTextField();
        JButton btnLoad = new JButton("Muat Kursi");

        topPanel.add(new JLabel("Pilih Studio:")); topPanel.add(cbStudio);
        topPanel.add(new JLabel("Ketik Jam Tayang (ex: 14:00):")); topPanel.add(txtTime);
        topPanel.add(new JLabel("")); topPanel.add(btnLoad);

        add(topPanel, BorderLayout.NORTH);

        //SEAT GRID (Tengah)
        seatPanel = new JPanel();
        seatPanel.setBorder(BorderFactory.createTitledBorder("Denah Kursi (Klik untuk Pesan)"));
        add(new JScrollPane(seatPanel), BorderLayout.CENTER);

        //BOTTOM
        JPanel bottom = new JPanel(new BorderLayout());
        lblStatus = new JLabel("Status: Siap");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JButton btnBack = new JButton("Kembali ke Dashboard");

        bottom.add(lblStatus, BorderLayout.WEST);
        bottom.add(btnBack, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        //LOGIC
        btnLoad.addActionListener(e -> loadSeats());
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
    }

    private void loadSeats() {
        seatPanel.removeAll();
        String studioName = (String) cbStudio.getSelectedItem();
        String time = txtTime.getText();

        Studio studio = facade.findStudio(studioName);
        if (studio == null) {
            JOptionPane.showMessageDialog(this, "Studio tidak ditemukan!");
            return;
        }

        // Buat Grid Layout sesuai dimensi studio
        int rows = studio.getRowCount();
        int cols = studio.getColCount();
        seatPanel.setLayout(new GridLayout(rows, cols, 5, 5));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JButton btnSeat = new JButton(getSeatLabel(i, j));

                // Cek apakah kursi sudah dibooking 
            
                
                final int r = i;
                final int c = j;
                
                // Warnai tombol berdasarkan status (Logic sederhana)
                
                btnSeat.setBackground(new Color(46, 204, 113)); // Hijau
                
                // Listener Klik
                btnSeat.addActionListener(e -> processBooking(studioName, time, r, c, btnSeat));
                
                seatPanel.add(btnSeat);
            }
        }
        seatPanel.revalidate();
        seatPanel.repaint();
    }

    private void processBooking(String studio, String time, int r, int c, JButton btn) {
        Ticket ticket = facade.bookTicket(studio, time, r, c);
        if (ticket != null) {
            btn.setBackground(new Color(231, 76, 60)); // Merah
            btn.setEnabled(false);
            lblStatus.setText("Berhasil! Tiket: " + ticket.getSeatCode() + " | Rp " + ticket.getFinalPrice());

            JOptionPane.showMessageDialog(this, 
                "Booking Sukses!\nID: " + ticket.getBookingId() +
                "\nKursi: " + ticket.getSeatCode() +
                "\nHarga: " + ticket.getFinalPrice());
        } else {
            JOptionPane.showMessageDialog(this, "Gagal! Kursi penuh atau Jadwal salah.");
        }
    }

    private String getSeatLabel(int r, int c) {
        char rowChar = (char)('A' + r);
        return "" + rowChar + (c + 1);
    }

    public void refreshData() {
        cbStudio.removeAllItems();
        for(String s : facade.getStudioNames()) cbStudio.addItem(s);
        seatPanel.removeAll();
        seatPanel.revalidate();
        seatPanel.repaint();
    }
}