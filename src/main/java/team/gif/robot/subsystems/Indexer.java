package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.Constants;
import team.gif.robot.RobotMap;


public class Indexer extends SubsystemBase {
    //Hardware config
    private static final TalonSRX wheelMotor = new TalonSRX(RobotMap.MOTOR_MID_INDEX);
    private static final CANSparkMax beltMotor = new CANSparkMax(RobotMap.MOTOR_BELT, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static final SparkMaxPIDController beltPIDControl = beltMotor.getPIDController();

    private static final DigitalInput sensorWheel = new DigitalInput(RobotMap.SENSOR_MID);
    private static final DigitalInput sensorBelt = new DigitalInput(RobotMap.BELT);

    public Indexer() {
        super();
        wheelMotor.configFactoryDefault();
        beltMotor.restoreFactoryDefaults();

        wheelMotor.setNeutralMode(NeutralMode.Brake);
        beltMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        wheelMotor.setInverted(false); // subject to change based on design feats I don't remember
        beltMotor.setInverted(false);

        beltPIDControl.setP(Constants.Indexer.kPBelt);
        beltPIDControl.setI(Constants.Indexer.kIBelt);
        beltPIDControl.setD(Constants.Indexer.kDBelt);
        beltPIDControl.setFF(Constants.Indexer.kFFBelt);
        beltPIDControl.setIZone(Constants.Indexer.kIZoneBelt);
    }

    public boolean getSensorStage() {
        return sensorWheel.get();
    }

    public boolean getSensorBelt() {
        return sensorBelt.get();
    }

    public void setMidMotorSpeed(double percent) {
        wheelMotor.set(ControlMode.PercentOutput, percent);
    }

    public void setBeltMotorSpeedPercent(double percent) {
        beltMotor.set(percent);
    }

    public void setBeltMotorSpeedPID(double setPoint) {
        beltPIDControl.setReference(setPoint, CANSparkMax.ControlType.kVelocity);
    }

    public double getBeltMotorSpeed() {
        return beltMotor.getEncoder().getVelocity();
    }
}