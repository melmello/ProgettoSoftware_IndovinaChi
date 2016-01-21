package sample.Client.ClientClass;

import static sample.Utilities.Class.ConstantCodes.*;
import static sample.Utilities.Class.Utilities.*;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import org.controlsfx.control.MaskerPane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
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
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import sample.Utilities.Class.Utilities;

import java.net.URL;
import java.util.*;

public class ClientGameController implements Initializable {

    private ClientMain main;
    private Scene gamingScene;
    private String imagePath;
    private Utilities utilities = new Utilities();


    @FXML ImageView stickerImage;
    @FXML ImageView myStickerImage;
    @FXML JFXToggleButton enableChangingSticker;
    @FXML JFXComboBox<String> hairComboBox;
    @FXML JFXComboBox<String> beardComboBox;
    @FXML JFXComboBox<String> faceComboBox;
    @FXML JFXComboBox<String> accessoriesComboBox;
    @FXML JFXComboBox<String> informationComboBox;
    @FXML MaskerPane maskerPaneWaitingOtherPlayerChoice;
    @FXML JFXListView<String> questionThatCouldBeChoosen;

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

    public void clientWantsToQuery() {
        int indexToTakeSqlParameter = -1;
        if (questionCanBeChoosenArray.contains(questionThatCouldBeChoosen.getSelectionModel().getSelectedItems())) {
            ArrayList<String> temporaryArrayFromList = new ArrayList<String>();
            temporaryArrayFromList.addAll(questionCanBeChoosenArray);
            for (int i = 0; i < questionCanBeChoosenArray.size(); i++) {
                if (temporaryArrayFromList.get(i).equals(questionThatCouldBeChoosen.getSelectionModel().getSelectedItems())) {
                    indexToTakeSqlParameter = i;
                }
            }
            if (indexToTakeSqlParameter != -1) {
                secondParameter = questionToSendToServer.get(indexToTakeSqlParameter);
                main.clientWantsToQuery();
            } else {
            }
        } else {
            System.out.println("ERRORE");
        }
    }


    public void reinitializeComboBox(){
        hairComboBox.setValue(null);
        beardComboBox.setValue(null);
        faceComboBox.setValue(null);
        accessoriesComboBox.setValue(null);
        informationComboBox.setValue(null);
        questionCanBeChoosenArray.clear();
        questionToSendToServer.clear();
    }

