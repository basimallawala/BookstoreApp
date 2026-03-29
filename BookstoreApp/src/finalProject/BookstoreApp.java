/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package finalProject;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 *
 * @author Sullay
*/
public class BookStoreApp extends JFrame {

    /*
    * the single BookstoreSystem instance shared across the entire app.
     * screens must go through getBookstoreSystem() to access data.
     */
    private BookstoreSystem bookstoreSystem;

    //constructs application window
    public BookStoreApp() {
        // Create the data model with empty lists; loadData() populates them
        // from books.txt and customers.txt if those files exist on disk.
        bookstoreSystem = new BookstoreSystem(new ArrayList<Book>(), new ArrayList<Customer>());
        bookstoreSystem.loadData();

        // create the GUI window
        setTitle("Bookstore App");
        setSize(600, 480);
       setLocationRelativeTo(null); // centres the window on the screen

        /*
         * DO_NOTHING_ON_CLOSE suppresses Java's default dispose-on-close
         * behaviour so we can save data first inside the WindowAdapter below
           instead of it automatically closing.
         */
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        /*
         * when the user clicks [x]:
         * -save data
        * -close window
         * -exit progra,
         */
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                bookstoreSystem.saveData(); 
                dispose();                  
                System.exit(0);            
            }
        });

        //show the login screen as the first panel
        switchTo(new LoginScreen(BookStoreApp.this));
        setVisible(true);
    }

    /**
     * replace the current screen with the screen that is being switched
     * and refreshes the UI
     */
    public void switchTo(JPanel panel) {
        setContentPane(panel); // swap the visible panel
        revalidate();          // re-run layout
        repaint();             // force a redraw
    }

    /**
     * @return the app's single BookstoreSystem instance.
     */
    public BookstoreSystem getBookstoreSystem() {
        return bookstoreSystem;
    }

    //run the app on Swing's UI thread 
   public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BookStoreApp(); // create and show the app window
            }
       });
    }
}