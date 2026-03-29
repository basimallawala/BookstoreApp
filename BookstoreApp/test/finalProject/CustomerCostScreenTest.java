package finalProject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Field;
import static org.junit.Assert.*;

/**
 * @author Sullay
 */
public class CustomerCostScreenTest {

    private BookStoreApp       app;       // main application window
    private Customer           customer;  // customer used in tests
    private CustomerCostScreen screen;    // screen under test

    @Before
    public void setUp() throws Exception {
        //build app and hide window for testing
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                app = new BookStoreApp();
                app.setVisible(false);
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


     //build a CustomerCostScreen for a customer with a given points
     //and total cost, then show it in the app.
    private void buildScreen(final int points, final double totalCost) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                //assign Silver or Gold tier depending on points
                CustomerStatus tier = (points >= 1000) ? new GoldTier() : new SilverTier();
                customer = new Customer("testuser", "testpass", points, tier);
                screen   = new CustomerCostScreen(app, customer, totalCost);
                app.switchTo(screen);
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
                if (found != null) return found;
            }
        }
        return null;
    }

    //access costLabel text     
    private String getCostLabelText() throws Exception {
        Field f = CustomerCostScreen.class.getDeclaredField("costLabel");
        f.setAccessible(true);
        return ((JLabel) f.get(screen)).getText();
    }

    //access pointsLabel text 
    private String getPointsLabelText() throws Exception {
        Field f = CustomerCostScreen.class.getDeclaredField("pointsLabel");
        f.setAccessible(true);
        return ((JLabel) f.get(screen)).getText();
    }

    //access totalCost field
    private double getStoredTotalCost() throws Exception {
        Field f = CustomerCostScreen.class.getDeclaredField("totalCost");
        f.setAccessible(true);
        return (double) f.get(screen);
    }

    
    @Test
    public void testIsJPanel() throws Exception {
        buildScreen(0, 100.0);
        //screen must be a JPanel
        assertTrue(screen instanceof JPanel);
    }

    @Test
    public void testHasLogoutButton() throws Exception {
        buildScreen(0, 100.0);
        //screen must have a Logout button
        assertNotNull(findButton(screen, "Logout"));
    }

    @Test
    public void testHasCostLabel() throws Exception {
        buildScreen(0, 100.0);
        //screen must have costLabel
        Field f = CustomerCostScreen.class.getDeclaredField("costLabel");
        f.setAccessible(true);
        assertNotNull(f.get(screen));
    }

    @Test
    public void testHasPointsLabel() throws Exception {
        buildScreen(0, 100.0);
        //screen must have pointsLabel
        Field f = CustomerCostScreen.class.getDeclaredField("pointsLabel");
        f.setAccessible(true);
        assertNotNull(f.get(screen));
    }


    @Test
    public void testCostLabelContains700() throws Exception {
        buildScreen(7000, 700.0);
        //cost label should show 700.00
        assertTrue(getCostLabelText().contains("700.00"));
    }

    @Test
    public void testCostLabelContainsZero() throws Exception {
        buildScreen(2000, 0.0);
        //cost label should show 0.00 for fully redeemed points
        assertTrue(getCostLabelText().contains("0.00"));
    }

    @Test
    public void testCostLabelContains80() throws Exception {
        buildScreen(800, 80.0);
        //cost label should show 80.00 after partial redemption
        assertTrue(getCostLabelText().contains("80.00"));
    }

    @Test
    public void testStoredTotalCostMatchesConstructorArgument() throws Exception {
        buildScreen(0, 123.45);
        //totalCost field has to match constructor argument
        assertEquals(123.45, getStoredTotalCost(), 0.001);
    }

    //points label test
    
    @Test
    public void testPointsLabelShowsSevenThousand() throws Exception {
        buildScreen(7000, 0.0);
        //points label should show 7000 points
        assertTrue(getPointsLabelText().contains("7000"));
    }

    @Test
    public void testPointsLabelShowsEightHundred() throws Exception {
        buildScreen(800, 80.0);
        //points label should show 800 points
        assertTrue(getPointsLabelText().contains("800"));
    }

    @Test
    public void testPointsLabelShowsZero() throws Exception {
        buildScreen(0, 50.0);
        //points label should show 0 points
        assertTrue(getPointsLabelText().contains("0"));
    }

    //testing customer status is correctly determined as gold or silver depending on points
    @Test
    public void testStatusIsGoldAtExactThreshold() throws Exception {
        buildScreen(1000, 0.0);
        assertTrue(getPointsLabelText().contains("Gold"));
    }

    @Test
    public void testStatusIsGoldAboveThreshold() throws Exception {
        buildScreen(7000, 0.0);
        assertTrue(getPointsLabelText().contains("Gold"));
    }

    @Test
    public void testStatusIsSilverBelowThreshold() throws Exception {
        buildScreen(800, 80.0);
        assertTrue(getPointsLabelText().contains("Silver"));
    }

    @Test
    public void testStatusIsSilverAtZeroPoints() throws Exception {
        buildScreen(0, 50.0);
        assertTrue(getPointsLabelText().contains("Silver"));
    }

    @Test
    public void testStatusIsSilverAtNineHundredNinetyNine() throws Exception {
        buildScreen(999, 0.0);
        assertTrue(getPointsLabelText().contains("Silver"));
    }

    @Test
    public void testGoldNotShownWhenSilver() throws Exception {
        buildScreen(500, 0.0);
        //silver customer; gold should not be shown
        assertFalse(getPointsLabelText().contains("Gold"));
    }

    @Test
    public void testSilverNotShownWhenGold() throws Exception {
        buildScreen(1500, 0.0);
        //gold customer; silver should not be shown
        assertFalse(getPointsLabelText().contains("Silver"));
    }


    //navigation tests
    
    @Test
    public void testLogoutNavigatesToLoginScreen() throws Exception {
        buildScreen(0, 100.0);
        final JButton logoutBtn = findButton(screen, "Logout");

        //simulate clicking Logout; it should go to LoginScreen
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                logoutBtn.doClick();
            }
        });

        assertTrue(app.getContentPane() instanceof LoginScreen);
    }

    @Test
    public void testLogoutDoesNotNavigateToCustomerStartScreen() throws Exception {
        buildScreen(0, 100.0);
        final JButton logoutBtn = findButton(screen, "Logout");

        //simulate clicking Logout; it should not go to CustomerStartScreen
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                logoutBtn.doClick();
            }
        });

        assertFalse(app.getContentPane() instanceof CustomerStartScreen);
    }
}

