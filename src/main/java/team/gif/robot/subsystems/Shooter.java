package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;

public class Shooter extends SubsystemBase
{
    private static final TalonSRX shooterMotor = new TalonSRX(RobotMap.INTAKE);

    public Shooter() {
        super();
        shooterMotor.configFactoryDefault();

        shooterMotor.setNeutralMode(NeutralMode.Brake);
        shooterMotor.setInverted(InvertType.InvertMotorOutput);

        // Configure soft and hard limits
        shooterMotor.configForwardSoftLimitEnable(false);
        shooterMotor.configReverseSoftLimitEnable(false);
        shooterMotor.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);
        shooterMotor.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);

        // Configure the sensor
        shooterMotor.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder, 0, 0);
        shooterMotor.setSensorPhase(true);

        shooterMotor.config_kP(0, 0.18);
        shooterMotor.config_kI(0, 0);
        shooterMotor.config_kD(0, 0);
        shooterMotor.config_kF(0, 0.032);

        shooterMotor.selectProfileSlot(0, 0);
    }

    //Set the speed of the intake as a decimal percentage - values 0.00 -> 1.00
    public void setSpeedPercent(double percent) {
        shooterMotor.set(ControlMode.PercentOutput, percent);
    }

    public void setSpeedPID(double setPoint) {
        shooterMotor.set(ControlMode.Velocity, setPoint);
    }


}
