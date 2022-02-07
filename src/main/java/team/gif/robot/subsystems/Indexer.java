package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;
import team.gif.robot.commands.indexer.IndexDefault;


public class Indexer extends SubsystemBase {
    //Hardware config
    private static final TalonSRX indexMotor = new TalonSRX(RobotMap.INDEXER);
    private static final TalonSRX beltMotor = new TalonSRX(RobotMap.BELT);

    private static final DigitalInput sOne = new DigitalInput(RobotMap.SENSOR_STAGE_ONE);
    private static final DigitalInput sTwo = new DigitalInput(RobotMap.SENSOR_STAGE_TWO);

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
