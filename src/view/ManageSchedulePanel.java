package view;

import controller.CinemaSystemFacade;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageSchedulePanel extends JPanel {
    private transient CinemaSystemFacade facade;
    private DefaultTableModel tableModel;

    private JComboBox<String> cbMovie;
    private JComboBox<String> cbStudio;
    private JTextField txtDay;
    private JTextField txtTime;

    public ManageSchedulePanel(CinemaSystemFacade facade, CardLayout cardLayout, JPanel mainPanel) {
        this.facade = facade;

        // setup panel form input dengan gridbaglayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 20);
        gbc.anchor = GridBagConstraints.WEST;

        // inisialisasi komponen input dan tombol
        cbMovie = TemplateAdmin.createStyledComboBox();
        cbStudio = TemplateAdmin.createStyledComboBox();
        txtDay = TemplateAdmin.createStyledTextField();
        txtTime = TemplateAdmin.createStyledTextField();
        JButton btnAdd = TemplateAdmin.createStyledButton("Buat Jadwal", new Color(155, 89, 182));

        //setting layout form

        // pilih film
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(TemplateAdmin.createLabel("Pilih Film:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cbMovie, gbc);

        // pilih studio
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(TemplateAdmin.createLabel("Pilih Studio:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cbStudio, gbc);

        // pilih hari
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(TemplateAdmin.createLabel("Hari :"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtDay, gbc);

        // pilih jam
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(TemplateAdmin.createLabel("Jam (HH:MM):"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtTime, gbc);

        // tombol buat jadwal
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(btnAdd, gbc);

        // setup tabel jadwal
        tableModel = new DefaultTableModel(new String[]{"Studio", "Film", "Waktu", "Info"}, 0) {
            @Override // tambahkan override biar sonarqube seneng
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);

        // setup tombol kembali
        JButton btnBack = TemplateAdmin.createStyledButton("Kembali", Color.GRAY);

        // masukkan semua komponen ke template admin
        TemplateAdmin.initPageLayout(this, "Atur Jadwal Penayangan", formPanel, table, btnBack);

        // aksi kalo tombol buat jadwal diklik
        btnAdd.addActionListener(e -> {
            try {
                facade.createSchedule((String)cbStudio.getSelectedItem(), (String)cbMovie.getSelectedItem(), txtDay.getText(), txtTime.getText());
                refreshData();
                JOptionPane.showMessageDialog(this, "Sukses!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Gagal: " + ex.getMessage()); }
        });

        // tombol balik ke dashboard
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
    }

    // fungsi refresh data dropdown dan tabel
    public void refreshData() {
        cbMovie.removeAllItems();
        for(String t : facade.getMovieTitles()) cbMovie.addItem(t);

        cbStudio.removeAllItems();
        for(String s : facade.getStudioNames()) cbStudio.addItem(s);

        tableModel.setRowCount(0);
        for(String[] row : facade.getAllSchedulesInfo()) tableModel.addRow(row);
    }
}