package sample.Server.ServerClass;

/** @author Giulio Melloni
 * Classe che rappresenta il thread del client nel server. Questa è la classe fondamentale che serve come ponte di comunicazione tra server e client.
 * E' qui che si ricevono i vari codici con tutte le azioni prefissate. E' qui che avviene l'assegnazione dello sticker personale, la query del client verso l'avversario.
 * In questa classe avviene, quindi, lo scambio di informazioni tra server e client singolo.
 */

import static sample.Utilities.Class.ConstantCodes.*;
import com.google.gson.reflect.TypeToken;
import sample.Utilities.Class.*;
import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import org.apache.commons.io.FilenameUtils;
import java.util.Random;

public class ServerThread extends Thread{

    private int positionInArrayList;
    private String serverIP;
    private CodeAndInformation codeAndInformation = new CodeAndInformation();
    private Connection connection;
    private ServerStart serverStart;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Gson gson;
    private Sticker mySticker;
    private Sticker opponentSticker;
    private ArrayList<String> sqlNicknameToBeRemoved = new ArrayList<>();
    private User user;

    /** Costruttore di ServerThread.
     * @param socket gli passo il socket su cui avverrà la comunicazione.
     * @param serverStart è il modo con cui posso legare il suo main col thread.
     * @param connection è la connessione al dbmysql.
     * @param positionInArrayList è la posizione nell'arrayList utile per il cambio di turno.
     */
    public ServerThread(Socket socket, ServerStart serverStart, Connection connection, int positionInArrayList){
        this.socket = socket;
        this.serverIP = socket.getInetAddress().getHostAddress();
        this.serverStart = serverStart;
        this.connection = connection;
        this.positionInArrayList = positionInArrayList;
    }

