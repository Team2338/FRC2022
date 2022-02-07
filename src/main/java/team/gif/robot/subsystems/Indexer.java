package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;

import com.revrobotics.ColorSensorV3;
import team.gif.robot.commands.indexer.IndexDefault;

import java.awt.*;

public class Indexer extends SubsystemBase {
    //Hardware config
    private static final TalonSRX indexMotor = new TalonSRX(RobotMap.INDEXER);
    private static final TalonSRX beltMotor = new TalonSRX(RobotMap.BELT);

    private static final DigitalInput sOne = new DigitalInput(RobotMap.SENSOR_STAGE_ONE);
    private static final DigitalInput sTwo = new DigitalInput(RobotMap.SENSOR_STAGE_TWO);

    private static final I2C.Port i2c = I2C.Port.kOnboard;
    private static final I2C.Port i2c2 = I2C.Port.kOnboard;
    private static final ColorSensorV3 colorSense = new ColorSensorV3(i2c);
    private static final ColorSensorV3 colorSense2 = new ColorSensorV3(i2c2);

    public Indexer() {
        super();
        indexMotor.setNeutralMode(NeutralMode.Brake);
        beltMotor.setNeutralMode(NeutralMode.Brake);

        indexMotor.setInverted(false); // subject to change based on design feats I don't remember
        beltMotor.setInverted(false);
    }

    public boolean[] sensorStates() {
        return new boolean[]{sOne.get(), sTwo.get()};
    }

    public String[] senseColors() {
        Color detected1 = colorSense.getColor();
        Color detected2 = colorSense2.getColor();
        String[] colors = new String[2];

        if(detected1.blue > 200 && detected1.red < 100) {
            colors[0] = "blue";
        }
        if(detected1.red > 200 && detected1.blue < 100) {
            colors[0] = "red";
        }
        if(detected2.blue > 200 && detected2.red < 100) {
            colors[1] = "blue";
        }
        if(detected2.red > 200 && detected1.red < 100) {
            colors[1] = "red";
        }
        if(!sensorStates()[0]) {
            colors[0] = null;
        }
        if(!sensorStates()[1]) {
            colors[1] = null;
        }
        return colors;
    }

    public void setIndexMotor(double voltage) {
        indexMotor.set(ControlMode.PercentOutput, voltage);
    }

    public void setBeltMotor(double voltage) {
        indexMotor.set(ControlMode.PercentOutput, voltage);
    }

    public void setDefaultCommand(Command defaultCommand) {
        super.setDefaultCommand(new IndexDefault());
    }
}
