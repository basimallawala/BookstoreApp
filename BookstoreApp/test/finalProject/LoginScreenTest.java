package finalProject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static org.junit.Assert.*;

/**
 * @author Sullay
 */
public class LoginScreenTest {

    private BookStoreApp app;
    private LoginScreen  loginScreen;

    @Before
    public void setUp() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                app = new BookStoreApp();
                app.setVisible(false);
                loginScreen = new LoginScreen(app);
                app.switchTo(loginScreen);
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        //dispose the application window after each test to clean up resources
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                if (app != null) {
                    app.dispose();
                }
            }
        });
    }

    //find the first JTextField that is not a JPasswordField
    private JTextField findTextField(Container root) {
        for (Component c : root.getComponents()) {
            if (c instanceof JPasswordField) {
            } else if (c instanceof JTextField jTextField) {
                return jTextField;
            }
            if (c instanceof Container container) {
                JTextField found = findTextField(container);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    //recursively search for a JPasswordField
    private JPasswordField findPasswordField(Container root) {
        for (Component c : root.getComponents()) {
            if (c instanceof JPasswordField jPasswordField) {
                return jPasswordField;
            }
            if (c instanceof Container container) {
                JPasswordField found = findPasswordField(container);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    //recursively search for a JButton with the given label
    private JButton findButton(Container root, String label) {
        for (Component c : root.getComponents()) {
            if (c instanceof JButton && label.equals(((JButton) c).getText())) {
                return (JButton) c;
            }
            if (c instanceof Container container) {
                JButton found = findButton(container, label);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    //automatically close any visible dialogs to prevent tests from hanging
    private void scheduleDismissDialogs() {
        Timer t = new Timer(150, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Window w : Window.getWindows()) {
                    if (w.isVisible() && w.getClass().getName().contains("Dialog")) {
                        w.dispose();
                    }
                }
            }
        });
        t.setRepeats(false);
        t.start();
    }

    //structure tests

    @Test
    public void testLoginScreenIsJPanel() {
        assertTrue(loginScreen instanceof JPanel);
    }

    @Test
    public void testHasUsernameTextField() {
        assertNotNull(findTextField(loginScreen));
    }

    @Test
    public void testHasPasswordField() {
        assertNotNull(findPasswordField(loginScreen));
    }

    @Test
    public void testHasLoginButton() {
        assertNotNull(findButton(loginScreen, "Login"));
    }

    //navigation tests - successful logins

    @Test
    public void testAdminCredentialsNavigateToOwnerStartScreen() throws Exception {
        final JTextField usernameField = findTextField(loginScreen);
        final JPasswordField passwordField = findPasswordField(loginScreen);
        final JButton loginBtn = findButton(loginScreen, "Login");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("admin");
                passwordField.setText("admin");
                loginBtn.doClick();
            }
        });

        assertTrue(app.getContentPane() instanceof OwnerStartScreen);
    }

    @Test
    public void testRegisteredCustomerNavigatesToCustomerStartScreen() throws Exception {
        app.getBookstoreSystem().addCustomer(
                new Customer("jane", "pass123", 0, new SilverTier()));

        final JTextField usernameField = findTextField(loginScreen);
        final JPasswordField passwordField = findPasswordField(loginScreen);
        final JButton loginBtn = findButton(loginScreen, "Login");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("jane");
                passwordField.setText("pass123");
                loginBtn.doClick();
            }
        });

        assertTrue(app.getContentPane() instanceof CustomerStartScreen);
    }

    //navigation tests - failed logins

    @Test
    public void testWrongPasswordStaysOnLoginScreen() throws Exception {
        app.getBookstoreSystem().addCustomer(
                new Customer("bob", "correctpass", 0, new SilverTier()));

        final JTextField usernameField = findTextField(loginScreen);
        final JPasswordField passwordField = findPasswordField(loginScreen);
        final JButton loginBtn = findButton(loginScreen, "Login");

        scheduleDismissDialogs();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("bob");
                passwordField.setText("wrongpass");
                loginBtn.doClick();
            }
        });

        assertTrue(app.getContentPane() instanceof LoginScreen);
    }

    @Test
    public void testUnknownCredentialsStayOnLoginScreen() throws Exception {
        final JTextField usernameField = findTextField(loginScreen);
        final JPasswordField passwordField = findPasswordField(loginScreen);
        final JButton loginBtn = findButton(loginScreen, "Login");

        scheduleDismissDialogs();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("nobody");
                passwordField.setText("nothing");
                loginBtn.doClick();
            }
        });

        assertTrue(app.getContentPane() instanceof LoginScreen);
    }

    @Test
    public void testWrongUsernameStaysOnLoginScreen() throws Exception {
        final JTextField usernameField = findTextField(loginScreen);
        final JPasswordField passwordField = findPasswordField(loginScreen);
        final JButton loginBtn = findButton(loginScreen, "Login");

        scheduleDismissDialogs();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("notadmin");
                passwordField.setText("admin");
                loginBtn.doClick();
            }
        });

        assertTrue(app.getContentPane() instanceof LoginScreen);
    }

    //admin must reach OwnerStartScreen, not CustomerStartScreen
    @Test
    public void testAdminGoesToOwnerNotCustomerScreen() throws Exception {
        final JTextField usernameField = findTextField(loginScreen);
        final JPasswordField passwordField = findPasswordField(loginScreen);
        final JButton loginBtn = findButton(loginScreen, "Login");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("admin");
                passwordField.setText("admin");
                loginBtn.doClick();
            }
        });

        assertFalse(app.getContentPane() instanceof CustomerStartScreen);
    }

    //a failed login must clear the password field
    @Test
    public void testFailedLoginClearsPasswordField() throws Exception {
        final JTextField usernameField = findTextField(loginScreen);
        final JPasswordField passwordField = findPasswordField(loginScreen);
        final JButton loginBtn = findButton(loginScreen, "Login");

        scheduleDismissDialogs();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("nobody");
                passwordField.setText("badpass");
                loginBtn.doClick();
            }
        });

        Thread.sleep(300);
        assertEquals("", new String(passwordField.getPassword()));
    }
}