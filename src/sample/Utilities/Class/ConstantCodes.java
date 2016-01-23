package sample.Utilities.Class;

public final class ConstantCodes {

    è una porva

    //from client to server
    public static final String CLIENT_DISCONNECTING = "CLIENT_DISCONNECTING";
    public static final String CLIENT_DISCONNECTING_FROM_LOGIN_SCREEN = "CLIENT_DISCONNECTING_FROM_LOGIN_SCREEN";
    public static final String CLIENT_WANTS_TO_LOGIN = "CLIENT_WANTS_TO_LOGIN";
    public static final String CLIENT_WANTS_TO_SIGNUP = "CLIENT_WANTS_TO_SIGNUP";
    public static final String CLIENT_GIVES_STICKER_INFO = "CLIENT_GIVES_STICKER_INFO";
    public static final String CLIENT_GIVES_QUERY = "CLIENT_GIVES_QUERY";
    public static final String CLIENT_WANTS_TO_KNOW_CONNECTED_CLIENT = "CLIENT_WANTS_TO_KNOW_CONNECTED_CLIENT";
    public static final String CLIENT_WANTS_TO_KNOW_CONNECTED_CLIENT_FOR_THE_FIRST_TIME = "CLIENT_WANTS_TO_KNOW_CONNECTED_CLIENT_FOR_THE_FIRST_TIME";
    public static final String CLIENT_WANTS_TO_SEND_RATING = "CLIENT_WANTS_TO_SEND_RATING";
    public static final String CLIENT_WANTS_TO_PLAY = "CLIENT_WANTS_TO_PLAY";
    public static final String CLIENT_SAYS_OK_FOR_PLAYING = "CLIENT_SAYS_OK_FOR_PLAYING";
    public static final String CLIENT_SAYS_NO_FOR_PLAYING = "CLIENT_SAYS_NO_FOR_PLAYING";
    public static final String CLIENT_GIVES_QUERY_FOR_STICKER = "CLIENT_GIVES_QUERY_FOR_STICKER";

    //from server to client
    public static final String SERVER_CLIENT_SUCCESSFUL_LOGIN = "SERVER_CLIENT_SUCCESSFUL_LOGIN";
    public static final String SERVER_CLIENT_SUCCESSFUL_SIGNUP = "SERVER_CLIENT_SUCCESSFUL_SIGNUP";
    public static final String SERVER_SENDS_STICKER_MUST_BE_REMOVED = "SERVER_SENDS_STICKER_MUST_BE_REMOVED";
    public static final String SERVER_CHANGES_ROUND = "SERVER_CHANGES_ROUND";
    public static final String SERVER_SENDS_CONNECTED_CLIENT = "SERVER_SENDS_CONNECTED_CLIENT";
    public static final String SERVER_REFRESHES_CONNECTED_CLIENT = "SERVER_REFRESHES_CONNECTED_CLIENT";
    public static final String SERVER_REFRESHES_CONNECTED_CLIENT_FOR_THE_FIRST_TIME = "SERVER_REFRESHES_CONNECTED_CLIENT_FOR_THE_FIRST_TIME";
    public static final String SERVER_CLIENT_NOT_FOUND = "SERVER_CLIENT_NOT_FOUND";
    public static final String SERVER_RECEIVED_GAME_REQUEST = "SERVER_RECEIVED_GAME_REQUEST";
    public static final String SERVER_ALLOWS_TO_GO_ON_GAME_SCREEN = "SERVER_ALLOWS_TO_GO_ON_GAME_SCREEN";
    public static final String SERVER_CLIENT_ALREADY_LOGGED = "SERVER_CLIENT_ALREADY_LOGGED";
    public static final String SERVER_CLIENT_ALREADY_IN_THE_DATABASE = "SERVER_CLIENT_ALREADY_IN_THE_DATABASE";
    public static final String SERVER_FORBIDS_THE_GAME = "SERVER_FORBIDS_THE_GAME";
    public static final String SERVER_REFRESHES_IN_GAME_CLIENT = "SERVER_REFRESHES_IN_GAME_CLIENT";
    public static final String SERVER_HAPPY_FOR_YOUR_WIN = "SERVER_HAPPY_FOR_YOUR_WIN";
    public static final String SERVER_SAD_FOR_YOUR_DEFEAT = "SERVER_SAD_FOR_YOUR_DEFEAT";

