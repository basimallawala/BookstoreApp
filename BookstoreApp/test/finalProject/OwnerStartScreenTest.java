package finalProject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Container;
import static org.junit.Assert.*;

/**
 * @author Sullay
 */
public class OwnerStartScreenTest {

    private BookStoreApp     app;
    private OwnerStartScreen ownerStartScreen;

    @Before
    public void setUp() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                app = new BookStoreApp();
                app.setVisible(false);
                ownerStartScreen = new OwnerStartScreen(app);
                app.switchTo(ownerStartScreen);
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

    //count all JButtons recursively
    private int countButtons(Container root) {
        int count = 0;
        for (Component c : root.getComponents()) {
            if (c instanceof JButton) {
                count++;
            }
            if (c instanceof Container container) {
                count += countButtons(container);
            }
        }
        return count;
    }

    //structure tests

    @Test
    public void testIsJPanel() {
        assertTrue(ownerStartScreen instanceof JPanel);
    }

    @Test
    public void testHasBooksButton() {
        assertNotNull(findButton(ownerStartScreen, "Books"));
    }

    @Test
    public void testHasCustomersButton() {
        assertNotNull(findButton(ownerStartScreen, "Customers"));
    }

    @Test
    public void testHasLogoutButton() {
        assertNotNull(findButton(ownerStartScreen, "Logout"));
    }

    //screen must have exactly three buttons
    @Test
    public void testExactlyThreeButtons() {
        assertEquals(3, countButtons(ownerStartScreen));
    }

    //navigation tests - correct destinations

    @Test
    public void testBooksButtonNavigatesToOwnerBooksScreen() throws Exception {
        final JButton booksBtn = findButton(ownerStartScreen, "Books");
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                booksBtn.doClick();
            }
        });
        assertTrue(app.getContentPane() instanceof OwnerBooksScreen);
    }

    @Test
    public void testCustomersButtonNavigatesToOwnerCustomersScreen() throws Exception {
        final JButton customersBtn = findButton(ownerStartScreen, "Customers");
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                customersBtn.doClick();
            }
        });
        assertTrue(app.getContentPane() instanceof OwnerCustomersScreen);
    }

    @Test
    public void testLogoutButtonNavigatesToLoginScreen() throws Exception {
        final JButton logoutBtn = findButton(ownerStartScreen, "Logout");
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                logoutBtn.doClick();
            }
        });
        assertTrue(app.getContentPane() instanceof LoginScreen);
    }

    //navigation tests - wrong destinations

    @Test
    public void testBooksButtonDoesNotGoToLoginScreen() throws Exception {
        final JButton booksBtn = findButton(ownerStartScreen, "Books");
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                booksBtn.doClick();
            }
        });
        assertFalse(app.getContentPane() instanceof LoginScreen);
    }

    @Test
    public void testLogoutButtonDoesNotGoToBooksScreen() throws Exception {
        final JButton logoutBtn = findButton(ownerStartScreen, "Logout");
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                logoutBtn.doClick();
            }
        });
        assertFalse(app.getContentPane() instanceof OwnerBooksScreen);
    }

    @Test
    public void testCustomersButtonDoesNotGoToLoginScreen() throws Exception {
        final JButton customersBtn = findButton(ownerStartScreen, "Customers");
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                customersBtn.doClick();
            }
        });
        assertFalse(app.getContentPane() instanceof LoginScreen);
    }
}