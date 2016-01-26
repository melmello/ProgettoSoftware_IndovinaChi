package sample.Utilities.Class;

import javafx.scene.Node;
import java.util.TimerTask;

public class ExtendTimerTask extends TimerTask{

    private Utilities utilities = new Utilities();
    private final boolean goOn;
    private final Node node;

    public ExtendTimerTask(boolean goOn, Node node){
        this.goOn = goOn;
        this.node = node;
    }

    public void run() {
        if (goOn) {
            utilities.fadeTransitionEffect(node, 1, 0, 3000);
        }
    }

}