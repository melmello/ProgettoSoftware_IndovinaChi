package sample.Utilities.Class;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Utilities {

    public void playSomeSound(){
        try{
            Media buttonSound = new Media(getClass().getResource("/sample/Utilities/Sound/ButtonClick.wav").toString());
            MediaPlayer mediaPlayer = new MediaPlayer(buttonSound);
            mediaPlayer.play();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Utilities() {
    }
}
