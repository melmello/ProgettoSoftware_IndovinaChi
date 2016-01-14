package sample.Utilities.Class;

import java.io.Serializable;

public class StickerQuery implements Serializable{

    String firstParameter;
    String secondParameter;

    public StickerQuery(String firstParameter, String secondParameter) {
        this.firstParameter = firstParameter;
        this.secondParameter = secondParameter;
    }

    public StickerQuery() {
    }

    @Override
    public String toString() {
        return "StickerQuery{" +
                "firstParameter='" + firstParameter + '\'' +
                ", secondParameter='" + secondParameter + '\'' +
                '}';
    }

    public String getFirstParameter() {
        return firstParameter;
    }

    public void setFirstParameter(String firstParameter) {
        this.firstParameter = firstParameter;
    }

    public String getSecondParameter() {
        return secondParameter;
    }

    public void setSecondParameter(String secondParameter) {
        this.secondParameter = secondParameter;
    }

}
