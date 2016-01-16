package sample.Client.ClientClass;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ClientChoiceController implements Initializable {

    ClientMain main;

    @FXML ImageView imageSingle;
    @FXML ImageView imageMulti;
    @FXML JFXListView<String> clientConnectedListView;
    @FXML JFXToggleButton temporaryButton;  //TODO eliminarlo

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
        main.clientWantsClientConnected();
    }

    //metodo che mi mostra quando clicco sul Play le modalità di gioco
    public void onClickOnPlayImage(){
        imageSingle.setVisible(true);
        imageMulti.setVisible(true);
        clientConnectedListView.setVisible(true);

    }

    //metodo che collega Choice screen e Game screen
    public void continueOnGameScreen(){
        main.continueOnGameScreen();
    }

    public void displayClientConnected(ArrayList<String> clientConnectedList) {
        ArrayList<String> clientConnectedListWithoutMe = new ArrayList<>();
        for (int cont = 0; cont < clientConnectedList.size(); cont++){
            if (!clientConnectedList.get(cont).equals(main.getUser().getUserUsername())){
                clientConnectedListWithoutMe.add(clientConnectedList.get(cont));
            }
        }
        ObservableList<String> clientConnectedObs = FXCollections.observableArrayList(clientConnectedList);
        ObservableList<String> clientConnectedObsWithoutMe = FXCollections.observableArrayList(clientConnectedListWithoutMe);
        clientConnectedListView.setItems(clientConnectedObsWithoutMe);
    }
}
