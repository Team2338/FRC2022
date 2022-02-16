package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;


public class Indexer extends SubsystemBase {
    //Hardware config
    private static final TalonSRX wheelMotor = new TalonSRX(RobotMap.WHEEL_INDEX);
    private static final TalonSRX beltMotor = new TalonSRX(RobotMap.BELT);

    private static final DigitalInput sensorWheel = new DigitalInput(RobotMap.SENSOR_STAGE_ONE);
    private static final DigitalInput sensorBelt = new DigitalInput(RobotMap.SENSOR_STAGE_TWO);

    public Indexer() {
        super();
        wheelMotor.setNeutralMode(NeutralMode.Brake);
        beltMotor.setNeutralMode(NeutralMode.Brake);

        wheelMotor.setInverted(false); // subject to change based on design feats I don't remember
        beltMotor.setInverted(false);
    }

    public boolean getSensorWheel() {
        return sensorWheel.get();
    }

    public boolean getSensorBelt() {
        return sensorBelt.get();
    }

    public void setIndexMotorSpeed(double percent) {
        wheelMotor.set(ControlMode.PercentOutput, percent);
    }

    public void setBeltMotorSpeed(double percent) {
        wheelMotor.set(ControlMode.PercentOutput, percent);
    }

}
