package com.gac2013.tdylf.pocketparamedic;

public class StateMachine {

    public static final int NONE = -1;
    public static final int ASK_SAFE = 0;
    public static final int ASK_CONSCIOUS = 1;
    public static final int DO_CONSCIOUS = 2;
    public static final int ASK_CONSCIOUS_TWO = 3;
    public static final int DO_CALL = 4;
    public static final int ASK_BREATHING = 5;
    public static final int DO_CPR = 6;
    public static final int ASK_BREATHING_TWO = 7;
    public static final int DO_PULSE = 8;
    public static final int ASK_PULSE = 9;
    public static final int LOG_PULSE = 10;
    public static final int ASK_SPINE = 11;
    public static final int DO_RECOVERY = 12;
    public static final int DO_MONITOR = 13;

    public static State[] states = new State[]{
            new State(ASK_SAFE, "Is the scene safe? ", ASK_CONSCIOUS, DO_CPR, NONE, 0),
            new State(ASK_CONSCIOUS, "Is the victim conscious?", NONE, DO_CONSCIOUS, NONE, 0),
            new State(DO_CONSCIOUS, "Shake victim and shout to them.", NONE, NONE, ASK_CONSCIOUS_TWO, 0),
            new State(ASK_CONSCIOUS_TWO, "Is the victim conscious now?", NONE, DO_CALL, NONE, 0),
            new State(DO_CALL, "Call 9 9 9!", NONE, NONE, ASK_BREATHING, 0),
            new State(ASK_BREATHING, "Is the victim breathing?", NONE, DO_CPR, NONE, 0),
            new State(DO_CPR, "Start CPR in time with me. Starting in 3... 2... 1", NONE, NONE, ASK_SAFE, 0), //play audio file automatically go on
            new State(ASK_BREATHING_TWO, "Stop! Is the victim breathing now?", DO_PULSE, NONE, NONE, 0),
            new State(DO_PULSE, "Take pulse. Get ready to count in 3... 2... 1. Start!", NONE, NONE, NONE, 0), // hardcode 10 seconds delay - "done" automatically
            new State(ASK_PULSE, "Stop! Could you detect a pulse?", LOG_PULSE, NONE, NONE, 0),
            new State(LOG_PULSE, "Tell me the number of pulses that you counted.", NONE, NONE, NONE, 0), //slider or voice input
            new State(ASK_SPINE, "Do you suspect a spine or head injury?", NONE, DO_RECOVERY, NONE, 0),
            new State(DO_RECOVERY, "Put victim into recovery position.", NONE, NONE, DO_MONITOR, 0),
            new State(DO_MONITOR, "Monitor breathing and pulse until help arrives.", NONE, NONE, NONE, 0)};

    private static int currentState = ASK_SAFE;

    public static State getCurrentState(){
        return getStateById(currentState);
    }

    public static State getStateById(int state_id){
        for(int i=0; i<states.length; i++){
            if(states[i] != null && states[i].getId() == state_id)
                return states[i];
        }
        return getStateById(ASK_SAFE);
    }

    public static boolean isCurrentStateDo(){
        return isStateDo(currentState);
    }

    public static boolean isCurrentStateAsk(){
        return isStateAsk(currentState);
    }

    public static boolean isStateDo(int id){
        return getStateById(id).getDoneAnswered() != NONE;
    }

    public static boolean isStateAsk(int id){
        return !isStateDo(id);
    }

    public static void setCurrentState(int state){
        currentState = state;
    }
}
