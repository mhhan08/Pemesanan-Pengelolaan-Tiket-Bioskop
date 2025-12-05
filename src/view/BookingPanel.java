package view;

import controller.CinemaSystemFacade;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import models.Schedule;
import models.Studio;
import models.Ticket;
import utils.TicketPrinter;


public class BookingPanel extends JPanel {
    private CinemaSystemFacade facade;

    private JComboBox<String> cbStudio;
    private JTextField txtTime;

    private JPanel seatPanel;
    private JLabel lblStatus;

    // HEADER INFO
    private JLabel lblFilm;
    private JLabel lblStudioType;
    private JLabel lblPrice;
    private JLabel lblCapacity;
    private JLabel lblBooked;
    private JLabel lblAvailable;

    private static final Color COLOR_AVAILABLE = Color.WHITE;
    private static final Color COLOR_SELECTED = new Color(255, 215, 0); // GOLD
    private static final Color COLOR_BOOKED = new Color(220, 53, 69);   // RED
    private static final Color GOLD_BORDER = new Color(212, 175, 55);

    public BookingPanel(CinemaSystemFacade facade, CardLayout layout, JPanel mainPanel) {
        this.facade = facade;

        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //TOP PANEL
        JPanel top = new JPanel(new GridLayout(2, 1));
        top.setBackground(Color.WHITE);

        // --- Baris 1: Input ---
        JPanel input = new JPanel(new GridLayout(3, 2, 10, 10));
        input.setBackground(Color.WHITE);
        input.setBorder(BorderFactory.createTitledBorder("Pilih Jadwal"));

        cbStudio = new JComboBox<>();
        txtTime = new JTextField();
        JButton btnLoad = new JButton("Muat Kursi");

        input.add(new JLabel("Studio:"));
        input.add(cbStudio);
        input.add(new JLabel("Jam Tayang (ex 14:00):"));
        input.add(txtTime);
        input.add(new JLabel(""));
        input.add(btnLoad);

        top.add(input);

        // --- Baris 2: Header Info (Film, Studio, Harga, Kursi) ---
        JPanel info = new JPanel(new GridLayout(3, 2, 5, 5));
        info.setBackground(Color.WHITE);
        info.setBorder(BorderFactory.createTitledBorder("Informasi Jadwal"));

        lblFilm = new JLabel("-");
        lblStudioType = new JLabel("-");
        lblPrice = new JLabel("-");
        lblCapacity = new JLabel("-");
        lblBooked = new JLabel("-");
        lblAvailable = new JLabel("-");

        info.add(new JLabel("Film:")); info.add(lblFilm);
        info.add(new JLabel("Tipe Studio:")); info.add(lblStudioType);
        info.add(new JLabel("Harga Final:")); info.add(lblPrice);
        info.add(new JLabel("Kapasitas:")); info.add(lblCapacity);
        info.add(new JLabel("Booked:")); info.add(lblBooked);
        info.add(new JLabel("Tersedia:")); info.add(lblAvailable);

        top.add(info);

        add(top, BorderLayout.NORTH);

        // SEAT PANEL
        seatPanel = new JPanel();
        seatPanel.setBackground(Color.WHITE);
        seatPanel.setBorder(BorderFactory.createTitledBorder("Pilih Kursi"));
        add(new JScrollPane(seatPanel), BorderLayout.CENTER);

        // BOTTOM 
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);

        lblStatus = new JLabel("Status: Menunggu…");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton btnBack = new JButton("Kembali ke Dashboard");

        bottom.add(lblStatus, BorderLayout.WEST);
        bottom.add(btnBack, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);

