/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalProject;

import java.awt.*;
import java.util.HashSet;
import javax.swing.*;

/**
 *
 * @author Basim
 */
public class LoginScreen extends JPanel {
    public LoginScreen(BookstoreApp app) {
        setLayout(new BorderLayout());

        // Title at top
        JLabel title = new JLabel("Welcome to the Bookstore App", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Form panel in center
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // spacing
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username label
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        // Username field
        gbc.gridx = 1;
        JTextField usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);

        // Password label
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        // Password field
        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Login button at bottom, centered
        JButton loginButton = new JButton("Login");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Optional: button action
        loginButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            
            
             // Checking for username and password
            
            if (user.equals("admin") && pass.equals("admin")) {
                
                // Let the user enter the admin terminal.
                // Go to OWNER START SCREEN
                app.setState(new OwnerStartState(app));
            }
            
            else {
                boolean foundUser = false;
                
                for (Customer c: app.getBookstoreSystem().getCustomerList()) {
                    if (user.equals(c.getUsername()) && pass.equals(c.getPassword())) {
                        foundUser = true;
                    }
                }
                
                if (foundUser) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    // app.setState(new CustomerStartState(app));


                }
                else {
                    JOptionPane.showMessageDialog(this, "Login unsuccessful. Try again.");
                }
            }
        });
    }
}
   