    private void comboBoxInitialization() {
        hairComboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (observable.getValue().intValue() != -1) {
                    switch (hairPossibility.get(observable.getValue().intValue()).toString()) {
                        case (lenghtCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha i capelli lunghi?", "Ha i capelli corti?", "E' pelato?"));
                            questionToSendToServer.addAll(Arrays.asList(longAnswer, shortAnswer, baldAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = hairLenght;
                            break;
                        }
                        case (colorCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha i capelli scuri?", "Ha i capelli chiari?"));
                            questionToSendToServer.addAll(Arrays.asList(trueAnswer, falseAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = hairColorBrown;
                            break;
                        }
                        case (typeCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha i capelli mossi?", "Ha i capelli lisci?"));
                            questionToSendToServer.addAll(Arrays.asList(trueAnswer, falseAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = hairTypeStraight;
                            break;
                        }
                    }
                }
            }
        });
        beardComboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (observable.getValue().intValue() != -1) {
                    switch (beardPossibility.get(observable.getValue().intValue()).toString()) {
                        case (lenghtCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha la barba lunga?", "Ha la barba corta?", "E' rasato?"));
                            questionToSendToServer.addAll(Arrays.asList(longAnswer, shortAnswer, shavedAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = beardLenght;
                            break;
                        }
                        case (colorCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha la barba scura?", "Ha la barba chiara?"));
                            questionToSendToServer.addAll(Arrays.asList(trueAnswer, falseAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = beardColorBrown;
                            break;
                        }
                        case (typeCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha il pizzetto?", "Ha i baffi?", "E' completamente barbuto?"));
                            questionToSendToServer.addAll(Arrays.asList(goateeAnswer, mustacheAnswer, wholeAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = beardType;
                            break;
                        }
                    }
                }
            }
        });

        faceComboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (observable.getValue().intValue() != -1) {
                    switch (facePossibility.get(observable.getValue().intValue()).toString()) {
                        case (eyesColorCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha gli occhi scuri?", "Ha gli occhi chiari?"));
                            questionToSendToServer.addAll(Arrays.asList(trueAnswer, falseAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = eyesColorBrown;
                            break;
                        }
                        case (noseDimensionCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha il naso grosso?", "Ha il naso piccolo?"));
                            questionToSendToServer.addAll(Arrays.asList(trueAnswer, falseAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = noseDimensionBig;
                            break;
                        }
                        case (smileCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Sta sorridendo?", "E' serio?"));
                            questionToSendToServer.addAll(Arrays.asList(trueAnswer, falseAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = smile;
                            break;
                        }
                        case (complexionCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("E' scuro di carnagione?", "E' chiaro di carnagione?"));
                            questionToSendToServer.addAll(Arrays.asList(trueAnswer, falseAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = complexionBrown;
                            break;
                        }
                    }
                }
            }
        });

        accessoriesComboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (observable.getValue().intValue() != -1) {
                    switch (accessoriesPossibility.get(observable.getValue().intValue()).toString()) {
                        case (earringsCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Porta almeno un orecchino?", "Non indossa orecchini?"));
                            questionToSendToServer.addAll(Arrays.asList(trueAnswer, falseAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = earrings;
                            break;
                        }
                        case (glassesCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Porta gli occhiali?", "Non indossa gli occhiali?"));
                            questionToSendToServer.addAll(Arrays.asList(trueAnswer, falseAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = glasses;
                            break;
                        }
                        case (headbandCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Indossa una fascia in testa?", "Non porta una fascia in testa?"));
                            questionToSendToServer.addAll(Arrays.asList(trueAnswer, falseAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = headband;
                            break;
                        }
                        case (moleCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha almeno un neo?", "Non ha nei?"));
                            questionToSendToServer.addAll(Arrays.asList(trueAnswer, falseAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = mole;
                            break;
                        }
                        case (frecklesCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha le lentiggini?", "Non ha le lentiggini?"));
                            questionToSendToServer.addAll(Arrays.asList(trueAnswer, falseAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = freckles;
                            break;
                        }
                    }
                }
            }
        });

        informationComboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (observable.getValue().intValue() != -1) {
                    switch (informationPossibility.get(observable.getValue().intValue()).toString()) {
                        case (nationalShirtCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Indossa la maglia della nazionale?", "Indossa la maglia di un club?"));
                            questionToSendToServer.addAll(Arrays.asList(trueAnswer, falseAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = nationalShirt;
                            break;
                        }
                        case (continentCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("E' europea la nazionale in cui ha giocato?", "E' americana la nazionale in cui ha giocato?", "E' asiatica la nazionale in cui ha giocato?"));
                            questionToSendToServer.addAll(Arrays.asList(europeAnswer, americaAnswer, asiaAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = continentCB;
                            break;
                        }
                        case (championshipCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("E' della Serie A il club con cui giocava?", "E' della Premier League il club con cui giocava?", "E' della Ligue il club con cui giocava?", "E' della BBVA il club con cui giocava?", "E' della Liga Argentina il club con cui giocava?"));
                            questionToSendToServer.addAll(Arrays.asList(serieaAnswer, premierAnswer, ligueAnswer, bbvaAnswer, ligaAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = championship;
                            break;
                        }
                        case (captainBandCB): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Indossa la fascia da capitano?", "Non indossa la fascia da capitano?"));
                            questionToSendToServer.addAll(Arrays.asList(trueAnswer, falseAnswer));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = captainBand;
                            break;
                        }
                    }
                }
            }
        });
    }

    private void setListViewHeight(JFXListView<String> questionThatCouldBeChoosen, Set<String> questionCanBeChoosenArray) {
        questionThatCouldBeChoosen.setPrefHeight((questionCanBeChoosenArray.size()+4)*17);
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
                utilities.scaleTransition(stickerToBeRemovedImage, 0, 0, 500);
            }
        }
        disableForChangingRound(true);
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
        questionThatCouldBeChoosen.setDisable(bool);
        hairComboBox.setDisable(bool);
        beardComboBox.setDisable(bool);
        faceComboBox.setDisable(bool);
        accessoriesComboBox.setDisable(bool);
        informationComboBox.setDisable(bool);
    }

    public String getFirstParameter() {
        return firstParameter;
    }

    public String getSecondParameter() {
        return secondParameter;
    }

    public String getImagePath() {
        return imagePath;
    }

}