package sample.Utilities.Class;

/** @author Giulio Melloni
 *  Classe che mi serve quando devo creare un utente. In questo modo ho una classe unica e semplice con username e password settabili per ogni oggetto che creo.
 */

import java.io.Serializable;

public class User implements Serializable{

    String userUsername;
    String userPassword;

    /** Costruttore di User.
     * @param textWithUsername nome dell'utente.
     * @param textWithPassword password dell'utente.
     */
    public User(String textWithUsername, String textWithPassword) {
        this.userPassword = textWithPassword;
        this.userUsername = textWithUsername;
    }

    /** toString usato per la serializzazione.
     * @return serializzazione dell'oggetto.
     */
    @Override
    public String toString() {
        return "User{" +
                "userUsername='" + userUsername + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }

    /** getter.
     * @return password.
     */
    public String getUserPassword() {
        return userPassword;
    }

    /** getter
     * @return username.
     */
    public String getUserUsername() {
        return userUsername;
    }

}
