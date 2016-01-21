package sample.Server.ServerClass;

import static sample.Utilities.Class.ConstantCodes.*;
import sample.Utilities.Class.Utilities;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerStartingController implements Initializable{//così posso implementare la visibilità iniziale o meno degli elementi

    Utilities utilities = new Utilities();
    ServerMain main;
    @FXML JFXTextField textWithServerIp;
    @FXML JFXTextField textWithServerPort;
    @FXML Text labelIP;
    @FXML Text labelDescription;
    @FXML Text labelTitle;
    @FXML JFXButton buttonStartingScreen;


    //metodo per inizializzare ciò che si vede o no nella mia schermata
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textWithServerIp.setVisible(false);
        labelIP.setVisible(false);
        labelDescription.setVisible(false);
        textWithServerPort.setText("8080");
        labelTitle.setFocusTraversable(true);
    }

    //metodo per collegare main e controller
    public void setMain (ServerMain main){
        this.main = main;
    }

    //metodo per quando clicco il Button che voglio connettere il server su quella porta e mostrare IP e popup con client connessi
    public void clickButton () {
        utilities.playSomeSound(buttonClickSound);
        if (textWithServerPort.getText().isEmpty()) {//se il testo è vuoto
            System.out.println("Errore: porta non inserita");
            labelDescription.setVisible(false);
        } else {
            try {
                main.startingServer();//faccio una getText della porta e lo passo nel metodo startingServer per far partire il Server
                buttonStartingScreen.setVisible(false);
                main.openNewWindow();//aprire un nuovo popup
                textWithServerPort.setEditable(false);
            } catch (NumberFormatException e) {//il testo contiene caratteri diversi da quelli accettati
                System.out.println(e + ": errore sulla digitazione");
            }
        }
    }

    //metodo per configurare l'IP e stamparlo a vista nel Server una volta fatto RUN
    public void initialConfiguration(String assignedIp) {
        textWithServerIp.setText(assignedIp);
        textWithServerIp.setVisible(true);
        labelIP.setVisible(true);
        labelDescription.setVisible(true);
    }

}