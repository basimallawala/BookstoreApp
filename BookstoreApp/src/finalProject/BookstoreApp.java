/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalProject;
import javax.swing.*;
import java.awt.*;

public class BookstoreApp {
    JFrame frame;
    JPanel container;
    CardLayout layout;

    public BookstoreApp() {
        frame = new JFrame("Bookstore");
        layout = new CardLayout();
        container = new JPanel(layout);

        // screens
        JPanel home = createHomePanel();
        JPanel cart = createCartPanel();
        JPanel login = createLoginPanel();

        container.add(home, "HOME");
        container.add(cart, "CART");
        container.add(login, "LOGIN");

        frame.add(container);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel();
        JButton goToCart = new JButton("Cart");

        goToCart.addActionListener(e -> layout.show(container, "CART"));

        panel.add(goToCart);
        return panel;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel();
        JButton back = new JButton("Login");

        back.addActionListener(e -> layout.show(container, "LOGIN"));

        panel.add(back);
        return panel;
    
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        JLabel loginLabel = new JLabel("Welcome to the Bookstore");
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> layout.show(container, "HOME"));
        
        panel.add(loginLabel);
        panel.add(homeButton);
        
        return panel;
    }

    public static void main(String[] args) {
        new BookstoreApp();
    }
}