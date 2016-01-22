package sample.Utilities.Class;

import sample.Server.ServerClass.ServerThread;
import java.io.Serializable;

public class Game implements Serializable{

    private ServerThread player1;
    private ServerThread player2;

    public Game(ServerThread player1, ServerThread player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Game() {
    }

    @Override
    public String toString() {
        return "Game{" +
                "player1='" + player1 + '\'' +
                ", player2='" + player2 + '\'' +
                '}';
    }

    public ServerThread getPlayer1() {
        return player1;
    }

    public void setPlayer1(ServerThread player1) {
        this.player1 = player1;
    }

    public ServerThread getPlayer2() {
        return player2;
    }

    public void setPlayer2(ServerThread player2) {
        this.player2 = player2;
    }
}
