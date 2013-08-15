package com.gac2013.tdylf.pocketparamedic;

/**
 * Created by demouser on 8/14/13.
 */
public class State {

    private int id;
    private String question;
    private int yesAnswered;
    private int noAnswered;
    private int doneAnswered;
    private int imageRference;

    public State(int id, String question, int yesAnswered, int noAnswered, int doneAnswered, int imageReference){
        this.id = id;
        this.question = question;
        this.yesAnswered = yesAnswered;
        this.noAnswered = noAnswered;
        this.doneAnswered = doneAnswered;
        this.imageRference = imageReference;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public int getYesAnswered() {
        return yesAnswered;
    }

    public int getNoAnswered() {
        return noAnswered;
    }

    public int getDoneAnswered() {
        return doneAnswered;
    }

    public int getImageResId() {
        return imageRference;
    }
}
