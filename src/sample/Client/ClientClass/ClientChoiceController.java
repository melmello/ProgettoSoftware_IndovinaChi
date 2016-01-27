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
import javafx.scene.control.*;
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
import java.util.*;

public class ClientChoiceController implements Initializable {

    private double clientRating;
    private String opponentChoosen;
    private Utilities utilities = new Utilities();
    private ClientMain main;
    private ImageView oldImage = new ImageView();
    private int timeDuration = 7500;
    private Timer timer = new Timer();
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

    //metodo che inizializza a false/true le cose che non si dovranno o si dovranno vedere
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anchorRating.setVisible(false);
        anchorWorld.setVisible(false);
        anchorPersonal.setVisible(false);
        anchorPlayer.setVisible(false);
        clientConnectedListView.setDisable(false);
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
        if (listView.getHeight() < 300){
            listView.setPrefHeight((clientConnectedListWithoutMe.size()+4)*17);
        } else {
            listView.setPrefHeight((7+4)*17);
        }
    }

    public void clientWantsToPlayAGameWith(){
        opponentChoosen = clientConnectedListView.getSelectionModel().getSelectedItem();
        System.out.println(opponentChoosen + " -> opponentChoosen");
        main.clientWantsToPlayAGameWith(opponentChoosen);
        clientConnectedListView.setDisable(true);
    }

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

    private void seeImageContext(Node node, Node nodeSelected, int duration) {
        utilities.fadeTransitionEffect(node, 0, 1, 1000);
        node.setVisible(true);
        if (oldImage.equals(nodeSelected)){
            duration*=2;
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                utilities.fadeTransitionEffect(node, 1, 0, 3000);
            }
        }, duration);
    }

    //metodo che permette di comunicare il voto dell'applicazione
    public void ratingGame() {
        clientRating = ratingBox.getRating();
        System.out.println(clientRating + " -> CLIENT RATING");
        main.clientWantsToSendRating(clientRating, titleRating.getText(), textRating.getText());
        main.notification("Recensione inviata con successo");
        titleRating.setText("");
        textRating.setText("");
    }

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
        } else if (result.get() == buttonNo){
            main.sendServerNoForPlaying(userAndNumber);
        }
    }

}