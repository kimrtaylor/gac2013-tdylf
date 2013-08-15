package com.gac2013.tdylf.pocketparamedic;

public class StateMachine {

    public static final int NONE = -1;
    public static final int ASK_SCENE_SAFE = 0;
    public static final int DO_GLOVES = 1;
    public static final int DO_SAVE_SCENE = 2;
    public static final int ASK_CONSCIOUS = 3;
    public static final int ASK_18 = 4;

    public static State[] states = new State[]{ new State(ASK_SCENE_SAFE, "Is the scene safe? ", DO_GLOVES, DO_SAVE_SCENE, NONE, 0),
                                         new State(DO_GLOVES, "Put on non-latex sterile gloves. ", NONE, NONE, ASK_CONSCIOUS, 0),
                                         new State(DO_SAVE_SCENE, "Safe the scene. ", NONE, NONE, ASK_SCENE_SAFE, 0),
                                         new State(ASK_CONSCIOUS, "Is the victim conscious? ", ASK_18, ASK_SCENE_SAFE, NONE, 0),
                                         new State(ASK_18, "Is the victim over 18? ", NONE, NONE, ASK_SCENE_SAFE, 0)};

    private static int currentState = ASK_SCENE_SAFE;

    public static State getCurrentState(){
        return getStateById(currentState);
    }

    public static State getStateById(int state_id){
        for(int i=0; i<states.length; i++){
            if(states[i] != null && states[i].getId() == state_id)
                return states[i];
        }
        return null;
    }

    public static void setCurrentState(int state){
        currentState = state;
    }
}
