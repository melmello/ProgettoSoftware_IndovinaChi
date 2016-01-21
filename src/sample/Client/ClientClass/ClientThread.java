package sample.Client.ClientClass;

import static sample.Utilities.Class.ConstantCodes.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sample.Utilities.Class.CodeAndInformation;
import sample.Utilities.Class.StickerQuery;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

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
                    case (SERVER_CLIENT_SUCCESSFUL_LOGIN):{/** ok */
                        main.continueOnChoiceScreen();
                        main.notification("Benvenuto " + main.getUser().getUserUsername() + "!");
                        break;
                    }
                    //Il Server risponde che ora si può cambiare l'interfaccia
                    case (SERVER_CLIENT_SUCCESSFUL_SIGNUP):{/** ok */
                        main.notificationForNewUser();
                        break;
                    }
                    //Il Server comunica che è pronto per ricevere le informazioni sullo Sticker scelto
                    case (SERVER_RECEIVES_STICKER_INFO):{
                        comunicateStickerInfo();
                        break;
                    }
                    //Il Server comunica che è pronto per ricevere le informazioni sulla query
                    case (SERVER_RECEIVES_QUERY_INFO):{
                        comunicateQueryInfo();
                        break;
                    }
                    //Il Server comunica che è pronto per mandare gli sticker che vanno rimossi
                    case (SERVER_SENDS_STICKER_MUST_BE_REMOVED):{
                        readyToReceiveStickerMustBeRemoved();
                        break;
                    }
                    case (SERVER_CHANGES_ROUND):{
                        readyToAbilitateClientScreen();
                        break;
                    }
                    case (SERVER_SENDS_CONNECTED_CLIENT):{/** ok */
                        main.displayClientConnected(codeAndInformation.getInformation());
                        break;
                    }
                    case (SERVER_REFRESHES_CONNECTED_CLIENT):{/** ok */
                        main.displayClientConnected(codeAndInformation.getInformation());
                        break;
                    }
                    case (SERVER_CLIENT_NOT_FOUND):{/** ok */
                        main.notification("Utente " + main.getUser().getUserUsername() + " con password " + main.getUser().getUserPassword() + " non trovato");
                        break;
                    }
                    case (SERVER_RECEIVED_GAME_REQUEST):{
                        main.playGameRequest(codeAndInformation.getInformation());
                        break;
                    }
                    case (SERVER_ALLOWS_TO_GO_ON_GAME_SCREEN):{/** ok */
                        main.continueOnGameScreen();
                        break;
                    }
                    case (SERVER_CLIENT_ALREADY_LOGGED):{/** ok */
                        main.notification("Utente " + main.getUser().getUserUsername() + " con password " + main.getUser().getUserPassword() + " già loggato");
                        break;
                    }
                    case (SERVER_CLIENT_ALREADY_IN_THE_DATABASE):{/** ok */
                        main.notification("Utente " + codeAndInformation.getInformation() + " già presente");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readyToAbilitateClientScreen() {
        main.readyToAbilitateClientScreen();
    }

    //metodo che serve per ricevere dal Server l'ArrayList di sticker da rimuovere
    private void readyToReceiveStickerMustBeRemoved() {
        gson = new Gson();
        try {
            writer.println(CLIENT_RECEIVES_NEW_STICLER);//comunico al Server che sono pronto a ricevere gli sticker nuovi
            String newStickerGson = reader.readLine();//aspetto la risposta del Server
            ArrayList<String> newStickers = gson.fromJson(newStickerGson, new TypeToken<ArrayList<String>>(){}.getType());//deserializzo il gson in un ArrayList
            System.out.println(newStickers);
            main.modifySticker(newStickers);//chiamata a metodo del main per modificare gli Sticker e eliminarli
            //passo il turno e quindi disabilito il button
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //metodo che serve per mandare al server le informazioni dei button
    private void comunicateQueryInfo() {
        gson = new Gson();
        StickerQuery stickerQuery = main.getStickerQuery();
        String stickerQueryString = gson.toJson(stickerQuery);
        writer.println(stickerQueryString);
    }

    //metodo che serve per comunicare le informazioni dello Sticker
    private void comunicateStickerInfo() {
        main.comunicateStickerInfo();
    }

}