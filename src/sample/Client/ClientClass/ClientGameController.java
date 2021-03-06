package sample.Client.ClientClass;

/** @author Giulio Melloni
 * Questa classe è il controller della schermata di gioco. E' questo il ponte tra l'azione del client in gioco e la comunicazione tra client e server. E' qui dove si capisce che sticker è stato scelto, che domanda è stata fatta.
 */

import static sample.Utilities.Class.ConstantCodes.*;
import com.jfoenix.controls.JFXListView;
import javafx.animation.Animation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.MaskerPane;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import sample.Utilities.Class.Utilities;
import java.net.URL;
import java.util.*;

public class ClientGameController implements Initializable {

    private boolean myOrHis = true;
    private boolean isItFirstTimeYouEnter = true;
    private double hSticker;
    private double wSticker;
    private ClientMain main;
    private Scene gamingScene;
    private String imagePath;
    private String firstParameter;
    private String secondParameter;
    private Utilities utilities = new Utilities();
    private ArrayList<String> questionCanBeChoosen = new ArrayList<>();
    private Animation animation;
    private ArrayList<String> questionToSendToServer = new ArrayList<>();
    public static final ObservableList hairPossibility = FXCollections.observableArrayList();
    public static final ObservableList beardPossibility = FXCollections.observableArrayList();
    public static final ObservableList facePossibility = FXCollections.observableArrayList();
    public static final ObservableList accessoriesPossibility = FXCollections.observableArrayList();
    public static final ObservableList informationPossibility = FXCollections.observableArrayList();
    public static final ObservableList questionAsked = FXCollections.observableArrayList();
    @FXML ToolBar toolBar;
    @FXML ImageView stickerImage;
    @FXML ImageView myStickerImage;
    @FXML ImageView hisStickerImage;
    @FXML ImageView targetImage = myStickerImage;
    @FXML Image questionMarkImage = new Image(QUESTION_MARK_PATH);
    @FXML JFXComboBox<String> hairComboBox;
    @FXML JFXComboBox<String> beardComboBox;
    @FXML JFXComboBox<String> faceComboBox;
    @FXML JFXComboBox<String> accessoriesComboBox;
    @FXML JFXComboBox<String> informationComboBox;
    @FXML MaskerPane maskerPaneWaitingOtherPlayerChoice;
    @FXML JFXListView<String> questionThatCouldBeChoosen;
    @FXML JFXListView<String> questionChoosenListView;
    @FXML AnchorPane anchorPane;

    /** Metodo che inizializza a false/true le cose che non si dovranno o si dovranno vedere
     * @param location null se la location non è nota (come in questo caso).
     * @param resources null se il root object non è localizzato (come in questo caso).
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hairPossibility.addAll(LENGTH_COMBO_BOX, COLOR_COMBO_BOX, TYPE_COMBO_BOX);
        beardPossibility.addAll(LENGTH_COMBO_BOX, COLOR_COMBO_BOX, TYPE_COMBO_BOX);
        facePossibility.addAll(EYESCOLOR_COMBO_BOX, NOSEDIMENSION_COMBO_BOX, SMILE_COMBO_BOX, COMPLEXION_COMBO_BOX);
        accessoriesPossibility.addAll(EARRINGS_COMBO_BOX, GLASSES_COMBO_BOX, HEADBAND_COMBO_BOX, MOLE_COMBO_BOX, FRECKLES_COMBO_BOX);
        informationPossibility.addAll(NATIONALSHIRT_COMBO_BOX, CONTINENT_COMBO_BOX, CHAMPIONSHIP_COMBO_BOX, CAPTAINBAND_COMBO_BOX);
        hairComboBox.setItems(hairPossibility);
        beardComboBox.setItems(beardPossibility);
        faceComboBox.setItems(facePossibility);
        accessoriesComboBox.setItems(accessoriesPossibility);
        informationComboBox.setItems(informationPossibility);
        comboBoxInitialization();
        imageInitialization();
        toolBar.setDisable(true);
        questionThatCouldBeChoosen.setDisable(true);
        maskerPaneWaitingOtherPlayerChoice.setVisible(false);
        wSticker = myStickerImage.getFitWidth();
        hSticker = myStickerImage.getFitHeight();
    }

    /** Metodo che serve per collegare main e controller.
     * @param main è l'istanza di ClientMain.
     * @param gamingScene è la scene che mi tornerà utile per il lookUp.
     */
    public void setMain(ClientMain main, Scene gamingScene) {
        this.main = main;
        this.gamingScene = gamingScene;
    }

