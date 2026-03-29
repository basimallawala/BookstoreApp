/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalProject;

/**
 *
 * @author Basim
 */
public class OwnerBooksState implements AppState {
    
    private BookstoreApp bookstoreApp;

    public OwnerBooksState(BookstoreApp bookstoreApp) {
        this.bookstoreApp = bookstoreApp;
    }

    public void enter() {
        bookstoreApp.show("OWNERBOOKS");
    }

    public void exit() {

  }
    
}
