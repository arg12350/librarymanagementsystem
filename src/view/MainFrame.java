package view;

import controller.LibraryController;
import util.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    public static final String LOGIN = "LOGIN";
    public static final String DASHBOARD = "DASHBOARD";
    public static final String BOOKS = "BOOKS";
    public static final String MEMBERS = "MEMBERS";
    public static final String ISSUE = "ISSUE";
    public static final String RETURN = "RETURN";
    public static final String HISTORY = "HISTORY";
    public static final String FINES = "FINES";

    private final LibraryController controller;
    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);

    private DashboardPanel dashboard;
    private BookPanel books;
    private MemberPanel members;
    private IssuePanel issue;
    private ReturnPanel returns;
    private HistoryPanel history;
    private FinePanel fines;

    public MainFrame() {
        super(Config.APP_NAME);
        controller = new LibraryController();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setSize(1280, 800);
        setLocationRelativeTo(null);

        buildScreens();
        add(content);
        showScreen(LOGIN);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(MainFrame.this,
                        "Exit the application?", "Confirm Exit",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);
                }
            }
        });
    }

    private void buildScreens() {
        dashboard = new DashboardPanel(this);
        books = new BookPanel(this);
        members = new MemberPanel(this);
        issue = new IssuePanel(this);
        returns = new ReturnPanel(this);
        history = new HistoryPanel(this);
        fines = new FinePanel(this);

        content.add(new LoginPanel(this), LOGIN);
        content.add(dashboard, DASHBOARD);
        content.add(books, BOOKS);
        content.add(members, MEMBERS);
        content.add(issue, ISSUE);
        content.add(returns, RETURN);
        content.add(history, HISTORY);
        content.add(fines, FINES);
    }

    public LibraryController getController() { return controller; }

    public void showScreen(String name) {
        if (LOGIN.equals(name)) {
            cards.show(content, LOGIN);
        } else {
            refreshAll();
            cards.show(content, name);
        }
    }

    public void logout() {
        showScreen(LOGIN);
    }

    public void refreshAll() {
        dashboard.refreshData();
        books.refreshData();
        members.refreshData();
        issue.refreshData();
        returns.refreshData();
        history.refreshData();
        fines.refreshData();
    }

    public JButton navButton(String text, String screen) {
        JButton b = new JButton(text);
        Theme.styleButton(b, false);
        b.addActionListener(e -> showScreen(screen));
        return b;
    }
}
