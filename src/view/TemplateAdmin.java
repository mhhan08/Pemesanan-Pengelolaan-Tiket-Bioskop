package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class TemplateAdmin {

    // private constructor agar tidak bisa diinstansiasi utility class
    private TemplateAdmin() {
        throw new IllegalStateException("utility class");
    }

    // konstanta font
    private static final String FONT_NAME = "Segoe UI";

    // warna tema
    public static final Color COLOR_HEADER = new Color(44, 62, 80);
    public static final Color COLOR_BG = new Color(245, 246, 250);
    public static final Color COLOR_ACCENT = new Color(52, 152, 219);
    public static final Color COLOR_SUCCESS = new Color(39, 174, 96);
    public static final Color COLOR_DANGER = new Color(231, 76, 60);
    public static final Color COLOR_WARNING = new Color(241, 196, 15);

    // menerima komponen komponen form table buttons dan menyusunnya menjadi halaman
    public static void initPageLayout(JPanel mainPanel, String title, JPanel formContent, JTable table, JButton... bottomButtons) {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(COLOR_BG);

        // header
        mainPanel.add(createHeaderPanel(title), BorderLayout.NORTH);

        // center form dan table
        JPanel centerContainer = new JPanel(new BorderLayout(0, 20));
        centerContainer.setOpaque(false);
        centerContainer.setBorder(new EmptyBorder(20, 30, 20, 30));

        // taruh form content ke dalam card putih
        JPanel formCard = new JPanel(new BorderLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(15, 20, 15, 20)
        ));
        formCard.add(formContent, BorderLayout.CENTER);
        centerContainer.add(formCard, BorderLayout.NORTH);

        // styling tabel dan scrollpane
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        centerContainer.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerContainer, BorderLayout.CENTER);

        // bottom buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        bottomPanel.setBackground(COLOR_BG);
        bottomPanel.setBorder(new EmptyBorder(0, 0, 0, 30));

        for (JButton btn : bottomButtons) {
            bottomPanel.add(btn);
        }
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }


    // helper component creators

    public static JPanel createHeaderPanel(String title) {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        header.setBackground(COLOR_HEADER);
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font(FONT_NAME, Font.BOLD, 24));
        lbl.setForeground(Color.WHITE);
        header.add(lbl);
        return header;
    }

    public static JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(300, 35));
        field.setFont(new Font(FONT_NAME, Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return field;
    }

    public static JComboBox<String> createStyledComboBox() {
        JComboBox<String> cb = new JComboBox<>();
        cb.setPreferredSize(new Dimension(300, 35));
        cb.setBackground(Color.WHITE);
        cb.setFont(new Font(FONT_NAME, Font.PLAIN, 14));
        return cb;
    }

    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font(FONT_NAME, Font.BOLD, 14));
        lbl.setForeground(new Color(100, 100, 100));
        return lbl;
    }

    public static JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font(FONT_NAME, Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 40));
        return btn;
    }

    // table styling
    public static void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(220, 220, 220));
        table.setFont(new Font(FONT_NAME, Font.PLAIN, 14));
        table.setSelectionBackground(new Color(214, 234, 248));
        table.setSelectionForeground(Color.BLACK);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, v, s, f, r, c);
                lbl.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return lbl;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, v, s, f, r, c);
                lbl.setBackground(new Color(52, 73, 94));
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font(FONT_NAME, Font.BOLD, 14));
                lbl.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
                return lbl;
            }
        });
        header.setPreferredSize(new Dimension(0, 40));
        header.setReorderingAllowed(false);
    }
}