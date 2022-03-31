package team.gif.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.Robot;
import team.gif.robot.RobotMap;

public class Indexer extends SubsystemBase {
    // Hardware config
    private static final CANSparkMax beltMotor = new CANSparkMax(RobotMap.MOTOR_BELT_COMPBOT, CANSparkMaxLowLevel.MotorType.kBrushless); // CompBot motor
    private static final CANSparkMax midMotor = new CANSparkMax(RobotMap.MOTOR_MID_INDEX, CANSparkMaxLowLevel.MotorType.kBrushless);

    private static final DigitalInput sensorEntry = new DigitalInput(RobotMap.SENSOR_ENTRY);
    private static final DigitalInput sensorMid = new DigitalInput(RobotMap.SENSOR_MID);
    private static final DigitalInput sensorBelt = new DigitalInput(RobotMap.SENSOR_BELT);

    public Indexer() {
        super();
        beltMotor.restoreFactoryDefaults();
        midMotor.restoreFactoryDefaults();

        beltMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        midMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        beltMotor.setInverted(false);
        midMotor.setInverted(!Robot.isCompBot);
    }

    public boolean getSensorEntry() {
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
        beltMotor.set(percent);
    }

    public double getBeltMotorSpeed() {
        return beltMotor.getEncoder().getVelocity();
    }

    public int getCargoCount() {
        int count = 0;

        if (getSensorEntry()) {
            count++;
        }

        if (getSensorMid()) {
            count++;
        }

        if (getSensorBelt()) {
            count++;
        }

        return count;
    }
}