    /** Metodo chiamato quando si ricarica il foglio fxml per una seconda partita in modo da reinizializzare tutti gli oggetti già presenti.
     */
    public void configureScreen(){
        hairPossibility.clear();
        beardPossibility.clear();
        facePossibility.clear();
        accessoriesPossibility.clear();
        informationPossibility.clear();
        Set<Node> set = anchorPane.lookupAll(".ImageSticker");
        Object[] arrayNode = set.toArray();
        for (int cont = 0; cont < set.size(); cont++) {
            ImageView imageView = (ImageView) arrayNode[cont];
            imageView.setScaleX(1);
            imageView.setScaleY(1);
            imageView.setVisible(true);
        }
        myStickerImage.setImage(questionMarkImage);
        hisStickerImage.setImage(questionMarkImage);
        hisStickerImage.setVisible(true);
        utilities.fadeTransitionEffect(hisStickerImage, 0, 1, 1);
        questionChoosenListView.setItems(null);
        myOrHis = true;
        isItFirstTimeYouEnter = true;
    }

    /** Metodo che serve per comunicare la query scelta dall'utente, aggiungerla alla listview di query fatte e inviarla al ClientMain tradotta da una domanda italiana in firstParameter e secondParameter.
     */
    public void clientWantsToQuery() {
        int indexToTakeSqlParameter = -1;
        System.out.println(questionCanBeChoosen);
        System.out.println(questionThatCouldBeChoosen.getSelectionModel().getSelectedItem());
        if (questionCanBeChoosen.contains(questionThatCouldBeChoosen.getSelectionModel().getSelectedItem())) {
            ArrayList<String> temporaryArrayFromList = new ArrayList<>();
            temporaryArrayFromList.addAll(questionCanBeChoosen);
            for(int cont = 0; cont < questionCanBeChoosen.size(); cont++) {
                if (temporaryArrayFromList.get(cont).equals(questionThatCouldBeChoosen.getSelectionModel().getSelectedItem())) {
                    indexToTakeSqlParameter = cont;
                    questionAsked.add(questionThatCouldBeChoosen.getSelectionModel().getSelectedItem());
                    questionChoosenListView.setItems(questionAsked);
                    if (questionAsked.size() < 6) {
                        questionChoosenListView.setPrefHeight((questionAsked.size() + 4) * 17);
                    } else {
                        questionChoosenListView.setPrefHeight((7 + 4) * 17);
                    }
                }
            }
            if (indexToTakeSqlParameter != -1) {
                secondParameter = questionToSendToServer.get(indexToTakeSqlParameter);
                main.clientWantsToQuery(firstParameter, secondParameter);
            }
        } else {
            System.out.println("ERRORE");
        }
    }

    /** Metodo che reinizializza le comboBox una volta deselezionate oppure passato il turno.
     */
    public void reinitializeComboBox(){
        hairComboBox.setValue(null);
        beardComboBox.setValue(null);
        faceComboBox.setValue(null);
        accessoriesComboBox.setValue(null);
        informationComboBox.setValue(null);
        questionCanBeChoosen.clear();
        questionToSendToServer.clear();
        questionThatCouldBeChoosen.setItems(null);
    }

