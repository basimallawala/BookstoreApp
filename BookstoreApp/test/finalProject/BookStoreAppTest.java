package finalProject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import static org.junit.Assert.*;


/**
 * @author Sullay
 */
public class BookStoreAppTest {

    private BookStoreApp app;

    @Before
    public void setUp() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                app = new BookStoreApp();
                app.setVisible(false); // suppress the window during test runs
                
                //clear any data loaded from disk
                app.getBookstoreSystem().bookList.clear();
                app.getBookstoreSystem().getCustomerList().clear();
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

    // getBookstoreSystem() must return a non-null instance after construction
    @Test
    public void testGetBookstoreSystemIsNotNull() {
        assertNotNull(app.getBookstoreSystem());
    }

    //testing JFrame
    @Test
    public void testWindowTitle() {
        assertEquals("Bookstore App", app.getTitle());
    }

    @Test
    public void testWindowWidth() {
        assertEquals(600, app.getWidth());
    }

    @Test
    public void testWindowHeight() {
        assertEquals(480, app.getHeight());
    }



    //the very first screen shown after construction must be LoginScreen
    @Test
    public void testInitialContentPaneIsLoginScreen() {
        assertTrue(app.getContentPane() instanceof LoginScreen);
    }

    //switchTo() must make the supplied panel the current content pane
    @Test
    public void testSwitchToReplacesContentPane() throws Exception {
        final JPanel replacement = new JPanel();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                app.switchTo(replacement);
            }
        });
        assertSame(replacement, app.getContentPane());
    }

    //calling switchTo() twice must leave only the second panel active
    @Test
    public void testSwitchToCalledTwiceKeepsLastPanel() throws Exception {
        final JPanel first = new JPanel();
        final JPanel second = new JPanel();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                app.switchTo(first);
                app.switchTo(second);
            }
        });
        assertSame(second, app.getContentPane());
    }


    /*
     * when the data files do not exist, loadData() silently catches IOException
     * and leaves customerList and bookList empty. 
     */
    @Test
    public void testCustomerListStartsEmpty() {
        assertEquals(0, app.getBookstoreSystem().getCustomerList().size());
    }

    @Test
    public void testBookListStartsEmpty() {
        assertEquals(0, app.getBookstoreSystem().bookList.size());
    }

    //ensure customers are retrievable
    @Test
    public void testAddedCustomerIsRetrievable() {
        Customer c = new Customer("alice", "pass", 0, new SilverTier());
        app.getBookstoreSystem().addCustomer(c);
        assertEquals(1, app.getBookstoreSystem().getCustomerList().size());
        assertEquals("alice", app.getBookstoreSystem().getCustomerList().get(0).getUsername());
    }

    //ensure books are retrievable
    @Test
    public void testAddedBookIsRetrievable() {
        Book b = new Book("Dune", 19.99);
        app.getBookstoreSystem().addBook(b);
        assertEquals(1, app.getBookstoreSystem().bookList.size());
        assertEquals("Dune", app.getBookstoreSystem().bookList.get(0).getName());
    }
}
