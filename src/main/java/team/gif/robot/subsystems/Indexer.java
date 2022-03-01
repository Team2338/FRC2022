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
    private static final CANSparkMax midMotor = new CANSparkMax(RobotMap.MOTOR_MID_INDEX, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static final SparkMaxPIDController midPIDControl = midMotor.getPIDController();

    private static final DigitalInput sensorEntry = new DigitalInput(RobotMap.SENSOR_ENTRY);
    private static final DigitalInput sensorMid = new DigitalInput(RobotMap.SENSOR_MID);
    private static final DigitalInput sensorBelt = new DigitalInput(RobotMap.SENSOR_BELT);

    public Indexer() {
        super();
        beltMotor.configFactoryDefault();
        midMotor.restoreFactoryDefaults();

        beltMotor.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder, 0, 0);

        beltMotor.setNeutralMode(NeutralMode.Brake);
        midMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        beltMotor.setInverted(false); // subject to change based on design feats I don't remember
        midMotor.setInverted(true);
    }

    public boolean getEntrySensor(){
        return sensorEntry.get();
    }

    public boolean getSensorMid() {
        return sensorMid.get();
    }

    public boolean getSensorBelt() {
        return sensorBelt.get();
    }

    public void setMidMotorSpeed(double percent) {
        midMotor.set(percent);
    }

    public void setBeltMotorSpeedPercent(double percent) {
        beltMotor.set(ControlMode.PercentOutput, percent);
    }

    public double getBeltMotorSpeed() {
        return beltMotor.getSelectedSensorVelocity();
    }
}