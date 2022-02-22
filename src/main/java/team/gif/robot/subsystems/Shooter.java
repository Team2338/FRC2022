package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.Constants;
import team.gif.robot.Robot;
import team.gif.robot.RobotMap;

public class Shooter extends SubsystemBase
{
    private static final TalonFX shooterMotor = new TalonFX(RobotMap.SHOOTER);

    public Shooter() {
        super();
        shooterMotor.configFactoryDefault();

        shooterMotor.setNeutralMode(NeutralMode.Coast);

        shooterMotor.setInverted(InvertType.InvertMotorOutput);
        shooterMotor.setInverted(TalonFXInvertType.Clockwise);

        // Configure soft and hard limits
        shooterMotor.configForwardSoftLimitEnable(false);
        shooterMotor.configReverseSoftLimitEnable(false);
        shooterMotor.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);
        shooterMotor.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);

        // Configure the sensor
        shooterMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 0);

        shooterMotor.config_kP(0, Constants.Shooter.kP);
        shooterMotor.config_kI(0, Constants.Shooter.kI);
        shooterMotor.config_kD(0, Constants.Shooter.kD);
        shooterMotor.config_kF(0, Constants.Shooter.kF);

        shooterMotor.selectProfileSlot(0, 0);
    }

    //Set the speed of the intake as a decimal percentage - values 0.00 -> 1.00
    public void setSpeedPercent(double percent) {
        shooterMotor.set(ControlMode.PercentOutput, percent);
    }

    public void setSpeedPID(double setPoint) {
        shooterMotor.set(ControlMode.Velocity, setPoint);
    }

    public double getSpeed(){
        return shooterMotor.getSelectedSensorVelocity();
    }

    public String getVelocity_Shuffleboard(){ return String.format("%12.0f",getSpeed());}

    public boolean isInToleranceHigh() {
        return Math.abs(getSpeed() - Robot.shooterRpm) < 2000;
    }

    public boolean isInToleranceLow() {
        return Math.abs(getSpeed() - Constants.Shooter.RPM_LOW) < 2000;
    }

}
