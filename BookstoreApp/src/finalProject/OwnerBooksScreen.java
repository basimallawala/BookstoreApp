/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalProject;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Basim
 */
public class OwnerBooksScreen extends JPanel {
    public OwnerBooksScreen(BookstoreApp bookstoreApp) {
      
        // Column names
        String[] columns = {"Book Name", "Book Price ($CAD)"};

     
        
      

        // Table model
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        for (Book b: bookstoreApp.getBookstoreSystem().getBookList()) {
            model.addRow(new Object[]{b.getName(), b.getPrice()});
        }
        
        

        // JTable
        JTable table = new JTable(model);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        
        add(scrollPane);

    }
    
}
