package sample.Utilities.Class;

import static sample.Utilities.Class.ConstantCodes.*;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Utilities {

    public void playSomeSound(String path){
        try{
            Media buttonSound = new Media(getClass().getResource(path).toString());
            MediaPlayer mediaPlayer = new MediaPlayer(buttonSound);
            mediaPlayer.play();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

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
        /*
        scaleTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imageView.setVisible(false);
            }
        });
         */
    }

    public Utilities() {
    }


}
