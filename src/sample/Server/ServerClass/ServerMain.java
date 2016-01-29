package sample.Server.ServerClass;

/** @author Giulio Melloni
 * Questa classe è il main del server in cui viene caricata la parte grafica.
 * Qui viene, infatti, gestito il comportamento in caso non si abbia un'interfaccia, vengono gestite le comunicazioni che avvengono col controller dopo qualche determinata azione (ad esempio cosa avviene quando clicco RUN SERVER).
 * Riassumento, questa classe è utilizzata a scopo grafico, ossia caricare le schermate e in base ad azioni fatte nel controller o nel main {@link #initialConfiguration(String)}.
 */

import static sample.Utilities.Class.ConstantCodes.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.*;
import java.io.IOException;

public class ServerMain extends Application {

    private ServerStartingController serverStartingController;
    private ServerClientCounterController serverClientCounterController;

    /** Main che parte nel caso in cui lo start non parta, ossia se, ad esempio, il sistemo su cui stiamo runnando il server è privo di interfaccia grafica.
     * Basterà infatti dare il comando "withoutInterface" quando si prova a lanciare il jar da terminale (java -jar Server.jar withoutInterface).
     * @param args è quello che mi permette di runnare senza interfaccia. Sono le parole aggiunte nella command-line quando runno il gioco.
     */
    public static void main(String[] args){
        if (args.length>0 && args[0].equals("withoutInterface")){
            System.out.println("Server partito senza interfaccia");
            ServerMain serverMain = new ServerMain();
            serverMain.startingServer();
        } else {
            launch(args);
        }
    }

    /** Metodo fratello del main, solo che questo viene definito come "main grafico".
     * In questo metodo viene creato uno stage con una relativa scene grafica, viene chiamato il setMain del controller passandogli l'istanza.
     * Anche qui troviamo l'handle con il caso in cui la schermata venga chiusa che segnala la disconnessione del server.
     * Lo start è anche il metodo in cui viene caricato tramite il loader (che poi sarà accoppiato al controller) il foglio FXML.
     * Viene caricato, inoltre, anche un .CSS che mi darà alcune direttive sullo stile, come il font oppure il carattere dei testi.
     * Qui è dove, insomma, gestisco la finestra che sarà utilizzabile dal client.
     * @param startingStage gli viene passato lo stage in modo che si possa usare all'interno del metodo.
     * @throws Exception classe utilizzata per evitare eccezioni a runtime, come il NullPointerException o il RuntimeException. E' come se l'intero metodo fosse circondato da un try catch con ogni eccezione.
     */
    public void start(Stage startingStage) throws Exception {
        FXMLLoader loaderServerStartingScreen = new FXMLLoader(getClass().getResource(STARTINGSCREEN_FXML));//creo un nuovo FMXLLoader passandogli il .fxml
        Parent screenServerStarting = loaderServerStartingScreen.load();//carico l'.fxml
        serverStartingController = loaderServerStartingScreen.getController();//serve per il controller
        startingStage.getIcons().add(new Image(STARTINGSCREEN_ICON));//Icona della finestra
        startingStage.setTitle("SERVER");//titolo della finestra
        serverStartingController.setMain(this);//collegare Controller e main
        Scene startingScene = new Scene(screenServerStarting); //dimensioni quando esco dal fullscreen
        startingStage.setScene(startingScene);//creo la scena
        startingStage.setResizable(false);
        startingScene.getStylesheets().add(getClass().getResource(STARTINGSCREEN_CSS).toExternalForm());//collegare il .css (ad esempio) per l'aggiunta del background
        startingStage.show();//comando che mostra la schermata
        startingStage.setOnCloseRequest(new EventHandler<WindowEvent>() {//Quando si disconnette un client mando questo segnale
            public void handle(WindowEvent we) {
                System.out.println("Server disconnesso.");
                Platform.exit();
                System.exit(0);
            }
        });
    }

    /** Metodo usato per creare e mostrare il popup con i client connessi una volta cliccato il button StartServer. Per maggiori informazioni {@link #start(Stage)}.
     */
    public void openClientCounterPopup(){
        try{
            FXMLLoader loaderServerClientCounterScreen = new FXMLLoader(getClass().getResource(CLIENTCOUNTERSCREEN_FXML));//carico .fxml
            Parent screenCounter = loaderServerClientCounterScreen.load();//creo un nuovo Parent
            serverClientCounterController = loaderServerClientCounterScreen.getController(); //bindo il Controller
            Stage clientCounterStage = new Stage();//creo uno Stage
            clientCounterStage.getIcons().add(new Image(CLIENTCOUNTERSCREEN_ICON));//icona della finestra
            clientCounterStage.setTitle("# CLIENT");//titolo finestra
            Rectangle2D myRectangleOfClient = Screen.getPrimary().getVisualBounds();                                //Queste tre righe servono per la posizione della finestra all'avvio di essa
            clientCounterStage.setX(myRectangleOfClient.getMinX() + myRectangleOfClient.getWidth() - 400);          //
            clientCounterStage.setY(myRectangleOfClient.getMinY() + myRectangleOfClient.getHeight() - 700);         //
            Scene clientCounterScene = new Scene(screenCounter);
            clientCounterStage.setScene(clientCounterScene);
            clientCounterStage.setResizable(false);
            clientCounterScene.getStylesheets().add(getClass().getResource(CLIENTCOUNTERSCREEN_CSS).toExternalForm());//collegare il .css (ad esempio) per l'aggiunta del background
            clientCounterStage.show();//mostro
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /** Metodo usato per far stampare l'IP del Server in un TextField del Server, chiamo quindi una funzione del controller passandogli il parametro.
     * @param assignedIp è l'ip che sto passando e sto mandando al controller, che sarà stampato sull'interfaccia del server.
     */
    public void initialConfiguration(String assignedIp){
        serverStartingController.initialConfiguration(assignedIp);
    }

    /** Metodo per stampare il numero dei client connessi nel popup del Server.
     * Questo è un metodo "bridge" tra il main del server e il controller.
     * @param clientNumber rappresenta il numero di client connessi che passo al controller.
     */
    public void printNumberOfClient(String clientNumber){
        serverClientCounterController.printNumberOfClient(clientNumber);
    }

    /** Metodo che, quando clicco il button, crea un nuovo serverStart passandogli l'istanza (this, ossia il main), e crea un nuovo thread passandogli serverStart.
     * In questo modo posso svincolare il server da due lavori diversi: gestione del client e gestione dell'interfaccia.
     */
    public void startingServer(){
        ServerStart serverStart = new ServerStart(this);//creo un nuovo ServerStart
        Thread thread = new Thread (serverStart);//creo un thread
        thread.start();//lo faccio partire
    }

}