        // LISTENERS
        btnLoad.addActionListener(e -> loadSeats());
        btnBack.addActionListener(e -> layout.show(mainPanel, "Dashboard"));
    }

    // LOAD SEATS
   
    private void loadSeats() {

        seatPanel.removeAll();

        String studioName = (String) cbStudio.getSelectedItem();
        String time = txtTime.getText();

        if (studioName == null || time == null || time.isBlank()) {
            JOptionPane.showMessageDialog(this, "Pilih studio & isi jam!");
            return;
        }

        Studio studio = facade.findStudio(studioName);
        if (studio == null) {
            JOptionPane.showMessageDialog(this, "Studio tidak ditemukan!");
            return;
        }

        Schedule sc = studio.getSchedule(time);
        if (sc == null) {
            JOptionPane.showMessageDialog(this, "Jadwal tidak ditemukan!");
            return;
        }

        // Update Info Header
        lblFilm.setText(sc.getMovie().getTitle());
        lblStudioType.setText(studio.getClass().getSimpleName().replace("Studio", ""));
        double finalPrice = sc.calculateFinalPrice(studio.getBasePrice());
        lblPrice.setText("Rp " + finalPrice);

        // Kapasitas: gunakan getter di Studio (tambahan kecil, lihat instruksi)
        lblCapacity.setText(String.valueOf(studio.getRowCount() * studio.getColCount()));

        // gunakan seat matrix di schedule untuk hitung booked
        int booked = hitungKursiBooked(sc);
        lblBooked.setText(String.valueOf(booked));
        lblAvailable.setText(String.valueOf(sc.getRowCount() * sc.getColCount() - booked));

        // Layout kursi
        int rows = sc.getRowCount();
        int cols = sc.getColCount();
        seatPanel.setLayout(new GridLayout(rows, cols, 8, 8));

        boolean[][] seats = sc.getSeatsMatrix();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                JButton btn = new JButton(getSeatLabel(r, c));
                btn.setFocusPainted(false);
                btn.setOpaque(true);
                btn.setContentAreaFilled(true);
                btn.setBackground(seats[r][c] ? COLOR_BOOKED : COLOR_AVAILABLE);
                btn.setBorder(BorderFactory.createLineBorder(GOLD_BORDER, 2)); // gold border
                btn.setFont(new Font("Segoe UI", Font.BOLD, 12));

                final int rr = r;
                final int cc = c;

                if (!seats[r][c]) {
                    btn.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            if (btn.isEnabled())
                                btn.setBackground(COLOR_SELECTED);
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            if (btn.isEnabled())
                                btn.setBackground(COLOR_AVAILABLE);
                        }
                    });

                    btn.addActionListener(e -> processBooking(studioName, time, rr, cc, btn, sc));
                } else {
                    // already booked
                    btn.setEnabled(false);
                }

                seatPanel.add(btn);
            }
        }

        seatPanel.revalidate();
        seatPanel.repaint();
    }

    private int hitungKursiBooked(Schedule sc) {
        int count = 0;
        boolean[][] s = sc.getSeatsMatrix();
        for (boolean[] row : s)
            for (boolean seat : row)
                if (seat) count++;
        return count;
    }

    // BOOK SEAT
    private void processBooking(String studioName, String time, int r, int c, JButton btn, Schedule sc) {

        Ticket ticket = facade.bookTicket(studioName, time, r, c);

        if (ticket == null) {
            JOptionPane.showMessageDialog(this, "Gagal booking!");
            return;
        }

        // update tampilan
        btn.setBackground(COLOR_BOOKED);
        btn.setEnabled(false);

        lblStatus.setText("Berhasil pesan kursi " + ticket.getSeatCode());

        JOptionPane.showMessageDialog(this,
            "Berhasil!\n" +
            "Film: " + sc.getMovie().getTitle() + "\n" +
            "Studio: " + studioName + "\n" +
            "Kursi: " + ticket.getSeatCode() + "\n" +
            "Harga: Rp " + ticket.getFinalPrice()
        );

        // Update info booking menggunakan seat matrix di schedule
        int booked = hitungKursiBooked(sc);
        lblBooked.setText(String.valueOf(booked));
        lblAvailable.setText(String.valueOf(sc.getRowCount() * sc.getColCount() - booked));

        // refresh seatPanel
        seatPanel.revalidate();
        seatPanel.repaint();

        new TicketPrinter(ticket).printTicket();
    }

    
    private String getSeatLabel(int r, int c) {
        return "" + (char) ('A' + r) + (c + 1);
    }

    public void refreshData() {
        cbStudio.removeAllItems();
        for (String s : facade.getStudioNames())
            cbStudio.addItem(s);

        seatPanel.removeAll();
        seatPanel.revalidate();
        seatPanel.repaint();
    }
}
