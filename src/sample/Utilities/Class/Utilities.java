package sample.Utilities.Class;

/** @author Giulio Melloni
 * Classe utilizzata per "l'effettistica" del progetto in modo tale che possa essere chiamata ovunque.
 * Troviamo infatti metodi stilistici che portano oggetti a fare una transizione come il FADEIN e FADEOUT, oppure la riproduzione di un suono.
 */

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Utilities {

    /** Costruttore vuoto di Utilities.
     */
    public Utilities() {
    }

    /** Metodo che riproduce un determinato suono
     * @param path è il path del suono da riprodurre.
     */
    public void playSomeSound(String path){
        try{
            Media buttonSound = new Media(getClass().getResource(path).toString());
            MediaPlayer mediaPlayer = new MediaPlayer(buttonSound);
            mediaPlayer.play();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /** Transizione che mi fermette di fare FadeIn FadeOut come effetto giocando sulla trasparenza.
     * @param nodeToEffect è il node su cui far l'effeto.
     * @param fromValue valore di inizio.
     * @param toValue valore di fine.
     * @param duration durata della transizione.
     */
    public void fadeTransitionEffect(Node nodeToEffect, float fromValue, float toValue, int duration){
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), nodeToEffect);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }

    //

    /** Metodo stilistico che serve per la transizione zoomOut degli stickers
     * @param node è il nodo su cui far l'effetto.
     * @param toValueX valore di fine della x.
     * @param toValueY valore di fine della y.
     * @param duration durata della transizione.
     */
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

    /** Metodo stilistico che serve per il pulse degli stickers (simula una pulsazione).
     * @param node è il nodo su cui far l'effetto.
     * @param toValueX valore di fine della x.
     * @param toValueY valore di fine della y.
     * @return scaleTransition (così che io possa fermarla siccome la durata è infinita)
     */
    public Animation scaleTransitionEffectCycle (Node node, float toValueX, float toValueY){
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), node);
        scaleTransition.setCycleCount(Animation.INDEFINITE);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
        scaleTransition.setFromX(node.getScaleX());
        scaleTransition.setFromY(node.getScaleY());
        scaleTransition.setToX(toValueX);
        scaleTransition.setToY(toValueY);
        scaleTransition.playFromStart();
        return scaleTransition;
    }

}
