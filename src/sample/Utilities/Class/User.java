package sample.Utilities.Class;

import java.io.Serializable;

public class User implements Serializable{

    String userUsername;
    String userPassword;

    //costruttore
    public User(String textWithUsername, String textWithPassword) {
        this.userPassword = textWithPassword;
        this.userUsername = textWithUsername;
    }

    @Override
    public String toString() {
        return "User{" +
                "userUsername='" + userUsername + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }
}
