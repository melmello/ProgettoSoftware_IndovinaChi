package sample.Utilities.Class;

public final class ConstantCodes {

    //from client to server
    public static final String CLIENT_DISCONNECTING = "CLIENT_DISCONNECTING";
    public static final String CLIENT_DISCONNECTING_FROM_LOGIN_SCREEN = "CLIENT_DISCONNECTING_FROM_LOGIN_SCREEN";
    public static final String CLIENT_WANTS_TO_LOGIN = "CLIENT_WANTS_TO_LOGIN";
    public static final String CLIENT_WANTS_TO_SIGNUP = "CLIENT_WANTS_TO_SIGNUP";
    public static final String CLIENT_GIVES_STICKER_INFO = "CLIENT_GIVES_STICKER_INFO";
    public static final String CLIENT_GIVES_QUERY = "CLIENT_GIVES_QUERY";
    public static final String CLIENT_RECEIVES_NEW_STICLER = "CLIENT_RECEIVES_NEW_STICLER";
    public static final String CLIENT_CHANGES_STICKER = "CLIENT_CHANGES_STICKER";
    public static final String CLIENT_WANTS_TO_KNOW_CONNECTED_CLIENT = "CLIENT_WANTS_TO_KNOW_CONNECTED_CLIENT";
    public static final String CLIENT_WANTS_TO_SEND_RATING = "CLIENT_WANTS_TO_SEND_RATING";
    public static final String CLIENT_WANTS_TO_PLAY = "CLIENT_WANTS_TO_PLAY";
    public static final String CLIENT_SAYS_OK_FOR_PLAYING = "CLIENT_SAYS_OK_FOR_PLAYING";

    //from server to client
    public static final String SERVER_CLIENT_SUCCESSFUL_LOGIN = "SERVER_CLIENT_SUCCESSFUL_LOGIN";
    public static final String SERVER_CLIENT_SUCCESSFUL_SIGNUP = "SERVER_CLIENT_SUCCESSFUL_SIGNUP";
    public static final String SERVER_RECEIVES_STICKER_INFO = "SERVER_RECEIVES_STICKER_INFO";
    public static final String SERVER_RECEIVES_QUERY_INFO = "SERVER_RECEIVES_QUERY_INFO";
    public static final String SERVER_SENDS_STICKER_MUST_BE_REMOVED = "SERVER_SENDS_STICKER_MUST_BE_REMOVED";
    public static final String SERVER_CHANGES_ROUND = "SERVER_CHANGES_ROUND";
    public static final String SERVER_SENDS_CONNECTED_CLIENT = "SERVER_SENDS_CONNECTED_CLIENT";
    public static final String SERVER_REFRESHES_CONNECTED_CLIENT = "SERVER_REFRESHES_CONNECTED_CLIENT";
    public static final String SERVER_CLIENT_NOT_FOUND = "SERVER_CLIENT_NOT_FOUND";
    public static final String SERVER_RECEIVED_GAME_REQUEST = "SERVER_RECEIVED_GAME_REQUEST";
    public static final String SERVER_ALLOWS_TO_GO_ON_GAME_SCREEN = "SERVER_ALLOWS_TO_GO_ON_GAME_SCREEN";
    public static final String SERVER_CLIENT_ALREADY_LOGGED = "SERVER_CLIENT_ALREADY_LOGGED";
    public static final String SERVER_CLIENT_ALREADY_IN_THE_DATABASE = "SERVER_CLIENT_ALREADY_IN_THE_DATABASE";

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

    //costanti da passare al db
    public static final String longAnswer = "long";
    public static final String shortAnswer = "short";
    public static final String baldAnswer = "bald";
    public static final String trueAnswer = "true";
    public static final String falseAnswer = "false";
    public static final String shavedAnswer = "shaved";
    public static final String goateeAnswer = "goatee";
    public static final String mustacheAnswer = "mustache";
    public static final String wholeAnswer = "whole";
    public static final String europeAnswer = "europe";
    public static final String americaAnswer = "america";
    public static final String asiaAnswer = "asia";
    public static final String serieaAnswer = "serieA";
    public static final String premierAnswer = "premier";
    public static final String ligueAnswer = "ligue";
    public static final String bbvaAnswer = "bbva";
    public static final String ligaAnswer = "liga";


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

    //server utility
    public static final int assignedPort = 8080;
    public static final String localhost = "localhost";

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

    //text per notifica
    public static final String indovinaChiText = "Indovina Chi";

    //path per i suoni
    public static final String buttonClickSound = "/sample/Utilities/Sound/ButtonClick.wav";
    public static final String goalSound = "/sample/Utilities/Sound/Goal.wav";
    public static final String ballShotSound = "/sample/Utilities/Sound/BallShot.wav";

    //metodo costruito per le eccezioni
    private ConstantCodes(){
        throw new AssertionError();
    }

    @Override
    public String toString() {
        return "ConstantCodes{}";
    }

}