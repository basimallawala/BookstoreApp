/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalProject;

/**
 *
 * @author Basim
 */
public class LoginState implements AppState {
    
    private BookstoreApp bookstoreApp;

    public LoginState(BookstoreApp bookstoreApp) {
        this.bookstoreApp = bookstoreApp;
    }

    public void enter() {
        bookstoreApp.show("LOGIN");
    }

    public void exit() {

    }
    
}