    /** Metodo che permette l'esecuzione vera e propria del thread creato dopo lo start().
     * Questo metodo è il cuore del server. In questo campo vengono ricevuti i segnali mandati dal client.
     * La comunicazione avviene quindi su un socket comune in cui tramite il writer e il reader ci si scrive o ci si legge.
     * La codifica usata per questo progetto è la seguente: il codice che arriva viene subito deserializzato in un oggetto di tipo CodeAndInformation.
     * Questo oggetto ha due parti:
     * - code: un codice costante in maiuscolo specificato nella classe ConstantCode
     * - information: una stringa che ha l'informazione che può servire. Se ho bisogno di passare un oggetto o un arraylist ho bisogno quindi di una ulteriore serializzazione.
     * In questo while (true) quindi il mio thread rimane sempre e verifica leggendo dal socket il codice ricevuto; va quindi nel case rispettivo e svolge determinate azioni.
     * @see #reader istanzio e creo il reader passandogli il socket su cui scriverò.
     * @see #writer istanzio il writer passandogli sempre il socket su cui scriverò.
     * @see #gson istanzio il gson che mi serve per serializzare codice ed informazione.
     */
    public void run(){
        try{
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            writer = new PrintWriter(this.socket.getOutputStream(),true);//output sul socket
            while (true) {
                gson = new Gson();
                String code = reader.readLine();
                codeAndInformation = gson.fromJson(code, CodeAndInformation.class);
                System.out.println(codeAndInformation.getCode() + " -> CODE" );
                switch (codeAndInformation.getCode()){
                    //Il client manda il segnale che si è disconesso dalla schermata di choice
                    case (CLIENT_DISCONNECTING_FROM_CHOICE_SCREEN):{
                        serverStart.removeClientDisconnected(user.getUserUsername());
                        break;
                    }
                    //Il client manda il segnale che si è disconnesso prima di loggarsi
                    case (CLIENT_DISCONNECTING_FROM_LOGIN_SCREEN):{
                        serverStart.removeClientDisconnectedFromLoginScreen();
                        break;
                    }
                    //Sto provando far loggare un utente nel login
                    case (CLIENT_WANTS_TO_LOGIN):{
                        readyToVerifyUser(codeAndInformation.getInformation());
                        break;
                    }
                    //Nuovo utente nel database
                    case (CLIENT_WANTS_TO_SIGNUP):{
                        readyToCreateNewUser(codeAndInformation.getInformation());
                        break;
                    }
                    //Server pronto per ricevere informazioni sugli sticker
                    case (CLIENT_GIVES_STICKER_INFO):{
                        readyToKnowStickerInfo(codeAndInformation.getInformation());
                        break;
                    }
                    //Client vuole mandare la query riguardo ad uno sticker
                    case (CLIENT_GIVES_QUERY): {
                        readyToKnowQuery(codeAndInformation.getInformation());
                        break;
                    }
                    case (CLIENT_WANTS_TO_KNOW_CONNECTED_CLIENT):{
                        serverStart.refreshClientConnected(codeAndInformation.getInformation());
                        break;
                    }
                    case (CLIENT_WANTS_TO_SEND_RATING):{
                        communicateClientRating(codeAndInformation.getInformation());
                        break;
                    }
                    case (CLIENT_WANTS_TO_PLAY):{
                        serverStart.sendingRequest(codeAndInformation.getInformation(), getUser().getUserUsername());
                        break;
                    }
                    case (CLIENT_SAYS_OK_FOR_PLAYING):{
                        serverStart.createTheGame(codeAndInformation.getInformation());
                        matchNumber(codeAndInformation.getInformation());
                        break;
                    }
                    case (CLIENT_SAYS_NO_FOR_PLAYING):{
                        serverStart.cancelTheGame(codeAndInformation.getInformation());
                        break;
                    }
                    case (CLIENT_WANTS_TO_KNOW_CONNECTED_CLIENT_FOR_THE_FIRST_TIME):{
                        serverStart.refreshClientConnectedForTheFirstTime(codeAndInformation.getInformation());
                        break;
                    }
                    case (CLIENT_GIVES_QUERY_FOR_STICKER):{
                        readyToKnowQuerySticker(codeAndInformation.getInformation());
                        break;
                    }
                    case (CLIENT_DISCONNECTING_FROM_GAME_SCREEN):{
                        serverStart.removeClientInGame(codeAndInformation.getInformation());
                        serverStart.removeClientDisconnected(codeAndInformation.getInformation());
                        System.out.println("Per essere precisi, " + codeAndInformation.getInformation() + " ha quittato");
                        break;
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /** Metodo che serve per creare il gioco inizializzando la posizione nell'arraylist.
     * @param information è un arraylist con in prima posizione il nome dell'utente e in seconda la password.
     */
    private void matchNumber(String information) {
        gson = new Gson();
        ArrayList<String> userAndNumber = gson.fromJson(information, new TypeToken<ArrayList<String>>() {}.getType());
        positionInArrayList = Integer.parseInt(userAndNumber.get(1));
    }

    /** Metodo che serve per la comunicazione del rating del client. In questo modo deserializzo le informazioni e le mando a serverStart.
     * @param information è un arrayList che contiene il voto, il titolo, il testo della recensione.
     */
    private void communicateClientRating(String information) {
        gson = new Gson();
        ArrayList<String> voteTitleText = gson.fromJson(information, new TypeToken<ArrayList<String>>() {}.getType());
        System.out.println(voteTitleText.get(0) + " è il voto dello user " + getUser().getUserUsername());
        serverStart.sendRating(Double.parseDouble(voteTitleText.get(0)), voteTitleText.get(1), voteTitleText.get(2), getUser().getUserUsername());
    }

    /** Metodo che serve per la comunicazione della query sullo sticker, ovvero chiedere se è lo sticker scelto (metodo usato per vincere la partita) da parte dell'utente.
     * @param information è il nickname dello sticker scelto.
     */
    private void readyToKnowQuerySticker(String information) {
        if(opponentSticker.getNicknameOfSticker().equals(information)){
            serverStart.swapGameArrayList(user.getUserUsername());
        } else {
            serverStart.changingRoundOfClient(positionInArrayList, user.getUserUsername(), mySticker);
        }
    }

    /** Metodo che serve per la comunicazione della query dal client (il gson con parametri).
     * Con questi parametri potrò costruire la query con cui posso trovare gli sticker da rimuovere nel client che ha fatto la domanda.
     * Ho quindi diversi casi di information. Ho il caso in cui il secondo parametro di StickerQuery sia un booleano, oppure sia una stringa, oppure sia un Booleano (ossia un booleano con in più la possibilità che sia null).
     * In ogni caso, l'azione svolta è quella di una chiamata di funzione che svolga la query {@link #settingQueryBooleanType(String, Boolean)}{@link #settingQueryStringType(String, String, Boolean)}{@link #settingQueryInvertTypeBoolean(String, Boolean)}{@link #settingQueryInvertTypeString(String, String)}.
     * @param information è un gson che al suo interno ha il primo e il secondo parametro per la query del db.
     */
    private void readyToKnowQuery(String information) {
        gson = new Gson();
        Boolean choice;
        StickerQuery stickerQuery = gson.fromJson(information, StickerQuery.class);
        System.out.println(stickerQuery + " -> STICKER QUERY");
        System.out.println(opponentSticker + " -> OPPONENT STICKER");
        switch (stickerQuery.getFirstParameter()) {
            //switch con case(primo parametro passato) e all'interno controllo se corrisponde o no il secondo parametro allo sticker scelto
            case (HAIRCOLORBROWN_FOR_QUERY): {
                if (opponentSticker.isHairColorBrownOfSticker() == stringToBool(stickerQuery.getSecondParameter())) {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                } else {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                }
                break;
            }
            case (HAIRLENGTH_FOR_QUERY): {
                if (opponentSticker.getHairLengthOfSticker().equals(stickerQuery.getSecondParameter())) {
                    choice = true;
                    settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                } else {
                    choice = false;
                    settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                }
                break;
            }
            case (BEARDCOLORBROWN_FOR_QUERY): {
                if (opponentSticker.isBeardColorBrownOfSticker() == null) {
                    settingQueryInvertTypeBoolean(stickerQuery.getFirstParameter(), !stringToBoolean(stickerQuery.getSecondParameter()));
                } else if (opponentSticker.isBeardColorBrownOfSticker() == stringToBoolean(stickerQuery.getSecondParameter())) {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBoolean(stickerQuery.getSecondParameter()));
                } else if (opponentSticker.isBeardColorBrownOfSticker() != stringToBoolean(stickerQuery.getSecondParameter())){
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBoolean(stickerQuery.getSecondParameter()));
                }
                break;
            }
            case (EYESCOLORBROWN_FOR_QUERY): {
                if (opponentSticker.isEyesColorBrownOfSticker() == stringToBool(stickerQuery.getSecondParameter())) {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                } else {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                }
                break;
            }
            case (COMPLEXIONBROWN_FOR_QUERY): {
                if (opponentSticker.isComplexionBrownOfSticker() == stringToBool(stickerQuery.getSecondParameter())) {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                } else {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                }
                break;
            }
            case (EARRINGS_FOR_QUERY): {
                if (opponentSticker.isEarringsOfSticker() == stringToBool(stickerQuery.getSecondParameter())) {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                } else {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                }
                break;
            }
            case (GLASSES_FOR_QUERY): {
                if (opponentSticker.isGlassesOfSticker() == stringToBool(stickerQuery.getSecondParameter())) {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                } else {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                }
                break;
            }
            case (HEADBAND_FOR_QUERY): {
                if (opponentSticker.isHeadbandOfSticker() == stringToBool(stickerQuery.getSecondParameter())) {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                } else {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                }
                break;
            }
            case (MOLE_FOR_QUERY): {
                if (opponentSticker.isMoleOfSticker() == stringToBool(stickerQuery.getSecondParameter())) {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                } else {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                }
                break;
            }
            case (BEARDTYPE_FOR_QUERY): {
                if (opponentSticker.getBeardTypeOfSticker() == null) {
                    settingQueryInvertTypeString(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter());
                } else {
                    if (opponentSticker.getBeardTypeOfSticker().equals(stickerQuery.getSecondParameter())) {
                        choice = true;
                        settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                    } else {
                        choice = false;
                        settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                    }
                }
                break;
            }
            case (FRECKLES_FOR_QUERY): {
                if (opponentSticker.isFrecklesOfSticker() == stringToBool(stickerQuery.getSecondParameter())) {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                } else {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                }
                break;
            }
            case (NATIONALSHIRT_FOR_QUERY): {
                if (opponentSticker.isNationalShirtOfSticker() == stringToBool(stickerQuery.getSecondParameter())) {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                } else {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                }
                break;
            }
            case (CONTINENT_FOR_QUERY): {
                if (opponentSticker.getContinentOfSticker() == null) {
                    settingQueryInvertTypeString(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter());
                } else {
                    if (opponentSticker.getContinentOfSticker().equals(stickerQuery.getSecondParameter())) {
                        choice = true;
                        settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                    } else {
                        choice = false;
                        settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                    }
                }
                break;
            }
            case (CHAMPIONSHIP_FOR_QUERY): {
                if (opponentSticker.getChampionshipOfSticker() == null){
                    settingQueryInvertTypeString(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter());
                } else {
                    if (opponentSticker.getChampionshipOfSticker().equals(stickerQuery.getSecondParameter())) {
                            choice = true;
                            settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                        } else {
                            choice = false;
                            settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                        }
                    }
                break;
            }
            case (CAPTAINBAND_FOR_QUERY): {
                if (opponentSticker.isCaptainBandOfSticker() == stringToBool(stickerQuery.getSecondParameter())) {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                } else {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                }
                break;
            }
            case (NOSEDIMENSIONBIG_FOR_QUERY): {
                if (opponentSticker.isNoseDimensionBigOfSticker() == stringToBool(stickerQuery.getSecondParameter())) {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                } else {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                }
                break;
            }
            case (SMILE_FOR_QUERY): {
                if (opponentSticker.isSmileOfSticker() == stringToBool(stickerQuery.getSecondParameter())) {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                } else {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                }
                break;
            }
            case (HAIRTYPESTRAIGHT_FOR_QUERY): {
                if (opponentSticker.isHairTypeStraightOfSticker() == null){
                    settingQueryInvertTypeBoolean(stickerQuery.getFirstParameter(), !stringToBoolean(stickerQuery.getSecondParameter()));
                } else if (opponentSticker.isHairTypeStraightOfSticker() == stringToBoolean(stickerQuery.getSecondParameter())) {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBoolean(stickerQuery.getSecondParameter()));
                } else if (opponentSticker.isHairTypeStraightOfSticker() != stringToBoolean(stickerQuery.getSecondParameter())){
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBoolean(stickerQuery.getSecondParameter()));
                }
                break;
            }
            case (BEARDLENGTH_FOR_QUERY): {
                if (opponentSticker.getBeardLengthOfSticker().equals(stickerQuery.getSecondParameter())) {
                    choice = true;
                    settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                } else {
                    choice = false;
                    settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                }
                break;
            }
        }
    }

    /** Metodo che serve per la creazione della query nel caso in cui lo sticker scelto abbia parametri null.
     * Viene fatta una query all'interno del database e restituito l'array con i nomi da rimuovere nella schermata del client che ha posto la domanda.
     * @param firstParameter nome della colonna su cui far la query.
     * @param secondParameter nome del parametro della colonna.
     */
    private void settingQueryInvertTypeString(String firstParameter, String secondParameter) {
        sqlNicknameToBeRemoved.clear();
        ResultSet resultSet;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM stickers WHERE " + firstParameter + " = '" + secondParameter + "'");//faccio la query con il nome ricevuto per creare la figurina
            while (resultSet.next()){
                sqlNicknameToBeRemoved.add(resultSet.getString(NICKNAME_FOR_QUERY));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println(sqlNicknameToBeRemoved + " -> sqlNicknameToBeRemoved");
        gson = new Gson();
        String nicknameGson = gson.toJson(sqlNicknameToBeRemoved);
        writer.println(CodeAndInformation.serializeToJson(SERVER_SENDS_STICKER_MUST_BE_REMOVED, nicknameGson));
        serverStart.changingRoundOfClient(positionInArrayList, user.getUserUsername(), mySticker);
    }

    /** Metodo che serve per la creazione della query nel caso in cui lo sticker scelto abbia parametri null.
     * Viene fatta una query all'interno del database e restituito l'array con i nomi da rimuovere nella schermata del client che ha posto la domanda.
     * @param firstParameter nome della colonna su cui far la query.
     * @param secondParameter nome del parametro della colonna.
     */
    private void settingQueryInvertTypeBoolean(String firstParameter, Boolean secondParameter) {
        sqlNicknameToBeRemoved.clear();
        ResultSet resultSet;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM stickers WHERE " + firstParameter + " = '" + secondParameter + "'");//faccio la query con il nome ricevuto per creare la figurina
            while (resultSet.next()){
                sqlNicknameToBeRemoved.add(resultSet.getString(NICKNAME_FOR_QUERY));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println(sqlNicknameToBeRemoved + " -> sqlNicknameToBeRemoved");
        gson = new Gson();
        String nicknameGson = gson.toJson(sqlNicknameToBeRemoved);
        writer.println(CodeAndInformation.serializeToJson(SERVER_SENDS_STICKER_MUST_BE_REMOVED, nicknameGson));
        serverStart.changingRoundOfClient(positionInArrayList, user.getUserUsername(), mySticker);
    }

    /** Creo la query con due parametri di tipo stringa in cui all'interno cerco = o != a seconda della scelta passata.
     * Viene fatta una query all'interno del database e restituito l'array con i nomi da rimuovere nella schermata del client che ha posto la domanda.
     * @param firstParameter nome della colonna su cui far la query.
     * @param secondParameter nome del parametro della colonna.
     * @param choice è la scelta, ossia se ho fatto una domanda e il server risponde GIUSTO oppure se ho fatto una domanda e il server mi risponde SBAGLIATO.
     */
    private void settingQueryStringType(String firstParameter, String secondParameter, Boolean choice) {
        sqlNicknameToBeRemoved.clear();
        ResultSet resultSet;
        try {
            Statement statement = connection.createStatement();
            if (choice) {
                resultSet = statement.executeQuery("SELECT * FROM stickers WHERE " + firstParameter + " != '" + secondParameter + "' || " + firstParameter + " IS NULL ");//faccio la query con il nome ricevuto per creare la figurina
            } else {
                resultSet = statement.executeQuery("SELECT * FROM stickers WHERE " + firstParameter + " = '" + secondParameter + "'");//faccio la query con il nome ricevuto per creare la figurina
            }
            while(resultSet.next()){
                sqlNicknameToBeRemoved.add(resultSet.getString(NICKNAME_FOR_QUERY));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(sqlNicknameToBeRemoved + " -> sqlNicknameToBeRemoved");
        gson = new Gson();
        String nicknameGson = gson.toJson(sqlNicknameToBeRemoved);
        writer.println(CodeAndInformation.serializeToJson(SERVER_SENDS_STICKER_MUST_BE_REMOVED, nicknameGson));
        serverStart.changingRoundOfClient(positionInArrayList, user.getUserUsername(), mySticker);
    }

    /** Creo la query con due parametri, uno stringa e uno booleano e cerco = o ! non più in questo metodo ma nella chiamata.
     * Viene fatta una query all'interno del database e restituito l'array con i nomi da rimuovere nella schermata del client che ha posto la domanda.
     * @param firstParameter nome della colonna su cui far la query.
     * @param secondParameter nome del parametro della colonna.
     */
    private void settingQueryBooleanType(String firstParameter, Boolean secondParameter){
        sqlNicknameToBeRemoved.clear();
        try {
            System.out.println(secondParameter + " -> secondParameter");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM stickers WHERE " + firstParameter + " != " + secondParameter + " || " + firstParameter + " IS NULL ");//faccio la query con il nome ricevuto per creare la figurina
            while(resultSet.next()){
                sqlNicknameToBeRemoved.add(resultSet.getString(NICKNAME_FOR_QUERY));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(sqlNicknameToBeRemoved + " -> sqlNicknameToBeRemoved");
        gson = new Gson();
        String nicknameGson = gson.toJson(sqlNicknameToBeRemoved);
        writer.println(CodeAndInformation.serializeToJson(SERVER_SENDS_STICKER_MUST_BE_REMOVED, nicknameGson));
        serverStart.changingRoundOfClient(positionInArrayList, user.getUserUsername(), mySticker);
    }

    /** Metodo che serve per castare una stringa a boolean.
     * @param stringConvertion stringa convertire.
     * @return stringa castata in booleano.
     */
    private static boolean stringToBool(String stringConvertion) {
        if (stringConvertion.equals(TRUEANSWER_FOR_QUERY))
            return true;
        if (stringConvertion.equals(FALSEANSWER_FOR_QUERY))
            return false;
        throw new IllegalArgumentException(stringConvertion + " non è booleano");
    }

    /** Metodo che serve per castare una stringa a Boolean (booleano ma con anche la possibilità di avere un valore NULL).
     * @param stringConvertion stringa convertire.
     * @return stringa castata in Booleano.
     */
    private static Boolean stringToBoolean(String stringConvertion){
        if (stringConvertion.equals(TRUEANSWER_FOR_QUERY))
            return true;
        if (stringConvertion.equals(FALSEANSWER_FOR_QUERY))
            return false;
        if (stringConvertion.equals(null))
            return null;
        throw new IllegalArgumentException(stringConvertion + " non è Booleano");
    }

    /** Metodo che serve per istanziare la variabile mySticker con i campi da cercare nel database.
     * In questo metodo taglio il path ricevuto dal client e ricerco nel database un elemento con quel nickname e setto ciò che ho trovato in mysticker.
     * Inoltre chiamo {@link #changeFirstPlayerInGame(int)} e setto il mio sticker come stickerOpponent dell'avversario.
     * @param information è il path dell'immagine scelta dal client.
     */
    private void readyToKnowStickerInfo(String information) {
        try {
            System.out.println(information + " -> information");
            String stickerName = FilenameUtils.getName(information);//prendo l'ultima parte del path
            stickerName = FilenameUtils.removeExtension(stickerName);//tolgo l'estensione
            System.out.println(stickerName + " -> stickerName");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM stickers WHERE nickname = '" + stickerName + "'");//faccio la query con il nome ricevuto per creare la figurina
            mySticker = new Sticker(null);//creo un elemento sticker vuoto
            while (resultSet.next()) {//popolo il mio oggetto mySticker con i campi che ho trovato nel database con chiave quella passata dal client
                mySticker.setNameOfSticker(resultSet.getString(NAME_FOR_QUERY));
                mySticker.setNicknameOfSticker(resultSet.getString(NICKNAME_FOR_QUERY));
                mySticker.setSurnameOfSticker(resultSet.getString(SURNAME_FOR_QUERY));
                mySticker.setHairColorBrownOfSticker(resultSet.getBoolean(HAIRCOLORBROWN_FOR_QUERY));
                mySticker.setHairLengthOfSticker(resultSet.getString(HAIRLENGTH_FOR_QUERY));
                mySticker.setBeardColorBrownOfSticker(resultSet.getBoolean(BEARDCOLORBROWN_FOR_QUERY));
                if (resultSet.wasNull()) {
                    mySticker.setBeardColorBrownOfSticker(null);
                }
                mySticker.setEyesColorBrownOfSticker(resultSet.getBoolean(EYESCOLORBROWN_FOR_QUERY));
                mySticker.setComplexionBrownOfSticker(resultSet.getBoolean(COMPLEXIONBROWN_FOR_QUERY));
                mySticker.setEarringsOfSticker(resultSet.getBoolean(EARRINGS_FOR_QUERY));
                mySticker.setGlassesOfSticker(resultSet.getBoolean(GLASSES_FOR_QUERY));
                mySticker.setHeadbandOfSticker(resultSet.getBoolean(HEADBAND_FOR_QUERY));
                mySticker.setMoleOfSticker(resultSet.getBoolean(MOLE_FOR_QUERY));
                mySticker.setBeardTypeOfSticker(resultSet.getString(BEARDTYPE_FOR_QUERY));
                mySticker.setFrecklesOfSticker(resultSet.getBoolean(FRECKLES_FOR_QUERY));
                mySticker.setNationalShirtOfSticker(resultSet.getBoolean(NATIONALSHIRT_FOR_QUERY));
                mySticker.setContinentOfSticker(resultSet.getString(CONTINENT_FOR_QUERY));
                mySticker.setChampionshipOfSticker(resultSet.getString(CHAMPIONSHIP_FOR_QUERY));
                mySticker.setCaptainBandOfSticker(resultSet.getBoolean(CAPTAINBAND_FOR_QUERY));
                mySticker.setNoseDimensionBigOfSticker(resultSet.getBoolean(NOSEDIMENSIONBIG_FOR_QUERY));
                mySticker.setSmileOfSticker(resultSet.getBoolean(SMILE_FOR_QUERY));
                mySticker.setHairTypeStraightOfSticker(resultSet.getBoolean(HAIRTYPESTRAIGHT_FOR_QUERY));
                if (resultSet.wasNull()) {
                    mySticker.setHairTypeStraightOfSticker(null);
                }
                mySticker.setBeardLengthOfSticker(resultSet.getString(BEARDLENGTH_FOR_QUERY));
            }
            System.out.println(mySticker + " -> mySticker");
            serverStart.setOpponentSticker(mySticker, positionInArrayList, user.getUserUsername());
            changeFirstPlayerInGame(positionInArrayList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Metodo che serve per la creazione di un nuovo utente non nel database.
     * Verifico che non sia già presente un utente con lo stesso nome e lo inserisco tramite una INSERT nel database.
     * @param information è il gson di user, con nomeUtente e passwordUtente.
     */
    private void readyToCreateNewUser(String information) {
        gson = new Gson();
        String userSQLName = null;//username tratto dalla query
        String userSQLPassword = null;//password tratta dalla query
        try{
            user = gson.fromJson(information, User.class);
            System.out.println(user);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE username = '" + user.getUserUsername() + "'");//query per verificare se un utente è già presente
            while(resultSet.next()){
                userSQLName = resultSet.getString(USERNAME_FOR_QUERY);
                userSQLPassword = resultSet.getString(PASSWORD_FOR_QUERY);
            }
            if (userSQLName != null) {
                System.out.println("Utente di nome " + userSQLName + " e password " + userSQLPassword + " già presente\nImmettere nuovo utente");
                writer.println(CodeAndInformation.serializeToJson(SERVER_CLIENT_ALREADY_IN_THE_DATABASE, user.getUserUsername()));
            } else {
                statement.execute("INSERT INTO USER(username,password) VALUES ('" + user.getUserUsername() + "','" + user.getUserPassword() + "')");
                writer.println(CodeAndInformation.serializeToJson(SERVER_CLIENT_SUCCESSFUL_SIGNUP, null));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /** Metodo che serve per verificare se ho già un utente con tal nome e password nel db.
     * Faccio quindi una query all'interno del database. Se trovo un utente simile mando OK, altrimenti mando NOT_FOUND.
     * Se un client inoltre è già connesso allora non può entrare.
     * @param information è il gson di user, con nomeUtente e passwordUtente.
     */
    public void readyToVerifyUser(String information) {
        gson = new Gson();
        String userSQLName = null;
        String userSQLPassword = null;
        Boolean userAlreadyLoggedBoolean = false;
        try {
            user = gson.fromJson(information, User.class);
            System.out.println(user + " -> user");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE BINARY username = '" + user.getUserUsername() + "' AND BINARY password = '" + user.getUserPassword() + "'");
            while (resultSet.next()) {
                userSQLName = resultSet.getString(USERNAME_FOR_QUERY);
                userSQLPassword = resultSet.getString(PASSWORD_FOR_QUERY);
            }
            for (int cont = 0; cont < serverStart.getListOfClientConnected().size(); cont++) {
                if (serverStart.getListOfClientConnected().get(cont).equals(userSQLName)) {
                    userAlreadyLoggedBoolean = true;
                }
            }
            if (userSQLName != null && userSQLPassword != null && !userAlreadyLoggedBoolean) {
                System.out.println("Utente di nome " + user.getUserUsername() + " e password " + user.getUserPassword() + " trovato\nLogin effettuato con successo");
                writer.println(CodeAndInformation.serializeToJson(SERVER_CLIENT_SUCCESSFUL_LOGIN, null));
                serverStart.insertNameInArrayList(user.getUserUsername());
                serverStart.refreshClientConnected(user.getUserUsername());
            } else {
                if (!userAlreadyLoggedBoolean) {
                    System.out.println("Utente " + user.getUserUsername() + "non trovato");
                    writer.println(CodeAndInformation.serializeToJson(SERVER_CLIENT_NOT_FOUND, user.getUserUsername()));
                } else {
                    System.out.println("Utente " + user.getUserUsername() + "già connesso");
                    writer.println(CodeAndInformation.serializeToJson(SERVER_CLIENT_ALREADY_LOGGED, user.getUserUsername()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Metodo utilizzato per dare il via al gioco e scegliere il primo giocatore con un random booleano.
     * @param positionInArrayList è la posizone all'interno dell'array di Game.
     */
    public void changeFirstPlayerInGame(int positionInArrayList){
        if (serverStart.getGameArrayList().get(positionInArrayList).getSticker1() != null && serverStart.getGameArrayList().get(positionInArrayList).getSticker2() != null) {
            Random randomNumber = new Random();
            boolean numberOfFirstPlayer = randomNumber.nextBoolean();
            serverStart.startGameWithRandomChoice(numberOfFirstPlayer, positionInArrayList);
        }
    }

    /** getter
     * @return reader è il reader del server
     */
    public BufferedReader getReader() {return reader;}

    /** setter
     * @param reader è il reader del server
     */
    public void setReader(BufferedReader reader) {this.reader = reader;}

    /** getter
     * @return writer è il writer del server
     */
    public PrintWriter getWriter() {return writer;}

    /** setter
     * @param writer è il writer del server
     */
    public void setWriter(PrintWriter writer) {this.writer = writer;}

    /** setter
     * @param opponentSticker è lo sticker avversario
     */
    public void setOpponentSticker(Sticker opponentSticker) {this.opponentSticker = opponentSticker;}

    /** getter
     * @return user è l'user che sta provando a registrarsi o loggare
     */
    public User getUser() {return user;}

    /** setter
     * @param user è l'user che sta provando a registrarsi o loggare
     */
    public void setUser(User user) {this.user = user;}

    /** setter
     * @param positionInArrayList è la posizione nell'array di Game
     */
    public void setPositionInArrayList(int positionInArrayList) {
        this.positionInArrayList = positionInArrayList;
    }

}