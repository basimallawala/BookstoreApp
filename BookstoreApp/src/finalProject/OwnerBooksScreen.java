/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalProject;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


/**
 *
 * @author Basim
 */
public class OwnerBooksScreen extends JPanel {
    public OwnerBooksScreen(BookstoreApp bookstoreApp) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // Column names
        // Table
        String[] cols = {"Book Name", "Book Price"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Loop through the books in our system and add them.
        for (Book b: bookstoreApp.getBookstoreSystem().getBookList()) {
            model.addRow(new Object[]{b.getName(), b.getPrice()});
        }

        add(scrollPane);

        // Row panel (text fields + button)
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField bookNameField = new JTextField();
        JTextField bookPriceField = new JTextField();
        JButton addButton = new JButton("Add");

        bookNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        bookPriceField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        row.add(bookNameField);
        row.add(Box.createRigidArea(new Dimension(5, 0)));
        row.add(bookPriceField);
        row.add(Box.createRigidArea(new Dimension(5, 0)));
        row.add(addButton);

        add(row);

        // Button action
        addButton.addActionListener(e -> {
            
            
            
            //  Check if the book name field is blank.
            if (bookNameField.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Book name must not be blank!");
            }
            
            // sketchy ahh regex code. check if the book name has any letters
            else if (bookPriceField.getText().matches(".*[a-zA-Z].*")) {
                JOptionPane.showMessageDialog(this, "Book price must not have any letters!");   
            }
            
            // check if the book price is blank
            else if (bookPriceField.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Book price must not be blank!");   
            }
            
            // If the book name and price is okay, send it off.
            else {
                model.addRow(new Object[]{
                    bookNameField.getText(),
                    bookPriceField.getText()
                });
                
                // BUG: the books wont save to persistent memory (and the customers too, probably)
                bookstoreApp.getBookstoreSystem().getBookList().add(new Book(bookNameField.getText(), Double.parseDouble(bookPriceField.getText())));
             
                bookNameField.setText("");
                bookPriceField.setText("");
            }
            
            
        });
        


    }
    
}
