package sample.Utilities.Class;

/** Classe utilizzata per settare lo sticker. In questo modo posso avere in un oggetto tutti i parametri trovati dal database senza dovere tutte le volte interrogare il database.
 */

import java.io.Serializable;

public class Sticker implements Serializable{

    private String nameOfSticker;
    private String nicknameOfSticker;
    private String surnameOfSticker;
    private boolean hairColorBrownOfSticker;
    private String hairLengthOfSticker;
    private Boolean beardColorBrownOfSticker;
    private boolean eyesColorBrownOfSticker;
    private boolean complexionBrownOfSticker;
    private boolean earringsOfSticker;
    private boolean glassesOfSticker;
    private boolean headbandOfSticker;
    private boolean moleOfSticker;
    private String beardTypeOfSticker;
    private boolean frecklesOfSticker;
    private boolean nationalShirtOfSticker;
    private String continentOfSticker;
    private String championshipOfSticker;
    private boolean captainBandOfSticker;
    private boolean noseDimensionBigOfSticker;
    private boolean smileOfSticker;
    private Boolean hairTypeStraightOfSticker;
    private String beardLengthOfSticker;

    /** Costruttore con solo Nickname dello Sticker.
     * @param nickname è il nickname dello sticker.
     */
    public Sticker(String nickname) {
        this.nicknameOfSticker = nickname;
    }

    /** Costruttore di Sticker.
     * @param name è il nome dello Sticker.
     * @param nickname è il nickname dello Sticker.
     * @param surname è il cognome dello Sticker.
     * @param hairColorBrown è il colore dei capelli dello Sticker.
     * @param hairLength è la lunghezza dei capelli dello Sticker.
     * @param beardColorBrown è il colore della barba dello Sticker.
     * @param eyesColorBrown è il colore degli occhi dello Sticker.
     * @param complexionBrown è il colore della carnagione dello Sticker.
     * @param earrings è un booleano che indica se lo sticker porta gli orecchini.
     * @param glasses è un booleano che indica se lo sticker porta gli occhiali.
     * @param headband è un booleano che indica se lo sticker porta la fascia per i capelli.
     * @param mole è un booleano che indica se lo sticker ha un neo.
     * @param beardType è il tipo di barba dello Sticker.
     * @param freckles è un booleano che indica se lo sticker ha le lentiggini.
     * @param nationalShirt è un booleano che indica se lo sticker indossa la maglia della nazionale o di un club.
     * @param continent è il nome del continente a cui appartiene la nazionalità della maglia che indossa.
     * @param championship è il nome del campionato a cui appartiene la nazionalità della maglia che indossa.
     * @param captainBand è un booleano che indica se ha la fascia da capitano.
     * @param noseDimensionBig è un booleano che indica la dimensione del naso.
     * @param smile è un booleano che indica se sta sorridendo.
     * @param hairTypeStraight è il tipo di capelli dello Sticker.
     * @param beardLength è la lunghezza della barba dello Sticker.
     */
    public Sticker(String name, String nickname, String surname, boolean hairColorBrown, String hairLength, Boolean beardColorBrown, boolean eyesColorBrown, boolean complexionBrown, boolean earrings, boolean glasses, boolean headband, boolean mole, String beardType, boolean freckles, boolean nationalShirt, String continent, String championship, boolean captainBand, boolean noseDimensionBig, boolean smile, Boolean hairTypeStraight, String beardLength) {
        this.nameOfSticker = name;
        this.nicknameOfSticker = nickname;
        this.surnameOfSticker = surname;
        this.hairColorBrownOfSticker = hairColorBrown;
        this.hairLengthOfSticker = hairLength;
        this.beardColorBrownOfSticker = beardColorBrown;
        this.eyesColorBrownOfSticker = eyesColorBrown;
        this.complexionBrownOfSticker = complexionBrown;
        this.earringsOfSticker = earrings;
        this.glassesOfSticker = glasses;
        this.headbandOfSticker = headband;
        this.moleOfSticker = mole;
        this.beardTypeOfSticker = beardType;
        this.frecklesOfSticker = freckles;
        this.nationalShirtOfSticker = nationalShirt;
        this.continentOfSticker = continent;
        this.championshipOfSticker = championship;
        this.captainBandOfSticker = captainBand;
        this.noseDimensionBigOfSticker = noseDimensionBig;
        this.smileOfSticker = smile;
        this.hairTypeStraightOfSticker = hairTypeStraight;
        this.beardLengthOfSticker = beardLength;
    }

