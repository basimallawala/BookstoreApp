package finalProject;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Basim
 */
public class OwnerCustomersState implements AppState {
    private BookstoreApp bookstoreApp;

    public OwnerCustomersState(BookstoreApp bookstoreApp) {
        this.bookstoreApp = bookstoreApp;
    }

    public void enter() {
        bookstoreApp.show("OWNERCUSTOMERS");
    }

    public void exit() {

    }
    
}
