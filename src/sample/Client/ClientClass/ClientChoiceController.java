package sample.Client.ClientClass;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToggleButton;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import org.controlsfx.control.Rating;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ClientChoiceController implements Initializable {

    ClientMain main;

    @FXML
    AnchorPane anchorPane;
    @FXML ImageView imageSingle;
    @FXML ImageView imageMulti;
    @FXML JFXListView<String> clientConnectedListView;
    @FXML JFXToggleButton temporaryButton;  //TODO eliminarlo
    @FXML ImageView ballImage;
    @FXML
    Rating ratingBox;

    //metodo che inizializza a false/true le cose che non si dovranno o si dovranno vedere
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageMulti.setVisible(false);
        imageSingle.setVisible(false);
        clientConnectedListView.setVisible(false);
    }

    //metodo che serve per far conoscere main e controller
    public void setMain(ClientMain main) {
        this.main = main;
        main.clientWantsClientConnected();
    }

    //metodo che mi mostra quando clicco sul Play le modalità di gioco
    public void onClickOnPlayImage(){
        imageSingle.setVisible(true);
        imageMulti.setVisible(true);
        clientConnectedListView.setVisible(true);

    }

    //metodo che collega Choice screen e Game screen
    public void continueOnGameScreen(){
        main.continueOnGameScreen();
    }

    public void displayClientConnected(ArrayList<String> clientConnectedList) {
        ArrayList<String> clientConnectedListWithoutMe = new ArrayList<>();
        for (int cont = 0; cont < clientConnectedList.size(); cont++){
            if (!clientConnectedList.get(cont).equals(main.getUser().getUserUsername())){
                clientConnectedListWithoutMe.add(clientConnectedList.get(cont));
            }
        }
        ObservableList<String> clientConnectedObs = FXCollections.observableArrayList(clientConnectedList);
        ObservableList<String> clientConnectedObsWithoutMe = FXCollections.observableArrayList(clientConnectedListWithoutMe);
        clientConnectedListView.setItems(clientConnectedObsWithoutMe);
    }

    public void ballMovement(Event event){
        ballImage.setScaleX(1);
        ballImage.setScaleY(1);
        ImageView imageChoosen = (ImageView) event.getTarget();
        Path path = new Path();
        path.getElements().add(new MoveTo(ballImage.getFitWidth() / 2, ballImage.getFitHeight() / 2));
        if (imageChoosen.getId().equals("imagePlayAGame") || imageChoosen.getId().equals("imageRating")) {
            ArcTo arcTo = new ArcTo(50, 50, 100, imageChoosen.getLayoutX() - ballImage.getLayoutX() + anchorPane.getLayoutX() + imageChoosen.getFitWidth() / 2, imageChoosen.getLayoutY() + -ballImage.getLayoutY() + anchorPane.getLayoutY() + imageChoosen.getFitHeight() / 2, false, false);
            path.getElements().add(arcTo);
            if (imageChoosen.getId().equals("imageRating")){
                ratingGame();
            } else {
                fadeTransitionEffect(imageSingle, 0, 1, 1000);
                fadeTransitionEffect(imageMulti, 0, 1, 1000);
            }
        } else {
            if (imageChoosen.getId().equals("imagePersonalScoreboard") || imageChoosen.getId().equals("imageWorldScoreboard")){
                LineTo lineTo = new LineTo(imageChoosen.getLayoutX() - ballImage.getLayoutX() + anchorPane.getLayoutX() + imageChoosen.getFitWidth()/2, imageChoosen.getLayoutY() + - ballImage.getLayoutY() + anchorPane.getLayoutY() + imageChoosen.getFitHeight()/2);
                path.getElements().add(lineTo);
            }
        }
        path.setStrokeWidth(1);
        final PathTransition pathTransition = PathTransitionBuilder.create()
                .node(ballImage)
                .path(path)
                .duration(Duration.millis(1000))
                .orientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT)
                .cycleCount(1)
                .build();
        pathTransition.playFromStart();
        fadeTransitionEffect(imageChoosen, 1, 0.3f, 1000);
        scaleTransition(ballImage, 0.5f, 0.5f, 1000);
    }

    private void ratingGame() {
        ratingBox.getRating();

    }

    //metodo per effetto fade
    public void fadeTransitionEffect(Node nodeToEffect, float fromValue, float toValue, int duration){
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), nodeToEffect);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }

    //metodo stilistico che serve per la transizione zoomOut degli stickers
    public void scaleTransition(Node node, float toValueX, float toValueY, int duration) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(duration), node);
        scaleTransition.setCycleCount(1);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
        scaleTransition.setFromX(node.getScaleX());
        scaleTransition.setFromY(node.getScaleY());
        scaleTransition.setToX(toValueX);
        scaleTransition.setToY(toValueY);
        scaleTransition.playFromStart();
    }

}