package sample.Server.ServerClass;

/** @author Giulio Melloni
 * Questa classe è usata come controller della schermata di conteggio dei client.
 * Questa schermata viene aggiornata quando un client entra nel login, oppure si disconnette da qualsiasi schermata: sia che sia in gioco, che nella schermata principale.
 * Riassumento, nel lato server, questa schermata è quella che potrebbe vedere quanti attualmente stanno usufruendo dei servizi offerti dal nostro server.
 */

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class ServerClientCounterController {

    @FXML JFXTextField textWithClientNumber;

    /** Stampo il numero di Client connessi nel Popup del server.
     * @param clientNumber è l'intero che sarà stampato nella schermata col numero dei client connessi, aggiornato ogni volta che un client verifica le proprie credenziali, oppure ogni volta che chiude il gioco.
     */
    public void printNumberOfClient(String clientNumber){
        textWithClientNumber.setText(clientNumber);
    }

}
