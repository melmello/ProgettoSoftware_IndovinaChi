package sample.Server.ServerClass;

import static sample.Utilities.Class.ConstantCodes.*;

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

    //costruttore
    public ServerThread(Socket socket, ServerStart serverStart, Connection connection, int positionInArrayList){
        this.socket = socket;
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
                gson = new Gson();
                String code = reader.readLine();
                codeAndInformation = gson.fromJson(code, CodeAndInformation.class);
                System.out.println(codeAndInformation.getCode() + " -> CODE");
                switch (codeAndInformation.getCode()){
                    //Il client manda il segnale che si è disconesso.
                    case (CLIENT_DISCONNECTING):{
                        serverStart.removeClientDisconnected(user.getUserUsername());
                        break;
                    }
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
                        readyToReceiveOtherClientName(codeAndInformation.getInformation());
                        break;
                    }
                    case (CLIENT_SAYS_OK_FOR_PLAYING):{
                        serverStart.createTheGame(codeAndInformation.getInformation());
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
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    private void readyToReceiveOtherClientName(String information) {
        serverStart.sendingRequest(information, getUser().getUserUsername());
    }

    private void communicateClientRating(String information) {
        System.out.println(information + " è il voto dello user " + getUser().getUserUsername());
        serverStart.sendRating(Double.parseDouble(information), getUser().getUserUsername());
    }

    private void readyToKnowQuerySticker(String information) {
        if(opponentSticker.getNicknameOfSticker().equals(information)){
            serverStart.swapGameArrayList(user.getUserUsername());
        }
    }

    //il server è pronto per ricevere la query dal client (il gson con parametri)
    private void readyToKnowQuery(String information) {
        gson = new Gson();
        Boolean choice;
        StickerQuery stickerQuery = gson.fromJson(information, StickerQuery.class);
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
                if (opponentSticker.isBeardColorBrownOfSticker() == stringToBool(stickerQuery.getSecondParameter())) {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                } else {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
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
                if (opponentSticker.getBeardTypeOfSticker().equals(stickerQuery.getSecondParameter())) {
                    choice = true;
                    settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                } else {
                    choice = false;
                    settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                }
                break;
            }
            case (FRECKLES_FOR_QUERY): {
                if (opponentSticker.isFreecklesOfSticker() == stringToBool(stickerQuery.getSecondParameter())) {
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
                if (opponentSticker.getContinentOfSticker().equals(stickerQuery.getSecondParameter())) {
                    choice = true;
                    settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                } else {
                    choice = false;
                    settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                }
                break;
            }
            case (CHAMPIONSHIP_FOR_QUERY): {
                if (opponentSticker.getChampionshipOfSticker().equals(stickerQuery.getSecondParameter())) {
                    choice = true;
                    settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
                } else {
                    choice = false;
                    settingQueryStringType(stickerQuery.getFirstParameter(), stickerQuery.getSecondParameter(), choice);
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
                if (opponentSticker.isHairTypeStraightOfSticker() == stringToBool(stickerQuery.getSecondParameter())) {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), stringToBool(stickerQuery.getSecondParameter()));
                } else {
                    settingQueryBooleanType(stickerQuery.getFirstParameter(), !stringToBool(stickerQuery.getSecondParameter()));
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

    //creo la query con due parametri di tipo stringa in cui all'interno cerco = o != a seconda della scelta passata
    private void settingQueryStringType(String firstParameter, String secondParameter, Boolean choice) {
        ResultSet resultSet;
        try {
            Statement statement = connection.createStatement();
            if (choice) {
                resultSet = statement.executeQuery("SELECT * FROM stickers WHERE " + firstParameter + " != '" + secondParameter + "'");//faccio la query con il nome ricevuto per creare la figurina
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
        serverStart.changingRoundOfClient(positionInArrayList);
    }

    //creo la query con due parametri, uno stringa e uno booleano e cerco = o ! non più in questo metodo ma nella chiamata
    private void settingQueryBooleanType(String firstParameter, Boolean secondParameter){
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
    }

    //metodo di casting da Stringa a Boolean
    private static boolean stringToBool(String stringConvertion) {
        if (stringConvertion.equals(TRUEANSWER_FOR_QUERY))
            return true;
        if (stringConvertion.equals(FALSEANSWER_FOR_QUERY))
            return false;
        throw new IllegalArgumentException(stringConvertion + " non è booleano");
    }

    //metodo che serve per sapere che sticker ha scelto l'utente
    private void readyToKnowStickerInfo(String information) {
        try {
            System.out.println(information + " -> information");
            String stickerName = FilenameUtils.getName(information);//prendo l'ultima parte del path
            stickerName = FilenameUtils.removeExtension(stickerName);//tolgo l'estensione
            System.out.println(stickerName + " -> stickerName");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM stickers WHERE nickname='" + stickerName + "'");//faccio la query con il nome ricevuto per creare la figurina
            mySticker = new Sticker(null);//creo un elemento sticker vuoto
            while (resultSet.next()) {//popolo il mio oggetto mySticker con i campi che ho trovato nel database con chiave quella passata dal client
                mySticker.setNameOfSticker(resultSet.getString(NAME_FOR_QUERY));
                mySticker.setNicknameOfSticker(resultSet.getString(NICKNAME_FOR_QUERY));
                mySticker.setSurnameOfSticker(resultSet.getString(SURNAME_FOR_QUERY));
                mySticker.setHairColorBrownOfSticker(resultSet.getBoolean(HAIRCOLORBROWN_FOR_QUERY));
                mySticker.setHairLengthOfSticker(resultSet.getString(HAIRLENGTH_FOR_QUERY));
                mySticker.setBeardColorBrownOfSticker(resultSet.getBoolean(BEARDCOLORBROWN_FOR_QUERY));
                mySticker.setEyesColorBrownOfSticker(resultSet.getBoolean(EYESCOLORBROWN_FOR_QUERY));
                mySticker.setComplexionBrownOfSticker(resultSet.getBoolean(COMPLEXIONBROWN_FOR_QUERY));
                mySticker.setEarringsOfSticker(resultSet.getBoolean(EARRINGS_FOR_QUERY));
                mySticker.setGlassesOfSticker(resultSet.getBoolean(GLASSES_FOR_QUERY));
                mySticker.setHeadbandOfSticker(resultSet.getBoolean(HEADBAND_FOR_QUERY));
                mySticker.setMoleOfSticker(resultSet.getBoolean(MOLE_FOR_QUERY));
                mySticker.setBeardTypeOfSticker(resultSet.getString(BEARDTYPE_FOR_QUERY));
                mySticker.setFreecklesOfSticker(resultSet.getBoolean(FRECKLES_FOR_QUERY));
                mySticker.setNationalShirtOfSticker(resultSet.getBoolean(NATIONALSHIRT_FOR_QUERY));
                mySticker.setContinentOfSticker(resultSet.getString(CONTINENT_FOR_QUERY));
                mySticker.setChampionshipOfSticker(resultSet.getString(CHAMPIONSHIP_FOR_QUERY));
                mySticker.setCaptainBandOfSticker(resultSet.getBoolean(CAPTAINBAND_FOR_QUERY));
                mySticker.setNoseDimensionBigOfSticker(resultSet.getBoolean(NOSEDIMENSIONBIG_FOR_QUERY));
                mySticker.setSmileOfSticker(resultSet.getBoolean(SMILE_FOR_QUERY));
                mySticker.setHairTypeStraightOfSticker(resultSet.getBoolean(HAIRTYPESTRAIGHT_FOR_QUERY));
                mySticker.setBeardLengthOfSticker(resultSet.getString(BEARDLENGTH_FOR_QUERY));
            }
            System.out.println(mySticker + " -> mySticker");
            serverStart.setOpponentSticker(mySticker, positionInArrayList);
            changeFirstPlayerInGame();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //metodo che serve per la creazione di un nuovo utente
    private void readyToCreateNewUser(String information) {
        gson = new Gson();
        String userSQLName = null;//username tratto dalla query
        String userSQLPassword = null;//password tratta dalla query
        try{
            user = gson.fromJson(information, User.class);
            System.out.println(user);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE username='" + user.getUserUsername() + "'");//query per verificare se un utente è già presente
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

    //metodo che serve per verificare se ho già un utente con tal nome e password nel db
    public void readyToVerifyUser(String information) {
        gson = new Gson();
        String userSQLName = null;
        String userSQLPassword = null;
        Boolean userAlreadyLoggedBoolean = false;
        try {
            user = gson.fromJson(information, User.class);
            System.out.println(user + " -> user");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE username='" + user.getUserUsername() + "' AND password='" + user.getUserPassword() + "'");
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

    public void changeFirstPlayerInGame(){
        if (mySticker != null && opponentSticker != null) {
            Random randomNumber = new Random();
            boolean numberOfFirstPlayer = randomNumber.nextBoolean();
            serverStart.startGameWithRandomChoice(numberOfFirstPlayer);
        }
    }

    //getter and setter
    public BufferedReader getReader() {return reader;}

    public void setReader(BufferedReader reader) {this.reader = reader;}

    public PrintWriter getWriter() {return writer;}

    public void setWriter(PrintWriter writer) {this.writer = writer;}

    public void setOpponentSticker(Sticker opponentSticker) {this.opponentSticker = opponentSticker;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

}