    /** Metodo chiamato al momento dell'inizializzazione in cui viene settata per ogni immagine l'azione di onDragDetected., onDragDone, onMouseEntered e onMouseExited.
     * Viene chiamato inoltre {@link #changeDragAndDrop(ImageView)}.
     */
    private void imageInitialization() {
        Set<Node> set = anchorPane.lookupAll(".ImageSticker");
        Object[] arrayNode = set.toArray();
        for (int cont = 0; cont < set.size(); cont++) {
            ImageView imageView = (ImageView) arrayNode[cont];
            imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stickerImage = (ImageView) event.getTarget();
                    Dragboard db = stickerImage.startDragAndDrop(TransferMode.ANY);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(stickerImage.getId());
                    db.setContent(content);
                    if (!myOrHis) {
                        animation = utilities.scaleTransitionEffectCycle(hisStickerImage, 1.3f, 1.3f);
                    } else if (myOrHis) {
                        animation = utilities.scaleTransitionEffectCycle(myStickerImage, 1.3f, 1.3f);
                    }
                    event.consume();
                }
            });
            imageView.setOnDragDone(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    animation.stop();
                    myStickerImage.setScaleX(hisStickerImage.getScaleX());
                    myStickerImage.setScaleY(hisStickerImage.getScaleY());
                    hisStickerImage.setScaleX(myStickerImage.getScaleX());
                    hisStickerImage.setScaleX(myStickerImage.getScaleY());
                    hisStickerImage.setScaleX(1);
                    hisStickerImage.setScaleY(1);
                    hisStickerImage.setFitWidth(wSticker);
                    hisStickerImage.setFitHeight(hSticker);
                }
            });
            imageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    zoomInSticker(event);
                }
            });
            imageView.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    zoomOutSticker(event);
                }
            });
        }
        changeDragAndDrop(myStickerImage);
    }

    /** Metodo con cui setto nel immagine target l'onDragOver e l'OnDragDropped.
     * @param imageToTargetOnDrag immagine su cui settare questi metodi. Inizialmente sarà l'immagine del mio sticker, ed una volta scelto sarà lo sticker avversario.
     */
    public void changeDragAndDrop(ImageView imageToTargetOnDrag) {
        imageToTargetOnDrag.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
        imageToTargetOnDrag.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    success = true;
                    if (imageToTargetOnDrag.equals(myStickerImage)) {
                        animation.stop();
                        imageToTargetOnDrag.setScaleX(1);
                        imageToTargetOnDrag.setScaleY(1);
                        imageToTargetOnDrag.setFitWidth(wSticker);
                        imageToTargetOnDrag.setFitHeight(hSticker);
                        imageToTargetOnDrag.setImage(new Image("/sample/Utilities/Stickers/" + db.getString() + ".jpg"));
                        imagePath = stickerImage.getImage().impl_getUrl();//salvo il path
                        main.settingMySticker();
                        disableForChangingRound(true);
                        System.out.println("Hai scelto il personaggio");
                        myOrHis = false;
                        targetImage = hisStickerImage;
                    } else if (imageToTargetOnDrag.equals(hisStickerImage)){
                        animation.stop();
                        imageToTargetOnDrag.setScaleX(1);
                        imageToTargetOnDrag.setScaleY(1);
                        imageToTargetOnDrag.setFitWidth(wSticker);
                        imageToTargetOnDrag.setFitHeight(hSticker);
                        imageToTargetOnDrag.setImage(new Image("/sample/Utilities/Stickers/" + db.getString() + ".jpg"));
                        main.clientWantsToQuerySticker(db.getString());
                        disableForChangingRound(true);
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
    }

    /** Metodo che mi serve per popolare le comboBox con relative domande su cui fare le query, e per ogni caso avere possibili firstParameter e secondParameter per le query differenti e corrispondenti a una relativa domanda in italiano.
     */
    private void comboBoxInitialization() {
        hairComboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (observable.getValue().intValue() != -1) {
                    switch (hairPossibility.get(observable.getValue().intValue()).toString()) {
                        case (LENGTH_COMBO_BOX): {
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("Ha i capelli lunghi?", "Ha i capelli corti?", "E' pelato?"));
                            questionToSendToServer.addAll(Arrays.asList(LONGANSWER_FOR_QUERY, SHORTANSWER_FOR_QUERY, BALDANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
                            firstParameter = HAIRLENGTH_FOR_QUERY;
                            break;
                        }
                        case (COLOR_COMBO_BOX): {
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("Ha i capelli scuri?", "Ha i capelli chiari?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
                            firstParameter = HAIRCOLORBROWN_FOR_QUERY;
                            break;
                        }
                        case (TYPE_COMBO_BOX): {
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("Ha i capelli mossi?", "Ha i capelli lisci?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
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
                        case (LENGTH_COMBO_BOX): {
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("Ha la barba lunga?", "Ha la barba corta?", "E' rasato?"));
                            questionToSendToServer.addAll(Arrays.asList(LONGANSWER_FOR_QUERY, SHORTANSWER_FOR_QUERY, SHAVEDANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
                            firstParameter = BEARDLENGTH_FOR_QUERY;
                            break;
                        }
                        case (COLOR_COMBO_BOX): {
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("Ha la barba scura?", "Ha la barba chiara?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
                            firstParameter = BEARDCOLORBROWN_FOR_QUERY;
                            break;
                        }
                        case (TYPE_COMBO_BOX): {
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("Ha il pizzetto?", "Ha i baffi?", "E' completamente barbuto?"));
                            questionToSendToServer.addAll(Arrays.asList(GOATEEANSWER_FOR_QUERY, MUSTACHEANSWER_FOR_QUERY, WHOLEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
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
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("Ha gli occhi scuri?", "Ha gli occhi chiari?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
                            firstParameter = EYESCOLORBROWN_FOR_QUERY;
                            break;
                        }
                        case (NOSEDIMENSION_COMBO_BOX): {
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("Ha il naso grosso?", "Ha il naso piccolo?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
                            firstParameter = NOSEDIMENSIONBIG_FOR_QUERY;
                            break;
                        }
                        case (SMILE_COMBO_BOX): {
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("Sta sorridendo?", "E' serio?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
                            firstParameter = SMILE_FOR_QUERY;
                            break;
                        }
                        case (COMPLEXION_COMBO_BOX): {
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("E' scuro di carnagione?", "E' chiaro di carnagione?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
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
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("Porta almeno un orecchino?", "Non indossa orecchini?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
                            firstParameter = EARRINGS_FOR_QUERY;
                            break;
                        }
                        case (GLASSES_COMBO_BOX): {
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("Porta gli occhiali?", "Non indossa gli occhiali?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
                            firstParameter = GLASSES_FOR_QUERY;
                            break;
                        }
                        case (HEADBAND_COMBO_BOX): {
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("Indossa una fascia in testa?", "Non porta una fascia in testa?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
                            firstParameter = HEADBAND_FOR_QUERY;
                            break;
                        }
                        case (MOLE_COMBO_BOX): {
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("Ha almeno un neo?", "Non ha nei?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
                            firstParameter = MOLE_FOR_QUERY;
                            break;
                        }
                        case (FRECKLES_COMBO_BOX): {
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("Ha le lentiggini?", "Non ha le lentiggini?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
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
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("Indossa la maglia della nazionale?", "Indossa la maglia di un club?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
                            firstParameter = NATIONALSHIRT_FOR_QUERY;
                            break;
                        }
                        case (CONTINENT_COMBO_BOX): {
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("E' europea la nazionale in cui ha giocato?", "E' americana la nazionale in cui ha giocato?", "E' asiatica la nazionale in cui ha giocato?"));
                            questionToSendToServer.addAll(Arrays.asList(EUROPEANSWER_FOR_QUERY, AMERICAANSWER_FOR_QUERY, ASIAANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
                            firstParameter = CONTINENT_FOR_QUERY;
                            break;
                        }
                        case (CHAMPIONSHIP_COMBO_BOX): {
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("E' della Serie A il club con cui giocava?", "E' della Premier League il club con cui giocava?", "E' della Ligue il club con cui giocava?", "E' della BBVA il club con cui giocava?", "E' della Liga Argentina il club con cui giocava?"));
                            questionToSendToServer.addAll(Arrays.asList(SERIEAANSWER_FOR_QUERY, PREMIERANSWER_FOR_QUERY, LIGUEANSWER_FOR_QUERY, BBVAANSWER_FOR_QUERY, LIGAANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
                            firstParameter = CHAMPIONSHIP_FOR_QUERY;
                            break;
                        }
                        case (CAPTAINBAND_COMBO_BOX): {
                            questionCanBeChoosen.clear();
                            questionToSendToServer.clear();
                            questionCanBeChoosen.addAll(Arrays.asList("Indossa la fascia da capitano?", "Non indossa la fascia da capitano?"));
                            questionToSendToServer.addAll(Arrays.asList(TRUEANSWER_FOR_QUERY, FALSEANSWER_FOR_QUERY));
                            questionThatCouldBeChoosen.setItems(FXCollections.observableArrayList(questionCanBeChoosen));
                            setListViewHeight(questionThatCouldBeChoosen, questionCanBeChoosen);
                            firstParameter = CAPTAINBAND_FOR_QUERY;
                            break;
                        }
                    }
                }
            }
        });
    }

    /** Metodo che serve per rimuovere gli sticker ricevuti in seguito ad una mia query dalla mio schermata del client.
     * @param newStickers è l'array con i nomi degli sticker che van rimossi.
     */
    public void modifySticker(ArrayList<String> newStickers) {
        for (int i = 0; i < newStickers.size(); i++) {
            ImageView stickerToBeRemovedImage = (ImageView) gamingScene.lookup("#" + newStickers.get(i));
            if (stickerToBeRemovedImage != null) {
                utilities.scaleTransition(stickerToBeRemovedImage, 0, 0, 500);
            }
        }
        disableForChangingRound(true);
    }

    /** Metodo stilistico che aumenta la scale degli sticker quando passo sopra col mouse.
     * @param event è l'immagine su cui sono.
     */
    public void zoomInSticker(Event event){
        ImageView imageOver = (ImageView) event.getTarget();
        imageOver.toFront();
        utilities.scaleTransition(imageOver, 1.3f, 1.3f, 250);
    }

    /** Metodo stilistico che diminusce la scale degli sticker quando passo sopra col mouse.
     * @param event è l'immagine da cui sto per spostarmi.
     */
    public void zoomOutSticker(Event event){
        ImageView imageOver = (ImageView) event.getTarget();
        imageOver.toFront();
        utilities.scaleTransition(imageOver, 1.0f, 1.0f, 250);
    }

    /** E' il metodo che disabilita il turno o abilita a seconda del booleano passato.
     * Se disabilitato, disabilito la possibilità di fare query sia tramite la comboBox che tramite il dragAndDrop.
     * Se abilitato, abilito tutto e setto l'onDragDone per ogni immagine.
     * @param bool è il booleano che se è true vuol dire che passo il turno, altrimenti false vuol dire che è il mio turno.
     */
    public void disableForChangingRound(Boolean bool){
        maskerPaneWaitingOtherPlayerChoice.setVisible(bool);
        questionChoosenListView.setDisable(bool);
        questionThatCouldBeChoosen.setDisable(bool);
        toolBar.setDisable(bool);
        hairComboBox.setDisable(bool);
        beardComboBox.setDisable(bool);
        faceComboBox.setDisable(bool);
        accessoriesComboBox.setDisable(bool);
        informationComboBox.setDisable(bool);
        reinitializeComboBox();
        questionThatCouldBeChoosen.setItems(null);
        if (bool){
            if (isItFirstTimeYouEnter){
                Set<Node> set = anchorPane.lookupAll(".ImageSticker");
                Object[] arrayNode = set.toArray();
                for (int cont = 0; cont < set.size(); cont++) {
                    ImageView imageView = (ImageView) arrayNode[cont];
                    imageView.setOnDragDone(new EventHandler<DragEvent>() {
                        @Override
                        public void handle(DragEvent event) {
                            animation.stop();
                            hisStickerImage.setScaleX(myStickerImage.getScaleX());
                            hisStickerImage.setScaleX(myStickerImage.getScaleY());
                            hisStickerImage.setScaleX(1);
                            hisStickerImage.setScaleY(1);
                            hisStickerImage.setFitWidth(wSticker);
                            hisStickerImage.setFitHeight(hSticker);
                        }
                    });
                }
                isItFirstTimeYouEnter = false;
            }
            myStickerImage.setOnDragOver(null);
            myStickerImage.setOnDragDropped(null);
            hisStickerImage.setOnDragOver(null);
            hisStickerImage.setOnDragDropped(null);
            targetImage = hisStickerImage;
            utilities.fadeTransitionEffect(hisStickerImage, 1, 0, 1000);
        } else if (!bool){
            changeDragAndDrop(hisStickerImage);
            hisStickerImage.setImage(questionMarkImage);
            utilities.fadeTransitionEffect(hisStickerImage, 0, 1, 1000);
        }
    }

    /** Modifica l'altezza della listView
     * @param questionThatCouldBeChoosen listView su cui modifico l'altezza.
     * @param questionCanBeChoosenArray arraylist che popola la mia listView.
     */
    private void setListViewHeight(JFXListView<String> questionThatCouldBeChoosen, ArrayList<String> questionCanBeChoosenArray) {
        questionThatCouldBeChoosen.setPrefHeight((questionCanBeChoosenArray.size()+4)*18.2);
    }

    /** getter.
     * @return il path dell'immagine.
     */
    public String getImagePath() {
        return imagePath;
    }

}