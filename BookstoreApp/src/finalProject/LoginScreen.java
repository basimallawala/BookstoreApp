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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Sullay
 */
public class LoginScreen extends JPanel {

    //reference to main app (used to switch screens)
    private final BookStoreApp app;

    //input fields
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    //constructor builds the login screen UI
    public LoginScreen(BookStoreApp app) {
        this.app = app;

        //stack components vertically
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //draw title
        JLabel title = new JLabel("Welcome to the BookStore App", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setAlignmentX(CENTER_ALIGNMENT);

        //draw username box
        JPanel userRow = new JPanel();
        userRow.setLayout(new BoxLayout(userRow, BoxLayout.X_AXIS));
        JLabel userLabel = new JLabel("Username: ");
        userLabel.setPreferredSize(new Dimension(80, 26));
        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(200, 26));
        usernameField.setPreferredSize(new Dimension(200, 26));
        userRow.add(userLabel);
        userRow.add(usernameField);
        userRow.setMaximumSize(new Dimension(320, 36));
        userRow.setAlignmentX(CENTER_ALIGNMENT);

        //draw password box
        JPanel passRow = new JPanel();
        passRow.setLayout(new BoxLayout(passRow, BoxLayout.X_AXIS));
        JLabel passLabel = new JLabel("Password: ");
        passLabel.setPreferredSize(new Dimension(80, 26));
        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(200, 26));
        passwordField.setPreferredSize(new Dimension(200, 26));
        passRow.add(passLabel);
        passRow.add(passwordField);
        passRow.setMaximumSize(new Dimension(320, 36));
        passRow.setAlignmentX(CENTER_ALIGNMENT);

        //draw login button
        JButton loginBtn = new JButton("Login");
        loginBtn.setAlignmentX(CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(100, 30));

        //add components to screen
        add(Box.createVerticalGlue());  
        add(title);
        add(Box.createVerticalStrut(20));
        add(userRow);
        add(Box.createVerticalStrut(10));
        add(passRow);
        add(Box.createVerticalStrut(16));
        add(loginBtn);
        add(Box.createVerticalGlue());

        //check the login credentials when login button is clicked
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkLogin();
            }
        });

        //check the login credentials when enter is pressed on the password
        passwordField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkLogin();
            }
        });
    }

    //check entered username and password
    private void checkLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        //check admin login
        if (username.equals("admin") && password.equals("admin")) {
            app.switchTo(new OwnerStartScreen(app));
            return;
        }

        //check all customers
        for (int i = 0; i < app.getBookstoreSystem().getCustomerList().size(); i++) {
            Customer c = app.getBookstoreSystem().getCustomerList().get(i);

            if (c.getUsername().equals(username) && c.getPassword().equals(password)) {
                app.switchTo(new CustomerStartScreen(app, c));
                return;
            }
        }

        //if login fails, show error and clear password
        JOptionPane.showMessageDialog(
                this,
                "Invalid username or password.",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE
        );

        passwordField.setText("");
    }
}