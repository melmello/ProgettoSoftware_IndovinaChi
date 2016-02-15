package sample.Client.ClientClass;

/** @author Giulio Melloni
 * Classe Main del Client, ossia quello dove avvengono le modifiche all'interfaccia, dove vengono caricati gli fxml, create le scene.
 * Mentre nel server abbiamo un ServerStart e un ServerMain per differenziare i ruoli di gestione dei client e dell'interfaccia, qui non ne ho bisogno e posso condensare tutto in un'unica classe.
 * E' qui che ho tutti i metodi che eseguo a seconda del segnale ricevuto dal Server nel while(true) di ClientThread.
 */

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
import java.util.Arrays;

public class ClientMain extends Application {

    private Socket clientSocket;
    private Stage loginStage;
    private Stage gamingStage;
    private PrintWriter writer;
    private User user;
    private Parent screenGame;
    private Scene gamingScene;
    private ClientGameController clientGameController;
    private ClientChoiceController clientChoiceController;
    private ClientLoginController clientLoginController;
    private StickerQuery stickerQuery;
    private Gson gson;

    /** Metodo main, lanciato in caso in cui l'interfaccia non parta.
     * Non è specificato in quanto l'interfaccia per questo gioco è fondamentale.
     * @param args sono gli argomenti passati al main.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /** Il main grafico. E' qui dove carico l'fxml, il css, dove creo un nuovo stage, dove creo una nuova scene, dove accoppio il controller e dove passo al main l'istanza.
     * Oltre alla parte grafica, come specificato all'inizio, in questo start si svolgono anche le azioni di:
     * - creazione di un nuovo socket sulla porta e sull'ip specificato.
     * - gestione dell'uscita con tanto di segnale inviato al server per aggiornare gli utenti connessi.
     * @param loginStage è lo stage primario iniziale.
     * @throws Exception è la gestione sulle eccezioni grafiche possibili.
     */
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
            clientSocket = new Socket(ASSIGNED_IP_SOCKET, ASSIGNED_PORT_SOCKET);
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

