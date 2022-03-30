package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.TalonSRXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.Constants;
import team.gif.robot.RobotMap;

public class Climber extends SubsystemBase {

    private static final TalonSRX hangMotor = new TalonSRX(RobotMap.MOTOR_HANGER);
    private static final Solenoid hangBrake = new Solenoid(PneumaticsModuleType.CTREPCM, RobotMap.SOLENOID_CLIMBER_BRAKE);

    public Climber() {
        super();
        hangMotor.setInverted(true);
        hangMotor.setSensorPhase(true);
        hangMotor.configFactoryDefault();

        hangMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_4_AinTempVbat, 20);

        // Soft Limits
        hangMotor.configReverseSoftLimitEnable(true);
        hangMotor.configReverseSoftLimitThreshold(0);

        hangMotor.enableCurrentLimit(false);
        hangMotor.configRemoteFeedbackFilter(RobotMap.MOTOR_INTAKE, RemoteSensorSource.TalonSRX_SelectedSensor, 0);
        hangMotor.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.RemoteSensor0, 0, 0);

        hangMotor.setNeutralMode(NeutralMode.Brake);
        hangMotor.config_kP(0, Constants.Climber.P);
        hangMotor.config_kF(0, Constants.Climber.F);

        releaseClimberBrake();
    }

    public void zeroEncoder() {
        hangMotor.setSelectedSensorPosition(0);
    }

    public void setVoltage(double speed){
        hangMotor.set(ControlMode.Current, speed);
    }

    public void setSpeed(double speed) {
        hangMotor.set(ControlMode.PercentOutput, speed);
    }

    public void setPidPosition(double setPoint) {
        hangMotor.set(ControlMode.Position, setPoint);
    }

    public double getPosition() {
        return hangMotor.getSelectedSensorPosition();
    }

    public String getPosition_Shuffleboard() {
        return String.format("%11.2f",hangMotor.getSelectedSensorPosition());
    }
    
    public double getInputVoltage() {
        return hangMotor.getBusVoltage();
    }
    
    public double getOutputVoltage() {
        return hangMotor.getMotorOutputVoltage();
    }
    
    public double getInputCurrent() {
        return hangMotor.getSupplyCurrent();
    }
    
    public double getOutputCurrent() {
        return hangMotor.getStatorCurrent();
    }
    
    public double getOutputPercent() {
        return hangMotor.getMotorOutputPercent();
    }
    
    public double getVelocity() {
        return hangMotor.getSelectedSensorVelocity();
    }

    public void enableLowerSoftLimit(boolean engage){
        hangMotor.configReverseSoftLimitEnable(engage);
    }

    public void setClimberBrake() {
        hangBrake.set(false);
    }

    public void releaseClimberBrake() {
        hangBrake.set(true);
    }
}
