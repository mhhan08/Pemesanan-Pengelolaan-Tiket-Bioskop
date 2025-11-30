package view;

import controller.CinemaSystemFacade;
import models.Studio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageStudioPanel extends JPanel {
    private transient CinemaSystemFacade facade;
    private DefaultTableModel tableModel;

    // komponen input
    private JTextField txtName;
    private JComboBox<String> cbType;

    public ManageStudioPanel(CinemaSystemFacade facade, CardLayout cardLayout, JPanel mainPanel) {
        this.facade = facade;

        // setup panel form input
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 20);
        gbc.anchor = GridBagConstraints.WEST;

        // inisialisasi komponen input dan tombol
        txtName = TemplateAdmin.createStyledTextField();
        cbType = TemplateAdmin.createStyledComboBox();
        cbType.addItem("Regular");
        cbType.addItem("Premiere");

        JButton btnAdd = TemplateAdmin.createStyledButton("Simpan Studio", TemplateAdmin.COLOR_SUCCESS);

        // nama studio
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(TemplateAdmin.createLabel("Nama Studio:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtName, gbc);

        // pilih studio
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(TemplateAdmin.createLabel("Tipe Studio:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cbType, gbc);

        //tombol
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(btnAdd, gbc);

        // setup tabel data studio
        tableModel = new DefaultTableModel(new String[]{"Nama Studio", "Tipe", "Kapasitas"}, 0) {
            @Override // override biar tabel tidak bisa diedit
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);

        // setup tombol bagian bawah
        JButton btnDelete = TemplateAdmin.createStyledButton("Hapus", TemplateAdmin.COLOR_DANGER);
        JButton btnBack = TemplateAdmin.createStyledButton("Kembali", Color.GRAY);

        // masukkan komponen ke template admin
        TemplateAdmin.initPageLayout(this, "Kelola Data Studio", formPanel, table, btnDelete, btnBack);

        //  simpan studio
        btnAdd.addActionListener(e -> {
            facade.addStudio((String)cbType.getSelectedItem(), txtName.getText());
            refreshTable();
            txtName.setText("");
        });

        // hapus studio
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row != -1) { facade.deleteStudio(row); refreshTable(); }
        });

        // tombol kembali ke dashboard
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
    }

    // fungsi refresh tabel studio
    public void refreshTable() {
        tableModel.setRowCount(0);
        for(Studio s : facade.getAllStudios()) {
            String type = (s instanceof models.PremiereStudio) ? "Premiere" : "Regular";
            tableModel.addRow(new Object[]{s.getName(), type, (s.getRowCount() * s.getColCount()) + " Kursi"});
        }
    }
}