    /** Metodo usato per serializzare lo Sticker.
     * @return serializzazione dello Sticker.
     */
    @Override
    public String toString() {
        return "Sticker{" +
                "nameOfSticker='" + nameOfSticker + '\'' +
                ", nicknameOfSticker='" + nicknameOfSticker + '\'' +
                ", surnameOfSticker='" + surnameOfSticker + '\'' +
                ", hairColorBrownOfSticker=" + hairColorBrownOfSticker +
                ", hairLengthOfSticker='" + hairLengthOfSticker + '\'' +
                ", beardColorBrownOfSticker=" + beardColorBrownOfSticker +
                ", eyesColorBrownOfSticker=" + eyesColorBrownOfSticker +
                ", complexionBrownOfSticker=" + complexionBrownOfSticker +
                ", earringsOfSticker=" + earringsOfSticker +
                ", glassesOfSticker=" + glassesOfSticker +
                ", headbandOfSticker=" + headbandOfSticker +
                ", moleOfSticker=" + moleOfSticker +
                ", beardTypeOfSticker='" + beardTypeOfSticker + '\'' +
                ", frecklesOfSticker=" + frecklesOfSticker +
                ", nationalShirtOfSticker=" + nationalShirtOfSticker +
                ", continentOfSticker='" + continentOfSticker + '\'' +
                ", championshipOfSticker='" + championshipOfSticker + '\'' +
                ", captainBandOfSticker=" + captainBandOfSticker +
                ", noseDimensionBigOfSticker=" + noseDimensionBigOfSticker +
                ", smileOfSticker=" + smileOfSticker +
                ", hairTypeStraightOfSticker=" + hairTypeStraightOfSticker +
                ", beardLengthOfSticker='" + beardLengthOfSticker + '\'' +
                '}';
    }

    /** getter.
     * @return nickname dello sticker.
     */
    public String getNicknameOfSticker() {
        return nicknameOfSticker;
    }

    /** getter.
     * @return se ha i capelli scuri.
     */
    public boolean isHairColorBrownOfSticker() {
        return hairColorBrownOfSticker;
    }

    /** getter.
     * @return tipo di capelli.
     */
    public String getHairLengthOfSticker() {
        return hairLengthOfSticker;
    }

    /** getter.
     * @return se ha la barba scura.
     */
    public Boolean isBeardColorBrownOfSticker() {
        return beardColorBrownOfSticker;
    }

    /** getter.
     * @return se ha gli occhi scuri.
     */
    public boolean isEyesColorBrownOfSticker() {
        return eyesColorBrownOfSticker;
    }

    /** getter.
     *
     * @return se è scuro di carnagione.
     */
    public boolean isComplexionBrownOfSticker() {return complexionBrownOfSticker;}

    /** getter.
     *
     * @return se porta gli orecchini.
     */
    public boolean isEarringsOfSticker() {
        return earringsOfSticker;
    }

    /** getter.
     *
     * @return se indossa gli occhiali.
     */
    public boolean isGlassesOfSticker() {
        return glassesOfSticker;
    }

    /** getter.
     *
     * @return se indossa una fascia per i capelli.
     */
    public boolean isHeadbandOfSticker() {
        return headbandOfSticker;
    }

    /** getter.
     *
     * @return se ha almeno un neo.
     */
    public boolean isMoleOfSticker() {
        return moleOfSticker;
    }

    /** getter.
     *
     * @return il tipo di barba.
     */
    public String getBeardTypeOfSticker() {
        return beardTypeOfSticker;
    }

    /** getter.
     *
     * @return se ha le lentiggini.
     */
    public boolean isFrecklesOfSticker() {
        return frecklesOfSticker;
    }

    /** getter.
     *
     * @return se indossa la maglia della nazionale
     */
    public boolean isNationalShirtOfSticker() {
        return nationalShirtOfSticker;
    }

    /** getter.
     *
     * @return il continente della maglia indossata.
     */
    public String getContinentOfSticker() {
        return continentOfSticker;
    }

    /** getter.
     *
     * @return il campionato della maglia indossata.
     */
    public String getChampionshipOfSticker() {
        return championshipOfSticker;
    }

    /** getter.
     *
     * @return se indossa la fascia di capitano.
     */
    public boolean isCaptainBandOfSticker() {
        return captainBandOfSticker;
    }

    /** getter.
     *
     * @return se ha il naso grosso.
     */
    public boolean isNoseDimensionBigOfSticker() {
        return noseDimensionBigOfSticker;
    }

    /** getter.
     *
     * @return se sta sorridendo.
     */
    public boolean isSmileOfSticker() {
        return smileOfSticker;
    }

    /** getter.
     *
     * @return se ha i capelli lisci.
     */
    public Boolean isHairTypeStraightOfSticker() {
        return hairTypeStraightOfSticker;
    }

    /** getter.
     *
     * @return la lunghezza della barba.
     */
    public String getBeardLengthOfSticker() {
        return beardLengthOfSticker;
    }

    /** setter.
     * @param nameOfSticker nome dello Sticker.
     */
    public void setNameOfSticker(String nameOfSticker) {
        this.nameOfSticker = nameOfSticker;
    }

    /** setter.
     * @param nicknameOfSticker nickname dello Sticker.
     */
    public void setNicknameOfSticker(String nicknameOfSticker) {
        this.nicknameOfSticker = nicknameOfSticker;
    }

