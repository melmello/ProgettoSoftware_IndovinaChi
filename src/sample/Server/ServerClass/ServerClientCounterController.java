package sample.Server.ServerClass;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class ServerClientCounterController {

    private ServerMain main;

    @FXML JFXTextField textWithClientNumber;

    public void setMain (ServerMain main){
        this.main = main;
    }

    //stampo il numero di Client connessi nel Popup del server
    public void printNumberOfClient(String clientNumber){
        textWithClientNumber.setText(clientNumber);
    }

}
