/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalProject;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 * @author Sullay
 */
public class CustomerStartScreen extends JPanel {

    private final BookStoreApp app;             //main app (used for navigation)
    private final Customer customer;            //current logged-in customer
    private final BookstoreSystem system;       // shared data (books + customers)
    private final DefaultTableModel tableModel; //table model for book list

    // build the screen
    public CustomerStartScreen(final BookStoreApp app, final Customer customer) {
        this.app = app;
        this.customer = customer;
        this.system = app.getBookstoreSystem();

        // vertical layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //welcome text
        JLabel welcomeLabel = new JLabel(buildWelcomeText(), SwingConstants.CENTER);
        welcomeLabel.setAlignmentX(CENTER_ALIGNMENT);

        //book text
        tableModel = new DefaultTableModel(new String[]{"Book Name", "Book Price", "Select"}, 0) {

            //show checkbox in column 2
            @Override
            public Class<?> getColumnClass(int col) {
               if (col == 2) {
                   return Boolean.class;
               } 
               else {
                   return String.class;
               }
           }
            //only checkbox column is editable
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 2;
            }
        };
        
        //build the table
        JTable table = new JTable(tableModel);
        loadCatalog();
        JScrollPane scrollPane = new JScrollPane(table);

        //build buttons and their layout
        JButton buyBtn = new JButton("Buy");
        JButton redeemBtn = new JButton("Redeem Points and Buy");
        JButton logoutBtn = new JButton("Logout");
        JPanel btnRow = new JPanel();
        btnRow.add(buyBtn);
        btnRow.add(redeemBtn);
        btnRow.add(logoutBtn);
        add(welcomeLabel);
        add(scrollPane);
        add(btnRow);

        //button actions
        buyBtn.addActionListener(e -> purchaseBooks(false));
        redeemBtn.addActionListener(e -> purchaseBooks(true));
        logoutBtn.addActionListener(e -> app.switchTo(new LoginScreen(app)));
    }

    //build welcome messages
    private String buildWelcomeText() {
        String status = (customer.getPoints() >= 1000) ? "Gold" : "Silver";

        return String.format(
            "<html>Welcome <b>%s</b>. You have <b>%d</b> points. Status: <b>%s</b></html>",
            customer.getUsername(), customer.getPoints(), status
        );
    }

    //load books into table
    private void loadCatalog() {
        tableModel.setRowCount(0);

        for (Book b : system.bookList) {
            tableModel.addRow(new Object[]{
                b.getName(),
                String.format("%.2f", b.getPrice()),
                false
            });
        }
    }

    //get selected books
    private ArrayList<Book> getSelectedBooks() {
        ArrayList<Book> selected = new ArrayList<>();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (Boolean.TRUE.equals(tableModel.getValueAt(i, 2))) {
                selected.add(system.bookList.get(i));
            }
        }

        return selected;
    }

    /*
     * processes selected books: 
     * applies optional points redemption
     * updates customer points and buys the selected books
     *shows the purchase receipt
    */
    private void purchaseBooks(boolean redeem) {
        ArrayList<Book> selected = getSelectedBooks();

        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one book.");
            return;
        }

        double totalCost = new BookCart(selected).calculateTotalCost();

        if (redeem) {
            int points = customer.getPoints();
            double discount = Math.min(totalCost, points / 100.0);

            totalCost -= discount;

            int used = (int)(discount * 100);
            int earned = (int)(totalCost * 10);

            customer.setPoints(points - used + earned);

        } else {
            int earned = (int)(totalCost * 10);
            customer.setPoints(customer.getPoints() + earned);
        }

        // go to receipt screen
        app.switchTo(new CustomerCostScreen(app, customer, totalCost));
    }
}