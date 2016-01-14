package sample.Utilities.Class;

import java.io.Serializable;

public class Sticker implements Serializable{

    String name;
    String nickname;
    String surname;
    boolean hairColorBrown;
    String hairLenght;
    boolean beardColorBrown;
    boolean eyesColorBrown;
    boolean complexionBrown;
    boolean earrings;
    boolean glasses;
    boolean headband;
    boolean mole;
    String beardType;
    boolean freeckles;
    boolean nationalShirt;
    String continent;
    String championship;
    boolean captainBand;
    boolean noseDimensionBig;
    boolean smile;
    boolean hairTypeStraight;
    String beardLenght;

    public Sticker(String nickname) {
        this.nickname = nickname;
    }

    public Sticker(String name, String nickname, String surname, boolean hairColorBrown, String hairLenght, boolean beardColorBrown, boolean eyesColorBrown, boolean complexionBrown, boolean earrings, boolean glasses, boolean headband, boolean mole, String beardType, boolean freeckles, boolean nationalShirt, String continent, String championship, boolean captainBand, boolean noseDimensionBig, boolean smile, boolean hairTypeStraight, String beardLenght) {
        this.name = name;
        this.nickname = nickname;
        this.surname = surname;
        this.hairColorBrown = hairColorBrown;
        this.hairLenght = hairLenght;
        this.beardColorBrown = beardColorBrown;
        this.eyesColorBrown = eyesColorBrown;
        this.complexionBrown = complexionBrown;
        this.earrings = earrings;
        this.glasses = glasses;
        this.headband = headband;
        this.mole = mole;
        this.beardType = beardType;
        this.freeckles = freeckles;
        this.nationalShirt = nationalShirt;
        this.continent = continent;
        this.championship = championship;
        this.captainBand = captainBand;
        this.noseDimensionBig = noseDimensionBig;
        this.smile = smile;
        this.hairTypeStraight = hairTypeStraight;
        this.beardLenght = beardLenght;
    }

    @Override
    public String toString() {
        return "Sticker{" +
                "name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", surname='" + surname + '\'' +
                ", hairColorBrown=" + hairColorBrown +
                ", hairLenght='" + hairLenght + '\'' +
                ", beardColorBrown=" + beardColorBrown +
                ", eyesColorBrown=" + eyesColorBrown +
                ", complexionBrown=" + complexionBrown +
                ", earrings=" + earrings +
                ", glasses=" + glasses +
                ", headband=" + headband +
                ", mole=" + mole +
                ", beardType='" + beardType + '\'' +
                ", freeckles=" + freeckles +
                ", nationalShirt=" + nationalShirt +
                ", continent='" + continent + '\'' +
                ", championship='" + championship + '\'' +
                ", captainBand=" + captainBand +
                ", noseDimensionBig=" + noseDimensionBig +
                ", smile=" + smile +
                ", hairTypeStraight=" + hairTypeStraight +
                ", beardLenght='" + beardLenght + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSurname() {
        return surname;
    }

    public boolean isHairColorBrown() {
        return hairColorBrown;
    }

    public String getHairLenght() {
        return hairLenght;
    }

    public boolean isBeardColorBrown() {
        return beardColorBrown;
    }

    public boolean isEyesColorBrown() {
        return eyesColorBrown;
    }

    public boolean isComplexionBrown() {
        return complexionBrown;
    }

    public boolean isEarrings() {
        return earrings;
    }

    public boolean isGlasses() {
        return glasses;
    }

    public boolean isHeadband() {
        return headband;
    }

    public boolean isMole() {
        return mole;
    }

    public String getBeardType() {
        return beardType;
    }

    public boolean isFreeckles() {
        return freeckles;
    }

    public boolean isNationalShirt() {
        return nationalShirt;
    }

    public String getContinent() {
        return continent;
    }

    public String getChampionship() {
        return championship;
    }

    public boolean isCaptainBand() {
        return captainBand;
    }

    public boolean isNoseDimensionBig() {
        return noseDimensionBig;
    }

    public boolean isSmile() {
        return smile;
    }

    public boolean isHairTypeStraight() {
        return hairTypeStraight;
    }

    public String getBeardLenght() {
        return beardLenght;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setHairColorBrown(boolean hairColorBrown) {
        this.hairColorBrown = hairColorBrown;
    }

    public void setHairLenght(String hairLenght) {
        this.hairLenght = hairLenght;
    }

    public void setBeardColorBrown(boolean beardColorBrown) {
        this.beardColorBrown = beardColorBrown;
    }

    public void setEyesColorBrown(boolean eyesColorBrown) {
        this.eyesColorBrown = eyesColorBrown;
    }

    public void setComplexionBrown(boolean complexionBrown) {
        this.complexionBrown = complexionBrown;
    }

    public void setEarrings(boolean earrings) {
        this.earrings = earrings;
    }

    public void setGlasses(boolean glasses) {
        this.glasses = glasses;
    }

    public void setHeadband(boolean headband) {
        this.headband = headband;
    }

    public void setMole(boolean mole) {
        this.mole = mole;
    }

    public void setBeardType(String beardType) {
        this.beardType = beardType;
    }

    public void setFreeckles(boolean freeckles) {
        this.freeckles = freeckles;
    }

    public void setNationalShirt(boolean nationalShirt) {
        this.nationalShirt = nationalShirt;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public void setChampionship(String championship) {
        this.championship = championship;
    }

    public void setCaptainBand(boolean captainBand) {
        this.captainBand = captainBand;
    }

    public void setNoseDimensionBig(boolean noseDimensionBig) {
        this.noseDimensionBig = noseDimensionBig;
    }

    public void setSmile(boolean smile) {
        this.smile = smile;
    }

    public void setHairTypeStraight(boolean hairTypeStraight) {
        this.hairTypeStraight = hairTypeStraight;
    }

    public void setBeardLenght(String beardLenght) {
        this.beardLenght = beardLenght;
    }
}
