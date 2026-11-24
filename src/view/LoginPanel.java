package view;

import util.Config;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPanel extends JPanel {
    private final MainFrame frame;
    private final JTextField username = new JTextField();
    private final JPasswordField password = new JPasswordField();

    public LoginPanel(MainFrame frame) {
        this.frame = frame;
        setBackground(Theme.OFF_WHITE);
        setLayout(new GridBagLayout());

        JPanel box = Theme.card();
        box.setPreferredSize(new Dimension(430, 430));

        JLabel brand = new JLabel("BBIT Library");
        brand.setFont(Theme.TITLE);
        brand.setForeground(Theme.SAGE_DARK);

        JLabel subtitle = new JLabel("Library Management System");
        subtitle.setFont(Theme.BODY);
        subtitle.setForeground(Theme.MUTED);

        JPanel heading = new JPanel();
        heading.setBackground(Theme.WHITE);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(brand);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);

        username.setPreferredSize(new Dimension(300, 42));
        password.setPreferredSize(new Dimension(300, 42));
        Theme.styleField(username);
        Theme.stylePassword(password);

        JButton login = new JButton("Sign In");
        Theme.styleButton(login, true);
        login.addActionListener(e -> authenticate());

        JCheckBox show = new JCheckBox("Show password");
        show.setBackground(Theme.WHITE);
        show.setFont(Theme.SMALL);
        show.addActionListener(e -> {
            password.setEchoChar(show.isSelected() ? (char) 0 : '•');
        });

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 8));
        form.setBackground(Theme.WHITE);
        form.add(label("Username"));
        form.add(username);
        form.add(label("Password"));
        form.add(password);
        form.add(show);
        form.add(login);

        box.add(heading, BorderLayout.NORTH);
        box.add(form, BorderLayout.CENTER);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.NONE;
        add(box, gc);

//        getRootPane().setDefaultButton(login); //LOGIN ON ENTER KEY
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(Theme.CHARCOAL);
        return l;
    }

    private void authenticate() {
        if (Config.ADMIN_USERNAME.equals(username.getText().trim())
                && Config.ADMIN_PASSWORD.equals(new String(password.getPassword()))) {
            username.setText("");
            password.setText("");
            frame.showScreen(MainFrame.DASHBOARD);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password.", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
