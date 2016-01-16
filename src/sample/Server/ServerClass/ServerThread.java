package sample.Server.ServerClass;

import static sample.Utilities.Class.ConstantCodes.*;
import sample.Utilities.Class.Sticker;
import sample.Utilities.Class.StickerQuery;
import sample.Utilities.Class.User;
import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import org.apache.commons.io.FilenameUtils;
import java.util.Random;

public class ServerThread extends Thread{

    private int clientNumberId;
    private int positionInArrayList;
    private String serverIP = null;
    private Connection connection;
    private ServerStart serverStart;
    private Socket socket;
    private BufferedReader reader = null;
    private PrintWriter writer = null;
    private Gson gson;
    private Sticker mySticker;
    private Sticker opponentSticker;
    private ArrayList<String> sqlNicknameToBeRemoved = new ArrayList<>();
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //costruttore
    public ServerThread(Socket socket, int clientNumberId, ServerStart serverStart, Connection connection, int positionInArrayList){
        this.socket = socket;
        this.clientNumberId = clientNumberId;
        this.serverIP = socket.getInetAddress().getHostAddress();
        this.serverStart = serverStart;
        this.connection = connection;
        this.positionInArrayList = positionInArrayList;
    }

    public void run(){
        try{
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));//input sul socket
            writer = new PrintWriter(this.socket.getOutputStream(),true);//output sul socket
                while (true) {
                    String code = reader.readLine();
                    switch (code){
                        //Il client manda il segnale che si è disconesso.
                        case (clientDisconnected):{
                            serverStart.changeClientNumber(user.getUserUsername());
                            serverStart.refreshClientConnected(user.getUserUsername());
                            break;
                        }
                        //Sto provando far loggare un utente nel login
                        case (clientReadyToLogin):{
                            readyToVerifyUser();
                            break;
                        }
                        //Nuovo utente nel database
                        case (clientReadyToCreateNewUser):{
                            readyToCreateNewUser();
                            break;
                        }
                        //Server pronto per ricevere informazioni sugli sticker
                        case (clientReadyToGiveStickerInfo):{
                            readyToKnowStickerInfo();
                            break;
                        }
                        //Client vuole mandare la query riguardo ad uno sticker
                        case (clientReadyToGiveQuery):{
                            readyToKnowQuery();
                            break;
                        }
                        //Client è pronto per ricevere dal server gli sticker da eliminare
                        case (readyToReceiveNewSticker):{
                            sendingNewSticker();
                            break;
                        }
                        case (changeMySticker):{
                            break;
                        }
                        case (wantsToKnowClientConnected):{
                            sendingClientConnected();
                            break;
                        }
                        case (clientReadyToReceiveClientsConnected):{
                            communicateClientConnected();
                            break;
                        }
                        case (clientIsGivingHisName):{
                            waitingForClientName();
                        }
                    }
                }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void waitingForClientName() {
    }

