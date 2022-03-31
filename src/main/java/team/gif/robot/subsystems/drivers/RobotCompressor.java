package team.gif.robot.subsystems.drivers;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import team.gif.robot.RobotMap;

public class RobotCompressor extends Compressor {
    public static AnalogInput pressureSensor = null;

    public RobotCompressor(int port, PneumaticsModuleType moduleType) {
        super(port, moduleType);
        pressureSensor = new AnalogInput(RobotMap.SENSOR_AIR_PRESSURE);
    }

    public double getPressure() {
        return 250 * (pressureSensor.getVoltage() / 4.82) - 25;
    }
}
