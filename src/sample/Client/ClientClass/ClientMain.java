package sample.Client.ClientClass;

import static sample.Utilities.Class.ConstantCodes.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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

    private Socket clientSocket;
    private Stage loginStage;
    private Stage gamingStage;
    private PrintWriter writer = null;
    private User user;
    private ClientGameController clientGameController;
    private ClientChoiceController clientChoiceController;
    private ClientLoginController clientLoginController;
    private StickerQuery stickerQuery;
    private String wantsToKnowClientConnected;

    @Override
    public void start(Stage loginStage) throws Exception {
        this.loginStage = loginStage;
        FXMLLoader loaderClientLoginScreen = new FXMLLoader(getClass().getResource(loginScreenFXML));//carico il .fxml
        Parent screenLogin = loaderClientLoginScreen.load();//creo una nuova loadscreen
        clientLoginController = loaderClientLoginScreen.getController();//collego il controller
        loginStage.getIcons().add(new Image(loginScreenIcon));//aggiungo icona per la finestra
        loginStage.setTitle("LOGIN SCREEN");//aggiungo titolo per la finestra
        clientLoginController.setMain(this);//collegare main e controller
        Scene loginScene = new Scene(screenLogin, 1850, 1000); //dimensioni quando esco dal fullscreen
        loginStage.setScene(loginScene);
        loginStage.setResizable(false);
        loginScene.getStylesheets().add(getClass().getResource(loginScreenCSS).toExternalForm());//collegare il .css (ad esempio) per l'aggiunta del background
        loginStage.show();//mostra
        //clientLoginController.updateWithConstraints(loginScene.getWidth(), loginScene.getHeight());//modifica width e height della finestra quando passo da fullscreen a window screen
        try {
            clientSocket = new Socket("127.0.0.1", assignedPort);//TODO localhost -> ip
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
        notification("Autenticazione avvenuta con successo " + user.getUserUsername());
    }

    //metodo che comunica che i campi sono compilati, invio il segnale al Server che voglio inviargli il gson
    public void readyForCreateNewUser(String textWithUsername, String textWithPassword) {
        writer.println(clientReadyToCreateNewUser);//mando segnale al server e aspetto
        this.user = new User(textWithUsername, textWithPassword);
    }

    //metodo che mi permette di cambiare schermata passando dal login al choiseScreen
    public void continueOnChoiceScreen() {
        Platform.runLater(new Runnable() {//metodo che mi permette di aprire una nuova finestra sfruttando la concorrenza dei Thread
            @Override
            public void run() {
                try {
                    FXMLLoader loaderClientChoiceScreen = new FXMLLoader(getClass().getResource(choiceScreenFXML));//carico .fxml
                    Parent screenChoice = loaderClientChoiceScreen.load();//creo un nuovo Parent
                    clientChoiceController = loaderClientChoiceScreen.getController(); //bindo il Controller
                    gamingStage = new Stage(); //uso un nuovo stage che mi servirà in futuro (infatti è attributo della classe) siccome manterrò la stessa finestra
                    gamingStage.getIcons().add(new Image(choiceScreenIcon));//icona della finestra
                    gamingStage.setTitle("IN GIOCO");//titolo finestra
                    Scene choiceScene = new Scene(screenChoice, 1850, 1000);
                    gamingStage.setScene(choiceScene);
                    gamingStage.setResizable(false);
                    choiceScene.getStylesheets().add(getClass().getResource(choiceScreenCSS).toExternalForm());//collegare il .css (ad esempio) per l'aggiunta del background
                    gamingStage.show();//mostro
                    clientChoiceController.setMain(ClientMain.this);//collegare main e controller
                    loginStage.close();
                    clientWantsClientConnected();
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

    //metodo che prosegue la schermata da choiseScreen a gameScreen (dove si gioca davvero) e rimango nella stessa finestra siccome gamingStage è lo stesso
    public void continueOnGameScreen() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader loaderClientGameScreen = new FXMLLoader(getClass().getResource(gameScreenFXML));//carico .fxml
                    Parent screenGame = loaderClientGameScreen.load();//creo un nuovo Parent
                    clientGameController = loaderClientGameScreen.getController(); //bindo il Controller
                    gamingStage.getIcons().add(new Image(gameScreenIcon));//icona della finestra
                    gamingStage.setTitle("INDOVINA CHI");//titolo finestra
                    //clientGameController.setMain(ClientMain.this, gamingScene.this);//collegare main e controller
                    Scene gamingScene = new Scene(screenGame, 1850, 1000);
                    gamingStage.setScene(gamingScene);
                    clientGameController.setMain(ClientMain.this, gamingScene);//collegare main e controller
                    gamingStage.setResizable(false);
                    gamingScene.getStylesheets().add(getClass().getResource(gameScreenCSS).toExternalForm());//collegare il .css (ad esempio) per l'aggiunta del background
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
        notification("Hai scelto il personaggio!");
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientGameController.modifySticker(newStickers);
            }
        });
    }

    public void readyToAbilitateClientScreen() {        //TODO manu (doppio Platform.runLater)
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                notification("Sta a te!");
                clientGameController.readyToAbilitateClientScreen();
            }
        });
    }

    //metodo che chiama la notifica
    public void notification(String messageOfTheMoment){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Image icona = new Image(notificationIcon);
                String messageOfNotification = messageOfTheMoment;
                TrayNotification tray = new TrayNotification(indovinaChiText, messageOfNotification, NotificationType.SUCCESS);
                tray.setImage(icona);
                tray.showAndWait();
                tray.showAndDismiss(Duration.seconds(1));
            }
        });
    }

    public void clientWantsClientConnected() {
        writer.println(wantsToKnowClientConnected);
    }

    public void displayClientConnected(ArrayList<String> clientConnected) {
        clientChoiceController.displayClientConnected(clientConnected);
    }

    public void notificationForNewUser() {
        notification("Registrazione avvenuta con successo " + user.getUserUsername());
        clientLoginController.setLoginNewUser(false);
        clientLoginController.setNewUserScreen();
    }

    //getter dell'User
    public User getUser() {
        return user;
    }

    //setter dell'User
    public void setUser(User user) {
        this.user = user;
    }

    public void clientWantsToSendRating() {
        writer.println(clientWantsToSendRatingCode);
    }

    public void sendClientRating() {
        writer.println(Double.toString(clientChoiceController.clientRating));
    }
}