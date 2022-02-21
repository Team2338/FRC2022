package team.gif.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;

public class Hood extends SubsystemBase {
	private static final Solenoid hood = new Solenoid(PneumaticsModuleType.CTREPCM, RobotMap.SOLENOID_HOOD);

	public Hood() {
		hood.set(false);
	}

	public void setHoodUp(boolean isUp) {
		hood.set(isUp);
	}
}
