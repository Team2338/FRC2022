package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;

public class Intake extends SubsystemBase
{
    private static final TalonSRX intakeMotor = new TalonSRX(RobotMap.INTAKE);


    public Intake() {
        super();
        intakeMotor.setInverted(true);
        intakeMotor.setNeutralMode(NeutralMode.Brake);


    }

    public void setSpeed(double speed) {
        intakeMotor.set(ControlMode.PercentOutput, speed);

    }

}
