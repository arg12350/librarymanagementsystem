package view;

import model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FinePanel extends BasePanel {
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Transaction", "Member", "Book", "Fine", "Payment"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JLabel outstanding = new JLabel("Outstanding: ₹0.00");

    public FinePanel(MainFrame frame) {
        super(frame, "Fine Management");
        Theme.styleTable(table);

        JPanel top = toolbar();
        outstanding.setFont(Theme.H2);
        outstanding.setForeground(Theme.SAGE_DARK);

        JButton paid = new JButton("Mark Paid");
        JButton unpaid = new JButton("Mark Unpaid");
        Theme.styleButton(paid, true); Theme.styleButton(unpaid, false);
        paid.addActionListener(e -> setPayment(true));
        unpaid.addActionListener(e -> setPayment(false));

        top.add(outstanding); top.add(paid); top.add(unpaid);
        body.add(top, BorderLayout.NORTH);
        body.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void refreshData() {
        model.setRowCount(0);
        for (Transaction t : frame.getController().transactions()) {
            if (t.getFineAmount() > 0) {
                model.addRow(new Object[]{t.getId(), t.getMemberName(), t.getBookTitle(),
                        String.format("₹%.2f", t.getFineAmount()), t.getPaymentStatus()});
            }
        }
        outstanding.setText("Outstanding: ₹" +
                String.format("%.2f", frame.getController().getService().outstandingFines()));
    }

    private void setPayment(boolean paid) {
        int row = table.getSelectedRow();
        if (row < 0) { info("Select a fine record."); return; }
        String id = String.valueOf(table.getValueAt(table.convertRowIndexToModel(row), 0));
        try {
            frame.getController().setFinePaid(id, paid);
            frame.refreshAll();
        } catch (Exception e) { error(e); }
    }
}
