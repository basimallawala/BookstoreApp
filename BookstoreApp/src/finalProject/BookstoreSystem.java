package finalProject;

import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
        
        // Reset the arrays.
        bookList.clear();
        customerList.clear();
        
        File customersFile = new File("src\\finalProject\\customers.txt");
        File booksFile = new File("src\\finalProject\\books.txt");
        
        


        
        try {
            
            if (!booksFile.exists()) { booksFile.createNewFile(); }
            if (!customersFile.exists()) { customersFile.createNewFile(); }
            
            // READING CUSTOMER DATA
            FileReader customerReader = new FileReader("src\\finalProject\\customers.txt");
            Scanner scan = new Scanner(customerReader);
            while(scan.hasNextLine()) {
                // Get the next line
                String line = scan.nextLine();
                
                String[] customerData = line.split(" ");
                
                // Read the customer data line into the array below and split into values
                String username = customerData[0];
                String password = customerData[1];
                int points = Integer.parseInt(customerData[2]);
                String status = customerData[3];
                
                
                // If the read points are AT or above 1000, make the customer gold
                if (points >= 1000) {
                    customerList.add(new Customer(username, password, points, new GoldTier()));
                }
                
                // Otherwise, make them silver.
                else {
                    customerList.add(new Customer(username, password, points, new SilverTier()));
                }
            }
            
            scan.close();
            
            // make another scanner just for the books (i know, not the best..)
            FileReader bookReader = new FileReader("src\\finalProject\\books.txt");
            Scanner bookScan = new Scanner(bookReader);
            
            // Read through the books. much easier
            while(bookScan.hasNextLine()) {
                String line = scan.nextLine();
                
                String[] bookData = line.split(" ");
                
                // Read the book data into an array of strings
                String bookName = bookData[0];
                double bookPrice = Double.parseDouble(bookData[1]);
                
                // Load each book into the book array
                bookList.add(new Book(bookName, bookPrice));

            }
            
            bookScan.close();
            
            
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
    }
    
    // Push the bookstore data to a text file.
    public void saveData() {
        try {
            // Write the customer data
            FileWriter customerWriter = new FileWriter("src\\finalProject\\customers.txt", false);
            
            for (Customer c: customerList) {
                // FORMAT: Username, Password, Points, Status
                String line = "%s %s %i %s\n";
                String status;
                
                if (c.getPoints() >= 1000) { status = "G"; }
                else { status = "S"; }
                
                customerWriter.write(line.format(line, c.getUsername(), c.getPassword(), c.getPoints(), status));

            }
            
            customerWriter.close(); // Close up the customer data
            
            // book writing FileWriter
            FileWriter bookWriter = new FileWriter("src\\finalProject\\books.txt", false);
            
            // Loop through the booklist.
            for (Book b: bookList) {
                // FORMAT: book name, book price
                String line = "%s %d\n";
                bookWriter.write(line.format(line, b.getName(), b.getPrice()));
            }
            
            bookWriter.close();
        
        // If none of this works, just print it out
        } catch (IOException e) {
            System.out.println("Cannot write to specified file.");
            e.printStackTrace();
        }
        
    }
    
    
}
