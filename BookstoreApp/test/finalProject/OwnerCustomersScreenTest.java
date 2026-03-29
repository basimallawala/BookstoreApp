package finalProject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
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
public class OwnerCustomersScreenTest {

    private BookStoreApp         app;
    private OwnerCustomersScreen ownerCustomersScreen;

    @Before
    public void setUp() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                app = new BookStoreApp();
                app.setVisible(false);
                ownerCustomersScreen = new OwnerCustomersScreen(app);
                app.switchTo(ownerCustomersScreen);
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

    //get a text field by its private field name
    private JTextField getField(String name) throws Exception {
        Field f = OwnerCustomersScreen.class.getDeclaredField(name);
        f.setAccessible(true);
        return (JTextField) f.get(ownerCustomersScreen);
    }

    //access the private tableModel field
    private DefaultTableModel getTableModel() throws Exception {
        Field f = OwnerCustomersScreen.class.getDeclaredField("tableModel");
        f.setAccessible(true);
        return (DefaultTableModel) f.get(ownerCustomersScreen);
    }

    //access the private customersTable field
    private JTable getTable() throws Exception {
        Field f = OwnerCustomersScreen.class.getDeclaredField("customersTable");
        f.setAccessible(true);
        return (JTable) f.get(ownerCustomersScreen);
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
        assertTrue(ownerCustomersScreen instanceof JPanel);
    }

    @Test
    public void testHasAddButton() {
        assertNotNull(findButton(ownerCustomersScreen, "Add"));
    }

    @Test
    public void testHasDeleteButton() {
        assertNotNull(findButton(ownerCustomersScreen, "Delete"));
    }

    @Test
    public void testHasBackButton() {
        assertNotNull(findButton(ownerCustomersScreen, "Back"));
    }

    //add customer - happy path

