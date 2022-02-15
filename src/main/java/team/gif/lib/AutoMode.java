package team.gif.lib;

public enum AutoMode {
	
	MOBILITY(0),
	SHOOT_1_BALL(0);
	
	private int value;
	
	AutoMode(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}
