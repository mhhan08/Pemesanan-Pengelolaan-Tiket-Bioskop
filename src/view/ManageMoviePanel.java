package view;

import controller.CinemaSystemFacade;
import models.Movie;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageMoviePanel extends JPanel {
    private transient CinemaSystemFacade facade;
    private DefaultTableModel tableModel;

    // deklarasi variabel dipisah per baris
    private JTextField txtTitle;
    private JTextField txtGenre;
    private JTextField txtDuration;

    public ManageMoviePanel(CinemaSystemFacade facade, CardLayout cardLayout, JPanel mainPanel) {
        this.facade = facade;

        // setup panel form input dengan gridbaglayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 20);
        gbc.anchor = GridBagConstraints.WEST;

        // inisialisasi komponen input dan tombol
        txtTitle = TemplateAdmin.createStyledTextField();
        txtGenre = TemplateAdmin.createStyledTextField();
        txtDuration = TemplateAdmin.createStyledTextField();
        JButton btnAdd = TemplateAdmin.createStyledButton("Simpan Film", TemplateAdmin.COLOR_SUCCESS);

        // menyusun layout form
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(TemplateAdmin.createLabel("Judul Film:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtTitle, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(TemplateAdmin.createLabel("Genre:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtGenre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(TemplateAdmin.createLabel("Durasi (menit):"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtDuration, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(btnAdd, gbc);

        // setup tabel data film
        tableModel = new DefaultTableModel(new String[]{"Judul Film", "Genre", "Durasi"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);

        // setup tombol bagian bawah
        JButton btnDelete = TemplateAdmin.createStyledButton("Hapus Terpilih", TemplateAdmin.COLOR_DANGER);
        JButton btnBack = TemplateAdmin.createStyledButton("Kembali", Color.GRAY);

        // masukkan semua komponen ke template admin
        TemplateAdmin.initPageLayout(this, "Kelola Data Film", formPanel, table, btnDelete, btnBack);

        // untuk tombol simpan
        btnAdd.addActionListener(e -> {
            try {
                facade.addMovie(txtTitle.getText(), txtGenre.getText(), Integer.parseInt(txtDuration.getText()));
                refreshTable();
                txtTitle.setText("");
                txtGenre.setText("");
                txtDuration.setText("");
                JOptionPane.showMessageDialog(this, "Berhasil Disimpan!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Input Error: " + ex.getMessage()); }
        });

        // untuk hapus data
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row != -1) { facade.deleteMovie(row); refreshTable(); }
        });

        // tombol kembali ke dashboard
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
    }

    // fungsi untuk refresh isi tabel dari database
    public void refreshTable() {
        tableModel.setRowCount(0);
        for(Movie m : facade.getAllMovies()) tableModel.addRow(new Object[]{m.getTitle(), m.getGenre(), m.getDurationMinutes() + " min"});
    }
}