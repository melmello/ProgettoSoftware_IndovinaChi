package sample.Client.ClientClass;

import static sample.Utilities.Class.ConstantCodes.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import sample.Utilities.Class.CodeAndInformation;
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
    private PrintWriter writer;
    private User user;
    private ClientGameController clientGameController;
    private ClientChoiceController clientChoiceController;
    private ClientLoginController clientLoginController;
    private StickerQuery stickerQuery;
    private Gson gson;

    //main che parte in caso la grafica non vada
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage loginStage) throws Exception {
        this.loginStage = loginStage;
        FXMLLoader loaderClientLoginScreen = new FXMLLoader(getClass().getResource(LOGINSCREEN_FXML));//carico il .fxml
        Parent screenLogin = loaderClientLoginScreen.load();//creo una nuova loadscreen
        clientLoginController = loaderClientLoginScreen.getController();//collego il controller
        loginStage.getIcons().add(new Image(LOGINSCREEN_ICON));//aggiungo icona per la finestra
        loginStage.setTitle("LOGIN SCREEN");//aggiungo titolo per la finestra
        clientLoginController.setMain(this);//collegare main e controller
        Scene loginScene = new Scene(screenLogin, 1850, 1000); //dimensioni quando esco dal fullscreen
        loginStage.setScene(loginScene);
        loginStage.setResizable(false);
        loginScene.getStylesheets().add(getClass().getResource(LOGINSCREEN_CSS).toExternalForm());//collegare il .css (ad esempio) per l'aggiunta del background
        loginStage.show();//mostra
        try {
            clientSocket = new Socket(ASSIGNED_IP_SOCKET, ASSIGNED_PORT_SOCKET);//TODO localhost -> ip
            ClientThread clientThread = new ClientThread(clientSocket, this);//creazione di un Thread sul socket passandogli l'istanza
            clientThread.start();//lo faccio partire
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERRORE, SERVER NON CONNESSO");
        }
        writer = new PrintWriter(clientSocket.getOutputStream(), true);
        this.loginStage.setOnCloseRequest(new EventHandler<WindowEvent>() {//handle per quando accade che disconnetto un client. Comunico DISCONNESSO con la writer
            public void handle(WindowEvent we) {
                writer.println(CodeAndInformation.serializeToJson(CLIENT_DISCONNECTING_FROM_LOGIN_SCREEN, null));
                System.out.println("Un client ha chiuso la schermata di login");
                Platform.exit();
                System.exit(0);
            }
        });
    }

    //metodo che mi permette di cambiare schermata passando dal login al choiseScreen
    public void continueOnChoiceScreen() {
        Platform.runLater(new Runnable() {//metodo che mi permette di aprire una nuova finestra sfruttando la concorrenza dei Thread
            @Override
            public void run() {
                try {
                    FXMLLoader loaderClientChoiceScreen = new FXMLLoader(getClass().getResource(CHOICESCREEN_FXML));//carico .fxml
                    Parent screenChoice = loaderClientChoiceScreen.load();//creo un nuovo Parent
                    clientChoiceController = loaderClientChoiceScreen.getController(); //bindo il Controller
                    gamingStage = new Stage(); //uso un nuovo stage che mi servirà in futuro (infatti è attributo della classe) siccome manterrò la stessa finestra
                    gamingStage.getIcons().add(new Image(CHOICESCREEN_ICON));//icona della finestra
                    gamingStage.setTitle("IN GIOCO");//titolo finestra
                    Scene choiceScene = new Scene(screenChoice, 1850, 1000);
                    gamingStage.setScene(choiceScene);
                    gamingStage.setResizable(false);
                    choiceScene.getStylesheets().add(getClass().getResource(CHOICESCREEN_CSS).toExternalForm());//collegare il .css (ad esempio) per l'aggiunta del background
                    gamingStage.show();//mostro
                    clientChoiceController.setMain(ClientMain.this);//collegare main e controller
                    loginStage.close();
                    writer.println(CodeAndInformation.serializeToJson(CLIENT_WANTS_TO_KNOW_CONNECTED_CLIENT_FOR_THE_FIRST_TIME, user.getUserUsername()));
                    gamingStage.setOnCloseRequest(new EventHandler<WindowEvent>() {//handle per quando accade che disconnetto un client. Comunico DISCONNESSO con la writer
                        public void handle(WindowEvent we) {
                            writer.println(CodeAndInformation.serializeToJson(CLIENT_DISCONNECTING, null));
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
                    FXMLLoader loaderClientGameScreen = new FXMLLoader(getClass().getResource(GAMESCREEN_FXML));//carico .fxml
                    Parent screenGame = loaderClientGameScreen.load();//creo un nuovo Parent
                    clientGameController = loaderClientGameScreen.getController(); //bindo il Controller
                    gamingStage.getIcons().add(new Image(GAMESCREEN_ICON));//icona della finestra
                    gamingStage.setTitle("INDOVINA CHI");//titolo finestra
                    Scene gamingScene = new Scene(screenGame, 1850, 1000);
                    gamingStage.setScene(gamingScene);
                    clientGameController.setMain(ClientMain.this, gamingScene);//collegare main e controller
                    gamingStage.setResizable(false);
                    gamingScene.getStylesheets().add(getClass().getResource(GAMESCREEN_CSS).toExternalForm());//collegare il .css (ad esempio) per l'aggiunta del background
                    ClientMain.this.gamingStage.setOnCloseRequest(new EventHandler<WindowEvent>() {//handle per quando accade che disconnetto un client. Comunico DISCONNESSO con la writer
                        public void handle(WindowEvent we) {
                            writer.println(CodeAndInformation.serializeToJson(CLIENT_DISCONNECTING, null));
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

    //metodo che comunica che i campi sono compilati, invio il segnale al Server che voglio inviargli il gson
    public void loginOrNewUser(String textWithUsername, String textWithPassword, Boolean login) {
        String codeToSend;
        gson = new Gson(); //creo un oggetto gson per serializzare e deserializzare
        user = new User(textWithUsername, textWithPassword);//istanzio un oggetto User
        String userString = gson.toJson(user);  //serializzo i campi dello User
        System.out.println(userString + " -> userString");
        if (login == true) {
            codeToSend = CodeAndInformation.serializeToJson(CLIENT_WANTS_TO_LOGIN, userString);
        } else {
            codeToSend = CodeAndInformation.serializeToJson(CLIENT_WANTS_TO_SIGNUP, userString);
        }
        writer.println(codeToSend);  //Mando segnale login e aspetto che ServerThread nel while(true) lo riceva
    }

    //metodo che comunica al server che il Client ha scelto lo sticker e vuole mandargli quale
    public void settingMySticker(){
        writer.println(CodeAndInformation.serializeToJson(CLIENT_GIVES_STICKER_INFO, clientGameController.getImagePath()));
        notification("Hai scelto il personaggio!");
        clientGameController.abilitateMaskerPane();
    }

    //metodo che dice al Server che il client è pronto per mandare la query
    public void clientWantsToQuery(){
        gson = new Gson();
        String stickerQueryString = gson.toJson(stickerQuery);
        writer.println(CodeAndInformation.serializeToJson(CLIENT_GIVES_QUERY, stickerQueryString));
    }

    //getter dell'oggetto stickerQuery
    public StickerQuery getStickerQuery() {
        stickerQuery = new StickerQuery(clientGameController.getFirstParameter() , clientGameController.getSecondParameter());
        return stickerQuery;
    }

    //funzione che serve per collegare thread e controller in modo che gli sticker vengano eliminati
    public void modifySticker(String information) {
        ArrayList<String> newStickers = gson.fromJson(information, new TypeToken<ArrayList<String>>(){}.getType());//deserializzo il gson in un ArrayList
        System.out.println(newStickers + " -> newStickers");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientGameController.modifySticker(newStickers);
            }
        });
    }

    public void readyToAbilitateClientScreen() {
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
                Image icona = new Image(NOTIFICATION_ICON);
                String messageOfNotification = messageOfTheMoment;
                TrayNotification tray = new TrayNotification(NOTIFICATION_TEXT, messageOfNotification, NotificationType.SUCCESS);
                tray.setImage(icona);
                tray.showAndWait();
                tray.showAndDismiss(Duration.seconds(3));
            }
        });
    }

    public void displayClientConnected(String information) {
        gson = new Gson();
        ArrayList<String> clientConnected = gson.fromJson(information, new TypeToken<ArrayList<String>>() {}.getType());
        System.out.println(clientConnected + " -> clientConnected");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientChoiceController.displayClientConnected(clientConnected);
            }
        });
    }

    public void displayClientInGame(String information) {
        gson = new Gson();
        ArrayList<String> clientInGame = gson.fromJson(information, new TypeToken<ArrayList<String>>() {}.getType());
        System.out.println(clientInGame + " -> clientInGame");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientChoiceController.displayClientInGame(clientInGame);
            }
        });
    }

    public void notificationForNewUser() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                notification("Registrazione avvenuta con successo " + user.getUserUsername());
                clientLoginController.setLoginNewUser(false);
                clientLoginController.setNewUserScreen();
            }
        });
    }

    public void clientWantsToSendRating(Double clientRating) {
        writer.println(CodeAndInformation.serializeToJson(CLIENT_WANTS_TO_SEND_RATING, Double.toString(clientRating)));
    }

    //metodo che informa il server che il client vuole giocare una partita con questo utente
    public void clientWantsToPlayAGameWith(String opponentChoosen) {
        writer.println(CodeAndInformation.serializeToJson(CLIENT_WANTS_TO_PLAY, opponentChoosen));
    }

    public void playGameRequest(String information) {
        gson = new Gson();
        ArrayList<String> userAndNumber = gson.fromJson(information, new TypeToken<ArrayList<String>>() {}.getType());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientChoiceController.playGameRequest(userAndNumber);
            }
        });
    }

    public void sendServerOkForPlaying(ArrayList<String> userAndNumber) {
        userAndNumber.set(0, user.getUserUsername());
        gson = new Gson();
        String information = gson.toJson(userAndNumber);
        writer.println(CodeAndInformation.serializeToJson(CLIENT_SAYS_OK_FOR_PLAYING, information));
    }

    public void sendServerNoForPlaying(ArrayList<String> userAndNumber) {
        userAndNumber.set(0, user.getUserUsername());
        gson = new Gson();
        String information = gson.toJson(userAndNumber);
        writer.println(CodeAndInformation.serializeToJson(CLIENT_SAYS_NO_FOR_PLAYING, information));
    }

    public void clientWantsToQuerySticker(String id) {
        writer.println(CodeAndInformation.serializeToJson(CLIENT_GIVES_QUERY_FOR_STICKER, id));
    }

    //getter & setter
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}