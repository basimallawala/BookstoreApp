/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalProject;

import javax.swing.*;

/**
 *
 * @author Basim
 */
public class OwnerStartScreen extends JPanel {
    public OwnerStartScreen(BookstoreApp bookstoreApp) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JButton goToBooks = new JButton("Go to Books");
        JButton goToCustomers = new JButton("Go to Customers");
        JButton logoutButton = new JButton("Logout");
        
        goToBooks.setAlignmentX((float) 0.5);
        goToCustomers.setAlignmentX((float) 0.5);
        logoutButton.setAlignmentX((float) 0.5);
            
        logoutButton.addActionListener(e -> bookstoreApp.setState(new LoginState(bookstoreApp)));
        goToBooks.addActionListener(e -> bookstoreApp.setState(new OwnerBooksState(bookstoreApp)));

        
        add(Box.createVerticalStrut(200));
        add(goToBooks);
        add(Box.createVerticalStrut(20));
        add(goToCustomers);
        add(Box.createVerticalStrut(20));
        add(logoutButton);

        
    }
    
    
    
}
