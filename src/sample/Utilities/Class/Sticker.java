package sample.Utilities.Class;

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
    private boolean freecklesOfSticker;
    private boolean nationalShirtOfSticker;
    private String continentOfSticker;
    private String championshipOfSticker;
    private boolean captainBandOfSticker;
    private boolean noseDimensionBigOfSticker;
    private boolean smileOfSticker;
    private Boolean hairTypeStraightOfSticker;
    private String beardLengthOfSticker;

    public Sticker(String nickname) {
        this.nicknameOfSticker = nicknameOfSticker;
    }

    public Sticker(String name, String nickname, String surname, boolean hairColorBrown, String hairLength, Boolean beardColorBrown, boolean eyesColorBrown, boolean complexionBrown, boolean earrings, boolean glasses, boolean headband, boolean mole, String beardType, boolean freeckles, boolean nationalShirt, String continent, String championship, boolean captainBand, boolean noseDimensionBig, boolean smile, Boolean hairTypeStraight, String beardLength) {
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
        this.freecklesOfSticker = freeckles;
        this.nationalShirtOfSticker = nationalShirt;
        this.continentOfSticker = continent;
        this.championshipOfSticker = championship;
        this.captainBandOfSticker = captainBand;
        this.noseDimensionBigOfSticker = noseDimensionBig;
        this.smileOfSticker = smile;
        this.hairTypeStraightOfSticker = hairTypeStraight;
        this.beardLengthOfSticker = beardLength;
    }

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
                ", freecklesOfSticker=" + freecklesOfSticker +
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

    public String getNameOfSticker() {
        return nameOfSticker;
    }

    public String getNicknameOfSticker() {
        return nicknameOfSticker;
    }

    public String getSurnameOfSticker() {
        return surnameOfSticker;
    }

    public boolean isHairColorBrownOfSticker() {
        return hairColorBrownOfSticker;
    }

    public String getHairLengthOfSticker() {
        return hairLengthOfSticker;
    }

    public Boolean isBeardColorBrownOfSticker() {
        return beardColorBrownOfSticker;
    }

    public boolean isEyesColorBrownOfSticker() {
        return eyesColorBrownOfSticker;
    }

    public boolean isComplexionBrownOfSticker() {return complexionBrownOfSticker;}

    public boolean isEarringsOfSticker() {
        return earringsOfSticker;
    }

    public boolean isGlassesOfSticker() {
        return glassesOfSticker;
    }

    public boolean isHeadbandOfSticker() {
        return headbandOfSticker;
    }

    public boolean isMoleOfSticker() {
        return moleOfSticker;
    }

    public String getBeardTypeOfSticker() {
        return beardTypeOfSticker;
    }

    public boolean isFreecklesOfSticker() {
        return freecklesOfSticker;
    }

    public boolean isNationalShirtOfSticker() {
        return nationalShirtOfSticker;
    }

    public String getContinentOfSticker() {
        return continentOfSticker;
    }

    public String getChampionshipOfSticker() {
        return championshipOfSticker;
    }

    public boolean isCaptainBandOfSticker() {
        return captainBandOfSticker;
    }

    public boolean isNoseDimensionBigOfSticker() {
        return noseDimensionBigOfSticker;
    }

    public boolean isSmileOfSticker() {
        return smileOfSticker;
    }

    public Boolean isHairTypeStraightOfSticker() {
        return hairTypeStraightOfSticker;
    }

    public String getBeardLengthOfSticker() {
        return beardLengthOfSticker;
    }

    public void setNameOfSticker(String nameOfSticker) {
        this.nameOfSticker = nameOfSticker;
    }

    public void setNicknameOfSticker(String nicknameOfSticker) {
        this.nicknameOfSticker = nicknameOfSticker;
    }

    public void setSurnameOfSticker(String surnameOfSticker) {
        this.surnameOfSticker = surnameOfSticker;
    }

    public void setHairColorBrownOfSticker(boolean hairColorBrownOfSticker) {
        this.hairColorBrownOfSticker = hairColorBrownOfSticker;
    }

    public void setHairLengthOfSticker(String hairLengthOfSticker) {
        this.hairLengthOfSticker = hairLengthOfSticker;
    }

    public void setBeardColorBrownOfSticker(Boolean beardColorBrownOfSticker) {
        this.beardColorBrownOfSticker = beardColorBrownOfSticker;
    }

    public void setEyesColorBrownOfSticker(boolean eyesColorBrownOfSticker) {
        this.eyesColorBrownOfSticker = eyesColorBrownOfSticker;
    }

    public void setComplexionBrownOfSticker(boolean complexionBrownOfSticker) {
        this.complexionBrownOfSticker = complexionBrownOfSticker;
    }

    public void setEarringsOfSticker(boolean earringsOfSticker) {
        this.earringsOfSticker = earringsOfSticker;
    }

    public void setGlassesOfSticker(boolean glassesOfSticker) {
        this.glassesOfSticker = glassesOfSticker;
    }

    public void setHeadbandOfSticker(boolean headbandOfSticker) {
        this.headbandOfSticker = headbandOfSticker;
    }

    public void setMoleOfSticker(boolean moleOfSticker) {
        this.moleOfSticker = moleOfSticker;
    }

    public void setBeardTypeOfSticker(String beardTypeOfSticker) {
        this.beardTypeOfSticker = beardTypeOfSticker;
    }

    public void setFreecklesOfSticker(boolean freecklesOfSticker) {
        this.freecklesOfSticker = freecklesOfSticker;
    }

    public void setNationalShirtOfSticker(boolean nationalShirtOfSticker) {
        this.nationalShirtOfSticker = nationalShirtOfSticker;
    }

    public void setContinentOfSticker(String continentOfSticker) {
        this.continentOfSticker = continentOfSticker;
    }

    public void setChampionshipOfSticker(String championshipOfSticker) {
        this.championshipOfSticker = championshipOfSticker;
    }

    public void setCaptainBandOfSticker(boolean captainBandOfSticker) {
        this.captainBandOfSticker = captainBandOfSticker;
    }

    public void setNoseDimensionBigOfSticker(boolean noseDimensionBigOfSticker) {
        this.noseDimensionBigOfSticker = noseDimensionBigOfSticker;
    }

    public void setSmileOfSticker(boolean smile) {
        this.smileOfSticker = smile;
    }

    public void setHairTypeStraightOfSticker(Boolean hairTypeStraightOfSticker) {
        this.hairTypeStraightOfSticker = hairTypeStraightOfSticker;
    }

    public void setBeardLengthOfSticker(String beardLengthOfSticker) {
        this.beardLengthOfSticker = beardLengthOfSticker;
    }
}
