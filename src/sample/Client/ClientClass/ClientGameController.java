package sample.Client.ClientClass;

import static sample.Utilities.Class.ConstantCodes.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.controlsfx.control.MaskerPane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.net.URL;
import java.util.*;

public class ClientGameController implements Initializable {

    private ClientMain main;
    private Scene gamingScene;
    private String imagePath;

    @FXML ImageView stickerImage;
    @FXML ImageView myStickerImage;
    @FXML JFXToggleButton enableChangingSticker;
    @FXML Button buttonQueryTest;
    @FXML JFXComboBox<String> hairComboBox;
    @FXML JFXComboBox<String> beardComboBox;
    @FXML JFXComboBox<String> faceComboBox;
    @FXML JFXComboBox<String> accessoriesComboBox;
    @FXML JFXComboBox<String> informationComboBox;
    @FXML JFXTextField settingQuestionForSticker;
    @FXML MaskerPane maskerPaneWaitingOtherPlayerChoice;

    public static final ObservableList hairPossibility = FXCollections.observableArrayList();
    public static final ObservableList beardPossibility = FXCollections.observableArrayList();
    public static final ObservableList facePossibility = FXCollections.observableArrayList();
    public static final ObservableList accessoriesPossibility = FXCollections.observableArrayList();
    public static final ObservableList informationPossibility = FXCollections.observableArrayList();

    AutoCompletionBinding<String> autoCompletionBinding;
    String[] questionCanBeChoosen = {};
    Set<String> questionCanBeChoosenArray = new HashSet<>(Arrays.asList(questionCanBeChoosen));
    ArrayList<String> questionToSendToServer = new ArrayList<>();

    String firstParameter;
    String secondParameter;

    public String getFirstParameter() {
        return firstParameter;
    }

    public void setFirstParameter(String firstParameter) {
        this.firstParameter = firstParameter;
    }

    public String getSecondParameter() {
        return secondParameter;
    }

    public void setSecondParameter(String secondParameter) {
        this.secondParameter = secondParameter;
    }

    public String getImagePath() {
        return imagePath;
    }

