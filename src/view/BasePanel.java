package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class BasePanel extends JPanel {
    protected final MainFrame frame;
    protected final JPanel body = new JPanel(new BorderLayout(12, 12));

    protected BasePanel(MainFrame frame, String title) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(Theme.OFF_WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Theme.OFF_WHITE);
        top.setBorder(new EmptyBorder(22, 28, 12, 28));

        JLabel heading = new JLabel(title);
        heading.setFont(Theme.TITLE);
        heading.setForeground(Theme.CHARCOAL);

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        nav.setOpaque(false);
        nav.add(frame.navButton("Home", MainFrame.DASHBOARD));
        nav.add(frame.navButton("Logout", MainFrame.LOGIN));

        top.add(heading, BorderLayout.WEST);
        top.add(nav, BorderLayout.EAST);

        body.setOpaque(false);
        body.setBorder(new EmptyBorder(0, 28, 28, 28));

        add(top, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
    }

    protected JPanel toolbar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        p.setOpaque(false);
        return p;
    }

    protected void error(Throwable e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Operation Failed",
                JOptionPane.ERROR_MESSAGE);
    }

    protected void info(String message) {
        JOptionPane.showMessageDialog(this, message, "Library System",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
