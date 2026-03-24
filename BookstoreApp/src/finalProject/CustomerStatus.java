/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package finalProject;

/**
 *
 * @author Basim
 */
public interface CustomerStatus {
    
    public void handleBuy(double totalCost, Customer customer);
    
    public String returnStatusString();
    
    public void handleRedeem(double totalCost, Customer customer);
    
    
}
