package com.gac2013.tdylf.pocketparamedic;

public class StateMachine {

    public static final int NONE = -1;
    public static final int SCENE_SAFE = 0;

    public static final int ID_A = 1;
    public static final int ID_B = 2;
    public static final int ID_C = 3;


    private static int currentState = ID_A;

    private static State[] states = new State[]{
            new State(ID_A, "A question", ID_B, ID_C, NONE, R.drawable.firstaid1),
            new State(ID_B, "A question", ID_C, ID_A, NONE, R.drawable.helpme),
            new State(ID_C, "A question", ID_A, ID_B, NONE, R.drawable.ic_launcher)
    };

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
