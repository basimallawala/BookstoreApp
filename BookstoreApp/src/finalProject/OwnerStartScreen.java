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
        JButton goToBooks = new JButton("Go to Books");
        JButton goToCustomers = new JButton("Go to Customers");
        JButton logoutButton = new JButton("Logout");
        
        goToBooks.addActionListener(e -> bookstoreApp.setState(new LoginState(bookstoreApp)));
    }
    
    
    
}
