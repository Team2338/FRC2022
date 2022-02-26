package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;

public class Collector extends SubsystemBase {
    private static final TalonSRX collectorMotor = new TalonSRX(RobotMap.MOTOR_INTAKE);
    private static final CANSparkMax entryMotor = new CANSparkMax(RobotMap.MOTOR_ENTRY, CANSparkMaxLowLevel.MotorType.kBrushless);

    private static final DigitalInput sensorEntry = new DigitalInput(RobotMap.SENSOR_ENTRY);

    public Collector() {
        super();
        collectorMotor.setInverted(true);
        collectorMotor.setNeutralMode(NeutralMode.Brake);
        entryMotor.setInverted(true);
    }

    //Set the speed of the intake as a decimal percentage - values 0.00 -> 1.00
    public void setSpeedPercent(double percent) {
        collectorMotor.set(ControlMode.PercentOutput, percent);
        entryMotor.set(percent);
    }

    public boolean getEntrySensor(){
        return sensorEntry.get();
    }
}