    /** Metodo che mi permette di cambiare schermata passando dal login al choiceScreen.
     * Metodo pressochè identico allo start(), cambia solo che creo una nuova finestra e nascondo la precedente. Infatti lo stage e la scene cambiano.
     * Inoltre carico un nuovo .fxml, un nuovo .css, accoppio un nuovo controller a cui passo la mia istanza.
     */
    public void continueOnChoiceScreen() {
        Platform.runLater(new Runnable() {//metodo che mi permette di aprire una nuova finestra sfruttando la concorrenza dei Thread
            @Override
            public void run() {
                try {
                    FXMLLoader loaderClientChoiceScreen = new FXMLLoader(getClass().getResource(CHOICESCREEN_FXML));//carico .fxml
                    Parent screenChoice = loaderClientChoiceScreen.load();//creo un nuovo Parent
                    clientChoiceController = loaderClientChoiceScreen.getController(); //bindo il Controller
                    if (gamingStage == null) {
                        gamingStage = new Stage(); //uso un nuovo stage che mi servirà in futuro (infatti è attributo della classe) siccome manterrò la stessa finestra
                    }
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
                            writer.println(CodeAndInformation.serializeToJson(CLIENT_DISCONNECTING_FROM_CHOICE_SCREEN, null));
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

    /** Metodo che prosegue la schermata da choiseScreen a gameScreen (dove si gioca davvero).
     * La differenza tra il passaggio da start() a choiceScreen() e questo è che qui rimango nella stessa finestra, infatti lo stage non cambia, e neanche la scene.
     * Tuttavia, come sempre, carico un nuovo .fxml e un nuovo .css, creando, difatti, una nuova interfaccia anche se nella stessa finestra.
     */
    public void continueOnGameScreen() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (gamingScene == null) {
                        FXMLLoader loaderClientGameScreen = new FXMLLoader(getClass().getResource(GAMESCREEN_FXML));//carico .fxml
                        screenGame = loaderClientGameScreen.load();//creo un nuovo Parent
                        clientGameController = loaderClientGameScreen.getController(); //bindo il Controller
                        gamingScene = new Scene(screenGame, 1850, 1000);
                    } else if (gamingScene != null) {
                        clientGameController.configureScreen();
                        clientGameController.initialize(null, null);
                    }
                    gamingStage.getIcons().add(new Image(GAMESCREEN_ICON));//icona della finestra
                    gamingStage.setTitle("INDOVINA CHI");//titolo finestra
                    gamingStage.setScene(gamingScene);
                    clientGameController.setMain(ClientMain.this, gamingScene);//collegare main e controller
                    gamingStage.setResizable(false);
                    gamingScene.getStylesheets().add(getClass().getResource(GAMESCREEN_CSS).toExternalForm());//collegare il .css (ad esempio) per l'aggiunta del background
                    ClientMain.this.gamingStage.setOnCloseRequest(new EventHandler<WindowEvent>() {//handle per quando accade che disconnetto un client. Comunico DISCONNESSO con la writer
                        public void handle(WindowEvent we) {
                            writer.println(CodeAndInformation.serializeToJson(CLIENT_DISCONNECTING_FROM_GAME_SCREEN, user.getUserUsername()));
                            System.out.println("Un client ha quittato");
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

    /** Metodo che comunica che voglio provare ad indentificarmi o a creare un nuovo utente nel gioco.
     * Invio quindi al server un gson con i miei dati.
     * @param textWithUsername username.
     * @param textWithPassword password.
     * @param login true se è login, false se è signup.
     */
    public void loginOrNewUser(String textWithUsername, String textWithPassword, Boolean login) {
        String codeToSend;
        gson = new Gson(); //creo un oggetto gson per serializzare e deserializzare
        user = new User(textWithUsername, textWithPassword);//istanzio un oggetto User
        String userString = gson.toJson(user);  //serializzo i campi dello User
        System.out.println(userString + " -> userString");
        if (login) {
            codeToSend = CodeAndInformation.serializeToJson(CLIENT_WANTS_TO_LOGIN, userString);
        } else {
            codeToSend = CodeAndInformation.serializeToJson(CLIENT_WANTS_TO_SIGNUP, userString);
        }
        writer.println(codeToSend);  //Mando segnale login e aspetto che ServerThread nel while(true) lo riceva
    }

    /** Metodo che comunica al server lo sticker scelto dal Client.
     */
    public void settingMySticker(){
        writer.println(CodeAndInformation.serializeToJson(CLIENT_GIVES_STICKER_INFO, clientGameController.getImagePath()));
        notification("Hai scelto il personaggio!");
    }

    /** Metodo che scrive al Server la query che il client ha scelto di fare.
     * @param firstParameter attributo del database su cui il client vuole fare la query.
     * @param secondParameter tuple del database su cui il client vuole fare la query.
     */
    public void clientWantsToQuery(String firstParameter, String secondParameter){
        gson = new Gson();
        stickerQuery = new StickerQuery(firstParameter, secondParameter);
        System.out.println(stickerQuery);
        String stickerQueryString = gson.toJson(stickerQuery);
        writer.println(CodeAndInformation.serializeToJson(CLIENT_GIVES_QUERY, stickerQueryString));
    }

    /** Metodo con la quale il client fa una query sullo sticker mandando al server l'id.
     * @param id è l'id dell'immagine scelta, nonchè nickname nel database.
     */
    public void clientWantsToQuerySticker(String id) {
        writer.println(CodeAndInformation.serializeToJson(CLIENT_GIVES_QUERY_FOR_STICKER, id));
    }

    /** Funzione che serve per collegare thread e controller in modo che gli sticker vengano eliminati. Svolge quindi una funzione di bridge ricevendo dal Thread l'arraylist di nomi da cancellare e passandola al controller.
     * @param information è l'array serializzato con i nomi degli sticker da rimuovere.
     */
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

    /** Metodo che, ricevuto il segnale che è il mio turno, modifica il controller mettendo a false i disable.
     */
    public void readyToAbilitateClientScreen() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                notification("Sta a te!");
                clientGameController.disableForChangingRound(false);
            }
        });
    }

    /** Metodo di notifica, ossia quando viene chiamato in basso a destra spunta una notifica che dopo 3 secondi sparisce.
     * @param messageOfTheMoment è il testo che sarà stampato a video nella notifica.
     */
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

    /** Metodo che aggiorna i client connessi nella schermata di Choice.
     * E' quindi anche questo un metodo bridge.
     * @param information è l'array serializzato con i client connessi.
     */
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

    /** Metodo identico a {@link #displayClientConnected(String)} solo che qui mostro i client in gioco.
     * @param information è l'array serializzato con i client in gioco.
     */
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

    /** Metodo che aggiorna la personal Leaderboard.
     * Information è serializzato e suddiviso in due arraylist, i match vinti e i match persi (serializzati anche loro).
     * @param information è l'informazione in cui ho gli arraylist con match vinti e persi.
     */
    public void displayPersonalLeaderboard(String information) {
        gson = new Gson();
        ArrayList<String> personalMatch = gson.fromJson(information, new TypeToken<ArrayList<String>>() {}.getType());
        gson = new Gson();
        ArrayList<String> personalMatchWon = gson.fromJson(personalMatch.get(0), new TypeToken<ArrayList<String>>() {}.getType());
        ArrayList<String> personalMatchLost = gson.fromJson(personalMatch.get(1), new TypeToken<ArrayList<String>>() {}.getType());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientChoiceController.displayPersonalLeaderboard(personalMatchWon, personalMatchLost);
            }
        });
    }

    /** Metodo identico a {@link #displayPersonalLeaderboard(String)}, soltanto che qui information è serializzato e contiene l'array ordinato per la leaderboard mondiale.
     * @param information è l'array serializzato e ordinato con la world leaderboard.
     */
    public void displayWorldLeaderboard(String information) {
        gson = new Gson();
        ArrayList<String> worldLeaderboard = gson.fromJson(information, new TypeToken<ArrayList<String>>() {}.getType());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientChoiceController.displayWorldLeaderboard(worldLeaderboard);
            }
        });
    }

    /** Metodo che notifica al nuovo utente la creazione avvenuta con successo.
     * Cambia schermata e lo manda su login.
     */
    public void notificationForNewUser() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                notification("Registrazione avvenuta con successo " + user.getUserUsername());
                clientLoginController.setLoginNewUser(false);
                clientLoginController.setFromServer(true);
                clientLoginController.setNewUserScreen();
            }
        });
    }

    /** Metodo che invia al Server le informazioni della recensione del client serializzate in un arraylist.
     * @param clientRating è il voto dato.
     * @param title è il titolo della recensione.
     * @param text è il commento della recensione.
     */
    public void clientWantsToSendRating(Double clientRating, String title, String text) {
        gson = new Gson();
        ArrayList<String> voteTitleText = new ArrayList<>();
        voteTitleText.addAll(Arrays.asList(Double.toString(clientRating), title, text));
        String voteTitleTextString = gson.toJson(voteTitleText);
        writer.println(CodeAndInformation.serializeToJson(CLIENT_WANTS_TO_SEND_RATING, voteTitleTextString));
    }

    /** Metodo che informa il server che il client vuole giocare una partita con l'utente opponentChoosen.
     * @param opponentChoosen è il nome dell'utente contro cui si vuole giocare.
     */
    public void clientWantsToPlayAGameWith(String opponentChoosen) {
        writer.println(CodeAndInformation.serializeToJson(CLIENT_WANTS_TO_PLAY, opponentChoosen));
    }

    /** Metodo che fa da bridge. Un utente ha inviato una richiesta di gioco e nel controller mostrerò un popup con ACCETTA o RIFIUTA la sfida.
     * @param information è un array in cui in prima posizione ho il nome dell'utente che vuole giocare, in seconda il numero del match che ha creato l'altro client quando ha mandato la richiesta.
     */
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

    /** Metodo usato per riabilitare la possibilità di chiedere di giocare.
     */
    public void serverForbids() {
        notification("L'utente non ha accettato la tua proposta");
        clientChoiceController.getClientConnectedListView().setDisable(false);
    }

    /** Metodo che informa il server che il client ha accettato la richiesta e vuole giocare.
     * @param userAndNumber è l'arrayList che contiene in prima posizione l'user che ha accettato e in seconda il matchNumber inerente alla partita creata
     */
    public void sendServerOkForPlaying(ArrayList<String> userAndNumber) {
        userAndNumber.set(0, user.getUserUsername());
        gson = new Gson();
        String information = gson.toJson(userAndNumber);
        writer.println(CodeAndInformation.serializeToJson(CLIENT_SAYS_OK_FOR_PLAYING, information));
    }

    /** Metodo duale di {@link #sendServerOkForPlaying(ArrayList)} solo che qui non si accetta.
     * Il comportamento tuttavia è uguale.
     @param userAndNumber è l'arrayList che contiene in prima posizione l'user che ha accettato e in seconda il matchNumber inerente alla partita creata
     */
    public void sendServerNoForPlaying(ArrayList<String> userAndNumber) {
        userAndNumber.set(0, user.getUserUsername());
        gson = new Gson();
        String information = gson.toJson(userAndNumber);
        writer.println(CodeAndInformation.serializeToJson(CLIENT_SAYS_NO_FOR_PLAYING, information));
    }

    /** getter.
     * @return l'utente.
     */
    public User getUser() {
        return user;
    }

    /** setter.
     * @param user l'utente.
     */
    public void setUser(User user) {
        this.user = user;
    }

}