package team.gif.lib;

public enum autoMode {

    MOBILITY(0),
    TWO_BALL_LEFT(0),
    TWO_BALL_RIGHT(0),
    TWO_BALL_MIDDLE(0),
    THREE_BALL_TERMINAL_MIDDLE(0),
    THREE_BALL_TERMINAL_RIGHT(0),
    FOUR_BALL_TERMINAL_RIGHT(0),
    FIVE_BALL_TERMINAL_RIGHT(0);

    private int value;

    autoMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