    //costanti stringhe per primo e secondo parametro nelle query
    public static final String NAME_FOR_QUERY = "name";
    public static final String SURNAME_FOR_QUERY = "surname";
    public static final String NICKNAME_FOR_QUERY = "nickname";
    public static final String HAIRCOLORBROWN_FOR_QUERY = "hairColorBrown";
    public static final String HAIRLENGTH_FOR_QUERY = "hairLength";
    public static final String HAIRTYPESTRAIGHT_FOR_QUERY = "hairTypeStraight";
    public static final String BEARDCOLORBROWN_FOR_QUERY = "beardColorBrown";
    public static final String BEARDLENGTH_FOR_QUERY = "beardLength";
    public static final String BEARDTYPE_FOR_QUERY = "beardType";
    public static final String EYESCOLORBROWN_FOR_QUERY = "eyesColorBrown";
    public static final String NOSEDIMENSIONBIG_FOR_QUERY = "noseDimensionBig";
    public static final String SMILE_FOR_QUERY = "smile";
    public static final String COMPLEXIONBROWN_FOR_QUERY = "complexionBrown";
    public static final String EARRINGS_FOR_QUERY = "earrings";
    public static final String GLASSES_FOR_QUERY = "glasses";
    public static final String HEADBAND_FOR_QUERY = "headband";
    public static final String MOLE_FOR_QUERY = "mole";
    public static final String FRECKLES_FOR_QUERY = "freckles";
    public static final String NATIONALSHIRT_FOR_QUERY = "nationalShirt";
    public static final String CONTINENT_FOR_QUERY = "continent";
    public static final String CHAMPIONSHIP_FOR_QUERY = "championship";
    public static final String CAPTAINBAND_FOR_QUERY = "captainBand";

    //costanti da passare al db
    public static final String LONGANSWER_FOR_QUERY = "long";
    public static final String SHORTANSWER_FOR_QUERY = "short";
    public static final String BALDANSWER_FOR_QUERY = "bald";
    public static final String TRUEANSWER_FOR_QUERY = "true";
    public static final String FALSEANSWER_FOR_QUERY = "false";
    public static final String SHAVEDANSWER_FOR_QUERY = "shaved";
    public static final String GOATEEANSWER_FOR_QUERY = "goatee";
    public static final String MUSTACHEANSWER_FOR_QUERY = "mustache";
    public static final String WHOLEANSWER_FOR_QUERY = "whole";
    public static final String EUROPEANSWER_FOR_QUERY = "europe";
    public static final String AMERICAANSWER_FOR_QUERY = "america";
    public static final String ASIAANSWER_FOR_QUERY = "asia";
    public static final String SERIEAANSWER_FOR_QUERY = "serieA";
    public static final String PREMIERANSWER_FOR_QUERY = "premier";
    public static final String LIGUEANSWER_FOR_QUERY = "ligue";
    public static final String BBVAANSWER_FOR_QUERY = "bbva";
    public static final String LIGAANSWER_FOR_QUERY = "liga";

    //costanti stringhe per database user
    public static final String USERNAME_FOR_QUERY = "username";
    public static final String PASSWORD_FOR_QUERY = "password";

