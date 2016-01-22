package sample.Utilities.Class;

import java.io.Serializable;

public class Game implements Serializable{

    private String player1;
    private String player2;

    public Game(String player1, String player2) {
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

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

}
