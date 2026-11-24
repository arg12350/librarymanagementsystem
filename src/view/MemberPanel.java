package view;

import model.Member;
import util.Validator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MemberPanel extends BasePanel {
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Name", "Department", "Phone", "Email"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JTextField search = new JTextField(25);

    public MemberPanel(MainFrame frame) {
        super(frame, "Member Management");
        Theme.styleTable(table);
        table.setAutoCreateRowSorter(true);

        JPanel top = toolbar();
        Theme.styleField(search);
        top.add(new JLabel("Search:")); top.add(search);
        search.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshTable(); }
            public void removeUpdate(DocumentEvent e) { refreshTable(); }
            public void changedUpdate(DocumentEvent e) { refreshTable(); }
        });

        JButton add = new JButton("Register Member");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        Theme.styleButton(add, true); Theme.styleButton(edit, false); Theme.styleButton(delete, false);
        add.addActionListener(e -> openDialog(null));
        edit.addActionListener(e -> editSelected());
        delete.addActionListener(e -> deleteSelected());
        top.add(add); top.add(edit); top.add(delete);

        body.add(top, BorderLayout.NORTH);
        body.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void refreshData() { refreshTable(); }

    private void refreshTable() {
        String q = search.getText().trim().toLowerCase();
        model.setRowCount(0);
        for (Member m : frame.getController().members()) {
            String all = (m.getId()+" "+m.getName()+" "+m.getDepartment()+" "+m.getPhone()+" "+m.getEmail()).toLowerCase();
            if (q.isEmpty() || all.contains(q))
                model.addRow(new Object[]{m.getId(), m.getName(), m.getDepartment(), m.getPhone(), m.getEmail()});
        }
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { info("Select a member first."); return; }
        String id = String.valueOf(table.getValueAt(table.convertRowIndexToModel(row), 0));
        openDialog(frame.getController().getService().findMember(id));
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { info("Select a member first."); return; }
        String id = String.valueOf(table.getValueAt(table.convertRowIndexToModel(row), 0));
        if (JOptionPane.showConfirmDialog(this, "Delete member " + id + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            frame.getController().deleteMember(id);
            frame.refreshAll();
        } catch (Exception e) { error(e); }
    }

    private void openDialog(Member existing) {
        JTextField id = new JTextField(existing == null ? "" : existing.getId());
        JTextField name = new JTextField(existing == null ? "" : existing.getName());
        JTextField dept = new JTextField(existing == null ? "" : existing.getDepartment());
        JTextField phone = new JTextField(existing == null ? "" : existing.getPhone());
        JTextField email = new JTextField(existing == null ? "" : existing.getEmail());
        id.setEnabled(existing == null);

        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
        p.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));
        p.add(new JLabel("Member ID")); p.add(id);
        p.add(new JLabel("Name")); p.add(name);
        p.add(new JLabel("Department")); p.add(dept);
        p.add(new JLabel("Phone")); p.add(phone);
        p.add(new JLabel("Email")); p.add(email);

        int result = JOptionPane.showConfirmDialog(this, p,
                existing == null ? "Register Member" : "Edit Member",
                JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        try {
            String mid = Validator.required(id.getText(), "Member ID");
            String ph = Validator.required(phone.getText(), "Phone");
            Validator.phone(ph);
            String em = Validator.required(email.getText(), "Email");
            Validator.email(em);

            Member m = new Member(mid, Validator.required(name.getText(), "Name"),
                    Validator.required(dept.getText(), "Department"), ph, em);
            if (existing == null) frame.getController().addMember(m);
            else frame.getController().updateMember(m);
            frame.refreshAll();
        } catch (Exception e) { error(e); }
    }
}
