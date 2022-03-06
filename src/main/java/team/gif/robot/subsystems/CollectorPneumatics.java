package team.gif.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;

public class CollectorPneumatics extends SubsystemBase {
	private static final DoubleSolenoid collectorSolenoidForward = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RobotMap.SOLENOID_COLLECTOR_FORWARD, RobotMap.SOLENOID_COLLECTOR_REVERSE);
//    private static final Solenoid entrySolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, RobotMap.SOLENOID_ENTRY);
    private static final Solenoid entrySolenoid = new Solenoid(1,PneumaticsModuleType.CTREPCM,RobotMap.SOLENOID_ENTRY);
	//private static final Solenoid collectorSolenoidReverse = new Solenoid(PneumaticsModuleType.CTREPCM, RobotMap.SOLENOID_COLLECTOR_REVERSE);

	public void collectorLower() {
		collectorSolenoidForward.set(DoubleSolenoid.Value.kForward);
	}

	public void collectorRaise() {
		collectorSolenoidForward.set(DoubleSolenoid.Value.kReverse);
	}

	public void neutral() {
		collectorSolenoidForward.set(DoubleSolenoid.Value.kOff);
	}

    public void entryLower() {
        entrySolenoid.set(true);
    }

    public void entryRaise() {
        entrySolenoid.set(false);
    }
}
