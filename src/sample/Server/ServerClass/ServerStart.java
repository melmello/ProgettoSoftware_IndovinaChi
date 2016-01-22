package sample.Server.ServerClass;

import static sample.Utilities.Class.ConstantCodes.*;
import static sample.Utilities.Class.SecurityClass.*;
import com.google.gson.Gson;
import javafx.concurrent.Task;
import sample.Utilities.Class.CodeAndInformation;
import sample.Utilities.Class.Game;
import sample.Utilities.Class.Sticker;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Properties;

public class ServerStart extends Task {

    private String assignedIp;
    private ServerSocket socketExchange;
    private ServerMain main;
    private Connection connection;
    //private ArrayList<ServerThread> threadsPlaying = new ArrayList<>();
    private ArrayList<ServerThread> threadsArrayList  = new ArrayList<>();
    private ArrayList<String> listOfClientConnected = new ArrayList<>();
    private ArrayList<Game> gameListView = new ArrayList<>();
    private Gson gson;

    //costruttore
    public ServerStart(ServerMain main) {
        this.main = main;
    }

    @Override
    protected Object call() throws Exception {
        System.out.println("Il server è connesso");//se sono qui vuol dire che si è connesso il server e ha generato un thread
        try {
            socketExchange = new ServerSocket(ASSIGNED_PORT_SOCKET);//istanzio il socket sulla tal porta su cui comunicherò
            Class.forName(DRIVER_MYSQL).newInstance();
            connection = DriverManager.getConnection(CONNECTION_URL_MYSQL, mySQLUsername, mySQLPassword);//mi collego al database
            System.out.println("Database connesso");
            assignedIp = Inet4Address.getLocalHost().getHostAddress();//ricavo l'IP globale del Server per stamparlo poi
            main.initialConfiguration(assignedIp); //chiamo il metodo che mi permette di collegarmi al Controller
            System.out.println("Sono il Server.\nIl mio indirizzo IP è: " + assignedIp + "\nLa mia porta è: " + ASSIGNED_PORT_SOCKET);
            main.printNumberOfClient(Integer.toString(listOfClientConnected.size()));
            while (true) {//while true del Thread in cui istanzia un thread per ogni client che si connette
                Socket serverSocket = socketExchange.accept();
                threadsArrayList.add(new ServerThread(serverSocket, this, connection, threadsArrayList.size()));//ogni volta che creo un Thread lo inserisco nell'ultimo posto dell'array
                Thread threadTask = new Thread(threadsArrayList.get(threadsArrayList.size()-1));//creo un threadTask con l'ultimo elemento dell'array
                threadTask.start();//faccio partire l'ultimo thread inserito
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //metodo per il cambio di turno
    public void changingRoundOfClient(int positionInArrayList){
        threadsArrayList.get((positionInArrayList + 1) % 2).getWriter().println(CodeAndInformation.serializeToJson(SERVER_CHANGES_ROUND, null));
    }

    public void setOpponentSticker(Sticker mySticker, int positionInArrayList) {
        threadsArrayList.get((positionInArrayList + 1) % 2).setOpponentSticker(mySticker);
    }

    public void startGameWithRandomChoice(boolean numberOfFirstPlayer) {
        threadsArrayList.get(castingBooleanToInt(numberOfFirstPlayer)).getWriter().println(CodeAndInformation.serializeToJson(SERVER_CHANGES_ROUND, null));
    }

    public int castingBooleanToInt(Boolean myBoolean){
        int myInt = (myBoolean) ? 1 : 0;
        return myInt;
    }

    public void insertNameInArrayList(String userSQLName) {
        System.out.println(userSQLName);
        listOfClientConnected.add(userSQLName);
    }

    public void removeClientDisconnected(String usernameDisconnected) {
        listOfClientConnected.remove(usernameDisconnected);
        for (int counter = 0; counter < threadsArrayList.size(); counter++){
            if (threadsArrayList.get(counter).getUser().getUserUsername().equals(usernameDisconnected)){
                threadsArrayList.remove(counter);
            }
        }
        refreshClientConnected(usernameDisconnected);
        System.out.println("Il client " + usernameDisconnected + " si è disconnesso");
    }

    public void refreshClientConnected(String usernameConnected) {
        main.printNumberOfClient(Integer.toString(listOfClientConnected.size()));
            for (int cont = 0; cont < threadsArrayList.size(); cont++) {
                if (threadsArrayList.get(cont).getUser().getUserUsername() != null && !threadsArrayList.get(cont).getUser().getUserUsername().equals(usernameConnected)) {
                    gson = new Gson();
                    String clientConnectedGson = gson.toJson(listOfClientConnected);
                    threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_REFRESHES_CONNECTED_CLIENT, clientConnectedGson));
                }
            }
    }

    public void sendRating(Double clientRating, String clientUsername) {
        final String username = googleMail;
        final String password = googlePassword;

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(googleMail));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(googleTrueMail));
            message.setSubject("RATING PROGETTO SOFTWARE");
            message.setText("Ciao, il tuo voto ricevuto è: " + clientRating + " \nInviato da user: " + clientUsername);
            Transport.send(message);
            System.out.println("Messaggio inviato");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeClientDisconnectedFromLoginScreen() {
        System.out.println("Un client non ha loggato: è uscito dalla schermata di login");
    }

    public void sendingRequest(String opponent, String userRequest) {
        int matchNumber = 0;
        for (int cont = 0; cont < threadsArrayList.size(); cont++){
            if (threadsArrayList.get(cont).getUser().getUserUsername().equals(opponent)){
                gson = new Gson();
                ArrayList<String> arrayListToSend = new ArrayList<>();
                arrayListToSend.add(userRequest);
                arrayListToSend.add(Integer.toString(matchNumber));
                String userAndMatchNumber = gson.toJson(arrayListToSend);
                threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_RECEIVED_GAME_REQUEST, userAndMatchNumber));
            }
            if (threadsArrayList.get(cont).getUser().getUserUsername().equals(userRequest)){
                gameListView.add(new Game(threadsArrayList.get(cont).getUser().getUserUsername(), null));
            }
        }
    }

    public void createTheGame(String userWhoAccepted) {
        for(int cont = 0; cont < threadsArrayList.size(); cont++){
            if (threadsArrayList.get(cont).getUser().getUserUsername().equals(userWhoAccepted)) {
                gameListView.add(threadsArrayList.get(cont));
            }
        }
        for(int cont = 0; cont < threadsPlaying.size(); cont++){
            gameListView.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_ALLOWS_TO_GO_ON_GAME_SCREEN, null));
        }
    }

    //TODO
    public void cancelTheGame(String information) {

    }

    //getter
    public ArrayList<String> getListOfClientConnected() {
        return listOfClientConnected;
    }

}