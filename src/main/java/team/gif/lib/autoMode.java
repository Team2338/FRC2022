package team.gif.lib;

public enum autoMode {

    MOBILITY(0),
    TWO_BALL(0),
    THREE_BALL_TERMINAL_RIGHT(0);


    private int value;
    autoMode(int value) {
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
}
