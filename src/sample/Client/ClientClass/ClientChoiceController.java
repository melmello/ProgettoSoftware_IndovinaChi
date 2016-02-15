package sample.Client.ClientClass;

/** @author Giulio Melloni
 * Classe in cui si gestisce il controller della seconda schermata, ossia la schermata di choice. In questa schermata si possono scegliere varie azioni calciando nel relativo angolo dove vi è l'icona:
 * - Vedere la propria scoreboard con le ultime partite vinte e perse aggiornate in tempo reale.
 * - Vedere la leaderboard mondiale e quindi il nostro posizionamento all'interno di essa, aggiornato in tempo reale.
 * - Fare una recensione inviando un voto e un commento.
 * - Giocare contro un client online e disponibile e vedere i client in gioco, anche questo aggiornato in tempo reale.
 * Come gli altri controller, quindi, questo è il ponte tra l'azione del client e il main del client in cui comunico tramite writer col server.
 */

import static sample.Utilities.Class.ConstantCodes.*;
import com.jfoenix.controls.JFXListView;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import org.controlsfx.control.Rating;
import sample.Utilities.Class.Utilities;
import java.net.URL;
import java.util.*;

public class ClientChoiceController implements Initializable {

    private double clientRating;
    private String opponentChoosen;
    private Utilities utilities = new Utilities();
    private ClientMain main;
    private ImageView oldImage = new ImageView();
    private ArrayList<Node> transitionOn = new ArrayList<>();
    private int timeDuration = 7500;
    private Timer timer = new Timer();
    @FXML Pane anchorPaneExternal;
    @FXML AnchorPane anchorPane;
    @FXML AnchorPane anchorRating;
    @FXML AnchorPane anchorWorld;
    @FXML AnchorPane anchorPersonal;
    @FXML AnchorPane anchorPlayer;
    @FXML TextField titleRating;
    @FXML TextArea textRating;
    @FXML JFXListView<String> personalScoreboardWonListView;
    @FXML JFXListView<String> personalScoreboardLostListView;
    @FXML JFXListView<String> worldScoreboardListView;
    @FXML JFXListView<String> clientConnectedListView;
    @FXML JFXListView<String> clientInGameListView;
    @FXML ImageView ballImage;
    @FXML ImageView goalKeeper;
    @FXML Rating ratingBox;

    /** Metodo che inizializza a false/true le cose che non si dovranno o si dovranno vedere.
     * @param location null se la location non è nota (come in questo caso).
     * @param resources null se il root object non è localizzato (come in questo caso).
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anchorRating.setVisible(false);
        anchorWorld.setVisible(false);
        anchorPersonal.setVisible(false);
        anchorPlayer.setVisible(false);
        clientConnectedListView.setDisable(false);
    }

    /** Metodo usato per collegare main e controller passando l'istanza.
     * @param main è l'istanza di ClientMain
     */
    public void setMain(ClientMain main) {
        this.main = main;
    }

    /** Metodo che quando chiamato prende clientConnectedList e popola la listView dei client connessi.
     * @param clientConnectedList array di stringhe con i nomi dei client connessi
     */
    public void displayClientConnected(ArrayList<String> clientConnectedList) {
        ArrayList<String> clientConnectedListWithoutMe = new ArrayList<>();
        for (int cont = 0; cont < clientConnectedList.size(); cont++){
            if (!clientConnectedList.get(cont).equals(main.getUser().getUserUsername())){
                clientConnectedListWithoutMe.add(clientConnectedList.get(cont));
            }
        }
        ObservableList<String> clientConnectedObsWithoutMe = FXCollections.observableArrayList(clientConnectedListWithoutMe);
        clientConnectedListView.setItems(clientConnectedObsWithoutMe);
        setListViewHeight(clientConnectedListWithoutMe, clientConnectedListView);
    }

    /** Metodo che quando chiamato prende clientInaGameList e popola la listView dei client in gioco.
     * @param clientInGameList array di stringhe con i nomi dei client in gioco.
     */
    public void displayClientInGame(ArrayList<String> clientInGameList){
        ObservableList<String> clientInGameObs = FXCollections.observableArrayList(clientInGameList);
        clientInGameListView.setItems(clientInGameObs);
        setListViewHeight(clientInGameList, clientInGameListView);
    }

    /** Metodo che prende due array, i match vinti e i match persi e con essi popola le due list view relative aggiorando i valori.
     * @param personalMatchWon array di stringhe con le partite vinte.
     * @param personalMatchLost array di stringhe con le partite perse.
     */
    public void displayPersonalLeaderboard(ArrayList<String> personalMatchWon, ArrayList<String> personalMatchLost) {
        ObservableList<String> personalMatchWonObs = FXCollections.observableArrayList(personalMatchWon);
        ObservableList<String> personalMatchLostObs = FXCollections.observableArrayList(personalMatchLost);
        personalScoreboardWonListView.setItems(personalMatchWonObs);
        personalScoreboardLostListView.setItems(personalMatchLostObs);
        setListViewHeight(personalMatchWon, personalScoreboardWonListView);
        setListViewHeight(personalMatchLost, personalScoreboardLostListView);
    }

