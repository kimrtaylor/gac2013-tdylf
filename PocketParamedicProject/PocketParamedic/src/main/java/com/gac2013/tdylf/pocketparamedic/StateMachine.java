package com.gac2013.tdylf.pocketparamedic;

/**
 * Created by demouser on 8/14/13.
 */
public class StateMachine {

    public static final int NONE = -1;
    public static final int SCENE_SAFE = 0;

    public int current_state = SCENE_SAFE;
    public State[] states = new State[]{};

    public StateMachine(){
    }

    public State getCurrentState(){
        return getStateById(current_state);
    }

    public State getStateById(int state_id){
        for(int i=0; i<states.length; i++){
            if(states[i] != null && states[i].getId() == state_id)
                return states[i];
        }
        return null;
    }

    public void setCurrentState(int state){
        current_state = state;
    }
}
