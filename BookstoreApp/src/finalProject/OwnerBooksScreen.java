/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalProject;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Sullay
 */
public class OwnerBooksScreen extends JPanel {

    //shared data model for books
    private final BookstoreSystem   bookstoreSystem;

    //yable and table model display the catalog
    private final JTable            booksTable;
    private final DefaultTableModel booksTableModel;

    //text fields for book name and price
    private final JTextField        bookName;
    private final JTextField        bookPrice;

    //buttons for actions: add, delete, back
    private final JButton           addBookButton;
    private final JButton           deleteBookButton;
    private final JButton           backButton;

   //build book management screen
    public OwnerBooksScreen(final BookStoreApp app) {
        this.bookstoreSystem = app.getBookstoreSystem();

        //set layout and border
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        //create and add heading label
        JLabel heading = new JLabel("Manage Books", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 15));
        heading.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        add(heading, BorderLayout.NORTH);

 
        //create table, make it read only,and populate from bookstoreSystem
        booksTableModel = new DefaultTableModel(new String[]{"Book Name", "Book Price"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        booksTable = new JTable(booksTableModel);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        booksTable.getTableHeader().setReorderingAllowed(false);
        loadBooksTable();
        add(new JScrollPane(booksTable), BorderLayout.CENTER);


        //create rows for the table showing book names, their associated prices,and the add button
        JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        middlePanel.add(new JLabel("Name:"));
        bookName = new JTextField(14);
        middlePanel.add(bookName);
        middlePanel.add(new JLabel("Price:"));
        bookPrice = new JTextField(8);
        middlePanel.add(bookPrice);
        addBookButton = new JButton("Add");
        middlePanel.add(addBookButton);

        //show delete and back buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        deleteBookButton = new JButton("Delete");
        backButton       = new JButton("Back");
        bottomPanel.add(deleteBookButton);
        bottomPanel.add(backButton);

        //display both rows using wrappers
        JPanel southWrapper = new JPanel(new BorderLayout());
        southWrapper.add(middlePanel, BorderLayout.NORTH);
        southWrapper.add(bottomPanel, BorderLayout.SOUTH);
        add(southWrapper, BorderLayout.SOUTH);

        //add selected book when clicking add book button
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        //remove selected book when clicking delete book button
        deleteBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });

        //return to owner dashboard when back button is clicked
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                app.switchTo(new OwnerStartScreen(app));
            }
        });
    }

    //clear and reload table from bookstoreSystem
    private void loadBooksTable() {
        booksTableModel.setRowCount(0);
        for (int i = 0; i < bookstoreSystem.bookList.size(); i++) {
            Book b = bookstoreSystem.bookList.get(i);
            booksTableModel.addRow(new Object[]{b.getName(), b.getPrice()});
        }
    }

    //validate inputs and add new book to system and table
    private void addBook() {
        String name      = bookName.getText().trim();
        String priceText = bookPrice.getText().trim();

        if (name.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both Name and Price.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for Price.");
            return;
        }

        if (price < 0) {
            JOptionPane.showMessageDialog(this, "Price cannot be negative.");
            return;
        }

        for (int i = 0; i < bookstoreSystem.bookList.size(); i++) {
            Book b = bookstoreSystem.bookList.get(i);
            if (b.getName().equalsIgnoreCase(name)) {
                JOptionPane.showMessageDialog(this, "A book with that name already exists.");
                return;
            }
        }

        Book newBook = new Book(name, price);
        bookstoreSystem.addBook(newBook);
        booksTableModel.addRow(new Object[]{name, price});
        bookName.setText("");
        bookPrice.setText("");
    }

    //remove selected book from system and table
    private void deleteBook() {
        int row = booksTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
            return;
        }

        String name = (String) booksTableModel.getValueAt(row, 0);

        for (int i = 0; i < bookstoreSystem.bookList.size(); i++) {
            Book b = bookstoreSystem.bookList.get(i);
            if (b.getName().equals(name)) {
                bookstoreSystem.removeBook(b);
                break;
            }
        }

        booksTableModel.removeRow(row);
    }
}