/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalProject;

import java.util.ArrayList;

/**
 *
 * @author Basim
 */
public class BookCart {
    
    ArrayList<Book> bookList;

    public BookCart(ArrayList<Book> bookList) {
        this.bookList = bookList;
    }

    public void addBook(Book book) {
        bookList.add(book);
    }
    
    public void removeBook(Book book) {
        bookList.remove(book);
    }
    
    public double calculateTotalCost() {
        double totalCost = 0;
        
        for (Book b: bookList) { totalCost += b.getPrice(); }
        
        return totalCost;
    }
    
}
