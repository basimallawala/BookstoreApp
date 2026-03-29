/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalProject;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Sullay
 */

public class CustomerCostScreen extends JPanel {

    private final double totalCost;      // total amount charged for this purchase
    private final JLabel costLabel;      // displays "Total Cost: $XX.XX"
    private final JLabel pointsLabel;    // displays "Points: P, Status: S"
    private final JButton logoutButton;  // button to return to login screen

    //builds reciept screen for completed purchase.
    public CustomerCostScreen(final BookStoreApp app, final Customer customer, double totalCost) {
        this.totalCost = totalCost;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //determine status string based on updated points
        String statusStr;
        if (customer.getPoints() >= 1000) {
            statusStr = "Gold";
        } else {
            statusStr = "Silver";
        }

        //show total cost
        costLabel = new JLabel("Total Cost: $" + String.format("%.2f", this.totalCost), SwingConstants.CENTER);
        costLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        costLabel.setAlignmentX(CENTER_ALIGNMENT);

        //show points and status
        pointsLabel = new JLabel("Points: " + customer.getPoints() + ", Status: " + statusStr, SwingConstants.CENTER);
        pointsLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        pointsLabel.setAlignmentX(CENTER_ALIGNMENT);

        //logout button
        logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(CENTER_ALIGNMENT);
        logoutButton.setMaximumSize(new Dimension(120, 32));

        //assemble the button and labels vertically
        add(Box.createVerticalGlue());
        add(costLabel);
        add(Box.createVerticalStrut(24));
        add(pointsLabel);
        add(Box.createVerticalStrut(24));
        add(logoutButton);
        add(Box.createVerticalGlue());

        //return to login screen when clicking logout button
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.switchTo(new LoginScreen(app));
            }
        });
    }
}