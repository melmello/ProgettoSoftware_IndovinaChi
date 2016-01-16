package sample.Server.ServerClass;

import static sample.Utilities.Class.ConstantCodes.*;
import static sample.Utilities.Class.SecurityClass.*;
import javafx.concurrent.Task;
import sample.Utilities.Class.Sticker;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

public class ServerStart extends Task {

    private int assignedPort;
    private int clientNumber = 0;
    private String assignedIp;
    private ServerSocket socketExchange;
    private ServerMain serverMain;
    private Connection connection;
    private ArrayList<ServerThread> threadsArrayList;
    private ArrayList<String> listOfClientConnected = new ArrayList<>();

    //costruttore
    public ServerStart(ServerMain serverMain) {
        this.serverMain = serverMain;
    }

    @Override
    protected Object call() throws Exception {
        System.out.println("Il server è connesso");//se sono qui vuol dire che si è connesso il server e ha generato un thread
        String connectionURL;
        assignedPort=8080;//assegno il numero della porta
        try {
            socketExchange = new ServerSocket(assignedPort);//istanzio il socket sulla tal porta su cui comunicherò
            Class.forName(driverMySql).newInstance();
            connection = DriverManager.getConnection(urlConnection, "root", rootPasswordMYSQL);//mi collego al database
            System.out.println("Database connesso");
            assignedIp = Inet4Address.getLocalHost().getHostAddress();//ricavo l'IP globale del Server per stamparlo poi
            serverMain.initialConfiguration(assignedIp); //chiamo il metodo che mi permette di collegarmi al Controller
            serverMain.printNumberOfClient(Integer.toString(clientNumber));//chiamo metodo che mi permette di stampare quanti client sono connessi (CASTING)
            System.out.println("Sono il Server.\nIl mio indirizzo IP è: " + assignedIp + "\nLa mia porta è: " + assignedPort);
            threadsArrayList = new ArrayList<ServerThread>();
            while (true) {//while true del Thread in cui istanzia un thread per ogni client che si connette
                Socket serverSocket = socketExchange.accept();
                threadsArrayList.add(new ServerThread(serverSocket, clientNumber, this, connection, threadsArrayList.size()));//ogni volta che creo un Thread lo inserisco nell'ultimo posto dell'array
                Thread threadTask = new Thread(threadsArrayList.get(threadsArrayList.size()-1));//creo un threadTask con l'ultimo elemento dell'array
                threadTask.start();//faccio partire l'ultimo thread inserito
                clientNumber++;
                serverMain.printNumberOfClient(Integer.toString(clientNumber));//vedi prima
                System.out.println("Numero di client connessi: " + clientNumber);
                System.out.println("Il client #" + clientNumber + " si è connesso");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //metodo usato quando un client di disconnette
    public void changeClientNumber(String nameToRemove){
        clientNumber--;
        serverMain.printNumberOfClient(Integer.toString(clientNumber));
        System.out.println("Numero di client connessi: " + clientNumber);
        System.out.println("Il client #" + clientNumber + " si è disconnesso");
        listOfClientConnected.remove(nameToRemove);

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
        listOfClientConnected.add(userSQLName);
    }

    public void refreshClientConnected(String username) {
        for (int cont = 0; cont < threadsArrayList.size(); cont++){
            if (threadsArrayList.get(cont).getUser().getUserUsername()!=null && !threadsArrayList.get(cont).getUser().getUserUsername().equals(username)) {
                threadsArrayList.get(cont).getWriter().println(serverWantsToRefreshClientConnected);
            }
        }
    }

    //getter
    public ArrayList<String> getListOfClientConnected() {
        return listOfClientConnected;
    }

}