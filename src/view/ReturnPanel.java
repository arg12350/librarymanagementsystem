package view;

import model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ReturnPanel extends BasePanel {
    private final JTextField search = new JTextField();
    private final JLabel details = new JLabel("Enter a Transaction ID, Member ID or Book ID.");
    private final DefaultListModel<String> resultModel = new DefaultListModel<>();
    private final JList<String> results = new JList<>(resultModel);

    public ReturnPanel(MainFrame frame) {
        super(frame, "Return Book");

        JPanel top = toolbar();
        Theme.styleField(search);
        search.setPreferredSize(new Dimension(300, 38));
        JButton find = new JButton("Search");
        JButton ret = new JButton("Return Selected");
        Theme.styleButton(find, true); Theme.styleButton(ret, false);

        top.add(new JLabel("Transaction / Member / Book:"));
        top.add(search); top.add(find); top.add(ret);

        results.setFont(Theme.BODY);
        results.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        results.addListSelectionListener(e -> showDetails());

        find.addActionListener(e -> searchTransactions());
        ret.addActionListener(e -> returnSelected());

        JPanel card = Theme.card();
        card.add(details, BorderLayout.NORTH);
        card.add(new JScrollPane(results), BorderLayout.CENTER);

        body.add(top, BorderLayout.NORTH);
        body.add(card, BorderLayout.CENTER);
    }

    public void refreshData() {
        resultModel.clear();
        details.setText("Enter a Transaction ID, Member ID or Book ID.");
    }

    private void searchTransactions() {
        String q = search.getText().trim().toLowerCase();
        resultModel.clear();
        for (Transaction t : frame.getController().transactions()) {
            String all = (t.getId()+" "+t.getMemberId()+" "+t.getBookId()+" "+t.getBookTitle()).toLowerCase();
            if (!q.isEmpty() && all.contains(q))
                resultModel.addElement(t.getId() + " — " + t.getBookTitle() + " — " +
                        t.getMemberName() + " — " + t.getStatus());
        }
        if (resultModel.isEmpty()) details.setText("No matching transactions.");
    }

    private String selectedId() {
        String s = results.getSelectedValue();
        return s == null ? null : s.split(" — ")[0];
    }

    private void showDetails() {
        String id = selectedId();
        if (id == null) return;
        Transaction t = frame.getController().getService().findTransaction(id);
        if (t != null) details.setText("<html><b>" + t.getId() + "</b> • " +
                t.getBookTitle() + " • Member: " + t.getMemberName() +
                " • Issue: " + t.getIssueDate() + " • Due: " + t.getDueDate() +
                " • Status: " + t.getStatus() + "</html>");
    }

    private void returnSelected() {
        String id = selectedId();
        if (id == null) { info("Select an issued transaction."); return; }
        try {
            Transaction t = frame.getController().getService().findTransaction(id);
            if (t == null) throw new IllegalArgumentException("Transaction not found.");
            if (t.getStatus() == Transaction.Status.RETURNED) throw new IllegalArgumentException("Already returned.");
            long days = Math.max(0, java.time.temporal.ChronoUnit.DAYS.between(
                    t.getDueDate(), LocalDate.now()));
            Transaction done = frame.getController().returnBook(id);
            info("Book returned successfully.\nOverdue days: " + days +
                    "\nFine: ₹" + String.format("%.2f", done.getFineAmount()));
            frame.refreshAll();
            searchTransactions();
        } catch (Exception e) { error(e); }
    }
}