    /** setter.
     * @param surnameOfSticker cognome dello Sticker.
     */
    public void setSurnameOfSticker(String surnameOfSticker) {
        this.surnameOfSticker = surnameOfSticker;
    }

    /** setter.
     * @param hairColorBrownOfSticker colore dei capelli dello Sticker.
     */
    public void setHairColorBrownOfSticker(boolean hairColorBrownOfSticker) {
        this.hairColorBrownOfSticker = hairColorBrownOfSticker;
    }

    /** setter.
     * @param hairLengthOfSticker lunghezza dei capelli dello Sticker.
     */
    public void setHairLengthOfSticker(String hairLengthOfSticker) {
        this.hairLengthOfSticker = hairLengthOfSticker;
    }

    /** setter.
     * @param beardColorBrownOfSticker colore della barba dello Sticker.
     */
    public void setBeardColorBrownOfSticker(Boolean beardColorBrownOfSticker) {
        this.beardColorBrownOfSticker = beardColorBrownOfSticker;
    }

    /** setter.
     * @param eyesColorBrownOfSticker colore degli occhi dello Sticker.
     */
    public void setEyesColorBrownOfSticker(boolean eyesColorBrownOfSticker) {
        this.eyesColorBrownOfSticker = eyesColorBrownOfSticker;
    }

    /** setter.
     * @param complexionBrownOfSticker colore della carnagione dello Sticker.
     */
    public void setComplexionBrownOfSticker(boolean complexionBrownOfSticker) {
        this.complexionBrownOfSticker = complexionBrownOfSticker;
    }

    /** setter.
     * @param earringsOfSticker orecchini dello Sticker.
     */
    public void setEarringsOfSticker(boolean earringsOfSticker) {
        this.earringsOfSticker = earringsOfSticker;
    }

    /** setter.
     * @param glassesOfSticker occhiali dello Sticker.
     */
    public void setGlassesOfSticker(boolean glassesOfSticker) {
        this.glassesOfSticker = glassesOfSticker;
    }

    /** setter.
     * @param headbandOfSticker fascia per i capelli dello Sticker.
     */
    public void setHeadbandOfSticker(boolean headbandOfSticker) {
        this.headbandOfSticker = headbandOfSticker;
    }

    /** setter.
     * @param moleOfSticker nei dello Sticker.
     */
    public void setMoleOfSticker(boolean moleOfSticker) {
        this.moleOfSticker = moleOfSticker;
    }

    /** setter.
     * @param beardTypeOfSticker tipo di barba dello Sticker.
     */
    public void setBeardTypeOfSticker(String beardTypeOfSticker) {
        this.beardTypeOfSticker = beardTypeOfSticker;
    }

    /** setter.
     * @param frecklesOfSticker lentiggini dello Sticker.
     */
    public void setFrecklesOfSticker(boolean frecklesOfSticker) {
        this.frecklesOfSticker = frecklesOfSticker;
    }

    /** setter.
     * @param nationalShirtOfSticker maglia dello Sticker.
     */
    public void setNationalShirtOfSticker(boolean nationalShirtOfSticker) {
        this.nationalShirtOfSticker = nationalShirtOfSticker;
    }

    /** setter.
     * @param continentOfSticker continente della maglia dello Sticker.
     */
    public void setContinentOfSticker(String continentOfSticker) {
        this.continentOfSticker = continentOfSticker;
    }

    /** setter.
     * @param championshipOfSticker campionato della maglia dello Sticker.
     */
    public void setChampionshipOfSticker(String championshipOfSticker) {
        this.championshipOfSticker = championshipOfSticker;
    }

    /** setter.
     * @param captainBandOfSticker fascia da capitano dello Sticker.
     */
    public void setCaptainBandOfSticker(boolean captainBandOfSticker) {
        this.captainBandOfSticker = captainBandOfSticker;
    }

    /** setter.
     * @param noseDimensionBigOfSticker dimensione del naso dello Sticker.
     */
    public void setNoseDimensionBigOfSticker(boolean noseDimensionBigOfSticker) {
        this.noseDimensionBigOfSticker = noseDimensionBigOfSticker;
    }

    /** setter.
     * @param smile sorriso dello Sticker.
     */
    public void setSmileOfSticker(boolean smile) {
        this.smileOfSticker = smile;
    }

    /** setter.
     * @param hairTypeStraightOfSticker tipo di capelli dello Sticker.
     */
    public void setHairTypeStraightOfSticker(Boolean hairTypeStraightOfSticker) {
        this.hairTypeStraightOfSticker = hairTypeStraightOfSticker;
    }

    /** setter.
     * @param beardLengthOfSticker lunghezza della barba dello Sticker.
     */
    public void setBeardLengthOfSticker(String beardLengthOfSticker) {
        this.beardLengthOfSticker = beardLengthOfSticker;
    }

}
