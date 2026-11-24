package view;

import model.Book;
import util.Validator;
import exception.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookPanel extends BasePanel {
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Title", "Author", "Category", "Publisher", "Quantity", "Available"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JTextField search = new JTextField(22);
    private final JComboBox<String> category = new JComboBox<>(new String[]{"All Categories"});
    private boolean editing = false;

    public BookPanel(MainFrame frame) {
        super(frame, "Book Management");
        Theme.styleTable(table);
        table.setAutoCreateRowSorter(true);

        JPanel top = toolbar();
        Theme.styleField(search);
        search.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshTable(); }
            public void removeUpdate(DocumentEvent e) { refreshTable(); }
            public void changedUpdate(DocumentEvent e) { refreshTable(); }
        });
        top.add(new JLabel("Search:"));
        top.add(search);
        top.add(new JLabel("Category:"));
        Theme.styleCombo(category);
        category.addActionListener(e -> refreshTable());
        top.add(category);

        JButton add = new JButton("Add Book");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        JButton sample = new JButton("Load Sample Data");
        Theme.styleButton(add, true); Theme.styleButton(edit, false);
        Theme.styleButton(delete, false); Theme.styleButton(sample, false);
        add.addActionListener(e -> openDialog(null));
        edit.addActionListener(e -> editSelected());
        delete.addActionListener(e -> deleteSelected());
        sample.addActionListener(e -> {
            try {
                frame.getController().getService().loadSampleData();
                info("Sample data loaded.");
                frame.refreshAll();
            } catch (Exception ex) { error(ex); }
        });
        top.add(add); top.add(edit); top.add(delete); top.add(sample);

        body.add(top, BorderLayout.NORTH);
        body.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void refreshData() {
        category.removeAllItems();
        category.addItem("All Categories");
        java.util.Set<String> cats = new java.util.TreeSet<>();
        for (Book b : frame.getController().books()) cats.add(b.getCategory());
        for (String c : cats) category.addItem(c);
        refreshTable();
    }

    private void refreshTable() {
        String q = search.getText().trim().toLowerCase();
        String cat = String.valueOf(category.getSelectedItem());
        model.setRowCount(0);
        for (Book b : frame.getController().books()) {
            boolean matches = q.isEmpty() ||
                    b.getId().toLowerCase().contains(q) ||
                    b.getTitle().toLowerCase().contains(q) ||
                    b.getAuthor().toLowerCase().contains(q) ||
                    b.getCategory().toLowerCase().contains(q);
            boolean catMatches = "All Categories".equals(cat) || b.getCategory().equals(cat);
            if (matches && catMatches) {
                model.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(),
                        b.getCategory(), b.getPublisher(), b.getQuantity(), b.getAvailableQuantity()});
            }
        }
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { info("Select a book first."); return; }
        String id = String.valueOf(table.getValueAt(table.convertRowIndexToModel(row), 0));
        openDialog(frame.getController().getService().findBook(id));
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { info("Select a book first."); return; }
        String id = String.valueOf(table.getValueAt(table.convertRowIndexToModel(row), 0));
        if (JOptionPane.showConfirmDialog(this, "Delete book " + id + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            frame.getController().deleteBook(id);
            frame.refreshAll();
        } catch (Exception e) { error(e); }
    }

    private void openDialog(Book existing) {
        JTextField id = new JTextField(existing == null ? "" : existing.getId());
        JTextField title = new JTextField(existing == null ? "" : existing.getTitle());
        JTextField author = new JTextField(existing == null ? "" : existing.getAuthor());
        JTextField cat = new JTextField(existing == null ? "" : existing.getCategory());
        JTextField pub = new JTextField(existing == null ? "" : existing.getPublisher());
        JTextField qty = new JTextField(existing == null ? "" : String.valueOf(existing.getQuantity()));

        id.setEnabled(existing == null);
        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
        p.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));
        p.add(new JLabel("Book ID")); p.add(id);
        p.add(new JLabel("Title")); p.add(title);
        p.add(new JLabel("Author")); p.add(author);
        p.add(new JLabel("Category")); p.add(cat);
        p.add(new JLabel("Publisher")); p.add(pub);
        p.add(new JLabel("Quantity")); p.add(qty);

        int result = JOptionPane.showConfirmDialog(this, p,
                existing == null ? "Add Book" : "Edit Book",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        try {
            String bid = Validator.required(id.getText(), "Book ID");
            Book b = new Book(bid, Validator.required(title.getText(), "Title"),
                    Validator.required(author.getText(), "Author"),
                    Validator.required(cat.getText(), "Category"),
                    Validator.required(pub.getText(), "Publisher"),
                    Validator.positiveInt(qty.getText(), "Quantity"));
            if (existing == null) frame.getController().addBook(b);
            else frame.getController().updateBook(b);
            frame.refreshAll();
        } catch (Exception e) { error(e); }
    }
}
