package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonSRXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.Constants;
import team.gif.robot.RobotMap;


public class Indexer extends SubsystemBase {
    //Hardware config
    private static final TalonSRX beltMotor = new TalonSRX(RobotMap.MOTOR_BELT);
    private static final CANSparkMax midMotor = new CANSparkMax(RobotMap.MOTOR_MID_INDEX, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static final SparkMaxPIDController midPIDControl = midMotor.getPIDController();

    private static final I2C.Port i2cPort = I2C.Port.kOnboard;
    private static final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);
    private static final ColorMatch colorMatch = new ColorMatch();

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

    public boolean getSensorMid() {
        return sensorMid.get();
    }

    public String getColorMid() {
        Color detectedColor = colorSensor.getColor();
        ColorMatchResult match = colorMatch.matchClosestColor(detectedColor);
        if(match.color == Constants.GameConstants.kBlueTarget) {
            return "Blue";
        } else if(match.color == Constants.GameConstants.kRedTarget) {
            return "Red";
        } else {
            return null;
        }
    }

    public boolean getSensorBelt() {
        return sensorBelt.get();
    }

    public String getColorBelt() {
        Color detectedColor = colorSensor.getColor();
        ColorMatchResult match = colorMatch.matchClosestColor(detectedColor);
        if(match.color == Constants.GameConstants.kBlueTarget) {
            return "Blue";
        } else if(match.color == Constants.GameConstants.kRedTarget) {
            return "Red";
        } else {
            return null;
        }
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