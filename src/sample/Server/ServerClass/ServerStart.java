package sample.Server.ServerClass;

import static sample.Utilities.Class.ConstantCodes.*;
import static sample.Utilities.Class.SecurityClass.*;
import javafx.concurrent.Task;
import sample.Utilities.Class.Sticker;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Properties;

public class ServerStart extends Task {

    private int clientNumber = 0;
    private String assignedIp;
    private ServerSocket socketExchange;
    private ServerMain serverMain;
    private Connection connection;
    private ArrayList<ServerThread> threadsPlaying = new ArrayList<>();
    private ArrayList<ServerThread> threadsArrayList  = new ArrayList<>();
    private ArrayList<String> listOfClientConnected = new ArrayList<>();

    //costruttore
    public ServerStart(ServerMain serverMain) {
        this.serverMain = serverMain;
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
            serverMain.initialConfiguration(assignedIp); //chiamo il metodo che mi permette di collegarmi al Controller
            System.out.println("Sono il Server.\nIl mio indirizzo IP è: " + assignedIp + "\nLa mia porta è: " + assignedPort);
            printNumberOfClient();
            while (true) {//while true del Thread in cui istanzia un thread per ogni client che si connette
                Socket serverSocket = socketExchange.accept();
                threadsArrayList.add(new ServerThread(serverSocket, clientNumber, this, connection, threadsArrayList.size()));//ogni volta che creo un Thread lo inserisco nell'ultimo posto dell'array
                Thread threadTask = new Thread(threadsArrayList.get(threadsArrayList.size()-1));//creo un threadTask con l'ultimo elemento dell'array
                threadTask.start();//faccio partire l'ultimo thread inserito
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void printNumberOfClient() {
        serverMain.printNumberOfClient(Integer.toString(listOfClientConnected.size()));
    }

    //metodo per il cambio di turno
    public void changingRoundOfClient(int positionInArrayList){
        threadsArrayList.get((positionInArrayList + 1) % 2).getWriter().println(changingRound);
    }

    public void setOpponentSticker(Sticker mySticker, int positionInArrayList) {
        threadsArrayList.get((positionInArrayList + 1) % 2).setOpponentSticker(mySticker);
    }

    public void startGameWithRandomChoice(boolean numberOfFirstPlayer) {
        System.out.println(castingBooleanToInt(numberOfFirstPlayer));
        threadsArrayList.get(castingBooleanToInt(numberOfFirstPlayer)).getWriter().println(changingRound);
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
        System.out.println("Client disconnesso");
    }


    public void refreshClientConnected(String usernameConnected) {
        printNumberOfClient();
        System.out.println(usernameConnected);
        System.out.println(threadsArrayList.size());
        if (threadsArrayList.size() != 1) {
            for (int cont = 0; cont < threadsArrayList.size(); cont++) {
                if (threadsArrayList.get(cont).getUser().getUserUsername() != null && !threadsArrayList.get(cont).getUser().getUserUsername().equals(usernameConnected)) {
                    threadsArrayList.get(cont).getWriter().println(serverWantsToRefreshClientConnected);
                }
            }
        }
    }

    //getter
    public ArrayList<String> getListOfClientConnected() {
        return listOfClientConnected;
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
            message.setText("Ciao, il tuo voto ricevuto è: " + clientRating + " \n Inviato da user: " + clientUsername);
            Transport.send(message);
            System.out.println("Done");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    public void removeClientDisconnectedFromLoginScreen() {
        System.out.println("Un client non ha loggato");
    }

    public void sendingRequest(String opponent, String userRequest) {
        for (int cont = 0; cont < threadsArrayList.size(); cont++){
            if (threadsArrayList.get(cont).getUser().getUserUsername().equals(opponent)){
                threadsArrayList.get(cont).getWriter().println(receivedGameRequest);
            }
            if (threadsArrayList.get(cont).getUser().getUserUsername().equals(userRequest)){
                threadsPlaying.add(threadsArrayList.get(cont));
            }
        }
    }

    public ArrayList<ServerThread> getThreadsPlaying() {
        return threadsPlaying;
    }

    public void createTheGame(String userWhoAccepted) {
        for(int cont = 0; cont < threadsArrayList.size(); cont++){
            if (threadsArrayList.get(cont).getUser().getUserUsername().equals(userWhoAccepted)) {
                threadsPlaying.add(threadsArrayList.get(cont));
            }
        }
        for(int cont = 0; cont < threadsPlaying.size(); cont++){
            threadsPlaying.get(cont).getWriter().println(goToGameScreen);
        }
    }
}