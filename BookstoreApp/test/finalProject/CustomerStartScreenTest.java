package finalProject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import static org.junit.Assert.*;

/**
 * @author Sullay
 */
public class CustomerStartScreenTest {

    private BookStoreApp        app;
    private Customer            customer;
    private CustomerStartScreen screen;

    private Book book50;
    private Book book100;
    private Book book200;
    private Book book500;

    @Before
    public void setUp() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                app = new BookStoreApp();
                app.setVisible(false);

                //clear any data loaded from disk before injecting test books
                app.getBookstoreSystem().bookList.clear();
                app.getBookstoreSystem().getCustomerList().clear();

                //add four test books directly to the in-memory list
                book50 = new Book("Book50",  50.0);
                book100 = new Book("Book100", 100.0);
                book200 = new Book("Book200", 200.0);
                book500 = new Book("Book500", 500.0);
                app.getBookstoreSystem().addBook(book50);
                app.getBookstoreSystem().addBook(book100);
                app.getBookstoreSystem().addBook(book200);
                app.getBookstoreSystem().addBook(book500);

                //customer starts at 0 points / SilverTier
                customer = new Customer("jane", "pass", 0, new SilverTier());
                app.getBookstoreSystem().addCustomer(customer);

                screen = new CustomerStartScreen(app, customer);
                app.switchTo(screen);
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

    //access the private tableModel field from a given screen instance
    private DefaultTableModel getTableModel(CustomerStartScreen target) throws Exception {
        Field f = CustomerStartScreen.class.getDeclaredField("tableModel");
        f.setAccessible(true);
        return (DefaultTableModel) f.get(target);
    }

