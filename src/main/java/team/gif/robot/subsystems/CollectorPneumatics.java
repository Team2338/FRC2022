package team.gif.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;

public class CollectorPneumatics extends SubsystemBase {
	private static final DoubleSolenoid collectorSolenoidForward = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RobotMap.SOLENOID_COLLECTOR_FORWARD, RobotMap.SOLENOID_COLLECTOR_REVERSE);
	//private static final Solenoid collectorSolenoidReverse = new Solenoid(PneumaticsModuleType.CTREPCM, RobotMap.SOLENOID_COLLECTOR_REVERSE);

	public void lower() {
		collectorSolenoidForward.set(DoubleSolenoid.Value.kForward);
	}

	public void raise() {
		collectorSolenoidForward.set(DoubleSolenoid.Value.kReverse);
	}

	public void neutral() {
		collectorSolenoidForward.set(DoubleSolenoid.Value.kOff);
	}
}
