package team.gif.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;

public class ClimberPneumatics extends SubsystemBase {
    public void ClimberPneumatics(){};

	private static final DoubleSolenoid fangs = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RobotMap.SOLENOID_FANG_FORWARD, RobotMap.SOLENOID_FANG_REVERSE);

	public void setFangsOut() {
		fangs.set(DoubleSolenoid.Value.kForward);
	}

	public void setFangsIn() {
		fangs.set(DoubleSolenoid.Value.kReverse);
	}
}
