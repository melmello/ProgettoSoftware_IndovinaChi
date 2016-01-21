package sample.Server.ServerClass;

import static sample.Utilities.Class.ConstantCodes.*;
import static sample.Utilities.Class.SecurityClass.*;

import com.google.gson.Gson;
import javafx.concurrent.Task;
import sample.Utilities.Class.CodeAndInformation;
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
    private ArrayList<ServerThread> threadsPlaying = new ArrayList<>();
    private ArrayList<ServerThread> threadsArrayList  = new ArrayList<>();
    private ArrayList<String> listOfClientConnected = new ArrayList<>();
    private Gson gson;

    //costruttore
    public ServerStart(ServerMain main) {
        this.main = main;
    }

    @Override
    protected Object call() throws Exception {
        System.out.println("Il server è connesso");//se sono qui vuol dire che si è connesso il server e ha generato un thread
        try {
            socketExchange = new ServerSocket(assignedPort);//istanzio il socket sulla tal porta su cui comunicherò
            Class.forName(driverMySql).newInstance();
            connection = DriverManager.getConnection(urlConnection, mySQLUsername, mySQLPassword);//mi collego al database
            System.out.println("Database connesso");
            assignedIp = Inet4Address.getLocalHost().getHostAddress();//ricavo l'IP globale del Server per stamparlo poi
            main.initialConfiguration(assignedIp); //chiamo il metodo che mi permette di collegarmi al Controller
            System.out.println("Sono il Server.\nIl mio indirizzo IP è: " + assignedIp + "\nLa mia porta è: " + assignedPort);
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
        threadsArrayList.get((positionInArrayList + 1) % 2).getWriter().println(SERVER_CHANGES_ROUND);
    }


    public void setOpponentSticker(Sticker mySticker, int positionInArrayList) {
        threadsArrayList.get((positionInArrayList + 1) % 2).setOpponentSticker(mySticker);
    }

    public void startGameWithRandomChoice(boolean numberOfFirstPlayer) {
        System.out.println(castingBooleanToInt(numberOfFirstPlayer));
        threadsArrayList.get(castingBooleanToInt(numberOfFirstPlayer)).getWriter().println(SERVER_CHANGES_ROUND);
    }

    public int castingBooleanToInt(Boolean myBoolean){
        int myInt = (myBoolean) ? 1 : 0;
        return myInt;
    }

    /** ok */
    public void insertNameInArrayList(String userSQLName) {
        System.out.println(userSQLName);
        listOfClientConnected.add(userSQLName);
    }

    /** ok */
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


    /** ok */
    public void refreshClientConnected(String usernameConnected) {
        main.printNumberOfClient(Integer.toString(listOfClientConnected.size()));
        //if (threadsArrayList.size() != 1) {
            for (int cont = 0; cont < threadsArrayList.size(); cont++) {
                if (threadsArrayList.get(cont).getUser().getUserUsername() != null && !threadsArrayList.get(cont).getUser().getUserUsername().equals(usernameConnected)) {
                    gson = new Gson();
                    String clientConnectedGson = gson.toJson(listOfClientConnected);
                    threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_REFRESHES_CONNECTED_CLIENT, clientConnectedGson));
                }
            }
        //}
    }

    /** ok */
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

    /** ok */
    public void removeClientDisconnectedFromLoginScreen() {
        System.out.println("Un client non ha loggato: è uscito dalla schermata di login");
    }

    public void sendingRequest(String opponent, String userRequest) {
        for (int cont = 0; cont < threadsArrayList.size(); cont++){
            if (threadsArrayList.get(cont).getUser().getUserUsername().equals(opponent)){
                threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_RECEIVED_GAME_REQUEST, userRequest));
            }
            if (threadsArrayList.get(cont).getUser().getUserUsername().equals(userRequest)){
                threadsPlaying.add(threadsArrayList.get(cont));
            }
        }
    }

    public void createTheGame(String userWhoAccepted) {
        for(int cont = 0; cont < threadsArrayList.size(); cont++){
            if (threadsArrayList.get(cont).getUser().getUserUsername().equals(userWhoAccepted)) {
                threadsPlaying.add(threadsArrayList.get(cont));
            }
        }
        for(int cont = 0; cont < threadsPlaying.size(); cont++){
            threadsPlaying.get(cont).getWriter().println(SERVER_ALLOWS_TO_GO_ON_GAME_SCREEN);
        }
    }

    //getter
    public ArrayList<String> getListOfClientConnected() {
        return listOfClientConnected;
    }

    public ArrayList<ServerThread> getThreadsPlaying() {
        return threadsPlaying;
    }

}