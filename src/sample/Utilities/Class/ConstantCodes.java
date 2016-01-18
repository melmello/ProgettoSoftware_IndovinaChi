package sample.Utilities.Class;

public final class ConstantCodes {

    //from client to server
    public static final String clientDisconnected = "CLIENT DISCONNECTED";
    public static final String clientReadyToLogin = "CLIENT READY TO LOGIN";
    public static final String clientReadyToCreateNewUser = "CLIENT READY TO CREATE NEW USER";
    public static final String clientReadyToGiveStickerInfo = "CLIENT READY TO GIVE STICKER INFO";
    public static final String clientReadyToGiveQuery = "CLIENT READY TO GIVE QUERY";
    public static final String readyToReceiveNewSticker = "READY TO RECEIVE NEW STICKER";
    public static final String changeMySticker = "CHANGE MY STICKER";
    public static final String wantsToKnowClientConnected = "WANTS TO KNOW CLIENT CONNECTED";
    public static final String readyToReceiveClientConnected = "READY TO RECEIVE CLIENT CONNECTED";
    public static final String clientReadyToReceiveClientsConnected = "CLIENT READY TO RECEIVE CLIENTS CONNECTED";
    public static final String clientIsGivingHisName = "CLIENT IS GIVING HIS NAME";

    //from server to client
    public static final String serverReadyToReceiveUserInfo = "READY TO RECEIVE USER INFO";
    public static final String successfulAuthentication = "SUCCESSFUL AUTHENTICATION";
    public static final String successfulUserCreation = "SUCCESSFUL USER CREATION";
    public static final String readyToReceiveStickerInfo = "READY TO RECEIVE STICKER INFO";
    public static final String readyToReceiveQueryInfo = "READY TO RECEIVE QUERY INFO";
    public static final String serverReadyToSentStickersMustBeRemoved = "SERVER READY TO SENT STICKERS MUST BE REMOVED";
    public static final String changingRound = "CHANGING ROUND";
    public static final String serverReadyToSendClientConnected = "SERVER READY TO SEND CLIENT CONNECTED";
    public static final String serverWantsToRefreshClientConnected = "SERVER WANTS TO REFRESH CLIENT CONNECTED";
    public static final String serverWantsNameOfClientDisconnecting = "SERVER WANTS NAME OF CLIENT DISCONNECTING";
    public static final String userNotFound = "USER NOT FOUND";

    //costanti stringhe per primo e secondo parametro nelle query
    public static final String name = "name";
    public static final String surname = "surname";
    public static final String nickname = "nickname";
    public static final String hairColorBrown = "hairColorBrown";
    public static final String hairLenght = "hairLenght";
    public static final String hairTypeStraight = "hairTypeStraight";
    public static final String beardColorBrown = "beardColorBrown";
    public static final String beardLenght = "beardLenght";
    public static final String beardType = "beardType";
    public static final String eyesColorBrown = "eyesColorBrown";
    public static final String noseDimensionBig = "noseDimensionBig";
    public static final String smile = "smile";
    public static final String complexionBrown = "complexionBrown";
    public static final String earrings = "earrings";
    public static final String glasses = "glasses";
    public static final String headband = "headband";
    public static final String mole = "mole";
    public static final String freckles = "freckles";
    public static final String nationalShirt = "nationalShirt";
    public static final String continent = "continent";
    public static final String championship = "championship";
    public static final String captainBand = "captainBand";

    //costanti stringhe per database user
    public static final String username = "username";
    public static final String password = "password";

    //path client
    public static final String loginScreenFXML = "/sample/Client/ClientFXML/ClientLoginScreen.fxml";
    public static final String loginScreenIcon = "/sample/Client/ClientImage/Icona.png";
    public static final String loginScreenCSS = "/sample/Client/ClientCSS/ClientLoginScreen.css";
    public static final String choiceScreenFXML = "/sample/Client/ClientFXML/ClientChoiceScreen.fxml";
    public static final String choiceScreenIcon = "/sample/Client/ClientImage/Icona.png";
    public static final String choiceScreenCSS = "/sample/Client/ClientCSS/ClientChoiceScreen.css";
    public static final String gameScreenFXML = "/sample/Client/ClientFXML/ClientGameScreen.fxml";
    public static final String gameScreenIcon = "/sample/Client/ClientImage/Icona.png";
    public static final String gameScreenCSS = choiceScreenCSS;
    public static final String notificationIcon = choiceScreenIcon;

    //path server
    public static final String startingScreenFXML = "/sample/Server/ServerFXML/ServerStartingScreen.fxml";
    public static final String startingScreenIcon = "/sample/Server/ServerImage/Icona.png";
    public static final String startingScreenCSS = "/sample/Server/ServerCSS/ServerStartingScreen.css";
    public static final String clientCounterScreenFXML = "/sample/Server/ServerFXML/ServerClientCounterScreen.fxml";
    public static final String clientCounterScreenIcon = "/sample/Server/ServerImage/IconaClient.png";
    public static final String clientCounterScreenCSS = "/sample/Server/ServerCSS/ServerClientCounterScreen.css";

    //database utility
    public static final String driverMySql = "com.mysql.jdbc.Driver";
    public static final String urlConnection = "jdbc:mysql://localhost:3306/indovinachi?useSSL=false";

    //comboBox utility
    public static final String lenghtCB = "Lunghezza";
    public static final String colorCB = "Colore";
    public static final String typeCB = "Tipo";
    public static final String eyesColorCB = "Colore degli occhi";
    public static final String noseDimensionCB = "Dimensione del naso";
    public static final String smileCB = "Sorriso";
    public static final String complexionCB = "Carnagione";
    public static final String earringsCB = "Orecchini";
    public static final String glassesCB = "Occhiali";
    public static final String headbandCB = "Fascia per capelli";
    public static final String moleCB = "Nei";
    public static final String frecklesCB = "Lentiggini";
    public static final String nationalShirtCB = "Maglia della nazionale";
    public static final String continentCB = "Continente della maglia";
    public static final String championshipCB = "Campionato della maglia";
    public static final String captainBandCB = "Fascia da capitano";

    public static final String indovinaChiText = "Indovina Chi";

    //metodo costruito per le eccezioni
    private ConstantCodes(){
        throw new AssertionError();
    }

    @Override
    public String toString() {
        return "ConstantCodes{}";
    }

}