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

    //from server to client
    public static final String serverReadyToReceiveUserInfo = "READY TO RECEIVE USER INFO";
    public static final String successfulAuthentication = "SUCCESSFUL AUTHENTICATION";
    public static final String successfulUserCreation = "SUCCESSFUL USER CREATION";
    public static final String readyToReceiveStickerInfo = "READY TO RECEIVE STICKER INFO";
    public static final String readyToReceiveQueryInfo = "READY TO RECEIVE QUERY INFO";
    public static final String serverReadyToSentStickersMustBeRemoved = "SERVER READY TO SENT STICKERS MUST BE REMOVED";
    public static final String changingRound = "CHANGING ROUND";

    //costanti stringhe per primo e secondo parametro nelle query
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

    //metodo costruito per le eccezioni
    private ConstantCodes(){
        throw new AssertionError();
    }

    @Override
    public String toString() {
        return "ConstantCodes{}";
    }

}
