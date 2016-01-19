package sample.Client.ClientClass;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sample.Utilities.Class.Utilities;
import java.net.URL;
import java.util.*;

public class ClientLoginController implements Initializable {//serve per avere l'implementazione del metodo initialize

    private ClientMain main;
    private boolean loginNewUser;
    private Utilities utilities;

    @FXML   AnchorPane anchorPane;
    @FXML   VBox loginVBox;
    @FXML   Text labelLoginTitle;
    @FXML   JFXTextField textWithUsername;
    @FXML   JFXPasswordField textWithPassword;
    @FXML   JFXPasswordField textWithPasswordConfirm;
    @FXML   JFXButton buttonUserScreen;
    @FXML   JFXButton buttonToContinue;
    @FXML   JFXToggleButton toggleContinue;//TODO eliminarlo

    //metodo che inizializza a false/true le cose che non si dovranno o si dovranno vedere
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginVBox.setVisible(false);
        //labelLoginTitle.setFocusTraversable(true);//focus sul titolo così non sono già dentro un campo nello scrivere
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

    //metodo che serve per far conoscere main e controller
    public void setMain(ClientMain main) {
        this.main = main;
    }

    //metodo che permette di proseguire e vedere la schermata di login cliccando sul background
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


    //metodo per creare un nuovo account
    public void confirmLoginScreen(){
        utilities.playSomeSound();
        if (textWithUsername.getText().isEmpty() || textWithPassword.getText().isEmpty()){ //se ho lasciato qualche campo vuoto mi fermo
            System.out.println("Dati non corretti");
        } else {
            main.readyForLoginUser(textWithUsername.getText(), textWithPassword.getText());//chiamata di funzione passando username, password
        }
    }

    //metodo che permette ad un utente nuovo di creare il proprio account
    public void newUserLogin (){
        utilities.playSomeSound();
        if (textWithUsername.getText().isEmpty() || textWithPassword.getText().isEmpty()) { //se ho lasciato qualche campo vuoto mi fermo
            System.out.println("Dati non corretti");

        } else {
            if (textWithPassword.getText().equals(textWithPasswordConfirm.getText())){
                main.readyForCreateNewUser(textWithUsername.getText(), textWithPassword.getText());//chiamo funzione passando parametri username e password
            } else {
                System.out.println("Password non corrispondenti, reinserire password");
            }
        }
    }

    //metodo per settare la schermata di login nel caso in cui sia NEW USER o LOGIN
    public void setNewUserScreen(){
        utilities.playSomeSound();
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
            //textWithPasswordConfirm.setVisible(true);
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
        textWithPassword.setText(null); //quando cambio schermata azzero i tre campi
        textWithUsername.setText(null);
        //textWithPasswordConfirm.setText(null);
    }

    //metodo che mi permette di proseguire dalla schermata di Login a quella di Choice
    public void continueOnChoiceScreen (){
        main.continueOnChoiceScreen();
    }

    public void setLoginNewUser(boolean loginNewUser) {
        this.loginNewUser = loginNewUser;
    }

}