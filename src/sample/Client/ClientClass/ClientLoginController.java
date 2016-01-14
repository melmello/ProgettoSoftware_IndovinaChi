package sample.Client.ClientClass;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientLoginController implements Initializable {//serve per avere l'implementazione del metodo initialize

    ClientMain main;
    boolean loginNewUser;

    @FXML   private AnchorPane anchorPane;
    @FXML   private VBox loginVBox;
    @FXML   private Text labelLoginTitle;
    @FXML   private JFXTextField textWithUsername;
    @FXML   private JFXPasswordField textWithPassword;
    @FXML   private JFXPasswordField textWithPasswordConfirm;
    @FXML   private JFXButton buttonLogin;
    @FXML   private JFXButton buttonNewUser;
    @FXML   private JFXButton buttonUserScreen;
    @FXML   private JFXToggleButton toggleContinue;//TODO eliminarlo

    //metodo che inizializza a false/true le cose che non si dovranno o si dovranno vedere
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textWithUsername.setVisible(false);
        textWithPassword.setVisible(false);
        textWithPasswordConfirm.setVisible(false);
        buttonLogin.setVisible(false);
        buttonNewUser.setVisible(false);
        buttonNewUser.setVisible(false);
        buttonUserScreen.setVisible(false);
        labelLoginTitle.setVisible(false);
        labelLoginTitle.setFocusTraversable(true);//focus sul titolo così non sono già dentro un campo nello scrivere
        loginNewUser = true;//entro nella schermata di login
    }

    //metodo che serve per far conoscere main e controller
    public void setMain(ClientMain main) {
        this.main = main;
    }

    //metodo che permette di proseguire e vedere la schermata di login cliccando sul background
    public void clickOnBackground(){
        System.out.println("Sto cliccando il background");
        textWithUsername.setVisible(true);
        textWithPassword.setVisible(true);
        textWithPasswordConfirm.setVisible(false);
        labelLoginTitle.setVisible(true);
        buttonLogin.setVisible(true);
        buttonNewUser.setVisible(false);
        buttonUserScreen.setVisible(true);
        anchorPane.setOnMouseClicked(null);//inibisce il background click
    }

    //metodo per il passaggio da fullscreen a window screen
    public void updateWithConstraints(double width, double height) {
        //anchorPane.setRightAnchor(loginPane, anchorPane.getWidth()*4/100 );
        anchorPane.setLeftAnchor(loginVBox, anchorPane.getWidth()*25/100);
        anchorPane.setTopAnchor(loginVBox, anchorPane.getHeight()*25/100);
        //anchorPane.setBottomAnchor(loginPane, anchorPane.getHeight()*4/100);
    }

    //metodo per creare un nuovo account
    public void confirmLoginScreen(){
        if (textWithUsername.getText().isEmpty() || textWithPassword.getText().isEmpty()){ //se ho lasciato qualche campo vuoto mi fermo
            System.out.println("Dati non corretti");
        } else {
            main.readyForLoginUser(textWithUsername.getText(), textWithPassword.getText());//chiamata di funzione passando username, password
        }
    }

    //metodo che permette ad un utente nuovo di creare il proprio account
    public void newUserLogin (){
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
        if(loginNewUser==true){
            labelLoginTitle.setText("NEW USER");
            buttonUserScreen.setText("HAI GIA' UN ACCOUNT?");
            buttonLogin.setVisible(false);
            buttonNewUser.setVisible(true);
            textWithPasswordConfirm.setVisible(true);
            loginNewUser=false;
        }
        else{
            labelLoginTitle.setText("LOGIN");
            buttonUserScreen.setText("SEI UN NUOVO UTENTE?");
            buttonLogin.setVisible(true);
            buttonNewUser.setVisible(false);
            textWithPasswordConfirm.setVisible(false);
            loginNewUser=true;
        }
        textWithPassword.setText(null); //quando cambio schermata azzero i tre campi
        textWithUsername.setText(null);
        textWithPasswordConfirm.setText(null);
    }

    //metodo che mi permette di proseguire dalla schermata di Login a quella di Choice
    public void continueOnChoiceScreen (){
        main.continueOnChoiceScreen();
    }

}