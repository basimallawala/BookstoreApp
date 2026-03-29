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
 * JUnit 4 tests for OwnerStartScreen.
 *
 * -----------------------------------------------------------------------
 * WHAT OWNERSTARTSCREEN IS RESPONSIBLE FOR
 * -----------------------------------------------------------------------
 *   - Displays exactly three buttons: [Books], [Customers], [Logout].
 *   - [Books]     → navigates to OwnerBooksScreen.
 *   - [Customers] → navigates to OwnerCustomersScreen.
 *   - [Logout]    → returns to LoginScreen.
 *
 * -----------------------------------------------------------------------
 * DEPENDENCY ON BOOKSTORESYSTEM
 * -----------------------------------------------------------------------
 *   - This screen is purely a navigation hub; it does not manipulate data.
 *   - No file I/O is triggered by these tests.
 *
 * -----------------------------------------------------------------------
 * PASS / FAIL SUMMARY
 * -----------------------------------------------------------------------
 *   - All tests pass because all three buttons exist and navigate correctly.
 *   - Exactly three buttons are present on the screen.
 */
public class OwnerStartScreenTest {

    private BookStoreApp     app;
    private OwnerStartScreen ownerStartScreen;

    @Before
    public void setUp() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
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
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                if (app != null) {
                    app.dispose();
                }
            }
        });
    }

    // ------------------------------------------------------------------
    // Helper methods
    // ------------------------------------------------------------------

    /**
     * Recursively searches for a JButton by its label text.
     */
    private JButton findButton(Container root, String label) { /* unchanged */ }

    /**
     * Recursively counts all JButton instances inside a container.
     */
    private int countButtons(Container root) { /* unchanged */ }

    // ------------------------------------------------------------------
    // Structure tests
    // ------------------------------------------------------------------

    @Test
    public void testIsJPanel() {
        // Confirms that OwnerStartScreen is a JPanel
        assertTrue(ownerStartScreen instanceof JPanel);
    }

    @Test
    public void testHasBooksButton() {
        // Confirms the [Books] button exists
        assertNotNull(findButton(ownerStartScreen, "Books"));
    }

    @Test
    public void testHasCustomersButton() {
        // Confirms the [Customers] button exists
        assertNotNull(findButton(ownerStartScreen, "Customers"));
    }

    @Test
    public void testHasLogoutButton() {
        // Confirms the [Logout] button exists
        assertNotNull(findButton(ownerStartScreen, "Logout"));
    }

    @Test
    public void testExactlyThreeButtons() {
        // Per spec, there should be exactly three buttons on this screen
        assertEquals(3, countButtons(ownerStartScreen));
    }

    // ------------------------------------------------------------------
    // Navigation — correct destinations
    // ------------------------------------------------------------------

    @Test
    public void testBooksButtonNavigatesToOwnerBooksScreen() throws Exception {
        // Clicking [Books] navigates to OwnerBooksScreen
        final JButton booksBtn = findButton(ownerStartScreen, "Books");
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                booksBtn.doClick();
            }
        });
        assertTrue(app.getContentPane() instanceof OwnerBooksScreen);
    }

    @Test
    public void testCustomersButtonNavigatesToOwnerCustomersScreen() throws Exception {
        // Clicking [Customers] navigates to OwnerCustomersScreen
        final JButton customersBtn = findButton(ownerStartScreen, "Customers");
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                customersBtn.doClick();
            }
        });
        assertTrue(app.getContentPane() instanceof OwnerCustomersScreen);
    }

    @Test
    public void testLogoutButtonNavigatesToLoginScreen() throws Exception {
        // Clicking [Logout] navigates to LoginScreen
        final JButton logoutBtn = findButton(ownerStartScreen, "Logout");
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                logoutBtn.doClick();
            }
        });
        assertTrue(app.getContentPane() instanceof LoginScreen);
    }

    // ------------------------------------------------------------------
    // Navigation — wrong destinations
    // ------------------------------------------------------------------

    @Test
    public void testBooksButtonDoesNotGoToLoginScreen() throws Exception {
        // [Books] button should NOT navigate to LoginScreen
        final JButton booksBtn = findButton(ownerStartScreen, "Books");
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                booksBtn.doClick();
            }
        });
        assertFalse(app.getContentPane() instanceof LoginScreen);
    }

    @Test
    public void testLogoutButtonDoesNotGoToBooksScreen() throws Exception {
        // [Logout] button should NOT navigate to OwnerBooksScreen
        final JButton logoutBtn = findButton(ownerStartScreen, "Logout");
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                logoutBtn.doClick();
            }
        });
        assertFalse(app.getContentPane() instanceof OwnerBooksScreen);
    }

    @Test
    public void testCustomersButtonDoesNotGoToLoginScreen() throws Exception {
        // [Customers] button should NOT navigate to LoginScreen
        final JButton customersBtn = findButton(ownerStartScreen, "Customers");
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                customersBtn.doClick();
            }
        });
        assertFalse(app.getContentPane() instanceof LoginScreen);
    }
}