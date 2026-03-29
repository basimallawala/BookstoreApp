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
public class OwnerBooksScreenTest {

    private BookStoreApp     app;
    private OwnerBooksScreen ownerBooksScreen;

    @Before
    public void setUp() throws Exception {
        //create the BookStoreApp and OwnerBooksScreen
        //window is hidden during testing.
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                app = new BookStoreApp();
                app.setVisible(false);
                ownerBooksScreen = new OwnerBooksScreen(app);
                app.switchTo(ownerBooksScreen);
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


    //search for a button with the given label inside the container.
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

    //get a text field by its private name
    private JTextField getField(String name) throws Exception {
        Field f = OwnerBooksScreen.class.getDeclaredField(name);
        f.setAccessible(true);
        return (JTextField) f.get(ownerBooksScreen);
    }

    //get the table model for the books table.
    private DefaultTableModel getTableModel() throws Exception {
        Field f = OwnerBooksScreen.class.getDeclaredField("booksTableModel");
        f.setAccessible(true);
        return (DefaultTableModel) f.get(ownerBooksScreen);
    }

    //get the JTable for the books table.
    private JTable getTable() throws Exception {
        Field f = OwnerBooksScreen.class.getDeclaredField("booksTable");
        f.setAccessible(true);
        return (JTable) f.get(ownerBooksScreen);
    }

    //automatically close any visible dialog windows to prevent blocking tests.
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


    //verify that the screen is a JPanel.
    @Test
    public void testIsJPanel() {
        assertTrue(ownerBooksScreen instanceof JPanel);
    }

    //verify that the add button exists on the screen.
    @Test
    public void testHasAddButton() {
        assertNotNull(findButton(ownerBooksScreen, "Add"));
    }

    //verify that the delete button exists on the screen.
    @Test
    public void testHasDeleteButton() {
        assertNotNull(findButton(ownerBooksScreen, "Delete"));
    }

    //verify that the back button exists on the screen.
    @Test
    public void testHasBackButton() {
        assertNotNull(findButton(ownerBooksScreen, "Back"));
    }


    //adding a valid book increases the number of books in the system
    @Test
    public void testAddValidBookIncreasesSystemBookList() throws Exception {
        final JTextField nameField  = getField("bookName");
        final JTextField priceField = getField("bookPrice");
        final JButton addBtn        = findButton(ownerBooksScreen, "Add");
        int before = app.getBookstoreSystem().bookList.size();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                nameField.setText("CleanCode");
                priceField.setText("49.99");
                addBtn.doClick();
            }
        });

        assertEquals(before + 1, app.getBookstoreSystem().bookList.size());
    }

    //adding a valid book adds a new row to the table
    @Test
    public void testAddValidBookAddsRowToTable() throws Exception {
        final JTextField nameField  = getField("bookName");
        final JTextField priceField = getField("bookPrice");
        final JButton addBtn        = findButton(ownerBooksScreen, "Add");
        final DefaultTableModel model = getTableModel();
        int before = model.getRowCount();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                nameField.setText("DesignPatterns");
                priceField.setText("39.99");
                addBtn.doClick();
            }
        });

        assertEquals(before + 1, model.getRowCount());
    }

    //adding a valid book clears the input fields after adding.
    @Test
    public void testAddValidBookClearsInputFields() throws Exception {
        final JTextField nameField  = getField("bookName");
        final JTextField priceField = getField("bookPrice");
        final JButton addBtn        = findButton(ownerBooksScreen, "Add");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                nameField.setText("Refactoring");
                priceField.setText("34.99");
                addBtn.doClick();
            }
        });

        assertEquals("", nameField.getText());
        assertEquals("", priceField.getText());
    }

    //verify that the added book stores the correct name.
    @Test
    public void testAddedBookHasCorrectName() throws Exception {
        final JTextField nameField  = getField("bookName");
        final JTextField priceField = getField("bookPrice");
        final JButton addBtn        = findButton(ownerBooksScreen, "Add");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                nameField.setText("SICP");
                priceField.setText("59.99");
                addBtn.doClick();
            }
        });

        int last = app.getBookstoreSystem().bookList.size() - 1;
        assertEquals("SICP", app.getBookstoreSystem().bookList.get(last).getName());
    }

    //verify that the added book stores the correct price.
    @Test
    public void testAddedBookHasCorrectPrice() throws Exception {
        final JTextField nameField  = getField("bookName");
        final JTextField priceField = getField("bookPrice");
        final JButton addBtn        = findButton(ownerBooksScreen, "Add");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                nameField.setText("TAOCP");
                priceField.setText("99.99");
                addBtn.doClick();
            }
        });

        int last = app.getBookstoreSystem().bookList.size() - 1;
        assertEquals(99.99, app.getBookstoreSystem().bookList.get(last).getPrice(), 0.001);
    }



    //if the name field is empty, the book is not added.
    @Test
    public void testAddWithEmptyNameDoesNotAdd() throws Exception {
        final JTextField nameField  = getField("bookName");
        final JTextField priceField = getField("bookPrice");
        final JButton addBtn        = findButton(ownerBooksScreen, "Add");
        int before = app.getBookstoreSystem().bookList.size();
        scheduleDismissDialogs();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                nameField.setText("");
                priceField.setText("10.00");
                addBtn.doClick();
            }
        });

        assertEquals(before, app.getBookstoreSystem().bookList.size());
    }

    //if the price field is empty, the book is not added.
    @Test
    public void testAddWithEmptyPriceDoesNotAdd() throws Exception {
        final JTextField nameField  = getField("bookName");
        final JTextField priceField = getField("bookPrice");
        final JButton addBtn        = findButton(ownerBooksScreen, "Add");
        int before = app.getBookstoreSystem().bookList.size();
        scheduleDismissDialogs();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                nameField.setText("SomeBook");
                priceField.setText("");
                addBtn.doClick();
            }
        });

        assertEquals(before, app.getBookstoreSystem().bookList.size());
    }

    //if the price is not a valid number, the book is not added.
    @Test
    public void testAddWithNonNumericPriceDoesNotAdd() throws Exception {
        final JTextField nameField  = getField("bookName");
        final JTextField priceField = getField("bookPrice");
        final JButton addBtn        = findButton(ownerBooksScreen, "Add");
        int before = app.getBookstoreSystem().bookList.size();
        scheduleDismissDialogs();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                nameField.setText("SomeBook");
                priceField.setText("notANumber");
                addBtn.doClick();
            }
        });

        assertEquals(before, app.getBookstoreSystem().bookList.size());
    }

    //if the price is negative, the book is not added.
    @Test
    public void testAddWithNegativePriceDoesNotAdd() throws Exception {
        final JTextField nameField  = getField("bookName");
        final JTextField priceField = getField("bookPrice");
        final JButton addBtn        = findButton(ownerBooksScreen, "Add");
        int before = app.getBookstoreSystem().bookList.size();
        scheduleDismissDialogs();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                nameField.setText("SomeBook");
                priceField.setText("-5.00");
                addBtn.doClick();
            }
        });

        assertEquals(before, app.getBookstoreSystem().bookList.size());
    }

    //if a book with the same name already exists, the new book is not added.
    @Test
    public void testAddDuplicateNameDoesNotAdd() throws Exception {
        final JTextField nameField  = getField("bookName");
        final JTextField priceField = getField("bookPrice");
        final JButton addBtn        = findButton(ownerBooksScreen, "Add");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                nameField.setText("UniqueBook");
                priceField.setText("20.00");
                addBtn.doClick();
            }
        });

        int afterFirst = app.getBookstoreSystem().bookList.size();
        scheduleDismissDialogs();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                nameField.setText("UniqueBook");
                priceField.setText("25.00");
                addBtn.doClick();
            }
        });

        assertEquals(afterFirst, app.getBookstoreSystem().bookList.size());
    }


    //deleting without selecting a row does not remove any book.
    @Test
    public void testDeleteWithNoSelectionDoesNotRemove() throws Exception {
        final JTextField nameField  = getField("bookName");
        final JTextField priceField = getField("bookPrice");
        final JButton addBtn        = findButton(ownerBooksScreen, "Add");
        final JButton deleteBtn     = findButton(ownerBooksScreen, "Delete");
        final JTable table          = getTable();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                nameField.setText("BookToKeep");
                priceField.setText("9.99");
                addBtn.doClick();
            }
        });

        int afterAdd = app.getBookstoreSystem().bookList.size();
        scheduleDismissDialogs();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                table.clearSelection();
                deleteBtn.doClick();
            }
        });

        assertEquals(afterAdd, app.getBookstoreSystem().bookList.size());
    }

    //deleting a selected book removes it from the system list.
    @Test
    public void testDeleteSelectedBookRemovesFromSystem() throws Exception {
        final JTextField nameField  = getField("bookName");
        final JTextField priceField = getField("bookPrice");
        final JButton addBtn        = findButton(ownerBooksScreen, "Add");
        final JButton deleteBtn     = findButton(ownerBooksScreen, "Delete");
        final JTable table          = getTable();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                nameField.setText("BookToDelete");
                priceField.setText("14.99");
                addBtn.doClick();
            }
        });

        int afterAdd = app.getBookstoreSystem().bookList.size();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                table.setRowSelectionInterval(0, 0);
                deleteBtn.doClick();
            }
        });

        assertEquals(afterAdd - 1, app.getBookstoreSystem().bookList.size());
    }

    //deleting a selected book removes its row from the table.
    @Test
    public void testDeleteSelectedBookRemovesRowFromTable() throws Exception {
        final JTextField nameField  = getField("bookName");
        final JTextField priceField = getField("bookPrice");
        final JButton addBtn        = findButton(ownerBooksScreen, "Add");
        final JButton deleteBtn     = findButton(ownerBooksScreen, "Delete");
        final JTable table          = getTable();
        final DefaultTableModel model = getTableModel();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                nameField.setText("TableRowBook");
                priceField.setText("7.99");
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



    //clicking the back button navigates to the OwnerStartScreen.
    @Test
    public void testBackNavigatesToOwnerStartScreen() throws Exception {
        final JButton backBtn = findButton(ownerBooksScreen, "Back");
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                backBtn.doClick();
            }
        });
        assertTrue(app.getContentPane() instanceof OwnerStartScreen);
    }
}