    //metodo che inizializza a false/true le cose che non si dovranno o si dovranno vedere
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hairPossibility.addAll(lenghtCB, colorCB, typeCB);
        beardPossibility.addAll(lenghtCB, colorCB, typeCB);
        facePossibility.addAll(eyesColorCB, noseDimensionCB, smileCB, complexionCB);
        accessoriesPossibility.addAll(earringsCB, glassesCB, headbandCB, moleCB, frecklesCB);
        informationPossibility.addAll(nationalShirtCB, continentCB, championshipCB, captainBandCB);
        hairComboBox.setItems(hairPossibility);
        beardComboBox.setItems(beardPossibility);
        faceComboBox.setItems(facePossibility);
        accessoriesComboBox.setItems(accessoriesPossibility);
        informationComboBox.setItems(informationPossibility);
        comboBoxInitialization();
        maskerPaneWaitingOtherPlayerChoice.setVisible(false);
    }

    public void clientWantsToQuery(){
        settingQuestionForSticker.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER){
                int indexToTakeSqlParameter = -1;
                if (questionCanBeChoosenArray.contains(settingQuestionForSticker.getText())){
                    ArrayList<String> temporaryArrayFromList = new ArrayList<String>();
                    temporaryArrayFromList.addAll(questionCanBeChoosenArray);
                    for (int i = 0; i < questionCanBeChoosenArray.size(); i++){
                        if (temporaryArrayFromList.get(i).equals(settingQuestionForSticker.getText())){
                            indexToTakeSqlParameter = i;
                        }
                    }
                    if (indexToTakeSqlParameter != -1){
                        secondParameter = questionToSendToServer.get(indexToTakeSqlParameter);
                        main.clientWantsToQuery();
                    } else {
                    }
                } else {
                    System.out.println("Errore nella scrittura della query");
                    //NOTIFICA
                }
            }
        });
    }

    public void reinitializeComboBox(){
            hairComboBox.setValue(null);
            beardComboBox.setValue(null);
            faceComboBox.setValue(null);
            accessoriesComboBox.setValue(null);
            informationComboBox.setValue(null);
            settingQuestionForSticker.setText(null);
            settingQuestionForSticker.clear();
            questionCanBeChoosenArray.clear();
            questionToSendToServer.clear();

    }

    private void comboBoxInitialization() {
        hairComboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                switch (hairPossibility.get(observable.getValue().intValue()).toString()){
                    case (lenghtCB):{
                        questionCanBeChoosenArray.clear();
                        questionCanBeChoosenArray.addAll(Arrays.asList("Ha i capelli lunghi?", "Ha i capelli corti?", "E' pelato?"));
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, questionCanBeChoosenArray);
                        firstParameter = hairLenght;
                        questionToSendToServer.clear();
                        questionToSendToServer.addAll(Arrays.asList("long", "short", "bald"));
                        break;
                    }
                    case (colorCB):{
                        questionCanBeChoosenArray.clear();
                        questionCanBeChoosenArray.addAll(Arrays.asList("Ha i capelli scuri?", "Ha i capelli chiari?"));
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, questionCanBeChoosenArray);
                        firstParameter = hairColorBrown;
                        questionToSendToServer.clear();
                        questionToSendToServer.addAll(Arrays.asList("true", "false"));
                        break;
                    }
                    case (typeCB):{
                        questionCanBeChoosenArray.clear();
                        questionCanBeChoosenArray.addAll(Arrays.asList("Ha i capelli mossi?", "Ha i capelli lisci?"));
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, questionCanBeChoosenArray);
                        firstParameter = hairTypeStraight;
                        questionToSendToServer.clear();
                        questionToSendToServer.addAll(Arrays.asList("true", "false"));
                        break;
                    }
                }
            }
        });

        beardComboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                switch (beardPossibility.get(observable.getValue().intValue()).toString()){
                    case (lenghtCB):{
                        questionCanBeChoosenArray.clear();
                        questionCanBeChoosenArray.addAll(Arrays.asList("Ha la barba lunga?", "Ha la barba corta?", "E' rasato?"));
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, questionCanBeChoosenArray);
                        firstParameter = beardLenght;
                        questionToSendToServer.clear();
                        questionToSendToServer.addAll(Arrays.asList("long", "short", "shaved"));
                        break;
                    }
                    case (colorCB):{
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, "Ha la barba scura?", "Ha la barba chiara?");
                        break;
                    }
                    case (typeCB):{
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, "Ha i baffi?", "Ha il pizzetto?", "Ha la barba intera");
                        break;
                    }
                }
            }
        });

        faceComboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                switch (facePossibility.get(observable.getValue().intValue()).toString()){
                    case (eyesColorCB):{
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, "Ha gli occhi scuri?", "Ha gli occhi chiari?");
                        settingQuestionForSticker.getText();
                        break;
                    }
                    case (noseDimensionCB):{
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, "Ha il naso grosso?", "Ha il naso piccolo?");
                        break;
                    }
                    case (smileCB):{
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, "Sta sorridendo?", "E' serio?");
                        break;
                    }
                    case (complexionCB): {
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, "E' chiaro di carnagione?", "E' scuro di carnagione?");
                        break;
                    }
                }
            }
        });

        accessoriesComboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                switch (accessoriesPossibility.get(observable.getValue().intValue()).toString()){
                    case (earringsCB):{
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, "Porta almeno un orecchino?", "Non indossa orecchini?");
                        break;
                    }
                    case (glassesCB):{
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, "Porta gli occhiali?", "Non indossa gli occhiali?");
                        break;
                    }
                    case (headbandCB):{
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, "Indossa una fascia in testa?", "Non porta una fascia in testa?");
                        break;
                    }
                    case (moleCB):{
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, "Ha almeno un neo?", "Non ha nei?");
                        break;
                    }
                    case (frecklesCB):{
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, "Ha le lentiggini?", "Non ha le lentiggini?");
                        break;
                    }
                }
            }
        });

        informationComboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                switch (informationPossibility.get(observable.getValue().intValue()).toString()){
                    case (nationalShirtCB):{
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, "Indossa la maglia della nazionale?", "Indossa la maglia di un club?");
                        break;
                    }
                    case (continentCB):{
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, "E' europea la nazionale in cui ha giocato?", "E' americana la nazionale in cui ha giocato?", "E' asiatica la nazionale in cui ha giocato?");
                        break;
                    }
                    case (championshipCB):{
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, "E' della Serie A il club con cui giocava?", "E' della Premier League il club con cui giocava?", "E' della Ligue il club con cui giocava?", "E' della BBVA il club con cui giocava?", "E' della Liga Argentina il club con cui giocava?");
                        break;
                    }
                    case (captainBandCB):{
                        TextFields.bindAutoCompletion((TextField) settingQuestionForSticker, "Indossa la fascia da capitano?", "Non indossa la fascia da capitano?");
                        break;
                    }
                }
            }
        });
    }

    //metodo che serve per far conoscere main e controller
    public void setMain(ClientMain main, Scene gamingScene) {
        this.main = main;
        this.gamingScene = gamingScene;
    }

    //metodo che permettere di scegliere il proprio Sticker con cui giocare
    public void chooseYourSticker(Event event) {
        stickerImage = (ImageView) event.getTarget();
        myStickerImage.setImage(stickerImage.getImage());
        if (!enableChangingSticker.isSelected()) {
            stickerImage = (ImageView) event.getTarget();
            myStickerImage.setImage(stickerImage.getImage());
        } else {
            imagePath = stickerImage.getImage().impl_getUrl();//salvo il path
            main.settingMySticker();
            System.out.println("Hai scelto il personaggio");
            enableChangingSticker.setDisable(true);//disabilito la possibilità di riattivare il button
        }
    }

    //metodo che serve per rimuovere gli sticker dalla mio schermata del client
    public void modifySticker(ArrayList<String> newStickers) {
        for (int i = 0; i < newStickers.size(); i++) {
            ImageView stickerToBeRemovedImage = (ImageView) gamingScene.lookup("#" + newStickers.get(i));
            if (stickerToBeRemovedImage != null) {
                scaleTransition(stickerToBeRemovedImage);
            }
        }
        disableForChangingRound(true);
    }

    //metodo stilistico che serve per la transizione zoomOut degli stickers
    public void scaleTransition(ImageView imageView) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), imageView);
        scaleTransition.setCycleCount(1);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
        scaleTransition.setFromX(imageView.getScaleX());
        scaleTransition.setFromY(imageView.getScaleY());
        scaleTransition.setToX(0);
        scaleTransition.setToY(0);
        scaleTransition.playFromStart();
        scaleTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imageView.setVisible(false);
            }
        });
    }

    public void readyToAbilitateClientScreen() {
        disableForChangingRound(false);
        //settingQuestionForSticker.setDisable(false);
    }

    public void abilitateMaskerPane() {
        maskerPaneWaitingOtherPlayerChoice.setVisible(true);
        disableForChangingRound(true);
    }
    public void disableForChangingRound(Boolean bool){
        maskerPaneWaitingOtherPlayerChoice.setVisible(bool);
        settingQuestionForSticker.setDisable(bool);
        hairComboBox.setDisable(bool);
        beardComboBox.setDisable(bool);
        faceComboBox.setDisable(bool);
        accessoriesComboBox.setDisable(bool);
        informationComboBox.setDisable(bool);
    }
}