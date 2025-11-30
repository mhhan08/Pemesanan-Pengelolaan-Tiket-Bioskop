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

    private static final String PANEL_DASHBOARD = "Dashboard";
    private static final String PANEL_MOVIE = "ManageMovie";
    private static final String PANEL_STUDIO = "ManageStudio";
    private static final String PANEL_SCHEDULE = "ManageSchedule";
    private static final String PANEL_BOOKING = "Booking";
    private static final String FONT_NAME = "Segoe UI"; // Konstanta font

    // controller utama
    private final transient CinemaSystemFacade facade = new CinemaSystemFacade();

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    // panel halaman lain
    private final ManageMoviePanel moviePanel;
    private final ManageStudioPanel studioPanel;
    private final ManageSchedulePanel schedulePanel;
    private final BookingPanel bookingPanel;

    // model table untuk dashboard
    private DefaultTableModel modelMovie;
    private DefaultTableModel modelStudio;
    private DefaultTableModel modelSchedule;

    public MainCinemaGUI() {
        setTitle("Cinema XXI System - Admin Dashboard");
        setSize(1200, 750);
        // windowconstants untuk static access
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel dashboard = createModernDashboard();

        // Inisialisasi panel
        moviePanel = new ManageMoviePanel(facade, cardLayout, mainPanel);
        studioPanel = new ManageStudioPanel(facade, cardLayout, mainPanel);
        schedulePanel = new ManageSchedulePanel(facade, cardLayout, mainPanel);
        bookingPanel = new BookingPanel(facade, cardLayout, mainPanel);

        // Tambahkan ke main panel
        mainPanel.add(dashboard, PANEL_DASHBOARD);
        mainPanel.add(moviePanel, PANEL_MOVIE);
        mainPanel.add(studioPanel, PANEL_STUDIO);
        mainPanel.add(schedulePanel, PANEL_SCHEDULE);
        mainPanel.add(bookingPanel, PANEL_BOOKING);

        add(mainPanel);
    }

    private JPanel createModernDashboard() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(TemplateAdmin.COLOR_BG);

        // header atas
        JPanel headerPanel = TemplateAdmin.createHeaderPanel("BIOSKOP MANAGEMENT SYSTEM");
        JLabel lblSub = new JLabel(" |  Admin Dashboard");
        lblSub.setFont(new Font(FONT_NAME, Font.PLAIN, 16));
        lblSub.setForeground(Color.LIGHT_GRAY);
        headerPanel.add(lblSub);

        // data table layout
        JPanel dataPanel = new JPanel(new GridBagLayout());
        dataPanel.setOpaque(false);
        // padding
        dataPanel.setBorder(new EmptyBorder(20, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0; // tinggi vertikal

        // tabel film lebar 25%
        modelMovie = new DefaultTableModel(new String[]{"Film", "Genre"}, 0);
        gbc.gridx = 0;
        gbc.weightx = 0.25;
        gbc.insets = new Insets(0, 0, 0, 10); // jarak kanan 10px
        dataPanel.add(createCardTable("Daftar Film Aktif", modelMovie), gbc);

        // tabel studio 25%
        modelStudio = new DefaultTableModel(new String[]{"Studio", "Tipe"}, 0);
        gbc.gridx = 1;
        gbc.weightx = 0.25;
        gbc.insets = new Insets(0, 5, 0, 5); // Jarak kiri kanan 5px
        dataPanel.add(createCardTable("Daftar Studio", modelStudio), gbc);

        // tabel jadwal lebar 50%
        modelSchedule = new DefaultTableModel(new String[]{"Film", "Jam", "Studio", "HTM"}, 0);
        gbc.gridx = 2;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(0, 10, 0, 0); // Jarak kiri 10px
        dataPanel.add(createCardTable("Jadwal Tayang", modelSchedule), gbc);

        // button bawah container
        JPanel actionContainer = new JPanel(new GridLayout(1, 2, 20, 0)); // Gap horizontal 20
        actionContainer.setOpaque(false);
        // padding
        actionContainer.setBorder(new EmptyBorder(10, 30, 30, 30));
        actionContainer.setPreferredSize(new Dimension(0, 220)); // Fix tinggi area tombol

        // sebelah kiri untuk pengelolaan
        JPanel adminZone = new JPanel(new GridLayout(3, 1, 0, 15)); // Gap vertikal antar tombol 15
        adminZone.setOpaque(false);
        adminZone.setBorder(BorderFactory.createTitledBorder("Area Pengelolaan (Admin)"));

        JButton btnMovie = TemplateAdmin.createStyledButton("Kelola Data Film", TemplateAdmin.COLOR_ACCENT);
        JButton btnStudio = TemplateAdmin.createStyledButton("Kelola Studio", TemplateAdmin.COLOR_ACCENT);
        JButton btnSchedule = TemplateAdmin.createStyledButton("Atur Jadwal Tayang", new Color(155, 89, 182)); // Ungu

        adminZone.add(btnMovie);
        adminZone.add(btnStudio);
        adminZone.add(btnSchedule);

        // sebelah kanan pemesanan
        JPanel bookingZone = new JPanel(new BorderLayout(0, 15));
        bookingZone.setOpaque(false);
        bookingZone.setBorder(BorderFactory.createTitledBorder("Area Kasir"));

        // tombol booking
        JButton btnBooking = TemplateAdmin.createStyledButton("BUKA MENU PEMESANAN", TemplateAdmin.COLOR_SUCCESS);
        btnBooking.setFont(new Font(FONT_NAME, Font.BOLD, 22));

        JButton btnRefresh = TemplateAdmin.createStyledButton("Refresh Data", Color.GRAY);
        btnRefresh.setPreferredSize(new Dimension(0, 35)); // tombol refresh untuk load data

        bookingZone.add(btnBooking, BorderLayout.CENTER);
        bookingZone.add(btnRefresh, BorderLayout.SOUTH);

        actionContainer.add(adminZone);
        actionContainer.add(bookingZone);

        // masukkan semua ke panel utama
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(dataPanel, BorderLayout.CENTER);
        panel.add(actionContainer, BorderLayout.SOUTH);

        // event listener
        btnMovie.addActionListener(e -> {
            moviePanel.refreshTable();
            cardLayout.show(mainPanel, PANEL_MOVIE);
        });

        btnStudio.addActionListener(e -> {
            studioPanel.refreshTable();
            cardLayout.show(mainPanel, PANEL_STUDIO);
        });

        btnSchedule.addActionListener(e -> {
            schedulePanel.refreshData();
            cardLayout.show(mainPanel, PANEL_SCHEDULE);
        });

        btnBooking.addActionListener(e -> {
            bookingPanel.refreshData();
            cardLayout.show(mainPanel, PANEL_BOOKING);
        });

        btnRefresh.addActionListener(e -> loadDashboardData());

        loadDashboardData();
        return panel;
    }

    private JPanel createCardTable(String title, DefaultTableModel model) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font(FONT_NAME, Font.BOLD, 16));
        lbl.setForeground(TemplateAdmin.COLOR_HEADER);
        lbl.setBorder(new EmptyBorder(15, 15, 10, 0)); // padding judul tabel

        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        TemplateAdmin.styleTable(table);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(null);
        sp.getViewport().setBackground(Color.WHITE);

        card.add(lbl, BorderLayout.NORTH);
        card.add(sp, BorderLayout.CENTER);
        return card;
    }

    private void loadDashboardData() {
        facade.refreshData();

        modelMovie.setRowCount(0);
        for(Movie m : facade.getAllMovies()) {
            modelMovie.addRow(new Object[]{m.getTitle(), m.getGenre()});
        }

        modelStudio.setRowCount(0);
        for(Studio s : facade.getAllStudios()) {
            String type = (s instanceof models.PremiereStudio) ? "Premiere" : "Regular";
            modelStudio.addRow(new Object[]{s.getName(), type});
        }

        modelSchedule.setRowCount(0);
        List<String[]> schedules = facade.getAllSchedulesInfo();
        for(String[] r : schedules) {
            modelSchedule.addRow(new Object[]{r[1], r[2], r[0], r[3]});
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new MainCinemaGUI().setVisible(true));
    }
}