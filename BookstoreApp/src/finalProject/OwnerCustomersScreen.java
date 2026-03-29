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
 * @author Sullay
 */
public class OwnerCustomersScreen extends JPanel {

    //shared data model for customers
    private final BookstoreSystem   system;

    //table and table model display the customer list
    private final DefaultTableModel tableModel;
    private final JTable            customersTable;

    //text fields for username and password input
    private final JTextField        usernameField;
    private final JTextField        passwordField;

    //build customer management screen
    public OwnerCustomersScreen(final BookStoreApp app) {
        this.system = app.getBookstoreSystem();

        //set layout and border
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //heading
        JLabel heading = new JLabel("Manage Customers", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 15));
        heading.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        add(heading, BorderLayout.NORTH);

        //customer table with 3 columns: username, password, points; table is read-only
        tableModel = new DefaultTableModel(new String[]{"Username", "Password", "Points"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        customersTable = new JTable(tableModel);
        customersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customersTable.getTableHeader().setReorderingAllowed(false);
        loadCustomersTable();
        add(new JScrollPane(customersTable), BorderLayout.CENTER);

        //create middle row with username, password, Add button
        JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        middlePanel.add(new JLabel("Username:"));
        usernameField = new JTextField(12);
        middlePanel.add(usernameField);
        middlePanel.add(new JLabel("Password:"));
        passwordField = new JTextField(12);
        middlePanel.add(passwordField);
        JButton addBtn = new JButton("Add");
        middlePanel.add(addBtn);

        //create bottom row with delete and back buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        JButton deleteBtn = new JButton("Delete");
        JButton backBtn   = new JButton("Back");
        bottomPanel.add(deleteBtn);
        bottomPanel.add(backBtn);

        //wrap middle and bottom rows and add to SOUTH
        JPanel southWrapper = new JPanel(new BorderLayout());
        southWrapper.add(middlePanel, BorderLayout.NORTH);
        southWrapper.add(bottomPanel, BorderLayout.SOUTH);
        add(southWrapper, BorderLayout.SOUTH);

        //add button adds new customer
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });

        //delete button removes selected customer
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCustomer();
            }
        });

        //back button returns to owner dashboard
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.switchTo(new OwnerStartScreen(app));
            }
        });
    }

    //clear and reload table from system
    private void loadCustomersTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < system.getCustomerList().size(); i++) {
            Customer c = system.getCustomerList().get(i);
            tableModel.addRow(new Object[]{c.getUsername(), c.getPassword(), c.getPoints()});
        }
    }

    //validate inputs and register new customer
    private void addCustomer() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both Username and Password.");
            return;
        }

        for (int i = 0; i < system.getCustomerList().size(); i++) {
            Customer c = system.getCustomerList().get(i);
            if (c.getUsername().equalsIgnoreCase(username)) {
                JOptionPane.showMessageDialog(this, "A customer with that username already exists.");
                return;
            }
        }

        if (username.equals("admin")) {
            JOptionPane.showMessageDialog(this, "Username 'admin' is reserved.");
            return;
        }

        //all checks passed; add customer with 0 points and Silver state
        Customer customer = new Customer(username, password, 0, new SilverTier());
        system.addCustomer(customer);
        tableModel.addRow(new Object[]{username, password, 0});
        usernameField.setText("");
        passwordField.setText("");
    }

    //remove selected customer from table and system
    private void deleteCustomer() {
        int row = customersTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.");
            return;
        }

        String username = (String) tableModel.getValueAt(row, 0);

        for (int i = 0; i < system.getCustomerList().size(); i++) {
            Customer c = system.getCustomerList().get(i);
            if (c.getUsername().equals(username)) {
                system.deleteCustomer(c);
                break;
            }
        }

        tableModel.removeRow(row);
    }
}