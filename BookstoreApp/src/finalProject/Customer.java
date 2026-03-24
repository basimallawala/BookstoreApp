/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalProject;

/**
 *
 * @author Basim
 */
public class Customer extends User {
    
    private int points;
    private CustomerStatus status;

    public Customer(String username, String password, int points, CustomerStatus status) {
        super(username, password);
        this.points = points;
        this.status = status;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    
    public void buyBook(BookCart cart) {
        
    }
    
    public void buyBookAndRedeem(BookCart cart) {
        
    }
   
    
}
