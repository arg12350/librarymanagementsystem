package view;

import javax.swing.*;
import java.awt.*;

public class ChartPanel extends JPanel {
    private int available;
    private int issued;

    public ChartPanel() {
        setPreferredSize(new Dimension(420, 220));
        setBackground(Theme.WHITE);
    }

    public void setValues(int available, int issued) {
        this.available = available;
        this.issued = issued;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Theme.CHARCOAL);
        g2.setFont(new Font("SansSerif", Font.BOLD, 15));
        g2.drawString("Availability Overview", 18, 25);

        int total = available + issued;
        int max = Math.max(total, 1);
        int x = 45, base = getHeight() - 35, barWidth = 95;

        int ah = (int)((getHeight() - 85) * (available / (double)max));
        int ih = (int)((getHeight() - 85) * (issued / (double)max));

        g2.setColor(Theme.SAGE);
        g2.fillRoundRect(x, base - ah, barWidth, ah, 12, 12);
        g2.setColor(Theme.MUTED);
        g2.fillRoundRect(x + 150, base - ih, barWidth, ih, 12, 12);

        g2.setColor(Theme.CHARCOAL);
        g2.drawString("Available", x + 15, base + 20);
        g2.drawString("Issued", x + 180, base + 20);
        g2.drawString(String.valueOf(available), x + 38, base - ah - 8);
        g2.drawString(String.valueOf(issued), x + 188, base - ih - 8);
        g2.dispose();
    }
}