    @Test
    public void testAddValidCustomerIncreasesSystemList() throws Exception {
        final JTextField usernameField = getField("usernameField");
        final JTextField passwordField = getField("passwordField");
        final JButton addBtn = findButton(ownerCustomersScreen, "Add");
        int before = app.getBookstoreSystem().getCustomerList().size();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("alice");
                passwordField.setText("pass1");
                addBtn.doClick();
            }
        });

        assertEquals(before + 1, app.getBookstoreSystem().getCustomerList().size());
    }

    @Test
    public void testAddValidCustomerAddsRowToTable() throws Exception {
        final JTextField usernameField = getField("usernameField");
        final JTextField passwordField = getField("passwordField");
        final JButton addBtn = findButton(ownerCustomersScreen, "Add");
        final DefaultTableModel model = getTableModel();
        int before = model.getRowCount();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("bob");
                passwordField.setText("pass2");
                addBtn.doClick();
            }
        });

        assertEquals(before + 1, model.getRowCount());
    }

    //new customers must always start with 0 points
    @Test
    public void testNewCustomerStartsWithZeroPoints() throws Exception {
        final JTextField usernameField = getField("usernameField");
        final JTextField passwordField = getField("passwordField");
        final JButton addBtn = findButton(ownerCustomersScreen, "Add");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("carol");
                passwordField.setText("pass3");
                addBtn.doClick();
            }
        });

        int last = app.getBookstoreSystem().getCustomerList().size() - 1;
        assertEquals(0, app.getBookstoreSystem().getCustomerList().get(last).getPoints());
    }

    //input fields must be cleared after a successful add
    @Test
    public void testAddValidCustomerClearsInputFields() throws Exception {
        final JTextField usernameField = getField("usernameField");
        final JTextField passwordField = getField("passwordField");
        final JButton addBtn = findButton(ownerCustomersScreen, "Add");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("dave");
                passwordField.setText("pass4");
                addBtn.doClick();
            }
        });

        assertEquals("", usernameField.getText());
        assertEquals("", passwordField.getText());
    }

    //add customer - validation failures

    @Test
    public void testAddWithEmptyUsernameDoesNotAdd() throws Exception {
        final JTextField usernameField = getField("usernameField");
        final JTextField passwordField = getField("passwordField");
        final JButton addBtn = findButton(ownerCustomersScreen, "Add");
        int before = app.getBookstoreSystem().getCustomerList().size();
        scheduleDismissDialogs();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("");
                passwordField.setText("pass5");
                addBtn.doClick();
            }
        });

        assertEquals(before, app.getBookstoreSystem().getCustomerList().size());
    }

    @Test
    public void testAddWithEmptyPasswordDoesNotAdd() throws Exception {
        final JTextField usernameField = getField("usernameField");
        final JTextField passwordField = getField("passwordField");
        final JButton addBtn = findButton(ownerCustomersScreen, "Add");
        int before = app.getBookstoreSystem().getCustomerList().size();
        scheduleDismissDialogs();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("eve");
                passwordField.setText("");
                addBtn.doClick();
            }
        });

        assertEquals(before, app.getBookstoreSystem().getCustomerList().size());
    }

    //"admin" is a reserved username and must be rejected
    @Test
    public void testAddWithAdminUsernameDoesNotAdd() throws Exception {
        final JTextField usernameField = getField("usernameField");
        final JTextField passwordField = getField("passwordField");
        final JButton addBtn = findButton(ownerCustomersScreen, "Add");
        int before = app.getBookstoreSystem().getCustomerList().size();
        scheduleDismissDialogs();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("admin");
                passwordField.setText("anypass");
                addBtn.doClick();
            }
        });

        assertEquals(before, app.getBookstoreSystem().getCustomerList().size());
    }

    //duplicate usernames must be rejected
    @Test
    public void testAddDuplicateUsernameDoesNotAdd() throws Exception {
        final JTextField usernameField = getField("usernameField");
        final JTextField passwordField = getField("passwordField");
        final JButton addBtn = findButton(ownerCustomersScreen, "Add");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("frank");
                passwordField.setText("pass6");
                addBtn.doClick();
            }
        });

        int afterFirst = app.getBookstoreSystem().getCustomerList().size();
        scheduleDismissDialogs();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("frank");
                passwordField.setText("otherpass");
                addBtn.doClick();
            }
        });

        assertEquals(afterFirst, app.getBookstoreSystem().getCustomerList().size());
    }

    //delete customer tests

    @Test
    public void testDeleteWithNoSelectionDoesNotRemove() throws Exception {
        final JTextField usernameField = getField("usernameField");
        final JTextField passwordField = getField("passwordField");
        final JButton addBtn  = findButton(ownerCustomersScreen, "Add");
        final JButton deleteBtn = findButton(ownerCustomersScreen, "Delete");
        final JTable table = getTable();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("grace");
                passwordField.setText("pass7");
                addBtn.doClick();
            }
        });

        int afterAdd = app.getBookstoreSystem().getCustomerList().size();
        scheduleDismissDialogs();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                table.clearSelection();
                deleteBtn.doClick();
            }
        });

        assertEquals(afterAdd, app.getBookstoreSystem().getCustomerList().size());
    }

    @Test
    public void testDeleteSelectedCustomerRemovesFromSystem() throws Exception {
        final JTextField usernameField = getField("usernameField");
        final JTextField passwordField = getField("passwordField");
        final JButton addBtn = findButton(ownerCustomersScreen, "Add");
        final JButton deleteBtn = findButton(ownerCustomersScreen, "Delete");
        final JTable table = getTable();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("henry");
                passwordField.setText("pass8");
                addBtn.doClick();
            }
        });

        int afterAdd = app.getBookstoreSystem().getCustomerList().size();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                table.setRowSelectionInterval(0, 0);
                deleteBtn.doClick();
            }
        });

        assertEquals(afterAdd - 1, app.getBookstoreSystem().getCustomerList().size());
    }

    @Test
    public void testDeleteSelectedCustomerRemovesRowFromTable() throws Exception {
        final JTextField usernameField = getField("usernameField");
        final JTextField passwordField = getField("passwordField");
        final JButton addBtn = findButton(ownerCustomersScreen, "Add");
        final JButton deleteBtn = findButton(ownerCustomersScreen, "Delete");
        final JTable table = getTable();
        final DefaultTableModel model       = getTableModel();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                usernameField.setText("iris");
                passwordField.setText("pass9");
                addBtn.doClick();
            }
        });

        int rowsBefore = model.getRowCount();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                table.setRowSelectionInterval(0, 0);
                deleteBtn.doClick();
            }
        });

        assertEquals(rowsBefore - 1, model.getRowCount());
    }

    //navigation tests

    @Test
    public void testBackNavigatesToOwnerStartScreen() throws Exception {
        final JButton backBtn = findButton(ownerCustomersScreen, "Back");
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                backBtn.doClick();
            }
        });
        assertTrue(app.getContentPane() instanceof OwnerStartScreen);
    }
}