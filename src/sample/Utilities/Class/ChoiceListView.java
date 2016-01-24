package sample.Utilities.Class;

import java.io.Serializable;
import java.util.ArrayList;

public class ChoiceListView implements Serializable{

    ArrayList<String> clientConnected;
    ArrayList<String> clientInGame;
    ArrayList<String> clientMatchWonPersonal;
    ArrayList<String> clientMatchLostPersonal;
    ArrayList<String> clientMatchWonWorld;
    ArrayList<String> clientMatchLostWorld;

    public ChoiceListView(ArrayList<String> clientConnected, ArrayList<String> clientInGame, ArrayList<String> clientMatchWonPersonal, ArrayList<String> clientMatchLostPersonal, ArrayList<String> clientMatchWonWorld, ArrayList<String> clientMatchLostWorld) {
        this.clientConnected = clientConnected;
        this.clientInGame = clientInGame;
        this.clientMatchWonPersonal = clientMatchWonPersonal;
        this.clientMatchLostPersonal = clientMatchLostPersonal;
        this.clientMatchWonWorld = clientMatchWonWorld;
        this.clientMatchLostWorld = clientMatchLostWorld;
    }

    public ChoiceListView() {
    }

    @Override
    public String toString() {
        return "ChoiceListView{" +
                "clientConnected=" + clientConnected +
                ", clientInGame=" + clientInGame +
                ", clientMatchWonPersonal=" + clientMatchWonPersonal +
                ", clientMatchLostPersonal=" + clientMatchLostPersonal +
                ", clientMatchWonWorld=" + clientMatchWonWorld +
                ", clientMatchLostWorld=" + clientMatchLostWorld +
                '}';
    }

    public ArrayList<String> getClientConnected() {
        return clientConnected;
    }

    public void setClientConnected(ArrayList<String> clientConnected) {
        this.clientConnected = clientConnected;
    }

    public ArrayList<String> getClientInGame() {
        return clientInGame;
    }

    public void setClientInGame(ArrayList<String> clientInGame) {
        this.clientInGame = clientInGame;
    }

    public ArrayList<String> getClientMatchWonPersonal() {
        return clientMatchWonPersonal;
    }

    public void setClientMatchWonPersonal(ArrayList<String> clientMatchWonPersonal) {
        this.clientMatchWonPersonal = clientMatchWonPersonal;
    }

    public ArrayList<String> getClientMatchLostPersonal() {
        return clientMatchLostPersonal;
    }

    public void setClientMatchLostPersonal(ArrayList<String> clientMatchLostPersonal) {
        this.clientMatchLostPersonal = clientMatchLostPersonal;
    }

    public ArrayList<String> getClientMatchWonWorld() {
        return clientMatchWonWorld;
    }

    public void setClientMatchWonWorld(ArrayList<String> clientMatchWonWorld) {
        this.clientMatchWonWorld = clientMatchWonWorld;
    }

    public ArrayList<String> getClientMatchLostWorld() {
        return clientMatchLostWorld;
    }

    public void setClientMatchLostWorld(ArrayList<String> clientMatchLostWorld) {
        this.clientMatchLostWorld = clientMatchLostWorld;
    }

}
