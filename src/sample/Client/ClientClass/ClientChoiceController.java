package sample.Client.ClientClass;

import static sample.Utilities.Class.ConstantCodes.*;
import com.jfoenix.controls.JFXListView;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import org.controlsfx.control.Rating;
import sample.Utilities.Class.Utilities;
import java.net.URL;
import java.sql.Array;
import java.util.*;

public class ClientChoiceController implements Initializable {

    private double clientRating;
    private String opponentChoosen;
    private Utilities utilities = new Utilities();
    private ClientMain main;
    @FXML AnchorPane anchorPane;
    @FXML JFXListView<String> personalScoreboardWonListView;
    @FXML JFXListView<String> personalScoreboardLostListView;
    @FXML JFXListView<String> worldScoreboardListView;
    @FXML JFXListView<String> clientConnectedListView;
    @FXML JFXListView<String> clientInGameListView;
    @FXML ImageView ballImage;
    @FXML ImageView goalKeeper;
    @FXML Rating ratingBox;

    //metodo che inizializza a false/true le cose che non si dovranno o si dovranno vedere
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personalScoreboardWonListView.setVisible(false);
        personalScoreboardLostListView.setVisible(false);
        worldScoreboardListView.setVisible(false);
        clientConnectedListView.setVisible(false);
        clientInGameListView.setVisible(false);
        ratingBox.setVisible(false);
    }

    //metodo che serve per far conoscere main e controller
    public void setMain(ClientMain main) {
        this.main = main;
    }

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

    public void displayClientInGame(ArrayList<String> clientInGameList){
        ObservableList<String> clientInGameObs = FXCollections.observableArrayList(clientInGameList);
        clientInGameListView.setItems(clientInGameObs);
        setListViewHeight(clientInGameList, clientInGameListView);
    }

    // TODO: 24/01/2016 VEDERE REFRESH LEADERBOARD!
    public void displayPersonalLeaderboard(ArrayList<String> personalMatchWon, ArrayList<String> personalMatchLost) {
        ObservableList<String> personalMatchWonObs = FXCollections.observableArrayList(personalMatchWon);
        ObservableList<String> personalMatchLostObs = FXCollections.observableArrayList(personalMatchLost);
        personalScoreboardWonListView.setItems(personalMatchWonObs);
        personalScoreboardLostListView.setItems(personalMatchLostObs);
        setListViewHeight(personalMatchWon, personalScoreboardWonListView);
        setListViewHeight(personalMatchLost, personalScoreboardLostListView);
    }

    public void displayWorldLeaderboard(ArrayList<String> worldLeaderboard) {
        ObservableList<String> worldLeaderboardObs = FXCollections.observableArrayList(worldLeaderboard);
        worldScoreboardListView.setItems(worldLeaderboardObs);
        setListViewHeight(worldLeaderboard, worldScoreboardListView);
    }

    private void setListViewHeight(ArrayList<String> clientConnectedListWithoutMe, ListView<String> listView) {
        listView.setPrefHeight((clientConnectedListWithoutMe.size()+4)*17);
    }

    public void clientWantsToPlayAGameWith(){
        opponentChoosen = clientConnectedListView.getSelectionModel().getSelectedItem();
        System.out.println(opponentChoosen + " -> opponentChoosen");
        main.clientWantsToPlayAGameWith(opponentChoosen);
    }

    public void ballMovement(Event event){
        utilities.playSomeSound(BALLSHOT_SOUND);
        ballImage.setScaleX(1);
        ballImage.setScaleY(1);
        ImageView imageChoosen = (ImageView) event.getTarget();
        Path path = new Path();
        path.getElements().add(new MoveTo(ballImage.getFitWidth() / 2, ballImage.getFitHeight() / 2));
        if (imageChoosen.getId().equals(IMAGE_PLAYAGAME) || imageChoosen.getId().equals(IMAGE_RATING)) {
            ArcTo arcTo = new ArcTo(50, 50, 100, imageChoosen.getLayoutX() - ballImage.getLayoutX() + anchorPane.getLayoutX() + imageChoosen.getFitWidth() / 2, imageChoosen.getLayoutY() + -ballImage.getLayoutY() + anchorPane.getLayoutY() + imageChoosen.getFitHeight() / 2, false, false);
            path.getElements().add(arcTo);
        } else {
            LineTo lineTo = new LineTo(imageChoosen.getLayoutX() - ballImage.getLayoutX() + anchorPane.getLayoutX() + imageChoosen.getFitWidth()/2, imageChoosen.getLayoutY() + - ballImage.getLayoutY() + anchorPane.getLayoutY() + imageChoosen.getFitHeight()/2);
            path.getElements().add(lineTo);
        }
        selectionOfClient(imageChoosen.getId());
        path.setStrokeWidth(1);
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
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        goalKeeper.setImage(new Image("/sample/Client/ClientImage/GoalKeeperStandBy.png"));;
                    }
                },
                2000
        );
    }

    // TODO: 25/01/2016 fare spostamento portiere
    private void selectionOfClient(String idOfImage) {
        switch (idOfImage) {
            case (IMAGE_RATING):{
                utilities.playSomeSound(GOAL_SOUND);
                seeImageContext(ratingBox, anchorPane.lookup(IMAGE_RATING));
                goalKeeper.setImage(new Image("/sample/Client/ClientImage/GoalKeeperJumpingRight.png"));
                break;
            }
            case (IMAGE_PLAYAGAME):{
                utilities.playSomeSound(GOAL_SOUND);
                seeImageContext(clientConnectedListView, anchorPane.lookup(IMAGE_PLAYAGAME));
                seeImageContext(clientInGameListView, anchorPane.lookup(IMAGE_PLAYAGAME));
                goalKeeper.setImage(new Image("/sample/Client/ClientImage/GoalKeeperJumpingLeft.png"));
                break;
            }
            case (IMAGE_WORLDSCOREBOARD):{
                utilities.playSomeSound(GOAL_SOUND);
                seeImageContext(worldScoreboardListView, anchorPane.lookup(IMAGE_WORLDSCOREBOARD));
                goalKeeper.setImage(new Image("/sample/Client/ClientImage/GoalKeeperJumpingRight.png"));
                break;
            }
            case (IMAGE_PERSONALSCOREBOARD):{
                utilities.playSomeSound(GOAL_SOUND);
                seeImageContext(personalScoreboardWonListView, anchorPane.lookup(IMAGE_PERSONALSCOREBOARD));
                seeImageContext(personalScoreboardLostListView, anchorPane.lookup(IMAGE_PERSONALSCOREBOARD));
                goalKeeper.setImage(new Image("/sample/Client/ClientImage/GoalKeeperJumpingLeft.png"));
                break;
            }
        }
    }

    private void seeImageContext(Node node, Node nodeSelected) {
        utilities.fadeTransitionEffect(node, 0, 1, 1000);
        node.setVisible(true);
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        disableImageContext(node, nodeSelected);
                    }
                },
                3000
        );
    }

    private void disableImageContext(Node nodeToHide, Node nodeToScreen){
        utilities.fadeTransitionEffect(nodeToHide, 1, 0, 3000);
        utilities.fadeTransitionEffect(nodeToScreen, 0.3f, 1, 1000);
    }

    //metodo che permette di comunicare il voto dell'applicazione
    public void ratingGame() {
        clientRating = ratingBox.getRating();
        System.out.println(clientRating + " -> CLIENT RATING");
        main.clientWantsToSendRating(clientRating);
    }

    public void playGameRequest(ArrayList<String> userAndNumber) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ACCETTA LA SFIDA");
        alert.setHeaderText(null);
        alert.setContentText("Il giocatore " + userAndNumber.get(0) + " ti ha inviato una richiesta di gioco.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            main.sendServerOkForPlaying(userAndNumber);
        } else {
            main.sendServerNoForPlaying(userAndNumber);
        }
    }

}