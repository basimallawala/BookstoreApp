/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalProject;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Sullay
 */
public class OwnerStartScreen extends JPanel {

    // buttons for navigating to owner's books or customer screen, as well as login screen
    private JButton booksButton;
    private JButton customersButton;
    private JButton logoutButton;

    //build dashbord for owner
    public OwnerStartScreen(final BookStoreApp app) {

        //set layout and background
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 250));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(12, 20, 12, 20);
        g.fill   = GridBagConstraints.HORIZONTAL;


        //create and add the title label
        JLabel title = new JLabel("Owner Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.gridx = 0;
        g.gridy = 0;
        add(title, g);

        //create buttons
        Dimension btnSize = new Dimension(160, 38);
        booksButton     = makeButton("Books",     btnSize);
        customersButton = makeButton("Customers", btnSize);
        logoutButton    = makeButton("Logout",    btnSize);

        //add buttons        
        g.gridy = 1; add(booksButton,     g);
        g.gridy = 2; add(customersButton, g);
        g.gridy = 3; add(logoutButton,    g);


        //go to books screen on clicking Books button
        booksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.switchTo(new OwnerBooksScreen(app));
            }
        });

        //go to customers screen on clicking Customers button
        customersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.switchTo(new OwnerCustomersScreen(app));
            }
        });

        //return to login screen on clicking logout button
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.switchTo(new LoginScreen(app));
            }
        });
    }

    
    //method makes a button with agiven text and size 
    private JButton makeButton(String text, Dimension size) {
        JButton b = new JButton(text);
        b.setPreferredSize(size);
        return b;
    }
}