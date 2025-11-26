package view;

import controller.CinemaSystemFacade;
import java.awt.*;
import javax.swing.*;
import models.Schedule;
import models.Studio;
import models.Ticket;

public class BookingPanel extends JPanel {

    private CinemaSystemFacade facade;

    private JComboBox<String> studioCombo;
    private JComboBox<String> scheduleCombo;
    private JButton seatButton;
    private JButton bookButton;
    private JTextField selectedSeatField;
    private JTextField priceField;

    // pilih kursi 
    private int selectedRow = -1;
    private int selectedCol = -1;

    public BookingPanel(CinemaSystemFacade facade) {
        this.facade = facade;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Pemesanan Tiket", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 0;
        add(title, gbc);

        gbc.gridwidth = 1;

        // Studio Dropdown
        JLabel studioLabel = new JLabel("Pilih Studio:");
        gbc.gridx = 0; gbc.gridy = 1;
        add(studioLabel, gbc);

        studioCombo = new JComboBox<>(facade.getStudioNames().toArray(new String[0]));
        gbc.gridx = 1; gbc.gridy = 1;
        add(studioCombo, gbc);

        // Jadwal Dropdown
        JLabel scheduleLabel = new JLabel("Pilih Jadwal:");
        gbc.gridx = 0; gbc.gridy = 2;
        add(scheduleLabel, gbc);

        scheduleCombo = new JComboBox<>();
        gbc.gridx = 1; gbc.gridy = 2;
        add(scheduleCombo, gbc);

        loadSchedules(); // load awal sesuai studio pertama

        // auto reload schedule ketika user ganti studio
        studioCombo.addActionListener(e -> loadSchedules());

        // pilih kursi
        JLabel seatLabel = new JLabel("Kursi Dipilih:");
        gbc.gridx = 0; gbc.gridy = 3;
        add(seatLabel, gbc);

        selectedSeatField = new JTextField();
        selectedSeatField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 3;
        add(selectedSeatField, gbc);

        seatButton = new JButton("Pilih Kursi");
        gbc.gridx = 1; gbc.gridy = 4;
        add(seatButton, gbc);

        seatButton.addActionListener(e -> openSeatSelector());

        // harga tiket 
        JLabel priceLabel = new JLabel("Harga:");
        gbc.gridx = 0; gbc.gridy = 5;
        add(priceLabel, gbc);

        priceField = new JTextField();
        priceField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 5;
        add(priceField, gbc);

        // button pesan
        bookButton = new JButton("Pesan Tiket");
        gbc.gridx = 1; gbc.gridy = 6;
        add(bookButton, gbc);

        bookButton.addActionListener(e -> processBooking());
    }
    // load jadwal sesuai studio yang dipilih
    private void loadSchedules() {
        scheduleCombo.removeAllItems();

        String studioName = (String) studioCombo.getSelectedItem();
        Studio studio = facade.findStudio(studioName);

        if (studio != null) {
            for (Schedule s : studio.getSchedules()) {
                scheduleCombo.addItem(s.getDay() + " | " + s.getTime());
            }
        }
    }

    // pemilihan kursi
    private void openSeatSelector() {
        String studioName = (String) studioCombo.getSelectedItem();
        String scheduleText = (String) scheduleCombo.getSelectedItem();

        if (studioName == null || scheduleText == null) {
            JOptionPane.showMessageDialog(this, "Pilih studio dan jadwal dulu");
            return;
        }

        String time = scheduleText.split("\\|")[1].trim();

        Studio studio = facade.findStudio(studioName);

        JDialog dialog = new JDialog((Frame) null, "Pilih Kursi", true);
        dialog.setLayout(new GridLayout(studio.getRowCount(), studio.getColCount()));

        JButton[][] seatButtons = new JButton[studio.getRowCount()][studio.getColCount()];

        for (int row = 0; row < studio.getRowCount(); row++) {
            for (int col = 0; col < studio.getColCount(); col++) {

                JButton seat = new JButton((char)('A' + row) + String.valueOf(col + 1));
                seatButtons[row][col] = seat;

                if (studio.isSeatBooked(row, col)) {
                    seat.setEnabled(false);
                    seat.setBackground(Color.RED);
                }

                int r = row, c = col;
                seat.addActionListener(e -> {
                    selectedRow = r;
                    selectedCol = c;

                    selectedSeatField.setText((char)('A' + r) + "" + (c + 1));

                    // auto display harga
                    double price = studio.getSchedule(time).calculateFinalPrice(studio.getBasePrice());
                    priceField.setText("Rp " + price);

                    dialog.dispose();
                });

                dialog.add(seat);
            }
        }

        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    //simpan tiket ke database
    private void processBooking() {
        if (selectedRow == -1 || selectedCol == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih kursi terlebih dahulu!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String studioName = (String) studioCombo.getSelectedItem();
        String scheduleText = (String) scheduleCombo.getSelectedItem();
        String time = scheduleText.split("\\|")[1].trim();

        Ticket ticket = facade.bookTicket(studioName, time, selectedRow, selectedCol);

        if (ticket != null) {
            JOptionPane.showMessageDialog(this,
                    "Tiket berhasil dipesan!\n\n" +
                            "Movie: " + ticket.getSchedule().getMovie().getTitle() + "\n" +
                            "Studio: " + studioName + "\n" +
                            "Waktu: " + ticket.getSchedule().getDay() + " " + ticket.getSchedule().getTime() + "\n" +
                            "Seat: " + ticket.getSeat() + "\n" +
                            "Harga: Rp " + ticket.getPrice(),
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Gagal memesan tiket!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
