package sample.Server.ServerClass;

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
    sout
    private ServerStartingController serverStartingController;
    private ServerClientCounterController serverClientCounterController;

    //main che parte in caso la grafica non vada
    public static void main(String[] args){
        launch(args);
    }

    //metodo main per la grafica
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

    //metodo per creare il popup
    public void openNewWindow(){
        try{
            FXMLLoader loaderServerClientCounterScreen = new FXMLLoader(getClass().getResource(CLIENTCOUNTERSCREEN_FXML));//carico .fxml
            Parent screenCounter = loaderServerClientCounterScreen.load();//creo un nuovo Parent
            serverClientCounterController = loaderServerClientCounterScreen.getController(); //bindo il Controller
            Stage clientCounterStage = new Stage();//creo uno Stage
            clientCounterStage.getIcons().add(new Image(CLIENTCOUNTERSCREEN_ICON));//icona della finestra
            clientCounterStage.setTitle("# CLIENT");//titolo finestra
            Rectangle2D myRectangleOfClient = Screen.getPrimary().getVisualBounds();                        //Queste tre righe servono per la posizione della finestra all'avvio di essa
            clientCounterStage.setX(myRectangleOfClient.getMinX() + myRectangleOfClient.getWidth() - 400);      //
            clientCounterStage.setY(myRectangleOfClient.getMinY() + myRectangleOfClient.getHeight() - 700);     //
            Scene clientCounterScene = new Scene(screenCounter);
            clientCounterStage.setScene(clientCounterScene);
            clientCounterStage.setResizable(false);
            clientCounterScene.getStylesheets().add(getClass().getResource(CLIENTCOUNTERSCREEN_CSS).toExternalForm());//collegare il .css (ad esempio) per l'aggiunta del background
            clientCounterStage.show();//mostro
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //metodo per far stampare l'IP del Server in un TextField del Server
    public void initialConfiguration(String assignedIp){
        serverStartingController.initialConfiguration(assignedIp);
    }

    //metodo per stampare il numero dei client connessi nel popup del Server
    public void printNumberOfClient(String clientNumber){
        serverClientCounterController.printNumberOfClient(clientNumber);
    }

    //metodo che fa partire il server quando clicco il button
    public void startingServer(){
        ServerStart serverStart = new ServerStart(this);//creo un nuovo ServerStart
        Thread thread = new Thread (serverStart);//creo un thread
        thread.start();//lo faccio partire
    }

}