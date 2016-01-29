package sample.Utilities.Class;

/** @author Giulio Melloni
 * Classe che mi serve per tenere traccia delle partite in gioco. In un oggetto istanziato su questo tipo di Classe avremo infatti il nome dei due giocatori ed entrambi gli sticker relativi.
 */

import sample.Server.ServerClass.ServerThread;
import java.io.Serializable;

public class Game implements Serializable{

    private ServerThread player1;
    private ServerThread player2;
    private Sticker sticker1;
    private Sticker sticker2;

    /** Costruttore di Game
     * @param player1 giocatore che ha mandato la richiesta
     * @param player2 giocatore che ha accettato la richiesta
     */
    public Game(ServerThread player1, ServerThread player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    /** Metodo usato per la serializzazione di Game
     * @return la serializzazione
     */
    @Override
    public String toString() {
        return "Game{" +
                "player1='" + player1 + '\'' +
                ", player2='" + player2 + '\'' +
                '}';
    }

    /** getter.
     * @return player1.
     */
    public ServerThread getPlayer1() {
        return player1;
    }

    /** setter.
     * @param player1 player1.
     */
    public void setPlayer1(ServerThread player1) {
        this.player1 = player1;
    }

    /** getter.
     * @return player2.
     */
    public ServerThread getPlayer2() {
        return player2;
    }

    /** setter.
     * @param player2 player2.
     */
    public void setPlayer2(ServerThread player2) {
        this.player2 = player2;
    }

    /** getter.
     * @return sticker del player1.
     */
    public Sticker getSticker1() {
        return sticker1;
    }

    /** setter.
     * @param sticker1 sticker del player1.
     */
    public void setSticker1(Sticker sticker1) {
        this.sticker1 = sticker1;
    }

    /** getter.
     * @return sticker del player2.
     */
    public Sticker getSticker2() {
        return sticker2;
    }

    /** setter.
     * @param sticker2 sticker del player2.
     */
    public void setSticker2(Sticker sticker2) {
        this.sticker2 = sticker2;
    }

}
