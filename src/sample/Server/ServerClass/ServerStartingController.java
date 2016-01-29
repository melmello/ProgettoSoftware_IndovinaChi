package sample.Server.ServerClass;

/** @author Giulio Melloni
 * Questa classe è utilizzata come controller per la schermata principale del server. In essa possiamo visualizzare ip e porta su cui siamo connessi.
 * In questa classe troviamo metodi che si occupano di collegare la UI dell'utente all'azione espressa nel main del Server.
 * Un esempio è il metodo clickButton che serve a creare la connessione creando un nuovo thread che sarà vero e proprio main del server e accetterà i client inserendoli nell'array di Thread.
 */

import static sample.Utilities.Class.ConstantCodes.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerStartingController implements Initializable{

    private ServerMain main;
    @FXML JFXTextField textWithServerIp;
    @FXML JFXTextField textWithServerPort;
    @FXML Text labelIP;
    @FXML Text labelDescription;
    @FXML Text labelTitle;
    @FXML JFXButton buttonStartingScreen;

    /** Metodo usato per inizializzare ciò che si vede o no nella mia schermata, fatto subito quando viene precaricata la schermata.
     * Vengono quindi messi non visibili o visibili alcuni oggetti, e viene assegnato il testo alla porta.
     * @param location null se la location non è nota (come in questo caso).
     * @param resources null se il root object non è localizzato (come in questo caso).
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textWithServerIp.setVisible(false);
        labelIP.setVisible(false);
        labelDescription.setVisible(false);
        textWithServerPort.setText(Integer.toString(ASSIGNED_PORT_SOCKET));
        labelTitle.setFocusTraversable(true);
    }

    /** Metodo per collegare main e controller. Questo sarà chiamato nel main quando sarà creato uno stage con relativa scene.
     * @param main usato per collegare controller e main.
     */
    public void setMain (ServerMain main){
        this.main = main;
    }

    /** Metodo chiamato quando clicco il Button che voglio connettere il server su quella porta e mostrare IP e popup con client connessi.
     * Si può vedere come ci sia una condizione sullo start del server: se non vi è una porta prefissata il server risponde che non vi è una porta inserita.
     * In caso contrario il server chiama il metodo del main che lo farà partire, setterà non più visibili alcuni campi e successivamente creerà il popup con il conteggio dei client connessi.
     */
    public void clickButton () {
        if (textWithServerPort.getText().isEmpty()) {//se il testo è vuoto
            System.out.println("Errore: porta non inserita");
            labelDescription.setVisible(false);
        } else {
            try {
                main.startingServer();//faccio una getText della porta e lo passo nel metodo startingServer per far partire il Server
                buttonStartingScreen.setVisible(false);
                main.openClientCounterPopup();//aprire un nuovo popup
                textWithServerPort.setEditable(false);
            } catch (NumberFormatException e) {//il testo contiene caratteri diversi da quelli accettati
                e.printStackTrace();
            }
        }
    }

    /** Metodo per configurare l'IP e stamparlo a vista nel Server una volta fatto RUN.
     * Questo metodo avrà come scopo di rendere visibile il campo con l'IP che prima era non visibile.
     * @param assignedIp passo dal main al controller l'IP a cui sono collegato.
     */
    public void initialConfiguration(String assignedIp) {
        textWithServerIp.setText(assignedIp);
        textWithServerIp.setVisible(true);
        labelIP.setVisible(true);
        labelDescription.setVisible(true);
    }

}