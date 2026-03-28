/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalProject;
import javax.swing.*;
import java.awt.*;


public class BookstoreApp {
    private CardLayout layout;
    private JPanel container;
    private AppState currentState;

    public BookstoreApp() {
        
        JFrame frame = new JFrame("Bookstore");
        layout = new CardLayout();
        container = new JPanel(layout);
        
        container.add(new OwnerStartScreen(this), "OWNERSTART");
        container.add(new LoginScreen(this), "LOGIN");
        
        frame.add(container);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        setState(new OwnerStartState(this));

    }

    public void setState(AppState newState) {
        if (currentState != null) currentState.exit();
        currentState = newState;
        currentState.enter();
    }

    public void show(String name) {
        layout.show(container, name);
    }

    public JPanel getContainer() {
        return container;
    }
    
    public static void main(String[] args) {
        new BookstoreApp();
    }
}