/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalProject;

/**
 *
 * @author Basim
 */
public class OwnerStartState implements AppState {

    private BookstoreApp bookstoreApp;

    public OwnerStartState(BookstoreApp bookstoreApp) {
        this.bookstoreApp = bookstoreApp;
    }

    public void enter() {
        bookstoreApp.show("OWNERSTART");
    }

    public void exit() {
        bookstoreApp.getBookstoreSystem().saveData();
    }

}
