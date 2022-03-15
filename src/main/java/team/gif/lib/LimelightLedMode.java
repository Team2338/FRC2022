package team.gif.lib;

public enum LimelightLedMode {
    CURRENT_PIPELINE(0),
    OFF(1),
    BLINK(2),
    ON(3);

    private final int value;

    LimelightLedMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
