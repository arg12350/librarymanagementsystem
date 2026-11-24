package view;

import model.Transaction;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistoryPanel extends BasePanel {
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Transaction", "Book", "Member", "Issue", "Due", "Return", "Status", "Fine", "Payment"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JTextField search = new JTextField(24);
    private final JComboBox<String> status = new JComboBox<>(new String[]{"All Status", "ACTIVE", "RETURNED"});

    public HistoryPanel(MainFrame frame) {
        super(frame, "Transaction History");
        Theme.styleTable(table);
        table.setAutoCreateRowSorter(true);

        JPanel top = toolbar();
        Theme.styleField(search);
        top.add(new JLabel("Search:")); top.add(search);
        top.add(new JLabel("Status:")); Theme.styleCombo(status); top.add(status);

        search.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshTable(); }
            public void removeUpdate(DocumentEvent e) { refreshTable(); }
            public void changedUpdate(DocumentEvent e) { refreshTable(); }
        });
        status.addActionListener(e -> refreshTable());

        body.add(top, BorderLayout.NORTH);
        body.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void refreshData() { refreshTable(); }

    private void refreshTable() {
        String q = search.getText().trim().toLowerCase();
        String st = String.valueOf(status.getSelectedItem());
        model.setRowCount(0);

        for (Transaction t : frame.getController().transactions()) {
            String all = (t.getId()+" "+t.getBookId()+" "+t.getBookTitle()+" "+t.getMemberId()+" "+t.getMemberName()).toLowerCase();
            boolean searchOk = q.isEmpty() || all.contains(q);
            boolean statusOk = "All Status".equals(st) || t.getStatus().name().equals(st);
            if (searchOk && statusOk) {
                model.addRow(new Object[]{t.getId(), t.getBookTitle(), t.getMemberName(),
                        t.getIssueDate(), t.getDueDate(), t.getReturnDate() == null ? "-" : t.getReturnDate(),
                        t.getStatus(), String.format("₹%.2f", t.getFineAmount()), t.getPaymentStatus()});
            }
        }
    }
}
