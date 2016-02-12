package sample.Client.ClientClass;

/** @author Giulio Melloni
 * Questa classe è il controller che gestisce la prima interfaccia visibile: quella in cui si possono verificare le proprie credenziali oppure si può creare un nuovo utente.
 * Qui avviene quindi la comunicazione tra main e controller grazie alle modifiche apportate all'interfaccia dal client, come la compilazione dei campi USERNAME e PASSWORD e come il click su Login.
 */

import static sample.Utilities.Class.ConstantCodes.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import sample.Utilities.Class.Utilities;
import java.net.URL;
import java.util.*;

public class ClientLoginController implements Initializable {//serve per avere l'implementazione del metodo initialize

    private boolean loginNewUser;
    private boolean fromServer = false;
    private ClientMain main;
    private Utilities utilities;
    @FXML   VBox loginVBox;
    @FXML   Text labelLoginTitle;
    @FXML   JFXTextField textWithUsername;
    @FXML   JFXPasswordField textWithPassword;
    @FXML   JFXPasswordField textWithPasswordConfirm;
    @FXML   JFXButton buttonUserScreen;
    @FXML   JFXButton buttonToContinue;

    /** Metodo che inizializza a false/true le cose che non si dovranno o si dovranno vedere.
     * @param location null se la location non è nota (come in questo caso).
     * @param resources null se il root object non è localizzato (come in questo caso).
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginVBox.setVisible(false);
        labelLoginTitle.setFocusTraversable(true);//focus sul titolo così non sono già dentro un campo nello scrivere
        loginScreenShow();
        utilities = new Utilities();
        loginNewUser = true;
        buttonToContinue.setText("LOGIN");
        buttonToContinue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                confirmLoginScreen();
            }
        });
    }

    /** Metodo che serve per collegare main e controller passandogli l'istanza.
     * @param main è l'istanza del ClientMain
     */
    public void setMain(ClientMain main) {
        this.main = main;
    }

    /** Metodo che permette di proseguire e vedere la schermata di login aspettando tre secondi e il tempo della transizione.
     */
    public void loginScreenShow(){
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        utilities.fadeTransitionEffect(loginVBox, 0, 1, 3000);
                        loginVBox.setVisible(true);
                        textWithPasswordConfirm.setVisible(false);
                    }
                },
                3000
        );
    }

    /** Metodo usato per accreditarsi nel gioco, in cui si verifica che entrambi i campi siano compilati.
     */
    public void confirmLoginScreen(){
        utilities.playSomeSound(BUTTONCLICK_SOUND);
        if (textWithUsername.getText().isEmpty() || textWithPassword.getText().isEmpty()){ //se ho lasciato qualche campo vuoto mi fermo
            main.notification("Dati non corretti");
        } else {
            main.loginOrNewUser(textWithUsername.getText(), textWithPassword.getText(), true);//chiamata di funzione passando username, password
        }
    }

    /** Metodo utilizzato per creare un nuovo utente, in cui si verifica che i campi siano compilati e le password corrispondenti.
     *
     */
    public void newUserLogin (){
        utilities.playSomeSound(BUTTONCLICK_SOUND);
        if (textWithUsername.getText().isEmpty() || textWithPassword.getText().isEmpty()) { //se ho lasciato qualche campo vuoto mi fermo
            main.notification("Dati non corretti");
        } else {
            if (textWithPassword.getText().equals(textWithPasswordConfirm.getText())){
                main.loginOrNewUser(textWithUsername.getText(), textWithPassword.getText(), false);//chiamo funzione passando parametri username e password
            } else {
                main.notification("Password non corrispondenti, reinserire password");
            }
        }
    }

    /** Metodo per settare la schermata di login nel caso in cui sia NEW USER o LOGIN a seconda di quando si clicca il button HAI GIA' UN ACCOUNT o SEI UN NUOVO UTENTE.
     */
    public void setNewUserScreen(){
        if (!fromServer) {
            utilities.playSomeSound(BUTTONCLICK_SOUND);
        }
        if(loginNewUser==true){
            labelLoginTitle.setText("NEW USER");
            buttonUserScreen.setText("HAI GIA' UN ACCOUNT?");
            buttonToContinue.setText("SIGN UP");
            buttonToContinue.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    newUserLogin();
                }
            });
            utilities.fadeTransitionEffect(textWithPasswordConfirm, 0, 1, 1000);
            textWithPasswordConfirm.setVisible(true);
            loginNewUser = false;
        }
        else{
            labelLoginTitle.setText("LOGIN");
            buttonUserScreen.setText("SEI UN NUOVO UTENTE?");
            buttonToContinue.setText("LOGIN");
            buttonToContinue.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    confirmLoginScreen();
                }
            });
            utilities.fadeTransitionEffect(textWithPasswordConfirm, 1, 0, 1000);
            loginNewUser = true;
        }
        textWithPassword.setText(""); //quando cambio schermata azzero i tre campi
        textWithUsername.setText("");
        textWithPasswordConfirm.setText("");
        fromServer = false;
    }

    /** setter.
     * @param loginNewUser è il boolean che se è true è Login, false è SignUp
     */
    public void setLoginNewUser(boolean loginNewUser) {
        this.loginNewUser = loginNewUser;
    }

    /** setter.
     * @param fromServer setto se è dal server l'azione.
     */
    public void setFromServer(boolean fromServer) {
        this.fromServer = fromServer;
    }

}