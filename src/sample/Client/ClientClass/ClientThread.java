package sample.Client.ClientClass;

import static sample.Utilities.Class.ConstantCodes.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sample.Utilities.Class.StickerQuery;
import sample.Utilities.Class.User;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread {

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
                String code = reader.readLine();
                switch (code) {
                    //Il Server è pronto per il Login e aspetta che io gli mandi il gson con username, password e porta
                    case (serverReadyToReceiveUserInfo): {
                        readyForAuthentication();//chiamata a metodo locale
                        break;
                    }
                    //Il Server risponde che l'autenticazione è avvenuta con successo e si può proseguire cambiando schermata
                    case (successfulAuthentication):{
                        continueOnChoiceScreen();
                        break;
                    }
                    //Il Server risponde che ora si può cambiare l'interfaccia
                    case (successfulUserCreation):{
                        comeBackToLogin();
                        break;
                    }
                    //Il Server comunica che è pronto per ricevere le informazioni sullo Sticker scelto
                    case (readyToReceiveStickerInfo):{
                        comunicateStickerInfo();
                        break;
                    }
                    //Il Server comunica che è pronto per ricevere le informazioni sulla query
                    case (readyToReceiveQueryInfo):{
                        comunicateQueryInfo();
                        break;
                    }
                    //Il Server comunica che è pronto per mandare gli sticker che vanno rimossi
                    case (serverReadyToSentStickersMustBeRemoved):{
                        readyToReceiveStickerMustBeRemoved();
                        break;
                    }
                    case (changingRound):{
                        readyToAbilitateClientScreen();
                        break;
                    }
                    case (serverReadyToSendClientConnected):{
                        serverComunicateClientConnected();
                        break;
                    }
                    case (serverWantsToRefreshClientConnected):{
                        serverReadyToComunicateClientsConnectedList();
                        break;
                    }
                    case (serverWantsNameOfClientDisconnecting):{
                        serverWantsNameClientOff();
                        break;
                    }
                    case (userNotFound):{
                        notificationToUserNotFound();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void continueOnChoiceScreen() {
        main.continueOnChoiceScreen();
    }

    private void notificationToUserNotFound() {
        main.notification("Utente " + main.getUser().getUserUsername() + " con password " + main.getUser().getUserPassword() + " non trovato");
    }

    private void serverWantsNameClientOff() {
        writer.println(main.getUser().getUserUsername());
    }

    private void serverReadyToComunicateClientsConnectedList() {
        gson = new Gson();
        writer.println(wantsToKnowClientConnected);
    }

    private void serverComunicateClientConnected() {
        gson = new Gson();
        try {
            writer.println(clientReadyToReceiveClientsConnected);
            String clientConnectedGson = reader.readLine();
            ArrayList<String> clientConnected = gson.fromJson(clientConnectedGson, new TypeToken<ArrayList<String>>() {
            }.getType());
            System.out.println(clientConnected);
            main.displayClientConnected(clientConnected);
        } catch (IOException e){
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
            writer.println(readyToReceiveNewSticker);//comunico al Server che sono pronto a ricevere gli sticker nuovi
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

    //metodo che mi cambia la schermata da login a choice
    private void comeBackToLogin() {
        main.notificationForNewUser();
    }

    //metodo per l'autenticazione, la creazione del gson e l'invio di esso
    private void readyForAuthentication() {
        gson = new Gson(); //creo un oggetto gson per serializzare e deserializzare
        User user = main.getUser(); //ripesco lo User dal ClientMain
        String userString = gson.toJson(user);  //serializzo i campi dello User
        System.out.println(userString);
        writer.println(userString);    //mando al ServerThread (CHE E' FERMO IN ASCOLTO) il gson
    }

}