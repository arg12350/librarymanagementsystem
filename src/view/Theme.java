package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public final class Theme {
    private Theme() {}

    public static final Color SAGE = new Color(112, 137, 111);
    public static final Color SAGE_DARK = new Color(78, 101, 78);
    public static final Color OFF_WHITE = new Color(247, 246, 241);
    public static final Color CHARCOAL = new Color(45, 49, 47);
    public static final Color MUTED = new Color(111, 117, 113);
    public static final Color WHITE = Color.WHITE;
    public static final Color BORDER = new Color(220, 222, 218);

    public static final Font TITLE = new Font("SansSerif", Font.BOLD, 28);
    public static final Font H2 = new Font("SansSerif", Font.BOLD, 20);
    public static final Font BODY = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font SMALL = new Font("SansSerif", Font.PLAIN, 12);

    public static void styleButton(JButton b, boolean primary) {
        b.setFont(BODY);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setBackground(primary ? SAGE : WHITE);
        b.setForeground(primary ? WHITE : CHARCOAL);
        b.setBorder(new EmptyBorder(10, 16, 10, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void styleField(JTextField f) {
        f.setFont(BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(8, 10, 8, 10)));
    }

    public static void stylePassword(JPasswordField f) {
        f.setFont(BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(8, 10, 8, 10)));
    }

    public static void styleCombo(JComboBox<?> c) {
        c.setFont(BODY);
        c.setBackground(WHITE);
    }

    public static void styleTable(JTable table) {
        table.setFont(BODY);
        table.setRowHeight(32);
        table.setGridColor(BORDER);
        table.setSelectionBackground(new Color(220, 231, 220));
        table.setSelectionForeground(CHARCOAL);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(235, 237, 232));
        table.getTableHeader().setForeground(CHARCOAL);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
    }

    public static JPanel card() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(18, 18, 18, 18)));
        return p;
    }
}
