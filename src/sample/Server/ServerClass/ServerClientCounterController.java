package sample.Server.ServerClass;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ServerClientCounterController {

    ServerMain main;
    @FXML   TextField textWithClientNumber;

    public void setMain (ServerMain main){
        this.main = main;
    }

    //stampo il numero di Client connessi nel Popup del server
    public void printNumberOfClient(String clientNumber){
        textWithClientNumber.setText(clientNumber);
    }

}
