package view;

import controller.CinemaSystemFacade;
import models.Movie;
import models.Studio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainCinemaGUI extends JFrame {

    // controller utama
    private CinemaSystemFacade facade = new CinemaSystemFacade();

    // layout manager untuk ganti ganti halaman
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // panel-panel halaman
    private ManageMoviePanel moviePanel;
    private ManageStudioPanel studioPanel;
    private ManageSchedulePanel schedulePanel;
    private BookingPanel bookingPanel;

    // model tabel untuk dashboard
    private DefaultTableModel modelMovie, modelStudio, modelSchedule;

    public MainCinemaGUI() {
        setTitle("Cinema XXI System - Admin Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // setup layout utama dengan cardlayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // inisialisasi halaman dashboard
        JPanel dashboard = createModernDashboard();

        // inisialisasi panel fitur
        moviePanel = new ManageMoviePanel(cardLayout, mainPanel);
        studioPanel = new ManageStudioPanel(cardLayout, mainPanel);
        schedulePanel = new ManageSchedulePanel(cardLayout, mainPanel);
        bookingPanel = new BookingPanel(cardLayout, mainPanel);

        // daftarkan panel ke cardlayout
        mainPanel.add(dashboard, "Dashboard");
        mainPanel.add(moviePanel, "ManageMovie");
        mainPanel.add(studioPanel, "ManageStudio");
        mainPanel.add(schedulePanel, "ManageSchedule");
        mainPanel.add(bookingPanel, "Booking");

        add(mainPanel);
    }

    // tampilan dashboard
    private JPanel createModernDashboard() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 246, 250));

        // header atas
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("BIOSKOP MANAGEMENT SYSTEM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSubtitle = new JLabel("Selamat Datang, Admin");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(189, 195, 199));

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(lblSubtitle, BorderLayout.EAST);

        // tabel overview data
        JPanel dataPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        dataPanel.setOpaque(false);
        dataPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // tabel  film
        modelMovie = new DefaultTableModel(new String[]{"Judul Film", "Genre"}, 0);
        dataPanel.add(createCardTable("Daftar Film Aktif", modelMovie));

        // tabel  studio
        modelStudio = new DefaultTableModel(new String[]{"Studio", "Tipe"}, 0);
        dataPanel.add(createCardTable("Daftar Studio", modelStudio));

        // tabel  jadwal
        modelSchedule = new DefaultTableModel(new String[]{"Film", "Jam", "Studio"}, 0);
        dataPanel.add(createCardTable("Jadwal Tayang", modelSchedule));

        // tombol aksi
        JPanel actionContainer = new JPanel(new GridLayout(1, 2, 30, 0));
        actionContainer.setOpaque(false);
        actionContainer.setBorder(new EmptyBorder(10, 30, 40, 30));

        // bagian admin sebelah kiri panel
        JPanel adminZone = new JPanel(new GridLayout(3, 1, 0, 15));
        adminZone.setOpaque(false);
        adminZone.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(127, 140, 141))
        ));

        JButton btnMovie = createStyledButton("Kelola Data Film", new Color(52, 152, 219));
        JButton btnStudio = createStyledButton("Kelola Studio", new Color(52, 152, 219));
        JButton btnSchedule = createStyledButton("Atur Jadwal Tayang", new Color(155, 89, 182));

        adminZone.add(btnMovie);
        adminZone.add(btnStudio);
        adminZone.add(btnSchedule);

        // bagian kasir sebelah kanan
        JPanel bookingZone = new JPanel(new BorderLayout());
        bookingZone.setOpaque(false);
        bookingZone.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(127, 140, 141))
        ));

        JButton btnBooking = createStyledButton("BUKA MENU PEMESANAN TIKET", new Color(39, 174, 96));
        btnBooking.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton btnRefresh = createStyledButton("Refresh Data", new Color(149, 165, 166));

        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshPanel.setOpaque(false);
        refreshPanel.add(btnRefresh);

        bookingZone.add(btnBooking, BorderLayout.CENTER);
        bookingZone.add(refreshPanel, BorderLayout.SOUTH);

        actionContainer.add(adminZone);
        actionContainer.add(bookingZone);

        // masukkan ke panel utama
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(dataPanel, BorderLayout.CENTER);
        panel.add(actionContainer, BorderLayout.SOUTH);

        // event listener untuk pindah halaman
        btnMovie.addActionListener(e -> cardLayout.show(mainPanel, "ManageMovie"));
        btnStudio.addActionListener(e -> cardLayout.show(mainPanel, "ManageStudio"));
        btnSchedule.addActionListener(e -> cardLayout.show(mainPanel, "ManageSchedule"));
        btnBooking.addActionListener(e -> cardLayout.show(mainPanel, "Booking"));

        // tombol refresh untuk reload data dari db
        btnRefresh.addActionListener(e -> loadDashboardData());

        // load data saat apk jalan
        loadDashboardData();

        return panel;
    }

    // helper untuk bikin tabel yang ada card nya
    private JPanel createCardTable(String title, DefaultTableModel model) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(44, 62, 80));
        lbl.setBorder(new EmptyBorder(0, 0, 10, 0));

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setShowGrid(false);
        table.getTableHeader().setBackground(new Color(236, 240, 241));

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());

        card.add(lbl, BorderLayout.NORTH);
        card.add(sp, BorderLayout.CENTER);
        return card;
    }

    // helper untuk styling tombol
    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // logic untuk ambil data dari facade ke dashboard
    private void loadDashboardData() {
        facade.refreshData();

        modelMovie.setRowCount(0);
        for (Movie m : facade.getAllMovies()) {
            modelMovie.addRow(new Object[]{m.getTitle(), m.getGenre()});
        }

        modelStudio.setRowCount(0);
        for (Studio s : facade.getAllStudios()) {
            String type = (s instanceof models.PremiereStudio) ? "Premiere" : "Regular";
            modelStudio.addRow(new Object[]{s.getName(), type});
        }

        modelSchedule.setRowCount(0);
        List<String[]> schedules = facade.getAllSchedulesInfo();
        for (String[] row : schedules) {
            modelSchedule.addRow(new Object[]{row[1], row[2], row[0]});
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new MainCinemaGUI().setVisible(true));
    }
}