    //path client
    public static final String LOGINSCREEN_FXML = "/sample/Client/ClientFXML/ClientLoginScreen.fxml";
    public static final String LOGINSCREEN_ICON = "/sample/Client/ClientImage/IndovinaChi.png";
    public static final String LOGINSCREEN_CSS = "/sample/Client/ClientCSS/ClientLoginScreen.css";
    public static final String CHOICESCREEN_FXML = "/sample/Client/ClientFXML/ClientChoiceScreen.fxml";
    public static final String CHOICESCREEN_ICON = LOGINSCREEN_ICON;
    public static final String CHOICESCREEN_CSS = "/sample/Client/ClientCSS/ClientChoiceScreen.css";
    public static final String GAMESCREEN_FXML = "/sample/Client/ClientFXML/ClientGameScreen.fxml";
    public static final String GAMESCREEN_ICON = CHOICESCREEN_ICON;
    public static final String GAMESCREEN_CSS = CHOICESCREEN_CSS;
    public static final String NOTIFICATION_ICON = CHOICESCREEN_ICON;

    //path server
    public static final String STARTINGSCREEN_FXML = "/sample/Server/ServerFXML/ServerStartingScreen.fxml";
    public static final String STARTINGSCREEN_ICON = "/sample/Server/ServerImage/Icona.png";
    public static final String STARTINGSCREEN_CSS = "/sample/Server/ServerCSS/ServerStartingScreen.css";
    public static final String CLIENTCOUNTERSCREEN_FXML = "/sample/Server/ServerFXML/ServerClientCounterScreen.fxml";
    public static final String CLIENTCOUNTERSCREEN_ICON = "/sample/Server/ServerImage/IconaClient.png";
    public static final String CLIENTCOUNTERSCREEN_CSS = "/sample/Server/ServerCSS/ServerClientCounterScreen.css";

    //database utility
    public static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
    public static final String CONNECTION_URL_MYSQL = "jdbc:mysql://localhost:3306/indovinachi?useSSL=false";

    //server utility
    public static final int ASSIGNED_PORT_SOCKET = 8080;
    public static final String ASSIGNED_IP_SOCKET = "localhost";

    //comboBox utility
    public static final String LENGTH_COMBO_BOX = "Lunghezza";
    public static final String COLOR_COMBO_BOX = "Colore";
    public static final String TYPE_COMBO_BOX = "Tipo";
    public static final String EYESCOLOR_COMBO_BOX = "Colore degli occhi";
    public static final String NOSEDIMENSION_COMBO_BOX = "Dimensione del naso";
    public static final String SMILE_COMBO_BOX = "Sorriso";
    public static final String COMPLEXION_COMBO_BOX = "Carnagione";
    public static final String EARRINGS_COMBO_BOX = "Orecchini";
    public static final String GLASSES_COMBO_BOX = "Occhiali";
    public static final String HEADBAND_COMBO_BOX = "Fascia per capelli";
    public static final String MOLE_COMBO_BOX = "Nei";
    public static final String FRECKLES_COMBO_BOX = "Lentiggini";
    public static final String NATIONALSHIRT_COMBO_BOX = "Maglia della nazionale";
    public static final String CONTINENT_COMBO_BOX = "Continente della maglia";
    public static final String CHAMPIONSHIP_COMBO_BOX = "Campionato della maglia";
    public static final String CAPTAINBAND_COMBO_BOX = "Fascia da capitano";

    //text per notifica
    public static final String NOTIFICATION_TEXT = "Indovina Chi";

    //path per i suoni
    public static final String BUTTONCLICK_SOUND = "/sample/Utilities/Sound/ButtonClick.wav";
    public static final String GOAL_SOUND = "/sample/Utilities/Sound/Goal.wav";
    public static final String BALLSHOT_SOUND = "/sample/Utilities/Sound/BallShot.wav";

    //nomi immagini
    public static final String IMAGE_PLAYAGAME = "imagePlayAGame";
    public static final String IMAGE_RATING = "imageRating";
    public static final String IMAGE_PERSONALSCOREBOARD = "imagePersonalScoreboard";
    public static final String IMAGE_WORLDSCOREBOARD = "imageWorldScoreboard";

    //metodo costruito per le eccezioni
    private ConstantCodes(){
        throw new AssertionError();
    }

    @Override
    public String toString() {
        return "ConstantCodes{}";
    }

}