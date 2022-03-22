package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.Constants;
import team.gif.robot.RobotMap;

public class Shooter extends SubsystemBase {
    private static final TalonFX shooterMotor = new TalonFX(RobotMap.MOTOR_SHOOTER);

    public Shooter() {
        super();
        shooterMotor.configFactoryDefault();

        shooterMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_4_AinTempVbat, 20);
        shooterMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 20);

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

        shooterMotor.configClosedloopRamp(1.0);
        shooterMotor.configOpenloopRamp(1.0);

        shooterMotor.selectProfileSlot(0, 0);
    }

    // Set the speed of the intake as a decimal percentage - values 0.00 -> 1.00
    public void setSpeedPercent(double percent) {
        shooterMotor.set(ControlMode.PercentOutput, percent);
    }

    public void setSpeedPID(double setPoint) {
        shooterMotor.set(ControlMode.Velocity, setPoint);
    }

    public double getSpeed() {
        return shooterMotor.getSelectedSensorVelocity();
    }

    public double getAcceleration(){
        if (shooterMotor.getControlMode() == ControlMode.Velocity){
            return shooterMotor.getErrorDerivative();
        }
        return 0;
    }

    public double getInputCurrent() {
        return shooterMotor.getSupplyCurrent();
    }

    public double getOutputCurrent() {
        return shooterMotor.getStatorCurrent();
    }

    public double getInputVoltage() {
        return shooterMotor.getBusVoltage();
    }

    public double getOutputVoltage() {
        return shooterMotor.getMotorOutputVoltage();
    }

    public double getGainP() {
        if (shooterMotor.getControlMode() == ControlMode.Velocity) {
            return Constants.Shooter.kP * shooterMotor.getClosedLoopError();
        }

        return 0;
    }

    public double getGainF() {
        if (shooterMotor.getControlMode() == ControlMode.Velocity) {
            return Constants.Shooter.kF * shooterMotor.getClosedLoopTarget();
        }

        return 0;
    }

    public double getGainI() {
        if (shooterMotor.getControlMode() == ControlMode.Velocity) {
            return Constants.Shooter.kI * shooterMotor.getIntegralAccumulator();
        }

        return 0;
    }

    public double getGainD() {
        if (shooterMotor.getControlMode() == ControlMode.Velocity) {
            return Constants.Shooter.kD * shooterMotor.getErrorDerivative();
        }

        return 0;
    }

    public double getOutputPercent() {
        return shooterMotor.getMotorOutputPercent();
    }

    public boolean isInTolerance() {
        //return Math.abs(shooterMotor.getClosedLoopError()) < Constants.Shooter.FLYWHEEL_TOLERANCE &&
        // ^^ Get the wheel to speed, press shooter button, end flywheel, release shooter button ... press shooter button, start flywheel ... belt moves
        // immediately and does not wait for the flywheel to get to speed. Changed it to the below
        if ( shooterMotor.getControlMode() == ControlMode.Disabled )
            return false;

        return (Math.abs( shooterMotor.getClosedLoopTarget() - getSpeed() ) < Constants.Shooter.FLYWHEEL_TOLERANCE) &&
            shooterMotor.getClosedLoopTarget() != 0;
    }

    public void setToNeutral() {
        shooterMotor.neutralOutput();
    }
}
