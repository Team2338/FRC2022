package team.gif.robot.subsystems;

import com.revrobotics.*;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.Constants;
import team.gif.robot.RobotMap;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climber extends SubsystemBase {
//    public static Hanger instance = null;

    private static final CANSparkMax hangMotor = new CANSparkMax(RobotMap.HANGER, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static final SparkMaxPIDController hangPIDController = hangMotor.getPIDController();
    private static final RelativeEncoder hangEncoder = hangMotor.getEncoder();
    private static final SparkMaxLimitSwitch limitSwitch = hangMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);


    public Climber() {
        super();
        hangMotor.setInverted(false);
        hangMotor.restoreFactoryDefaults();

        // Limit Switch
        limitSwitch.enableLimitSwitch(true);
        // Soft Limits
        hangMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, false);
        hangMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, false);

        //TODO: DISABLE HARD & SOFT LIMITS

        hangMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        SmartDashboard.putBoolean("Hang Control", false);

        //TODO: REPLACE SMARTDASHBOARD WITH NETWORKENTRY
        //TODO: ADD DEFAULT COMMAND FOR CLIMBER
    }

    public void zeroEncoder() {
        hangEncoder.setPosition(0);
    }

    public void setVoltage(double speed){
        hangMotor.setVoltage(speed);
    }

    public void setSpeed(double speed) {
        hangMotor.set(speed);
    }

    public void setF() {
        hangPIDController.setFF(Constants.Climber.F);
    }

    public void setFGravity() {
        hangPIDController.setFF(Constants.Climber.GRAV_FEED_FORWARD);
    }

    public double getPosition() {
        return hangEncoder.getPosition();
    }

    public String getPosition_Shuffleboard() {
        return String.format("%11.2f",hangEncoder.getPosition());
    }

    public void enableLowerSoftLimit(boolean engage){hangMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, engage);};
}
