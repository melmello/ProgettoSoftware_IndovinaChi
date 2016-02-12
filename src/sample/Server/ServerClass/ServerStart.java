package sample.Server.ServerClass;

/** @author Giulio Melloni
 * Questa classe rappresenta il cuore vero e proprio del server. E' qui che vengono svolti i compiti da server, ossia la comunicazione coi client,
 * la gestione delle partite, la gestione dell'interfaccia con liste di giocatori connessi e in gioco per ogni utente.
 * Se il main si occupava della parte grafica, ServerStart si occupa della gestione di più client, diversamente da ServerThread che si occupa della gestione del thread singolo.
 */

import static sample.Utilities.Class.ConstantCodes.*;
import static sample.Utilities.Class.SecurityClass.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class ServerStart extends Task {

    private String assignedIp;
    private ServerSocket socketExchange;
    private ServerMain main;
    private Connection connection;
    private ArrayList<String> threadsPlaying = new ArrayList<>();
    private ArrayList<ServerThread> threadsArrayList  = new ArrayList<>();
    private ArrayList<String> listOfClientConnected = new ArrayList<>();
    private ArrayList<Game> gameArrayList = new ArrayList<>();
    private Gson gson;

    /** Costruttore di ServerStart (che serve per poter successivamente collegare ServerStart e Main).
     * @param main serve per collegare il main con ServerStart (che quando è stato creato è stato creato passandogli l'istanza).
     */
    public ServerStart(ServerMain main) {
        this.main = main;
    }

    /** Metodo che è chiamato non appena nel main faccio thread.start(). La sua importanza è notevole in quanto in esso:
     * - istanzio un oggetto di tipo ServerSocket che mi servirà per la comunicazione col client.
     * - creo la connessione al server-database mysql dandogli l'url di connessione, username e password.
     * - resto nel while (true) finchè non mi si connette un client, che di conseguenza lo aggiungerò all'array di Thread e farò partire.
     * - stampare a schermo l'ip del server.
     * @return null siccome non deve ritornare niente la call di un thread.
     * @throws Exception classe utilizzata per evitare eccezioni a runtime, come il NullPointerException o il RuntimeException. E' come se l'intero metodo fosse circondato da un try catch con ogni eccezione.
     */
    @Override
    protected Object call() throws Exception {
        System.out.println("Il server è connesso");//se sono qui vuol dire che si è connesso il server e ha generato un thread
        try {
            socketExchange = new ServerSocket(ASSIGNED_PORT_SOCKET);//istanzio il socket sulla tal porta su cui comunicherò
            Class.forName(DRIVER_MYSQL).newInstance();
            connection = DriverManager.getConnection(CONNECTION_URL_MYSQL, MYSQL_USERNAME, MYSQL_PASSWORD);//mi collego al database
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

    /** Metodo per il cambio di turno dei giocatori. Qui mando all'altro client che può giocare e setto l'OpponentSticker come lo sticker del giocatore che ha passato.
     * @param positionInArrayList posizione all'interno della gameArrayList, utile per ritrovare in fretta, senza bisogno di un for, la partita in corso.
     * @param usernameToChange username del giocatore che ha finito il turno e passa.
     * @param mySticker sticker del giocatore che ha finito il turno e passa.
     */
    public void changingRoundOfClient(int positionInArrayList, String usernameToChange, Sticker mySticker){
        if (gameArrayList.get(positionInArrayList).getPlayer1().getUser().getUserUsername().equals(usernameToChange)){
            gameArrayList.get(positionInArrayList).getPlayer2().getWriter().println(CodeAndInformation.serializeToJson(SERVER_CHANGES_ROUND, null));
            gameArrayList.get(positionInArrayList).getPlayer2().setOpponentSticker(mySticker);
        } else if (gameArrayList.get(positionInArrayList).getPlayer2().getUser().getUserUsername().equals(usernameToChange)) {
            gameArrayList.get(positionInArrayList).getPlayer1().getWriter().println(CodeAndInformation.serializeToJson(SERVER_CHANGES_ROUND, null));
            gameArrayList.get(positionInArrayList).getPlayer1().setOpponentSticker(mySticker);
        }
    }

    /** Metodo che serve per settare lo stickerAvversario inizialmente quando un giocatore sceglie per la prima volta lo sticker.
     * @param mySticker sticker scelto dal primo personaggio che sarà settato come sticker del rivale.
     * @param positionInArrayList posizione all'interno della gameArrayList, utile per ritrovare in fretta, senza bisogno di un for, la partita in corso.
     * @param usernameToChange username del giocatore che ha finito il turno e passa.
     */
    public void setOpponentSticker(Sticker mySticker, int positionInArrayList, String usernameToChange) {
        System.out.println(positionInArrayList + " -> POSITION SERVERSTART");
        if (usernameToChange.equals(gameArrayList.get(positionInArrayList).getPlayer1().getUser().getUserUsername())) {
            gameArrayList.get(positionInArrayList).setSticker1(mySticker);
            gameArrayList.get(positionInArrayList).getPlayer2().setOpponentSticker(mySticker);
        } else if (usernameToChange.equals(gameArrayList.get(positionInArrayList).getPlayer2().getUser().getUserUsername())){
            gameArrayList.get(positionInArrayList).setSticker2(mySticker);
            gameArrayList.get(positionInArrayList).getPlayer1().setOpponentSticker(mySticker);
        }
    }

    /** Metodo che sceglie (casualmente) chi è il primo a partire e manda al giocatore scelto che è il suo turno.
     * @param numberOfFirstPlayer player scelto (o il numero 0, quindi Player1 o il numero 1 quindi Player2).
     * @param positionInArrayList posizione all'interno della gameArrayList, utile per ritrovare in fretta, senza bisogno di un for, la partita in corso.
     */
    public void startGameWithRandomChoice(boolean numberOfFirstPlayer, int positionInArrayList) {
        if (numberOfFirstPlayer){
            gameArrayList.get(positionInArrayList).getPlayer1().getWriter().println(CodeAndInformation.serializeToJson(SERVER_CHANGES_ROUND, null));
        } else if (!numberOfFirstPlayer){
            gameArrayList.get(positionInArrayList).getPlayer2().getWriter().println(CodeAndInformation.serializeToJson(SERVER_CHANGES_ROUND, null));
        }
    }

    /** Metodo che serve per inserire all'interno della lista di client connessi (lista di stringhe) il nome del giocatore che si è appena connesso.
     * @param userSQLName nome del giocatore che si è appena connesso.
     */
    public void insertNameInArrayList(String userSQLName) {
        System.out.println(userSQLName + " -> userSQLName");
        listOfClientConnected.add(userSQLName);
    }

    /** Metodo che serve semplicemente per stampare che un client non si è neanche loggato all'interno del gioco, ma ha chiuso la schermata di login.
     */
    public void removeClientDisconnectedFromLoginScreen() {
        System.out.println("Un client non ha loggato: è uscito dalla schermata di login");
    }

    /** Metodo che serve per rimuovere il client che si è disconnesso. Esso viene rimosso sia dalla listaClientConnessi (String) che dall'ArrayList di Thread.
     * Inoltre viene chiamata {@link #refreshClientConnected(String)}
     * @param information è il nome del client che si è disconnesso.
     */
    public void removeClientDisconnected(String information) {
        listOfClientConnected.remove(information);
        for (int cont = 0; cont < threadsArrayList.size(); cont++){
            if (threadsArrayList.get(cont).getUser().getUserUsername().equals(information)){
                threadsArrayList.remove(cont);
            }
        }
        refreshClientConnected(information);
        System.out.println("Il client " + information + " si è disconnesso");
    }

    /** Metodo che server per rimuovere un client quando è in gioco e quitta, assegnando così la vittoria all'altro giocatore.
     * Viene quindi cercato con un for il thread e una volta individuato viene rimossa la partita, viene rimosso dall'array di Thread, viene mandato a tutti di refreshare i client connessi e viene mandato all'altro player che ha vinto la partita.
     * @param information è il nome del client che si è disconnesso.
     */
    public void removeClientInGame(String information) {
        for(int cont = 0; cont < gameArrayList.size(); cont++){
            if (gameArrayList.get(cont).getPlayer1().getUser().getUserUsername().equals(information)){
                insertNewMatchInLeaderboard(gameArrayList.get(cont).getPlayer2().getUser().getUserUsername(), information);
                gameArrayList.get(cont).getPlayer2().getWriter().println(CodeAndInformation.serializeToJson(SERVER_HAPPY_FOR_YOUR_WIN, information));
                refreshLeaderboardSearchingUsername();
                threadsArrayList.add(gameArrayList.get(cont).getPlayer2());
                listOfClientConnected.add(gameArrayList.get(cont).getPlayer2().getUser().getUserUsername());
                threadsPlaying.remove(gameArrayList.get(cont).getPlayer2().getUser().getUserUsername());
                threadsPlaying.remove(information);
                gameArrayList.get(cont).setPlayer1(null);
                gameArrayList.get(cont).setPlayer2(null);
                gameArrayList.get(cont).setSticker1(null);
                gameArrayList.get(cont).setSticker2(null);
                gameArrayList.remove(cont);
            } else if (gameArrayList.get(cont).getPlayer2().getUser().getUserUsername().equals(information)){
                insertNewMatchInLeaderboard(gameArrayList.get(cont).getPlayer1().getUser().getUserUsername(), information);
                gameArrayList.get(cont).getPlayer1().getWriter().println(CodeAndInformation.serializeToJson(SERVER_HAPPY_FOR_YOUR_WIN, information));
                refreshLeaderboardSearchingUsername();
                threadsArrayList.add(gameArrayList.get(cont).getPlayer1());
                listOfClientConnected.add(gameArrayList.get(cont).getPlayer1().getUser().getUserUsername());
                threadsPlaying.remove(gameArrayList.get(cont).getPlayer1().getUser().getUserUsername());
                threadsPlaying.remove(information);
                gameArrayList.get(cont).setPlayer1(null);
                gameArrayList.get(cont).setPlayer2(null);
                gameArrayList.get(cont).setSticker1(null);
                gameArrayList.get(cont).setSticker2(null);
                gameArrayList.remove(cont);
            }
        }
        main.printNumberOfClient(Integer.toString(listOfClientConnected.size()));
    }

    /** Metodo che serve per rimettere i giocatori in gioco nei giocatori connessi ed eliminare la partita in corso. Viene anche fatto l'inserimento della partita nel database e comunicato ai giocatori chi ha vinto e chi ha perso.
     * Ricerco quindi grazie all'username di chi ha vinto la partita, setto i campi winner e loser e rimetto i Thread nell'array di client connessi e li tolgo dall'array di thread in gioco e dall'array di partite.
     * Faccio, quindi, una INSERT nel database con cui aggiorno con l'ultimo risultato la leaderboard. Mando quindi a tutti di refreshare le leaderboard e ai due sfidanti mando il segnale WIN or LOSE a seconda dell'esito.
     * @param usernameWhoWin utente che ha vinto la partita.
     */
    public void swapGameArrayList(String usernameWhoWin) {
        String winner = null;
        String loser = null;
        for(int cont = 0; cont < gameArrayList.size(); cont++){
            if (gameArrayList.get(cont).getPlayer1().getUser().getUserUsername().equals(usernameWhoWin) || gameArrayList.get(cont).getPlayer2().getUser().getUserUsername().equals(usernameWhoWin)){
                threadsArrayList.add(gameArrayList.get(cont).getPlayer1());
                threadsArrayList.add(gameArrayList.get(cont).getPlayer2());
                listOfClientConnected.add(gameArrayList.get(cont).getPlayer1().getUser().getUserUsername());
                listOfClientConnected.add(gameArrayList.get(cont).getPlayer2().getUser().getUserUsername());
                if (gameArrayList.get(cont).getPlayer1().getUser().getUserUsername().equals(usernameWhoWin)){
                    winner = gameArrayList.get(cont).getPlayer1().getUser().getUserUsername();
                    loser = gameArrayList.get(cont).getPlayer2().getUser().getUserUsername();
                } else {
                    loser = gameArrayList.get(cont).getPlayer1().getUser().getUserUsername();
                    winner = gameArrayList.get(cont).getPlayer2().getUser().getUserUsername();
                }
                threadsPlaying.remove(gameArrayList.get(cont).getPlayer1().getUser().getUserUsername());
                threadsPlaying.remove(gameArrayList.get(cont).getPlayer2().getUser().getUserUsername());
                gameArrayList.get(cont).setPlayer1(null);
                gameArrayList.get(cont).setPlayer2(null);
                gameArrayList.get(cont).setSticker1(null);
                gameArrayList.get(cont).setSticker2(null);
                gameArrayList.remove(cont);
            }
        }
        System.out.println("THE WINNER IS " + winner);
        System.out.println("THE LOSER IS " + loser);
        try {
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO leaderboard(winner, loser) VALUES ('" + winner + "','" + loser + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        gson = new Gson();
        String clientConnectedGson = gson.toJson(listOfClientConnected);
        gson = new Gson();
        String clientInGameGson = gson.toJson(threadsPlaying);
        refreshLeaderboardSearchingUsername();
        for (int cont = 0; cont < threadsArrayList.size(); cont++){
            if(threadsArrayList.get(cont).getUser().getUserUsername().equals(winner)){
                threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_HAPPY_FOR_YOUR_WIN, loser));
            } else if (threadsArrayList.get(cont).getUser().getUserUsername().equals(loser)){
                threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_SAD_FOR_YOUR_DEFEAT, winner));
            } else {
                threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_REFRESHES_CONNECTED_CLIENT, clientConnectedGson));
                threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_REFRESHES_IN_GAME_CLIENT, clientInGameGson));
            }
        }
        main.printNumberOfClient(Integer.toString(listOfClientConnected.size()));
    }

    /** Metodo che invia a qualsiasi altro client connesso di refreshare la schermata coi client connessi e i client in game, ovviamente a tutti tranne all'username in questione.
     * @param usernameDisconnected è il nome del client che si è disconnesso.
     */
    public void refreshClientConnected(String usernameDisconnected) {
        main.printNumberOfClient(Integer.toString(listOfClientConnected.size()));
        gson = new Gson();
        String clientConnectedGson = gson.toJson(listOfClientConnected);
        gson = new Gson();
        String clientInGameGson = gson.toJson(threadsPlaying);
        for (int cont = 0; cont < threadsArrayList.size(); cont++) {
            if (threadsArrayList.get(cont).getUser() != null && threadsArrayList.get(cont).getUser().getUserUsername() != null && !threadsArrayList.get(cont).getUser().getUserUsername().equals(usernameDisconnected)) {
                threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_REFRESHES_CONNECTED_CLIENT, clientConnectedGson));
                threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_REFRESHES_IN_GAME_CLIENT, clientInGameGson));
            }
        }
    }

    /** Metodo che manda a ogni client connesso di refreshare la leaderboard e chiama {@link #refreshLeaderboard(String)}.
     */
    public void refreshLeaderboardSearchingUsername(){
        for (int cont = 0; cont < threadsArrayList.size(); cont++){
            refreshLeaderboard(threadsArrayList.get(cont).getUser().getUserUsername());
        }
    }

    /** Metodo chiamato passando il nome del client in questione e ad esso gli viene inviato di aggiornare la leaderboard con i dati in questione.
     * @param information nome dell'utente che deve refreshare la leaderboard.
     */
    public void refreshLeaderboard(String information){
        ArrayList<String> leaderboardArray = queryingForLeaderboard(information);
        for (int cont = 0; cont < threadsArrayList.size(); cont++) {
            if (threadsArrayList.get(cont).getUser() != null && threadsArrayList.get(cont).getUser().getUserUsername() != null && threadsArrayList.get(cont).getUser().getUserUsername().equals(information)) {
                threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_REFRESHES_PERSONAL_LEADERBOARD, leaderboardArray.get(0)));
                threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_REFRESHES_WORLD_LEADERBOARD, leaderboardArray.get(1)));
            }
        }
    }

    /** Metodo chiamato per refreshare per la PRIMA VOLTA i client connessi e i client in gioco e le leaderboard quando carico la schermata di Choice.
     * @param information nome dell'utente che deve refreshare tutte e 4 le listview.
     */
    public void refreshClientConnectedForTheFirstTime(String information) {
        gson = new Gson();
        String clientConnectedGson = gson.toJson(listOfClientConnected);
        gson = new Gson();
        String clientInGameGson = gson.toJson(threadsPlaying);
        ArrayList<String> leaderboardArray = queryingForLeaderboard(information);
        for (int cont = 0; cont < threadsArrayList.size(); cont++) {
            if (threadsArrayList.get(cont).getUser() != null && threadsArrayList.get(cont).getUser().getUserUsername() != null && threadsArrayList.get(cont).getUser().getUserUsername().equals(information)) {
                threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_REFRESHES_CONNECTED_CLIENT_FOR_THE_FIRST_TIME, clientConnectedGson));
                threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_REFRESHES_IN_GAME_CLIENT, clientInGameGson));
                threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_REFRESHES_PERSONAL_LEADERBOARD, leaderboardArray.get(0)));
                threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_REFRESHES_WORLD_LEADERBOARD, leaderboardArray.get(1)));
            }
        }
        main.printNumberOfClient(Integer.toString(listOfClientConnected.size()));
    }

    /** Metodo che mi serve per aggiornare il database con l'ultima vittoria e sconfitta di questi giocatori.
     * @param winner è il nome del vincitore.
     * @param loser è il nome dello sconfitto.
     */
    public void insertNewMatchInLeaderboard(String winner, String loser){
        try {
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO leaderboard(winner,loser) VALUES ('" + winner + "','" + loser + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Metodo utilizzato per costruire i due array con le partite vinte e con le partite perse, e con la classifica generale.
     * Qui è dove faccio le relative query per la leaderboard personale e la leaderboard mondiale e le inserisco all'interno dell'array.
     * @param information è il nome dell'utente a cui verranno fatte visualizzare le sue informazioni.
     * @return returnArrayList è un arrayList che contiene due arrayList, uno per la leaderboardPersonale (a sua volta diviso in due arraylist: vittorie e sconfitte) e l'altro per la leaderboardMondiale.
     */
    private ArrayList<String> queryingForLeaderboard(String information) {
        ArrayList<String> personalMatchWon = new ArrayList<>();
        ArrayList<String> personalMatchLost = new ArrayList<>();
        ArrayList<String> worldMatch = new ArrayList<>();
        String personalMatchString = null;
        String worldMatchString = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM leaderboard WHERE winner = '" + information + "' OR loser = '" + information + "'");//faccio la query con il nome ricevuto per creare la figurina
            while (resultSet.next()){
                if (information.equals(resultSet.getString(WINNER_FOR_QUERY))){
                    personalMatchWon.add(resultSet.getString(LOSER_FOR_QUERY));
                } else if (information.equals(resultSet.getString(LOSER_FOR_QUERY))){
                    personalMatchLost.add(resultSet.getString(WINNER_FOR_QUERY));
                }
            }
            gson = new Gson();
            String personalMatchWonString = gson.toJson(personalMatchWon);
            gson = new Gson();
            String personalMatchLostString = gson.toJson(personalMatchLost);
            ArrayList<String> personalMatchArray = new ArrayList<>();
            personalMatchArray.add(personalMatchWonString);
            personalMatchArray.add(personalMatchLostString);
            gson = new Gson();
            personalMatchString = gson.toJson(personalMatchArray);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT winner, COUNT(*) AS CONTATORE FROM leaderboard GROUP BY winner ORDER BY CONTATORE DESC");
            while (resultSet.next()){
                worldMatch.add(resultSet.getString(WINNER_FOR_QUERY));
            }
            gson = new Gson();
            worldMatchString = gson.toJson(worldMatch);
        } catch (SQLException e){
            e.printStackTrace();
        }
        ArrayList<String> returnArrayList = new ArrayList<>();
        returnArrayList.add(personalMatchString);
        returnArrayList.add(worldMatchString);
        return returnArrayList;
    }

    /** Metodo utilizzato per comunicare via mail la recensione fatta da un utente dell'applicazione.
     * @param clientRating è il voto dato.
     * @param title è il titolo della recensione.
     * @param text è il testo della recensione.
     * @param clientUsername è il nome dell'utente che l'ha recensita.
     */
    public void sendRating(Double clientRating, String title, String text, String clientUsername) {
        final String username = DEVELOPMENT_MAIL;
        final String password = DEVELOPMENT_PASSWORD;
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
            message.setFrom(new InternetAddress(DEVELOPMENT_MAIL));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(GOOGLE_MAIL));
            message.setSubject("RATING PROGETTO SOFTWARE");
            message.setText("Ciao, il tuo voto ricevuto è: " + clientRating + " \nInviato da user: " + clientUsername + "\nLa sua recensione è stata:\nTitolo - \t" + title + "\nTesto - \t" + text);
            Transport.send(message);
            System.out.println("Messaggio inviato");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /** Metodo che viene chiamato quando un giocatore vuole inviare una richiesta di gioco ad un altro giocatore.
     * In questo metodo cerco con un for il nome dell'utente all'interno dell'array di partite e creo un nuovo game settando, per ora, a null il player 2 che non ha ancora risposto.
     * @param opponent è il nome dell'avversario che si vuole sfidare.
     * @param userRequest è il nome del client che ha lanciato la sfida.
     */
    public void sendingRequest(String opponent, String userRequest) {
        int matchNumber = gameArrayList.size();
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
                gameArrayList.add(new Game(threadsArrayList.get(cont), null));
                threadsArrayList.get(cont).setPositionInArrayList(matchNumber);
            }
        }
    }

    /** Metodo che viene chiamato ogni volta che un giocatore accetta una proposta di gioco dall'altro.
     * In questo caso rintraccio la partita che avevo prima creato e setto il player 2. Rimuovo quindi gli array dall'array di Thread connessi e li metto in quelli in gioco.
     * Mando, inoltre, ad entrambi che possono andare sulla schermata di gioco e dico a tutti di refreshare i client connessi.
     * @param information è il nome dell'utente che ha accettato la sfida e la posizione in gameArrayList
     */
    public void createTheGame(String information) {
        ArrayList<String> userAndNumber = gson.fromJson(information, new TypeToken<ArrayList<String>>() {}.getType());
        int currentMatchNumber = Integer.parseInt(userAndNumber.get(1));
        System.out.println(currentMatchNumber + " E' LA PARTITA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        for(int cont = 0; cont < threadsArrayList.size(); cont++){
            if (threadsArrayList.get(cont).getUser().getUserUsername().equals(userAndNumber.get(0))) {
                gameArrayList.get(currentMatchNumber).setPlayer2(threadsArrayList.get(cont));
            }
        }
        gameArrayList.get(currentMatchNumber).getPlayer1().getWriter().println(CodeAndInformation.serializeToJson(SERVER_ALLOWS_TO_GO_ON_GAME_SCREEN, null));
        gameArrayList.get(currentMatchNumber).getPlayer2().getWriter().println(CodeAndInformation.serializeToJson(SERVER_ALLOWS_TO_GO_ON_GAME_SCREEN, null));
        threadsPlaying.add(gameArrayList.get(currentMatchNumber).getPlayer1().getUser().getUserUsername());
        threadsPlaying.add(gameArrayList.get(currentMatchNumber).getPlayer2().getUser().getUserUsername());
        listOfClientConnected.remove(gameArrayList.get(currentMatchNumber).getPlayer1().getUser().getUserUsername());
        listOfClientConnected.remove(gameArrayList.get(currentMatchNumber).getPlayer2().getUser().getUserUsername());
        gson = new Gson();
        String threadsPlayingString = gson.toJson(threadsPlaying);
        gson = new Gson();
        String clientConnectedGson = gson.toJson(listOfClientConnected);
        int positionThreadMustBeRemoved[] = {-1, -1};
        for(int cont = 0; cont < threadsArrayList.size(); cont++){
            if (threadsArrayList.get(cont).getUser().getUserUsername().equals(gameArrayList.get(currentMatchNumber).getPlayer1().getUser().getUserUsername())){
                positionThreadMustBeRemoved[0] = cont;
            } else if (threadsArrayList.get(cont).getUser().getUserUsername().equals(gameArrayList.get(currentMatchNumber).getPlayer2().getUser().getUserUsername())) {
                positionThreadMustBeRemoved[1] = cont;
            } else {
                threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_REFRESHES_CONNECTED_CLIENT, clientConnectedGson));
                threadsArrayList.get(cont).getWriter().println(CodeAndInformation.serializeToJson(SERVER_REFRESHES_IN_GAME_CLIENT, threadsPlayingString));
            }
        }
        for (int cont = 0; cont < threadsArrayList.size(); cont++){
            System.out.println(threadsArrayList.get(cont).getUser().getUserUsername() + " SHEHEHSJEHF");
        }
        if (positionThreadMustBeRemoved[0] > positionThreadMustBeRemoved [1]) {
            threadsArrayList.remove(positionThreadMustBeRemoved[0]);
            threadsArrayList.remove(positionThreadMustBeRemoved[1]);
        } else {
            threadsArrayList.remove(positionThreadMustBeRemoved[1]);
            threadsArrayList.remove(positionThreadMustBeRemoved[0]);
        }
        refreshClientConnected(null);
    }

    /** Metodo chiamato quando un utente non accetta la sfida di un altro giocatore.
     * In questo caso rintraccio comunque nell'array il giocatore che aveva mandato la sfida così da rimuoverla.
     * @param information è il gson di un ArrayList con utente che ha proposto la sfida e posizione nell'arraylist
     */
    public void cancelTheGame(String information) {
        ArrayList<String> userAndNumber = gson.fromJson(information, new TypeToken<ArrayList<String>>() {}.getType());
        for(int cont = 0; cont < threadsArrayList.size(); cont++){
            if (threadsArrayList.get(cont).getUser().getUserUsername().equals(userAndNumber.get(0))) {
                gameArrayList.get(Integer.parseInt(userAndNumber.get(1))).getPlayer1().getWriter().println(CodeAndInformation.serializeToJson(SERVER_FORBIDS_THE_GAME, null));
                gameArrayList.remove(Integer.parseInt(userAndNumber.get(1)));
            }
        }
    }

    /** getter
     * @return lista dei client Connessi
     */
    public ArrayList<String> getListOfClientConnected() {
        return listOfClientConnected;
    }

    /** getter
     * @return l'array di partite
     */
    public ArrayList<Game> getGameArrayList() {
        return gameArrayList;
    }

}