    /** Metodo che prende in ingresso l'array con la classifica mondiale e con esso popola la relativa listView.
     * @param worldLeaderboard array ordinato in ordine decrescente con la classifica mondiale.
     */
    public void displayWorldLeaderboard(ArrayList<String> worldLeaderboard) {
        ObservableList<String> worldLeaderboardObs = FXCollections.observableArrayList(worldLeaderboard);
        worldScoreboardListView.setItems(worldLeaderboardObs);
        setListViewHeight(worldLeaderboard, worldScoreboardListView);
    }

    /** Metodo stilistico che setta l'altezza della listview che nel caso in cui abbia meno di 7 elementi è in un modo, altrimenti in un altro.
     * @param objectInList lista degli oggetti.
     * @param listView listView su cui modifico l'altezza.
     */
    private void setListViewHeight(ArrayList<String> objectInList, ListView<String> listView) {
        if (objectInList.size() < 8){
            listView.setPrefHeight((objectInList.size()+4)*18.2f);
        } else {
            listView.setPrefHeight((8+4)*18.2f);
        }
    }

    /** Il client seleziona un altro client dalla lista dei client connessi perchè vuole giocarci. Qui disabilito la possibilità che possa sceglierne un altro nel frattempo.
     */
    public void clientWantsToPlayAGameWith(){
        opponentChoosen = clientConnectedListView.getSelectionModel().getSelectedItem();
        System.out.println(opponentChoosen + " -> opponentChoosen");
        main.clientWantsToPlayAGameWith(opponentChoosen);
        clientConnectedListView.setDisable(true);
    }

