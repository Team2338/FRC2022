package team.gif.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;

public class Hood extends SubsystemBase {
    private static final DoubleSolenoid hood = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RobotMap.SOLENOID_HOOD_UP, RobotMap.SOLENOID_HOOD_DOWN);

    public Hood() {
        hood.set(DoubleSolenoid.Value.kReverse);
    }

    public void setHoodUp() {
        hood.set(DoubleSolenoid.Value.kForward);
    }

    public void setHoodDown() {
        hood.set(DoubleSolenoid.Value.kReverse);
    }
}
