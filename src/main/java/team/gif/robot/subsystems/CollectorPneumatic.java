package team.gif.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;

public class CollectorPneumatic extends SubsystemBase {
	private static final Solenoid collectorSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, RobotMap.SOLENOID_COLLECTOR);

	public CollectorPneumatic() {
		collectorSolenoid.set(false);
	}

	public void lower() {collectorSolenoid.set(true);}

	public void raise() {collectorSolenoid.set(false);}
}
