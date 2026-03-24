package finalProject;

import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Basim
 */
public class BookstoreSystem {
    ArrayList<Book> bookList;
    ArrayList<Customer> customerList;

    public BookstoreSystem(ArrayList<Book> bookList, ArrayList<Customer> customerList) {
        this.bookList = bookList;
        this.customerList = customerList;
    }

    public void addBook(Book book) {
        bookList.add(book);
    }
    
    public void removeBook(Book book) {
        bookList.remove(book);
    }
    
    public void addCustomer(Customer customer) {
        customerList.add(customer);
    }
    
    public ArrayList<Customer> getCustomers() {
        return customerList;
    }
    
    public void deleteCustomer(Customer customer) {
        customerList.remove(customer);
    }
    
    // Load the bookstore data from the machine.
    public void loadData() {
        
    }
    
    // Push the bookstore data to a text file.
    public void saveData() {
        
    }
    
    
}