    private void clientIsGoingOff() {
        try {
            writer.println(serverWantsNameOfClientDisconnecting);
            String clientNameOff = reader.readLine();
            serverStart.refreshClientConnected(clientNameOff);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void communicateClientConnected() {
        gson = new Gson();
        String clientConnectedGson = gson.toJson(serverStart.getListOfClientConnected());
        System.out.println(clientConnectedGson);
        writer.println(clientConnectedGson);
    }

    private void sendingClientConnected() {
        writer.println(serverReadyToSendClientConnected);
        gson = new Gson();
        String clientConnectedGson = gson.toJson(serverStart.getListOfClientConnected());
        System.out.println(clientConnectedGson);
        writer.println(clientConnectedGson);
    }


    //metodo che crea un gson con tutti  i nickname da eliminare
    private void sendingNewSticker() {
        gson = new Gson();
        String nicknameGson = gson.toJson(sqlNicknameToBeRemoved);
        System.out.println(nicknameGson);
        writer.println(nicknameGson);
        serverStart.changingRoundOfClient(positionInArrayList);
    }

    //il server è pronto per ricevere la query dal client (il gson con parametri)
    private void readyToKnowQuery() {
        gson = new Gson();
        Boolean choice;
        try {
            writer.println(readyToReceiveQueryInfo);
            String userQuery = reader.readLine();
            StickerQuery stickerQuery = gson.fromJson(userQuery, StickerQuery.class);
            switch (stickerQuery.getFirstParameter()) {
                //switch con case(primo parametro passato) e all'interno controllo se corrisponde o no il secondo parametro allo sticker scelto
                case (hairColorBrown): {
                    if (opponentSticker.isHairColorBrown() == stringToBool(stickerQuery.getSecondParameter())) {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                    } else {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                    }
                    break;
                }
                case (hairLenght): {
                        if (opponentSticker.getHairLenght().equals(stickerQuery.getSecondParameter())) {
                            choice = true;
                            settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                        } else {
                            choice = false;
                            settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                        }
                    break;
                }
                case (beardColorBrown): {
                    if (opponentSticker.isBeardColorBrown() == stringToBool(stickerQuery.getSecondParameter())) {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                    } else {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                    }
                    break;
                }
                case (eyesColorBrown): {
                    if (opponentSticker.isEyesColorBrown() == stringToBool(stickerQuery.getSecondParameter())) {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                    } else {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                    }
                    break;
                }
                case (complexionBrown): {
                    if (opponentSticker.isComplexionBrown() == stringToBool(stickerQuery.getSecondParameter())) {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                    } else {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                    }
                    break;
                }
                case (earrings): {
                    if (opponentSticker.isEarrings() == stringToBool(stickerQuery.getSecondParameter())) {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                    } else {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                    }
                    break;
                }
                case (glasses): {
                    if (opponentSticker.isGlasses() == stringToBool(stickerQuery.getSecondParameter())) {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                    } else {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                    }
                    break;
                }
                case (headband): {
                    if (opponentSticker.isHeadband() == stringToBool(stickerQuery.getSecondParameter())) {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                    } else {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                    }
                    break;
                }
                case (mole): {
                    if (opponentSticker.isMole() == stringToBool(stickerQuery.getSecondParameter())) {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                    } else {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                    }
                    break;
                }
                case (beardType): {
                        if (opponentSticker.getBeardType().equals(stickerQuery.getSecondParameter())) {
                            choice = true;
                            settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                        } else {
                            choice = false;
                            settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                        }
                    break;
                }
                case (freckles): {
                    if (opponentSticker.isFreeckles() == stringToBool(stickerQuery.getSecondParameter())) {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                    } else {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                    }
                    break;
                }
                case (nationalShirt): {
                    if (opponentSticker.isNationalShirt() == stringToBool(stickerQuery.getSecondParameter())) {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                    } else {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                    }
                    break;
                }
                case (continent): {
                        if (opponentSticker.getContinent().equals(stickerQuery.getSecondParameter())) {
                            choice = true;
                            settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                        } else {
                            choice = false;
                            settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                        }
                    break;
                }
                case (championship): {
                        if (opponentSticker.getChampionship().equals(stickerQuery.getSecondParameter())) {
                            choice = true;
                            settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                        } else {
                            choice = false;
                            settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                        }
                    break;
                }
                case (captainBand): {
                    if (opponentSticker.isCaptainBand() == stringToBool(stickerQuery.getSecondParameter())) {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                    } else {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                    }
                    break;
                }
                case (noseDimensionBig): {
                    if (opponentSticker.isNoseDimensionBig() == stringToBool(stickerQuery.getSecondParameter())) {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                    } else {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                    }
                    break;
                }
                case (smile): {
                    if (opponentSticker.isSmile() == stringToBool(stickerQuery.getSecondParameter())) {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                    } else {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                    }
                    break;
                }
                case (hairTypeStraight): {
                    if (opponentSticker.isHairTypeStraight() == stringToBool(stickerQuery.getSecondParameter())) {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                    } else {
                        settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
                    }
                    break;
                }
                case (beardLenght): {
                    if (opponentSticker.getBeardLenght().equals(stickerQuery.getSecondParameter())) {
                        choice = true;
                        settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                    } else {
                        choice = false;
                        settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //creo la query con due parametri di tipo stringa in cui all'interno cerco = o != a seconda della scelta passata
    private void settingQueryStringType(String firstParameter, String secondParameter, Boolean choice) {
        ResultSet resultSet;
        try {
            Statement statement = connection.createStatement();
            if (choice == true) {
                resultSet = statement.executeQuery("SELECT * FROM stickers WHERE " + firstParameter + " != '" + secondParameter + "'");//faccio la query con il nome ricevuto per creare la figurina
            } else {
                resultSet = statement.executeQuery("SELECT * FROM stickers WHERE " + firstParameter + " = '" + secondParameter + "'");//faccio la query con il nome ricevuto per creare la figurina
            }
            while(resultSet.next()){
                sqlNicknameToBeRemoved.add(resultSet.getString("nickname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(sqlNicknameToBeRemoved);
        writer.println(serverReadyToSentStickersMustBeRemoved);
    }

    //creo la query con due parametri, uno stringa e uno booleano e cerco = o ! non più in questo metodo ma nella chiamata
    private void settingQueryBooleanType(String firstParameter, Boolean secondParameter){
        try {
            System.out.println(secondParameter);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM stickers WHERE " + firstParameter + " != " + secondParameter + " || " + firstParameter + " IS NULL ");//faccio la query con il nome ricevuto per creare la figurina
            while(resultSet.next()){
                sqlNicknameToBeRemoved.add(resultSet.getString("nickname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(sqlNicknameToBeRemoved);
        writer.println(serverReadyToSentStickersMustBeRemoved);
    }

    //metodo di casting da Stringa a Boolean
    private static boolean stringToBool(String stringConvertion) {
        if (stringConvertion.equals("true"))
            return true;
        if (stringConvertion.equals("false"))
            return false;
        throw new IllegalArgumentException(stringConvertion + " is not a bool. Only 1 and 0 are.");
    }

    //metodo che serve per sapere che sticker ha scelto l'utente
    private void readyToKnowStickerInfo() {
        try {
            writer.println(readyToReceiveStickerInfo);//comunico che sono pronto al client
            String stickerStringPath = reader.readLine();//sono in attesa che il client mi comunichi che sticker ha scelto
            System.out.println(stickerStringPath);
            String stickerName = FilenameUtils.getName(stickerStringPath);//prendo l'ultima parte del path
            stickerName = FilenameUtils.removeExtension(stickerName);//tolgo l'estensione
            System.out.println(stickerName);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM stickers WHERE nickname='" + stickerName + "'");//faccio la query con il nome ricevuto per creare la figurina
            mySticker = new Sticker(null);//creo un elemento sticker vuoto
            while(resultSet.next()){//popolo il mio oggetto mySticker con i campi che ho trovato nel database con chiave quella passata dal client
                mySticker.setName(resultSet.getString(name));
                mySticker.setNickname(resultSet.getString(nickname));
                mySticker.setSurname(resultSet.getString(surname));
                mySticker.setHairColorBrown(resultSet.getBoolean(hairColorBrown));
                mySticker.setHairLenght(resultSet.getString(hairLenght));
                mySticker.setBeardColorBrown(resultSet.getBoolean(beardColorBrown));
                mySticker.setEyesColorBrown(resultSet.getBoolean(eyesColorBrown));
                mySticker.setComplexionBrown(resultSet.getBoolean(complexionBrown));
                mySticker.setEarrings(resultSet.getBoolean(earrings));
                mySticker.setGlasses(resultSet.getBoolean(glasses));
                mySticker.setHeadband(resultSet.getBoolean(headband));
                mySticker.setMole(resultSet.getBoolean(mole));
                mySticker.setBeardType(resultSet.getString(beardType));
                mySticker.setFreeckles(resultSet.getBoolean(freckles));
                mySticker.setNationalShirt(resultSet.getBoolean(nationalShirt));
                mySticker.setContinent(resultSet.getString(continent));
                mySticker.setChampionship(resultSet.getString(championship));
                mySticker.setCaptainBand(resultSet.getBoolean(captainBand));
                mySticker.setNoseDimensionBig(resultSet.getBoolean(noseDimensionBig));
                mySticker.setSmile(resultSet.getBoolean(smile));
                mySticker.setHairTypeStraight(resultSet.getBoolean(hairTypeStraight));
                mySticker.setBeardLenght(resultSet.getString(beardLenght));
            }
            System.out.println(mySticker);
            serverStart.setOpponentSticker(mySticker, positionInArrayList);
            changeFirstPlayerInGame();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //metodo che serve per la creazione di un nuovo utente
    private void readyToCreateNewUser() {
        gson = new Gson();
        String userSQLName = null;//username tratto dalla query
        String userSQLPassword = null;//password tratta dalla query
        try{
            writer.println(serverReadyToReceiveUserInfo);//server manda a client che è pronto e in ascolto del gson
            String userString = reader.readLine();
            System.out.println(userString);
            user = gson.fromJson(userString, User.class);//deserializzo il gson
            System.out.println(user);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE username='" + user.getUserUsername() + "' AND password='" + user.getUserPassword() + "'");//query per verificare se un utente è già presente
            while(resultSet.next()){
                userSQLName = resultSet.getString(username);
                userSQLPassword = resultSet.getString(password);
            }
            if (userSQLName != null) {
                System.out.println("Utente di nome " + userSQLName + " e password " + userSQLPassword + " già presente\nImmettere nuovo utente");
            } else {
                statement.execute("INSERT INTO USER(username,password) VALUES ('" + user.getUserUsername() + "','" + user.getUserPassword() + "')");
                writer.println(successfulUserCreation);
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    //metodo che serve per verificare se ho già un utente con tal nome e password nel db
    public void readyToVerifyUser() {
        gson = new Gson();
        String userSQLName = null;
        String userSQLPassword = null;
        try {
            writer.println(serverReadyToReceiveUserInfo);//Il server manda ClientThread che è pronto per la registrazione
            String userString = reader.readLine();
            System.out.println(userString);
            user = gson.fromJson(userString, User.class);//deserializzo il gson
            System.out.println(user);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE username='" + user.getUserUsername() + "' AND password='" + user.getUserPassword() + "'");
            while(resultSet.next()){
                userSQLName = resultSet.getString(username);
                userSQLPassword = resultSet.getString(password);
            }
            if (userSQLName != null && userSQLPassword != null){
                System.out.println("Utente di nome " + userSQLName + " e password " + userSQLPassword + " trovato\nLogin effettuato con successo");
                writer.println(successfulAuthentication);
                serverStart.insertNameInArrayList(userSQLName);
                serverStart.refreshClientConnected(userSQLName);
            } else {
                System.out.println("Utente non trovato");
                writer.println(userNotFound);
            }
        } catch (SQLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeFirstPlayerInGame(){
        System.out.println(mySticker);
        System.out.println(opponentSticker);
        if (mySticker != null && opponentSticker != null) {
            Random randomNumber = new Random();
            boolean numberOfFirstPlayer = randomNumber.nextBoolean();
            serverStart.startGameWithRandomChoice(numberOfFirstPlayer);
        }
    }

    //getter and setter
    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public void setOpponentSticker(Sticker opponentSticker) {
        this.opponentSticker = opponentSticker;
    }

}
