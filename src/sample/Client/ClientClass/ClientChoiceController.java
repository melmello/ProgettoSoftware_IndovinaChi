package sample.Client.ClientClass;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientChoiceController implements Initializable {

    ClientMain main;

    @FXML ImageView imageSingle;
    @FXML ImageView imageMulti;
    @FXML JFXListView<String> clientConnectedListView;

    //metodo che inizializza a false/true le cose che non si dovranno o si dovranno vedere
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageMulti.setVisible(false);
        imageSingle.setVisible(false);
        clientConnectedListView.setVisible(false);
    }

    //metodo che serve per far conoscere main e controller
    public void setMain(ClientMain main) {
        this.main = main;
    }

    //metodo che mi mostra quando clicco sul Play le modalità di gioco
    public void onClickOnPlayImage(){
        imageSingle.setVisible(true);
        imageMulti.setVisible(true);
        clientConnectedListView.setVisible(true);

    }

    //metodo che apre nuove finestre se clicco su personal scoreboard
    public void onClickOnPersonalScoreboard(){
        main.onClickOnPersonalScoreboard();
    }

    //metodo che apre nuove finestre se clicco su world scoreboard
    public void onClickOnWorldScoreboard(){
        main.onClickOnWorldScoreboard();
    }

    /*
    //metodo che popola la list view con i client online
    public void populateClientConnectedListView(){
        ObservableList<String> clientConnected = FXCollections.observableArrayList("si");
        clientConnectedListView.setItems(clientConnected);
        //main.populateClientConnectedListView();
    }
    */

    //metodo che collega Choice screen e Game screen
    public void continueOnGameScreen(){
        main.continueOnGameScreen();
    }

}
