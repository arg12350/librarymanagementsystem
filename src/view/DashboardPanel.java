package view;

import model.Book;
import model.Transaction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private final MainFrame frame;
    private final JLabel totalBooks = cardValue();
    private final JLabel available = cardValue();
    private final JLabel issued = cardValue();
    private final JLabel members = cardValue();
    private final JLabel overdue = cardValue();
    private final ChartPanel chart = new ChartPanel();
    private final DefaultListModel<String> recentModel = new DefaultListModel<>();

    public DashboardPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(18, 18));
        setBackground(Theme.OFF_WHITE);
        setBorder(new EmptyBorder(28, 28, 28, 28));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Dashboard");
        title.setFont(Theme.TITLE);
        title.setForeground(Theme.CHARCOAL);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        actions.add(frame.navButton("Books", MainFrame.BOOKS));
        actions.add(frame.navButton("Members", MainFrame.MEMBERS));
        actions.add(frame.navButton("Issue", MainFrame.ISSUE));
        actions.add(frame.navButton("Return", MainFrame.RETURN));
        actions.add(frame.navButton("History", MainFrame.HISTORY));
        actions.add(frame.navButton("Fines", MainFrame.FINES));
        header.add(title, BorderLayout.WEST);
        header.add(actions, BorderLayout.EAST);

        JPanel stats = new JPanel(new GridLayout(1, 5, 12, 0));
        stats.setOpaque(false);
        stats.add(stat("Total Books", totalBooks));
        stats.add(stat("Available", available));
        stats.add(stat("Issued", issued));
        stats.add(stat("Members", members));
        stats.add(stat("Overdue", overdue));

        JPanel recent = Theme.card();
        recent.add(new JLabel("Recent Activity"), BorderLayout.NORTH);
        recentModel.clear();
        JList<String> list = new JList<>(recentModel);
        list.setFont(Theme.BODY);
        list.setBackground(Theme.WHITE);
        recent.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel lower = new JPanel(new GridLayout(1, 2, 18, 0));
        lower.setOpaque(false);
        JPanel chartCard = Theme.card();
        chartCard.add(chart, BorderLayout.CENTER);
        lower.add(chartCard);
        lower.add(recent);

        add(header, BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout(18, 18));
        center.setOpaque(false);
        center.add(stats, BorderLayout.NORTH);
        center.add(lower, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private JPanel stat(String name, JLabel value) {
        JPanel p = Theme.card();
        JLabel label = new JLabel(name);
        label.setFont(Theme.SMALL);
        label.setForeground(Theme.MUTED);
        p.add(label, BorderLayout.NORTH);
        p.add(value, BorderLayout.CENTER);
        return p;
    }

    private JLabel cardValue() {
        JLabel l = new JLabel("0");
        l.setFont(new Font("SansSerif", Font.BOLD, 26));
        l.setForeground(Theme.SAGE_DARK);
        return l;
    }

    public void refreshData() {
        int total = 0, avail = 0;
        for (Book b : frame.getController().books()) {
            total += b.getQuantity();
            avail += b.getAvailableQuantity();
        }
        totalBooks.setText(String.valueOf(total));
        available.setText(String.valueOf(avail));
        issued.setText(String.valueOf(total - avail));
        members.setText(String.valueOf(frame.getController().members().size()));
        overdue.setText(String.valueOf(frame.getController().getService().overdueCount()));
        chart.setValues(avail, total - avail);

        recentModel.clear();
        for (Transaction t : frame.getController().getService().recentTransactions(6)) {
            recentModel.addElement(t.getId() + " • " + t.getMemberName() + " • " +
                    t.getBookTitle() + " • " + t.getStatus());
        }
    }
}
