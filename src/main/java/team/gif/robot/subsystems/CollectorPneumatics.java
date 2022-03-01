package team.gif.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;

public class CollectorPneumatics extends SubsystemBase {
	private static final Solenoid collectorSolenoidForward = new Solenoid(PneumaticsModuleType.CTREPCM, RobotMap.SOLENOID_COLLECTOR_FORWARD);
	private static final Solenoid collectorSolenoidReverse = new Solenoid(PneumaticsModuleType.CTREPCM, RobotMap.SOLENOID_COLLECTOR_REVERSE);

	public void lower() {
		collectorSolenoidForward.set(true);
		collectorSolenoidReverse.set(false);
	}

	public void raise() {
		collectorSolenoidForward.set(false);
		collectorSolenoidReverse.set(true);
	}

	public void neutral() {
		collectorSolenoidForward.set(false);
		collectorSolenoidReverse.set(false);
	}
}