    /** Metodo che serve per spostare la palla secondo una determinata funzione matematica e spostarla sull'immagine scelta.
     * @param event è l'evento, ossia l'immagine che ho scelto. Viene chiamato {@link #selectionOfClient(String, ImageView)}.
     */
    public void ballMovement(Event event){
        ballImage.setScaleX(1);
        ballImage.setScaleY(1);
        utilities.fadeTransitionEffect(oldImage, 0.3f, 1f, 1000);
        ImageView imageChoosen = (ImageView) event.getTarget();
        Path path = new Path();
        path.getElements().add(new MoveTo(ballImage.getFitWidth()/2, ballImage.getFitHeight()/2));
        if (imageChoosen.getId().equals(IMAGE_PLAYAGAME) || imageChoosen.getId().equals(IMAGE_RATING)) {
            ArcTo arcTo = new ArcTo(50, 50, 100, imageChoosen.getLayoutX() - ballImage.getLayoutX() + anchorPane.getLayoutX() + imageChoosen.getFitWidth() / 2, imageChoosen.getLayoutY() + -ballImage.getLayoutY() + anchorPane.getLayoutY() + imageChoosen.getFitHeight() / 2, false, false);
            path.getElements().add(arcTo);
        } else {
            LineTo lineTo = new LineTo(imageChoosen.getLayoutX() - ballImage.getLayoutX() + anchorPane.getLayoutX() + imageChoosen.getFitWidth()/2, imageChoosen.getLayoutY() + - ballImage.getLayoutY() + anchorPane.getLayoutY() + imageChoosen.getFitHeight()/2);
            path.getElements().add(lineTo);
        }
        selectionOfClient(imageChoosen.getId(), imageChoosen);
        path.setStrokeWidth(1);
        utilities.playSomeSound(BALLSHOT_SOUND);
        final PathTransition pathTransition = PathTransitionBuilder.create()
                .node(ballImage)
                .path(path)
                .duration(Duration.millis(1000))
                .orientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT)
                .cycleCount(1)
                .build();
        pathTransition.playFromStart();
        utilities.fadeTransitionEffect(imageChoosen, 1, 0.3f, 1000);
        utilities.scaleTransition(ballImage, 0.5f, 0.5f, 1000);
        oldImage = imageChoosen;
    }

    /** A seconda dell'immagine scelta il portiere si tuffa e un determinato pane si mostra per una frazione di tempo. Viene chiamato {@link #seeImageContext(Node, Node, int)}.
     * @param idOfImage è l'id dell'immagine scelta.
     * @param imageChoosen è l'immagine scelta.
     */
    private void selectionOfClient(String idOfImage, ImageView imageChoosen) {
        Path path = new Path();
        path.getElements().add(new MoveTo(goalKeeper.getFitWidth() / 2, goalKeeper.getFitHeight() / 2));
        switch (idOfImage) {
            case (IMAGE_RATING):{
                seeImageContext(anchorRating, anchorPane.lookup("#" + IMAGE_RATING), timeDuration*4);
                goalKeeper.setImage(new Image(GOALKEEPER_JUMPING_RIGHT));
                goalKeeper.setRotate(0);
                LineTo lineTo = new LineTo(imageChoosen.getLayoutX() - goalKeeper.getLayoutX() - imageChoosen.getFitWidth() / 2, imageChoosen.getLayoutY() - goalKeeper.getLayoutY() + imageChoosen.getFitHeight() / 2 + 100);
                path.getElements().add(lineTo);
                break;
            }
            case (IMAGE_PLAYAGAME):{
                seeImageContext(anchorPlayer, anchorPane.lookup("#" + IMAGE_PLAYAGAME), timeDuration);
                goalKeeper.setImage(new Image(GOALKEEPER_JUMPING_LEFT));
                goalKeeper.setRotate(0);
                LineTo lineTo = new LineTo(imageChoosen.getLayoutX() - goalKeeper.getLayoutX() - imageChoosen.getFitWidth() / 2 + 200, imageChoosen.getLayoutY() - goalKeeper.getLayoutY() + imageChoosen.getFitHeight() / 2 + 100);
                path.getElements().add(lineTo);
                break;
            }
            case (IMAGE_WORLDSCOREBOARD):{
                seeImageContext(anchorWorld, anchorPane.lookup("#" + IMAGE_WORLDSCOREBOARD), timeDuration);
                goalKeeper.setImage(new Image(GOALKEEPER_JUMPING_RIGHT));
                LineTo lineTo = new LineTo(imageChoosen.getLayoutX() - goalKeeper.getLayoutX() - imageChoosen.getFitWidth() / 2 - 50, 200);
                goalKeeper.setRotate(25);
                path.getElements().add(lineTo);
                break;
            }
            case (IMAGE_PERSONALSCOREBOARD):{
                seeImageContext(anchorPersonal, anchorPane.lookup("#" + IMAGE_PERSONALSCOREBOARD), timeDuration);
                goalKeeper.setImage(new Image(GOALKEEPER_JUMPING_LEFT));
                LineTo lineTo = new LineTo(imageChoosen.getLayoutX() - goalKeeper.getLayoutX() - imageChoosen.getFitWidth() / 2 + 250, 200);
                goalKeeper.setRotate(-25);
                path.getElements().add(lineTo);
                break;
            }
        }
        path.setStrokeWidth(1);
        final PathTransition pathTransition = PathTransitionBuilder.create()
                .node(goalKeeper)
                .path(path)
                .duration(Duration.millis(1000))
                .orientation(PathTransition.OrientationType.NONE)
                .cycleCount(1)
                .build();
        pathTransition.playFromStart();
    }

    /** Viene reso visibile il pane relativo e viene attivato il timer con relativa duration (per la recensione la duration sarà ovviamente maggiore).
     * @param node è il pane da mostrare.
     * @param nodeSelected è l'immagine selezionata.
     * @param duration è la durata.
     */
    private void seeImageContext(Node node, Node nodeSelected, int duration) {
        utilities.fadeTransitionEffect(node, 0, 1, 1000);
        node.setVisible(true);
        if (oldImage.equals(nodeSelected) || transitionOn.contains(nodeSelected)){
            duration*=2;
            timer.cancel();
            timer.purge();
            transitionOn.remove(nodeSelected);
        }
        transitionOn.add(nodeSelected);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                utilities.fadeTransitionEffect(node, 1, 0, 3000);
                transitionOn.remove(nodeSelected);
            }
        }, duration);
    }

    /** Metodo che comunica al main voto, testo e titolo della recensione dell'applicazione.
     */
    public void ratingGame() {
        clientRating = ratingBox.getRating();
        System.out.println(clientRating + " -> CLIENT RATING");
        main.clientWantsToSendRating(clientRating, titleRating.getText(), textRating.getText());
        main.notification("Recensione inviata con successo");
        titleRating.setText("");
        textRating.setText("");
    }

    /** Metodo che mostra il popup quando un giocatore ci sfida. In questo caso viene disabilitata la possibilità di mandare nel frattempo una richiesta ad un altro client.
     * @param userAndNumber è l'array con in posizione 0 l'utente e in posizione 1 il numero del match.
     */
    public void playGameRequest(ArrayList<String> userAndNumber) {
        clientConnectedListView.setDisable(true);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ACCETTA LA SFIDA");
        alert.setHeaderText(null);
        alert.setContentText("Il giocatore " + userAndNumber.get(0) + " ti ha inviato una richiesta di gioco.\nVuoi accettarla?");
        ButtonType buttonYes = new ButtonType("Accetta la sfida");
        ButtonType buttonNo = new ButtonType("Annulla");
        alert.getButtonTypes().setAll(buttonYes, buttonNo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonYes){
            main.sendServerOkForPlaying(userAndNumber);
            clientConnectedListView.setDisable(false);

        } else if (result.get() == buttonNo){
            main.sendServerNoForPlaying(userAndNumber);
            clientConnectedListView.setDisable(false);
        }
    }

    /** getter.
     * @return listView con i client connessi.
     */
    public JFXListView<String> getClientConnectedListView() {
        return clientConnectedListView;
    }
}