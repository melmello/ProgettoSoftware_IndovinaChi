package sample.Client.ClientClass;

import static sample.Utilities.Class.ConstantCodes.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import sample.Utilities.Class.StickerQuery;
import sample.Utilities.Class.User;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientMain extends Application {

    private int assignedPort;
    private Socket clientSocket;
    private Stage loginStage;
    private Stage gamingStage;
    private PrintWriter writer = null;
    private User user;
    private ClientGameController clientGameController;
    private ClientWorldScoreboardController clientWorldScoreboardController;
    private ClientChoiceController clientChoiceController;
    private ClientLoginController clientLoginController;
    private ClientPersonalScoreboardController clientPersonalScoreboardController;
    private StickerQuery stickerQuery;

    @Override
    public void start(Stage loginStage) throws Exception {
        this.loginStage = loginStage;
        //this.loginStage.setFullScreen(true);//parte in fullscreen
        FXMLLoader loaderClientLoginScreen = new FXMLLoader(getClass().getResource("/sample/Client/ClientFXML/ClientLoginScreen.fxml"));//carico il .fxml
        Parent screenLogin = loaderClientLoginScreen.load();//creo una nuova loadscreen
        clientLoginController = loaderClientLoginScreen.getController();//collego il controller
        loginStage.getIcons().add(new Image("/sample/Client/ClientImage/Icona.png"));//aggiungo icona per la finestra
        loginStage.setTitle("LOGIN");//aggiungo titolo per la finestra
        clientLoginController.setMain(this);//collegare main e controller
        Scene loginScene = new Scene(screenLogin, 1800, 950); //dimensioni quando esco dal fullscreen
        loginStage.setScene(loginScene);
        loginStage.setResizable(false);
        loginScene.getStylesheets().add(getClass().getResource("/sample/Client/ClientCSS/ClientLoginScreen.css").toExternalForm());//collegare il .css (ad esempio) per l'aggiunta del background
        loginStage.show();//mostra
        clientLoginController.updateWithConstraints(loginScene.getWidth(), loginScene.getHeight());//modifica width e height della finestra quando passo da fullscreen a window screen
        assignedPort = 8080;//inizializzo la porta
        try {
            clientSocket = new Socket("localhost", assignedPort);
            ClientThread clientThread = new ClientThread(clientSocket, this);//creazione di un Thread sul socket passandogli l'istanza
            clientThread.start();//lo faccio partire
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERRORE, SERVER NON CONNESSO");
        }
        writer = new PrintWriter(clientSocket.getOutputStream(), true);
        this.loginStage.setOnCloseRequest(new EventHandler<WindowEvent>() {//handle per quando accade che disconnetto un client. Comunico DISCONNESSO con la writer
            public void handle(WindowEvent we) {
                writer.println(clientDisconnected);
                System.out.println("Client disconnesso");
                Platform.exit();
                System.exit(0);
            }
        });
    }

    //main che parte in caso la grafica non vada
    public static void main(String[] args) {
        launch(args);
    }

    //metodo che comunica che i campi sono compilati, invio il segnale al Server che voglio inviargli il gson
    public void readyForLoginUser(String textWithUsername, String textWithPassword) {
        writer.println(clientReadyToLogin);  //Mando segnale login e aspetto che ServerThread nel while(true) lo riceva
        this.user = new User(textWithUsername, textWithPassword); //istanzio un oggetto User
    }

    //metodo che comunica che i campi sono compilati, invio il segnale al Server che voglio inviargli il gson
    public void readyForCreateNewUser(String textWithUsername, String textWithPassword) {
        writer.println(clientReadyToCreateNewUser);//mando segnale al server e aspetto
        this.user = new User(textWithUsername, textWithPassword);
    }

    //getter dell'User
    public User getUser() {
        return user;
    }

    //setter dell'User
    public void setUser(User user) {
        this.user = user;
    }

    //metodo che mi permette di cambiare schermata passando dal login al choiseScreen
    public void continueOnChoiceScreen() {
        Platform.runLater(new Runnable() {//metodo che mi permette di aprire una nuova finestra sfruttando la concorrenza dei Thread
            @Override
            public void run() {
                try {
                    FXMLLoader loaderClientChoiceScreen = new FXMLLoader(getClass().getResource("/sample/Client/ClientFXML/ClientChoiceScreen.fxml"));//carico .fxml
                    Parent screenChoice = loaderClientChoiceScreen.load();//creo un nuovo Parent
                    clientChoiceController = loaderClientChoiceScreen.getController(); //bindo il Controller
                    gamingStage = new Stage(); //uso un nuovo stage che mi servirà in futuro (infatti è attributo della classe) siccome manterrò la stessa finestra
                    //gamingStage.setFullScreen(true);//parte in fullscreen
                    gamingStage.getIcons().add(new Image("/sample/Client/ClientImage/Icona.png"));//icona della finestra
                    gamingStage.setTitle("IN GIOCO");//titolo finestra
                    clientChoiceController.setMain(ClientMain.this);//collegare main e controller
                    Scene choiceScene = new Scene(screenChoice, 1800, 950);
                    gamingStage.setScene(choiceScene);
                    gamingStage.setResizable(false);
                    choiceScene.getStylesheets().add(getClass().getResource("/sample/Client/ClientCSS/ClientChoiceScreen.css").toExternalForm());//collegare il .css (ad esempio) per l'aggiunta del background
                    gamingStage.show();//mostro
                    loginStage.close();
                    gamingStage.setOnCloseRequest(new EventHandler<WindowEvent>() {//handle per quando accade che disconnetto un client. Comunico DISCONNESSO con la writer
                        public void handle(WindowEvent we) {
                            writer.println(clientDisconnected);
                            System.out.println("Client disconnesso");
                            Platform.exit();
                            System.exit(0);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //metodo che mi permette di aprire una window in più che mostra la scoreboard personale
    public void onClickOnPersonalScoreboard() {
        Platform.runLater(new Runnable() {//metodo che mi permette di aprire una nuova finestra sfruttando la concorrenza dei Thread
            @Override
            public void run() {
                try {
                    FXMLLoader loaderClientPersonalScoreboardScreen = new FXMLLoader(getClass().getResource("/sample/Client/ClientFXML/ClientPersonalScoreboardScreen.fxml"));//carico .fxml
                    Parent screenPersonalScoreboard = loaderClientPersonalScoreboardScreen.load();//creo un nuovo Parent
                    clientPersonalScoreboardController = loaderClientPersonalScoreboardScreen.getController(); //bindo il Controller
                    Stage personalScoreboardStage = new Stage();//creo uno Stage
                    personalScoreboardStage.getIcons().add(new Image("/sample/Client/ClientImage/ScoreboardPersonale.png"));//icona della finestra
                    personalScoreboardStage.setTitle("SCOREBOARD PERSONALE");//titolo finestra
                    Rectangle2D myRectangleOfClient = Screen.getPrimary().getVisualBounds();                                 //Queste tre righe servono per la posizione della finestra all'avvio di essa
                    personalScoreboardStage.setX(myRectangleOfClient.getMinX() + myRectangleOfClient.getWidth() - 400);      //
                    personalScoreboardStage.setY(myRectangleOfClient.getMinY() + myRectangleOfClient.getHeight() - 700);     //
                    Scene personalScoreboardScene = new Scene(screenPersonalScoreboard, 500, 500);
                    personalScoreboardStage.setScene(personalScoreboardScene);
                    personalScoreboardStage.setResizable(false);
                    personalScoreboardScene.getStylesheets().add(getClass().getResource("/sample/Client/ClientCSS/ClientChoiceScreen.css").toExternalForm());//collegare il .css (ad esempio) per l'aggiunta del background
                    personalScoreboardStage.show();//mostro
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //metodo che mi permette di aprire una window in più che mostra la scoreboard mondiale
    public void onClickOnWorldScoreboard() {
        Platform.runLater(new Runnable() {//metodo che mi permette di aprire una nuova finestra sfruttando la concorrenza dei Thread
            @Override
            public void run() {
                try {
                    FXMLLoader loaderClientWorldScoreboardScreen = new FXMLLoader(getClass().getResource("/sample/Client/ClientFXML/ClientWorldScoreboardScreen.fxml"));//carico .fxml
                    Parent worldScoreboardScreen = loaderClientWorldScoreboardScreen.load();//creo un nuovo Parent
                    clientWorldScoreboardController = loaderClientWorldScoreboardScreen.getController(); //bindo il Controller
                    Stage worldScoreboardStage = new Stage();//creo uno Stage
                    worldScoreboardStage.getIcons().add(new Image("/sample/Client/ClientImage/ScoreboardMondiale.png"));//icona della finestra
                    worldScoreboardStage.setTitle("SCOREBOARD MONDIALE");//titolo finestra
                    Rectangle2D myRectangleOfClient = Screen.getPrimary().getVisualBounds();                              //Queste tre righe servono per la posizione della finestra all'avvio di essa
                    worldScoreboardStage.setX(myRectangleOfClient.getMinX() + myRectangleOfClient.getWidth() - 400);      //
                    worldScoreboardStage.setY(myRectangleOfClient.getMinY() + myRectangleOfClient.getHeight() - 700);     //
                    Scene worldScoreboardScene = new Scene(worldScoreboardScreen, 500, 500);
                    worldScoreboardStage.setScene(worldScoreboardScene);
                    worldScoreboardStage.setResizable(false);
                    worldScoreboardScene.getStylesheets().add(getClass().getResource("/sample/Client/ClientCSS/ClientChoiceScreen.css").toExternalForm());//collegare il .css (ad esempio) per l'aggiunta del background
                    worldScoreboardStage.show();//mostro
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //metodo che prosegue la schermata da choiseScreen a gameScreen (dove si gioca davvero) e rimango nella stessa finestra siccome gamingStage è lo stesso
    public void continueOnGameScreen() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader loaderClientGameScreen = new FXMLLoader(getClass().getResource("/sample/Client/ClientFXML/ClientGameScreen.fxml"));//carico .fxml
                    Parent screenGame = loaderClientGameScreen.load();//creo un nuovo Parent
                    clientGameController = loaderClientGameScreen.getController(); //bindo il Controller
                    //gamingStage.setFullScreen(true);//parte in fullscreen
                    gamingStage.getIcons().add(new Image("/sample/Client/ClientImage/Icona.png"));//icona della finestra
                    gamingStage.setTitle("IN GIOCO");//titolo finestra
                    //clientGameController.setMain(ClientMain.this, gamingScene.this);//collegare main e controller
                    Scene gamingScene = new Scene(screenGame, 1800, 950);
                    gamingStage.setScene(gamingScene);
                    clientGameController.setMain(ClientMain.this, gamingScene);//collegare main e controller
                    gamingStage.setResizable(false);
                    gamingScene.getStylesheets().add(getClass().getResource("/sample/Client/ClientCSS/ClientChoiceScreen.css").toExternalForm());//collegare il .css (ad esempio) per l'aggiunta del background
                    ClientMain.this.gamingStage.setOnCloseRequest(new EventHandler<WindowEvent>() {//handle per quando accade che disconnetto un client. Comunico DISCONNESSO con la writer
                        public void handle(WindowEvent we) {
                            writer.println(clientDisconnected);
                            System.out.println("Client disconnesso");
                            Platform.exit();
                            System.exit(0);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //metodo che comunica al server che il Client ha scelto lo sticker e vuole mandargli quale
    public void settingMySticker(){
        writer.println(clientReadyToGiveStickerInfo);
        notification("IndovinaChi", "Hai scelto il personaggio!", NotificationType.SUCCESS);

    }

    //metodo che comunica al server il path dell'immagine scelta
    public void comunicateStickerInfo() {
        writer.println(clientGameController.getImagePath());
        clientGameController.abilitateMaskerPane();
    }

    //metodo che dice al Server che il client è pronto per mandare la query
    public void clientWantsToQuery(){
        writer.println(clientReadyToGiveQuery);
    }

    //getter dell'oggetto stickerQuery
    public StickerQuery getStickerQuery() {
        stickerQuery = new StickerQuery(clientGameController.getFirstParameter() , clientGameController.getSecondParameter());
        return stickerQuery;
    }

    //funzione che serve per collegare thread e controller in modo che gli sticker vengano eliminati
    public void modifySticker(ArrayList<String> newStickers) {
        clientGameController.modifySticker(newStickers);
    }

    public void readyToAbilitateClientScreen() {
        notification("IndovinaChi", "Sta a te!", NotificationType.SUCCESS);
        clientGameController.readyToAbilitateClientScreen();
    }

    public void notification(String titleOfTheMoment, String messageOfTheMoment, NotificationType typeOfTheMoment){
        Image icona = new Image("/sample/Client/ClientImage/Icona.png");
        String titleOfNotification = titleOfTheMoment;
        String messageOfNotification = messageOfTheMoment;
        NotificationType typeOfModification = typeOfTheMoment;
        TrayNotification tray = new TrayNotification(titleOfNotification, messageOfNotification, typeOfModification);
        tray.setImage(icona);
        tray.showAndWait();
        tray.showAndDismiss(Duration.seconds(3));
    }

}