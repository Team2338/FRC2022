package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonSRXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Solenoid;
import team.gif.robot.Constants;
import team.gif.robot.RobotMap;

public class Shooter extends SubsystemBase
{
    private static final TalonSRX shooterMotor = new TalonSRX(RobotMap.SHOOTER);
    private static final Solenoid hood = new Solenoid(PneumaticsModuleType.CTREPCM, RobotMap.SOLENOID_HOOD);

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

        shooterMotor.config_kP(0, Constants.shooter.kP);
        shooterMotor.config_kI(0, Constants.shooter.kI);
        shooterMotor.config_kD(0, Constants.shooter.kD);
        shooterMotor.config_kF(0, Constants.shooter.kF);

        shooterMotor.selectProfileSlot(0, 0);

        hood.set(false);
    }

    //Set the speed of the intake as a decimal percentage - values 0.00 -> 1.00
    public void setSpeedPercent(double percent) {
        shooterMotor.set(ControlMode.PercentOutput, percent);
    }

    public void setSpeedPID(double setPoint) {
        shooterMotor.set(ControlMode.Velocity, setPoint);
    }

    public void setHood(boolean isUp) {
        hood.set(isUp);
    }
}
