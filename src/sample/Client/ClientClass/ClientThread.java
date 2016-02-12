package sample.Client.ClientClass;

/** @author Giulio Melloni
 * Questa classe è quella che fa da ponte tra il client e il server. Classe speculare rispetto a ServerThread, questa è quella che riceve i codici dal Server e in base ad ognuno di esso compie un'azione specifica.
 * La gestione del codice è, ovviamente, identica a quella del server e quindi è specificata nel ServerThread {@link sample.Server.ServerClass.ServerThread}.
 * L'importanza di questa classe è quindi notevole: se prima nel server la classe Thread era quella che eseguiva azioni in base alle azioni del client sulla propria interfaccia, ora nel client è ciò che collega il controller alle modifiche apportate dal server.
 */

import static sample.Utilities.Class.ConstantCodes.*;
import com.google.gson.Gson;
import javafx.application.Platform;
import sample.Utilities.Class.CodeAndInformation;
import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {

    private CodeAndInformation codeAndInformation;
    private String clientIp = null;
    private ClientMain main;
    private Socket clientSocket = null;
    private BufferedReader reader;
    private PrintWriter writer;
    private Gson gson;

    /** Costruttore di ClientThread
     * @param clientSocket è il socket del Client su cui avviene la comunicazione
     * @param main è ClientMain con cui posso collegarmi per arrivare al controller
     */
    public ClientThread(Socket clientSocket, ClientMain main) {
        this.clientSocket = clientSocket;
        this.main = main;
        this.clientIp = clientSocket.getInetAddress().getHostAddress();
    }

    /** Metodo cuore del Thread. In questo metodo il thread accetta i segnali inviati dal Server.
     * Qui viene inoltre istanziato un nuovo reader e un nuovo writer che servono per leggere e scrivere sul socket.
     * Ad ogni Code di CodeAndInformation inviato quindi viene eseguita una tal azione specificata nello switch, inserito in un while(true), abilitato fino a quando un client segnala con un handle la propria uscita.
     */
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));//input sul socket
            writer = new PrintWriter(this.clientSocket.getOutputStream(),true);//output sul socket
            while (true) {
                gson = new Gson();
                String code = reader.readLine();
                codeAndInformation = gson.fromJson(code, CodeAndInformation.class);
                System.out.println(codeAndInformation.getCode() + " -> CODE FROM SERVER");
                switch (codeAndInformation.getCode()) {
                    //Il Server risponde che l'autenticazione è avvenuta con successo e si può proseguire cambiando schermata
                    case (SERVER_CLIENT_SUCCESSFUL_LOGIN):{
                        main.continueOnChoiceScreen();
                        main.notification("Benvenuto " + main.getUser().getUserUsername() + "!");
                        break;
                    }
                    //Il Server risponde che ora si può cambiare l'interfaccia
                    case (SERVER_CLIENT_SUCCESSFUL_SIGNUP):{
                        main.notificationForNewUser();
                        break;
                    }
                    //Il Server comunica che è pronto per mandare gli sticker che vanno rimossi
                    case (SERVER_SENDS_STICKER_MUST_BE_REMOVED):{
                        main.modifySticker(codeAndInformation.getInformation());
                        break;
                    }
                    case (SERVER_CHANGES_ROUND):{
                        main.readyToAbilitateClientScreen();
                        break;
                    }
                    case (SERVER_REFRESHES_CONNECTED_CLIENT):{
                        main.displayClientConnected(codeAndInformation.getInformation());
                        break;
                    }
                    case (SERVER_CLIENT_NOT_FOUND):{
                        main.notification("Utente " + main.getUser().getUserUsername() + " con password " + main.getUser().getUserPassword() + " non trovato");
                        break;
                    }
                    case (SERVER_RECEIVED_GAME_REQUEST):{
                        main.playGameRequest(codeAndInformation.getInformation());
                        break;
                    }
                    case (SERVER_ALLOWS_TO_GO_ON_GAME_SCREEN):{
                        main.continueOnGameScreen();
                        break;
                    }
                    case (SERVER_CLIENT_ALREADY_LOGGED):{
                        main.notification("Utente " + main.getUser().getUserUsername() + " con password " + main.getUser().getUserPassword() + " già loggato");
                        break;
                    }
                    case (SERVER_CLIENT_ALREADY_IN_THE_DATABASE):{
                        main.notification("Utente " + codeAndInformation.getInformation() + " già presente");
                        break;
                    }
                    case (SERVER_FORBIDS_THE_GAME):{
                        main.serverForbids();
                        break;
                    }
                    case (SERVER_REFRESHES_CONNECTED_CLIENT_FOR_THE_FIRST_TIME):{
                        main.displayClientConnected(codeAndInformation.getInformation());
                        break;
                    }
                    case (SERVER_REFRESHES_IN_GAME_CLIENT):{
                        main.displayClientInGame(codeAndInformation.getInformation());
                        break;
                    }
                    case (SERVER_REFRESHES_PERSONAL_LEADERBOARD):{
                        main.displayPersonalLeaderboard(codeAndInformation.getInformation());
                        break;
                    }
                    case (SERVER_REFRESHES_WORLD_LEADERBOARD):{
                        main.displayWorldLeaderboard(codeAndInformation.getInformation());
                        break;
                    }
                    case (SERVER_HAPPY_FOR_YOUR_WIN):{
                        main.continueOnChoiceScreen();
                        main.notification("Hai vinto la partita contro " + codeAndInformation.getInformation() + ". Complimenti!");
                        break;
                    }
                    case (SERVER_SAD_FOR_YOUR_DEFEAT):{
                        main.continueOnChoiceScreen();
                        main.notification("Hai perso la partita contro " + codeAndInformation.getInformation() + ". Non abbatterti!");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Platform.exit();
            System.exit(0);
            System.out.println("SERVER CRASHATO");
        }
    }

}