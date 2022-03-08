package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.Constants;
import team.gif.robot.RobotMap;

public class Climber extends SubsystemBase {

    private static final TalonSRX hangMotor = new TalonSRX(RobotMap.MOTOR_HANGER);


    public Climber() {
        super();
        hangMotor.setInverted(false);
        hangMotor.configFactoryDefault();

        // Soft Limits
        hangMotor.enableCurrentLimit(false);


        hangMotor.setNeutralMode(NeutralMode.Brake);
        // TODO: can't call multiple times or we crash

        //TODO: ADD DEFAULT COMMAND FOR CLIMBER
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

    public void setF() {
        hangMotor.config_kF(0, Constants.Climber.F);
    }

    public void setFGravity() {
        hangMotor.config_kF(0, Constants.Climber.GRAV_FEED_FORWARD);
    }

    public double getPosition() {
        return hangMotor.getSelectedSensorPosition();
    }

    public String getPosition_Shuffleboard() {
        return String.format("%11.2f",hangMotor.getSelectedSensorPosition());
    }

    public void enableLowerSoftLimit(boolean engage){
        hangMotor.enableCurrentLimit(engage);
    }
}
