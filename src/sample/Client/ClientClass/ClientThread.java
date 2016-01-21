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
                System.out.println(codeAndInformation.getCode());
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
                    case (SERVER_SENDS_CONNECTED_CLIENT):{
                        main.displayClientConnected(codeAndInformation.getInformation());
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
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}