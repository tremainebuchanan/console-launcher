package com.tremainebuchanan.consolelauncher.models;

/**
 * Created by captain_kirk on 8/12/17.
 */

public class Command {
    private String user_input;

    public Command(){}

    public  Command(String user_input){
        this.user_input = user_input;
    }

    public String getUserInput(){
        return this.user_input;
    }

    public void setUserInput(String user_input){
        this.user_input = user_input;
    }
}
