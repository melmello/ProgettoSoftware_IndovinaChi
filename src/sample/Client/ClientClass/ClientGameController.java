package sample.Client.ClientClass;

import static sample.Utilities.Class.ConstantCodes.*;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.controlsfx.control.MaskerPane;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import sample.Utilities.Class.Utilities;
import java.net.URL;
import java.util.*;

public class ClientGameController implements Initializable {

    private ClientMain main;
    private Scene gamingScene;
    private String imagePath;
    private String firstParameter;
    private String secondParameter;
    private Utilities utilities = new Utilities();
    private String[] questionCanBeChoosen = {};
    private Set<String> questionCanBeChoosenArray = new HashSet<>(Arrays.asList(questionCanBeChoosen));
    private ArrayList<String> questionToSendToServer = new ArrayList<>();

    public static final ObservableList hairPossibility = FXCollections.observableArrayList();
    public static final ObservableList beardPossibility = FXCollections.observableArrayList();
    public static final ObservableList facePossibility = FXCollections.observableArrayList();
    public static final ObservableList accessoriesPossibility = FXCollections.observableArrayList();
    public static final ObservableList informationPossibility = FXCollections.observableArrayList();

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

    //metodo che inizializza a false/true le cose che non si dovranno o si dovranno vedere
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hairPossibility.addAll(LENGHT_COMBO_BOX, COLOR_COMBO_BOX, TYPE_COMBO_BOX);
        beardPossibility.addAll(LENGHT_COMBO_BOX, COLOR_COMBO_BOX, TYPE_COMBO_BOX);
        facePossibility.addAll(EYESCOLOR_COMBO_BOX, NOSEDIMENSION_COMBO_BOX, SMILE_COMBO_BOX, COMPLEXION_COMBO_BOX);
        accessoriesPossibility.addAll(EARRINGS_COMBO_BOX, GLASSES_COMBO_BOX, HEADBAND_COMBO_BOX, MOLE_COMBO_BOX, FRECKLES_COMBO_BOX);
        informationPossibility.addAll(NATIONALSHIRT_COMBO_BOX, CONTINENT_COMBO_BOX, CHAMPIONSHIP_COMBO_BOX, CAPTAINBAND_COMBO_BOX);
        hairComboBox.setItems(hairPossibility);
        beardComboBox.setItems(beardPossibility);
        faceComboBox.setItems(facePossibility);
        accessoriesComboBox.setItems(accessoriesPossibility);
        informationComboBox.setItems(informationPossibility);
        comboBoxInitialization();
        maskerPaneWaitingOtherPlayerChoice.setVisible(false);
    }

    //metodo che serve per far conoscere main e controller
    public void setMain(ClientMain main, Scene gamingScene) {
        this.main = main;
        this.gamingScene = gamingScene;
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
                        case (LENGHT_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha i capelli lunghi?", "Ha i capelli corti?", "E' pelato?"));
                            questionToSendToServer.addAll(Arrays.asList(LONGANSWER_FOR_QUERY, SHORTANSWER_FOR_QUERY, BALDANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = HAIRLENGTH_FOR_QUERY;
                            break;
                        }
                        case (COLOR_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha i capelli scuri?", "Ha i capelli chiari?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = HAIRCOLORBROWN_FOR_QUERY;
                            break;
                        }
                        case (TYPE_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha i capelli mossi?", "Ha i capelli lisci?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = HAIRTYPESTRAIGHT_FOR_QUERY;
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
                        case (LENGHT_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha la barba lunga?", "Ha la barba corta?", "E' rasato?"));
                            questionToSendToServer.addAll(Arrays.asList(LONGANSWER_FOR_QUERY, SHORTANSWER_FOR_QUERY, SHAVEDANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = BEARDLENGHT_FOR_QUERY;
                            break;
                        }
                        case (COLOR_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha la barba scura?", "Ha la barba chiara?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = BEARDCOLORBROWN_FOR_QUERY;
                            break;
                        }
                        case (TYPE_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha il pizzetto?", "Ha i baffi?", "E' completamente barbuto?"));
                            questionToSendToServer.addAll(Arrays.asList(GOATEEANSWER_FOR_QUERY, MUSTACHEANSWER_FOR_QUERY, WHOLEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = BEARDTYPE_FOR_QUERY;
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
                        case (EYESCOLOR_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha gli occhi scuri?", "Ha gli occhi chiari?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = EYESCOLORBROWN_FOR_QUERY;
                            break;
                        }
                        case (NOSEDIMENSION_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha il naso grosso?", "Ha il naso piccolo?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = NOSEDIMENSIONBIG_FOR_QUERY;
                            break;
                        }
                        case (SMILE_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Sta sorridendo?", "E' serio?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = SMILE_FOR_QUERY;
                            break;
                        }
                        case (COMPLEXION_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("E' scuro di carnagione?", "E' chiaro di carnagione?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = COMPLEXIONBROWN_FOR_QUERY;
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
                        case (EARRINGS_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Porta almeno un orecchino?", "Non indossa orecchini?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = EARRINGS_FOR_QUERY;
                            break;
                        }
                        case (GLASSES_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Porta gli occhiali?", "Non indossa gli occhiali?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = GLASSES_FOR_QUERY;
                            break;
                        }
                        case (HEADBAND_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Indossa una fascia in testa?", "Non porta una fascia in testa?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = HEADBAND_FOR_QUERY;
                            break;
                        }
                        case (MOLE_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha almeno un neo?", "Non ha nei?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = MOLE_FOR_QUERY;
                            break;
                        }
                        case (FRECKLES_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Ha le lentiggini?", "Non ha le lentiggini?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = FRECKLES_FOR_QUERY;
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
                        case (NATIONALSHIRT_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Indossa la maglia della nazionale?", "Indossa la maglia di un club?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = NATIONALSHIRT_FOR_QUERY;
                            break;
                        }
                        case (CONTINENT_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("E' europea la nazionale in cui ha giocato?", "E' americana la nazionale in cui ha giocato?", "E' asiatica la nazionale in cui ha giocato?"));
                            questionToSendToServer.addAll(Arrays.asList(EUROPEANSWER_FOR_QUERY, AMERICAANSWER_FOR_QUERY, ASIAANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = CONTINENT_FOR_QUERY;
                            break;
                        }
                        case (CHAMPIONSHIP_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("E' della Serie A il club con cui giocava?", "E' della Premier League il club con cui giocava?", "E' della Ligue il club con cui giocava?", "E' della BBVA il club con cui giocava?", "E' della Liga Argentina il club con cui giocava?"));
                            questionToSendToServer.addAll(Arrays.asList(SERIEAANSWER_FOR_QUERY, PREMIERANSWER_FOR_QUERY, LIGUEANSWER_FOR_QUERY, BBVAANSWER_FOR_QUERY, LIGAANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = CHAMPIONSHIP_FOR_QUERY;
                            break;
                        }
                        case (CAPTAINBAND_COMBO_BOX): {
                            questionCanBeChoosenArray.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosenArray.addAll(Arrays.asList("Indossa la fascia da capitano?", "Non indossa la fascia da capitano?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosenArray));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosenArray);
                            firstParameter = CAPTAINBAND_FOR_QUERY;
                            break;
                        }
                    }
                }
            }
        });
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

    private void setListViewHeight(JFXListView<String> questionThatCouldBeChoosen, Set<String> questionCanBeChoosenArray) {
        questionThatCouldBeChoosen.setPrefHeight((questionCanBeChoosenArray.size()+4)*17);
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