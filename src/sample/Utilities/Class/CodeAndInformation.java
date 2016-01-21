package sample.Utilities.Class;

import com.google.gson.Gson;
import java.io.Serializable;

public class CodeAndInformation implements Serializable{

    private String code;
    private String information;

    public CodeAndInformation(String code, String information) {
        this.code = code;
        this.information = information;
    }

    public CodeAndInformation() {
    }

    @Override
    public String toString() {
        return "CodeAndInformation{" +
                "code='" + code + '\'' +
                ", information='" + information + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public static String serializeToJson(String firstParameter, String secondParameter){
        CodeAndInformation codeAndInformation = new CodeAndInformation(firstParameter, secondParameter);
        Gson gson = new Gson();
        String jsonString = gson.toJson(codeAndInformation);
        return jsonString;
    }

}
