package sample.Client.ClientClass;

import static sample.Utilities.Class.ConstantCodes.*;
import com.google.gson.Gson;
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

    //costruttore
    public ClientThread(Socket clientSocket, ClientMain main) {
        this.clientSocket = clientSocket;
        this.main = main;
        this.clientIp = clientSocket.getInetAddress().getHostAddress();
    }

    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));//input sul socket
            writer = new PrintWriter(this.clientSocket.getOutputStream(),true);//output sul socket
            while (true) {
                gson = new Gson();
                String code = reader.readLine();
                codeAndInformation = gson.fromJson(code, CodeAndInformation.class);
                System.out.println(codeAndInformation.getCode() + " -> CODE");
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
                        main.notification("L'utente non ha accettato la tua proposta");
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
                        break;
                    }
                    case (SERVER_SAD_FOR_YOUR_DEFEAT):{
                        main.continueOnChoiceScreen();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}