    //check the Select checkbox for the row matching the given book
    private void selectBookInTable(DefaultTableModel model, Book book) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(book.getName())) {
                model.setValueAt(Boolean.TRUE, i, 2);
                return;
            }
        }
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
    public void testIsJPanel() {
        assertTrue(screen instanceof JPanel);
    }

    @Test
    public void testHasBuyButton() {
        assertNotNull(findButton(screen, "Buy"));
    }

    @Test
    public void testHasRedeemButton() {
        assertNotNull(findButton(screen, "Redeem Points and Buy"));
    }

    @Test
    public void testHasLogoutButton() {
        assertNotNull(findButton(screen, "Logout"));
    }

    //catalog table tests

    //setUp adds 4 books so the catalog must have exactly 4 rows
    @Test
    public void testCatalogHasCorrectNumberOfRows() throws Exception {
        DefaultTableModel model = getTableModel(screen);
        assertEquals(4, model.getRowCount());
    }

    //every Select checkbox must start unchecked after loadCatalog()
    @Test
    public void testAllCheckboxesStartUnchecked() throws Exception {
        DefaultTableModel model = getTableModel(screen);
        for (int i = 0; i < model.getRowCount(); i++) {
            assertEquals(Boolean.FALSE, model.getValueAt(i, 2));
        }
    }

    //Buy with no selection - must not navigate or change points

    @Test
    public void testBuyWithNoSelectionStaysOnCurrentScreen() throws Exception {
        scheduleDismissDialogs();
        final JButton buyBtn = findButton(screen, "Buy");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                buyBtn.doClick();
            }
        });

        assertTrue(app.getContentPane() instanceof CustomerStartScreen);
    }

    @Test
    public void testBuyWithNoSelectionDoesNotChangePoints() throws Exception {
        scheduleDismissDialogs();
        final JButton buyBtn = findButton(screen, "Buy");
        int before = customer.getPoints();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                buyBtn.doClick();
            }
        });

        assertEquals(before, customer.getPoints());
    }

    //Redeem with no selection - must not navigate

    @Test
    public void testRedeemWithNoSelectionStaysOnCurrentScreen() throws Exception {
        scheduleDismissDialogs();
        final JButton redeemBtn = findButton(screen, "Redeem Points and Buy");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                redeemBtn.doClick();
            }
        });

        assertTrue(app.getContentPane() instanceof CustomerStartScreen);
    }

    //scenario 1: Buy $200 + $500 with 0 points -> TC=$700, points=7000, Gold

    @Test
    public void testBuyScenario1NavigatesToCostScreen() throws Exception {
        final DefaultTableModel model = getTableModel(screen);
        final JButton buyBtn = findButton(screen, "Buy");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                selectBookInTable(model, book200);
                selectBookInTable(model, book500);
                buyBtn.doClick();
            }
        });

        assertTrue(app.getContentPane() instanceof CustomerCostScreen);
    }

    @Test
    public void testBuyScenario1TotalCostIsSevenHundred() throws Exception {
        final DefaultTableModel model = getTableModel(screen);
        final JButton buyBtn = findButton(screen, "Buy");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                selectBookInTable(model, book200);
                selectBookInTable(model, book500);
                buyBtn.doClick();
            }
        });

        Field costField = CustomerCostScreen.class.getDeclaredField("totalCost");
        costField.setAccessible(true);
        double tc = (double) costField.get(app.getContentPane());
        assertEquals(700.0, tc, 0.001);
    }

    @Test
    public void testBuyScenario1PointsBecomeSevenThousand() throws Exception {
        final DefaultTableModel model = getTableModel(screen);
        final JButton buyBtn = findButton(screen, "Buy");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                selectBookInTable(model, book200);
                selectBookInTable(model, book500);
                buyBtn.doClick();
            }
        });

        //700 * 10 = 7000 points
        assertEquals(7000, customer.getPoints());
    }

    @Test
    public void testBuyScenario1StatusIsGold() throws Exception {
        final DefaultTableModel model = getTableModel(screen);
        final JButton buyBtn = findButton(screen, "Buy");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                selectBookInTable(model, book200);
                selectBookInTable(model, book500);
                buyBtn.doClick();
            }
        });

        //7000 >= 1000 -> Gold
        assertTrue(customer.getPoints() >= 1000);
    }

    //scenario 2: Redeem $50 book with 7000 points -> TC=$0, points=2000, Gold

    @Test
    public void testRedeemScenario2TotalCostIsZero() throws Exception {
        customer.setPoints(7000);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                screen = new CustomerStartScreen(app, customer);
                app.switchTo(screen);
            }
        });

        final DefaultTableModel model = getTableModel(screen);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                selectBookInTable(model, book50);
                findButton(screen, "Redeem Points and Buy").doClick();
            }
        });

        Field costField = CustomerCostScreen.class.getDeclaredField("totalCost");
        costField.setAccessible(true);
        assertEquals(0.0, (double) costField.get(app.getContentPane()), 0.001);
    }

    @Test
    public void testRedeemScenario2PointsBecomeTwoThousand() throws Exception {
        customer.setPoints(7000);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                screen = new CustomerStartScreen(app, customer);
                app.switchTo(screen);
            }
        });

        final DefaultTableModel model = getTableModel(screen);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                selectBookInTable(model, book50);
                findButton(screen, "Redeem Points and Buy").doClick();
            }
        });

        //pointsUsed=5000, pointsEarned=0 -> 7000 - 5000 + 0 = 2000
        assertEquals(2000, customer.getPoints());
    }

    @Test
    public void testRedeemScenario2StatusRemainsGold() throws Exception {
        customer.setPoints(7000);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                screen = new CustomerStartScreen(app, customer);
                app.switchTo(screen);
            }
        });

        final DefaultTableModel model = getTableModel(screen);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                selectBookInTable(model, book50);
                findButton(screen, "Redeem Points and Buy").doClick();
            }
        });

        //2000 >= 1000 -> still Gold
        assertTrue(customer.getPoints() >= 1000);
    }

    //scenario 3: Redeem $100 book with 2000 points -> TC=$80, points=800, Silver

    @Test
    public void testRedeemScenario3TotalCostIsEighty() throws Exception {
        customer.setPoints(2000);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                screen = new CustomerStartScreen(app, customer);
                app.switchTo(screen);
            }
        });

        final DefaultTableModel model = getTableModel(screen);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                selectBookInTable(model, book100);
                findButton(screen, "Redeem Points and Buy").doClick();
            }
        });

        Field costField = CustomerCostScreen.class.getDeclaredField("totalCost");
        costField.setAccessible(true);
        assertEquals(80.0, (double) costField.get(app.getContentPane()), 0.001);
    }

    @Test
    public void testRedeemScenario3PointsBecomeEightHundred() throws Exception {
        customer.setPoints(2000);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                screen = new CustomerStartScreen(app, customer);
                app.switchTo(screen);
            }
        });

        final DefaultTableModel model = getTableModel(screen);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                selectBookInTable(model, book100);
                findButton(screen, "Redeem Points and Buy").doClick();
            }
        });

        //2000 - 2000 + (80*10) = 800
        assertEquals(800, customer.getPoints());
    }

    @Test
    public void testRedeemScenario3StatusDropsToSilver() throws Exception {
        customer.setPoints(2000);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                screen = new CustomerStartScreen(app, customer);
                app.switchTo(screen);
            }
        });

        final DefaultTableModel model = getTableModel(screen);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                selectBookInTable(model, book100);
                findButton(screen, "Redeem Points and Buy").doClick();
            }
        });

        //800 < 1000 -> Silver
        assertTrue(customer.getPoints() < 1000);
    }

    //total cost can never go negative

    @Test
    public void testRedeemCostNeverGoesNegative() throws Exception {
        customer.setPoints(999999);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                screen = new CustomerStartScreen(app, customer);
                app.switchTo(screen);
            }
        });

        final DefaultTableModel model = getTableModel(screen);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                selectBookInTable(model, book50);
                findButton(screen, "Redeem Points and Buy").doClick();
            }
        });

        Field costField = CustomerCostScreen.class.getDeclaredField("totalCost");
        costField.setAccessible(true);
        double tc = (double) costField.get(app.getContentPane());
        assertTrue("Total cost must not be negative, got: " + tc, tc >= 0.0);
    }

    //logout tests

    @Test
    public void testLogoutNavigatesToLoginScreen() throws Exception {
        final JButton logoutBtn = findButton(screen, "Logout");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                logoutBtn.doClick();
            }
        });

        assertTrue(app.getContentPane() instanceof LoginScreen);
    }

    @Test
    public void testLogoutDoesNotNavigateToOwnerScreen() throws Exception {
        final JButton logoutBtn = findButton(screen, "Logout");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                logoutBtn.doClick();
            }
        });

        assertFalse(app.getContentPane() instanceof OwnerStartScreen);
    }
}