package view;

import model.Book;
import model.Member;
import model.Transaction;

import javax.swing.*;
import java.awt.*;

public class IssuePanel extends BasePanel {
    private final JComboBox<String> memberBox = new JComboBox<>();
    private final JComboBox<String> bookBox = new JComboBox<>();
    private final JLabel memberDetails = new JLabel(" ");
    private final JLabel bookDetails = new JLabel(" ");

    public IssuePanel(MainFrame frame) {
        super(frame, "Issue Book");

        JPanel form = Theme.card();
        form.setPreferredSize(new Dimension(620, 330));

        memberBox.addActionListener(e -> updateMemberDetails());
        bookBox.addActionListener(e -> updateBookDetails());
        Theme.styleCombo(memberBox); Theme.styleCombo(bookBox);

        JButton issue = new JButton("Issue Book");
        Theme.styleButton(issue, true);
        issue.addActionListener(e -> issueBook());

        JPanel grid = new JPanel(new GridLayout(0, 1, 8, 8));
        grid.setBackground(Theme.WHITE);
        grid.add(new JLabel("Registered Member"));
        grid.add(memberBox);
        grid.add(memberDetails);
        grid.add(Box.createVerticalStrut(5));
        grid.add(new JLabel("Available Book"));
        grid.add(bookBox);
        grid.add(bookDetails);
        grid.add(Box.createVerticalStrut(8));
        grid.add(issue);

        form.add(grid, BorderLayout.CENTER);

        body.add(form, BorderLayout.NORTH);
        body.add(new JLabel("Borrowing period: 14 days • Maximum active books per member: 3"),
                BorderLayout.CENTER);
    }

    public void refreshData() {
        String selectedMember = (String) memberBox.getSelectedItem();
        String selectedBook = (String) bookBox.getSelectedItem();
        memberBox.removeAllItems();
        for (Member m : frame.getController().members())
            memberBox.addItem(m.getId() + " — " + m.getName());

        bookBox.removeAllItems();
        for (Book b : frame.getController().books())
            if (b.isAvailable()) bookBox.addItem(b.getId() + " — " + b.getTitle());

        selectStarting(memberBox, selectedMember);
        selectStarting(bookBox, selectedBook);
        updateMemberDetails();
        updateBookDetails();
    }

    private void selectStarting(JComboBox<String> box, String value) {
        if (value == null) return;
        for (int i = 0; i < box.getItemCount(); i++)
            if (value.equals(box.getItemAt(i))) { box.setSelectedIndex(i); break; }
    }

    private void updateMemberDetails() {
        String s = (String) memberBox.getSelectedItem();
        if (s == null) { memberDetails.setText(" "); return; }
        String id = s.split(" — ")[0];
        Member m = frame.getController().getService().findMember(id);
        memberDetails.setText(m == null ? " " :
                "Department: " + m.getDepartment() + " • Active books: " +
                        frame.getController().getService().activeCountForMember(id));
    }

    private void updateBookDetails() {
        String s = (String) bookBox.getSelectedItem();
        if (s == null) { bookDetails.setText(" "); return; }
        String id = s.split(" — ")[0];
        Book b = frame.getController().getService().findBook(id);
        bookDetails.setText(b == null ? " " :
                "Author: " + b.getAuthor() + " • Available copies: " + b.getAvailableQuantity());
    }

    private void issueBook() {
        try {
            String m = selectedId(memberBox);
            String b = selectedId(bookBox);
            if (m == null || b == null) throw new IllegalArgumentException("Select a member and an available book.");
            Transaction t = frame.getController().issue(m, b);
            info("Book issued successfully.\nTransaction: " + t.getId() +
                    "\nDue date: " + t.getDueDate());
            frame.refreshAll();
        } catch (Exception e) { error(e); }
    }

    private String selectedId(JComboBox<String> box) {
        Object value = box.getSelectedItem();
        return value == null ? null : String.valueOf(value).split(" — ")[0];
    }
}
