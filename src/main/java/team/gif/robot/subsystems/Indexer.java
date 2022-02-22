package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonSRXFeedbackDevice;
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
    private static final TalonSRX beltMotor = new TalonSRX(RobotMap.MOTOR_BELT);
    private static final CANSparkMax wheelMotor = new CANSparkMax(RobotMap.MOTOR_MID_INDEX, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static final SparkMaxPIDController wheelPIDControl = wheelMotor.getPIDController();

    private static final DigitalInput sensorWheel = new DigitalInput(RobotMap.SENSOR_MID);
    private static final DigitalInput sensorBelt = new DigitalInput(RobotMap.BELT);

    public Indexer() {
        super();
        beltMotor.configFactoryDefault();
        wheelMotor.restoreFactoryDefaults();

        beltMotor.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder, 0, 0);

        beltMotor.setNeutralMode(NeutralMode.Brake);
        wheelMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        beltMotor.setInverted(false); // subject to change based on design feats I don't remember
        wheelMotor.setInverted(true);
    }

    public boolean getSensorStage() {
        return sensorWheel.get();
    }

    public boolean getSensorBelt() {
        return sensorBelt.get();
    }

    public void setMidMotorSpeed(double percent) {
        wheelMotor.set(percent);
    }

    public void setBeltMotorSpeedPercent(double percent) {
        beltMotor.set(ControlMode.PercentOutput, percent);
    }

    public double getBeltMotorSpeed() {
        return beltMotor.getSelectedSensorVelocity();
    }
}