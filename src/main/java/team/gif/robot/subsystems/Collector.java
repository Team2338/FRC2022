package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;

public class Collector extends SubsystemBase {
    private static final TalonSRX intakeMotor = new TalonSRX(RobotMap.MOTOR_INTAKE);
    private static final CANSparkMax helperMotor = new CANSparkMax(RobotMap.MOTOR_HELPER, CANSparkMaxLowLevel.MotorType.kBrushless);

    private static final Solenoid collectorSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, RobotMap.SOLENOID_COLLECTOR);

    public Collector() {
        super();
        intakeMotor.setInverted(true);
        intakeMotor.setNeutralMode(NeutralMode.Brake);
    }

    //Set the speed of the intake as a decimal percentage - values 0.00 -> 1.00
    public void setSpeedPercent(double percent) {
        intakeMotor.set(ControlMode.PercentOutput, percent);
        helperMotor.set(percent);
    }

    public void setSolenoid(boolean isExtended) {
        collectorSolenoid.set(isExtended);
    }
}
