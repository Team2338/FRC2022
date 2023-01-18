package team.gif.robot.subsystems.drivers;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import team.gif.robot.Globals;
import team.gif.robot.RobotMap;

public class Pigeon {

    public static PigeonIMU _pigeon;

    private static Pigeon instance = null;

    private final PigeonIMU.GeneralStatus _pigeonGenStatus = new PigeonIMU.GeneralStatus();

    private static double[] pitchHistory;

    public static Pigeon getInstance() {
        if (instance == null) {
            instance = new Pigeon();
        }
        return instance;
    }

    public Pigeon() {
        _pigeon = new PigeonIMU(RobotMap.PIGEON_CAN);
        instance = this;
        _pigeon.configFactoryDefault();

        pitchHistory = new double [] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    }

    public Pigeon(TalonSRX talon) {
        _pigeon = new PigeonIMU(talon);
        instance = this;
    }

    public void addPitch(double pitch){
        pitchHistory[19] = pitchHistory[18];
        pitchHistory[18] = pitchHistory[17];
        pitchHistory[17] = pitchHistory[16];
        pitchHistory[16] = pitchHistory[15];
        pitchHistory[15] = pitchHistory[14];
        pitchHistory[14] = pitchHistory[13];
        pitchHistory[13] = pitchHistory[12];
        pitchHistory[12] = pitchHistory[11];
        pitchHistory[11] = pitchHistory[10];
        pitchHistory[10] = pitchHistory[9];
        pitchHistory[9] = pitchHistory[8];
        pitchHistory[8] = pitchHistory[7];
        pitchHistory[7] = pitchHistory[6];
        pitchHistory[6] = pitchHistory[5];
        pitchHistory[5] = pitchHistory[4];
        pitchHistory[4] = pitchHistory[3];
        pitchHistory[3] = pitchHistory[2];
        pitchHistory[2] = pitchHistory[1];
        pitchHistory[1] = pitchHistory[0];
        pitchHistory[0] = pitch;
    }

    public void printHistory(){
        System.out.println( "history: " + pitchHistory[19] + " " + pitchHistory[0] );
    }

    public double comparePitch(){
        return pitchHistory[19] - pitchHistory[0];
    }
    public void addToShuffleboard(String tabName, String widgetTitle) {
        // Puts a Gyro type widget on dashboard and assigns
        // the function getHeading_Shuffleboard
        ShuffleboardTab tab = Shuffleboard.getTab(tabName); //gets a reference to the shuffleboard tab
        tab.add(widgetTitle, (x) -> {
            x.setSmartDashboardType("Gyro");
            x.addDoubleProperty("Value", () -> getCompassHeading(), null);
        });
    }

    /**
     * Returns heading from pigeon
     * turning counterclockwise, values increase
     * turning clockwise, values decrease
     * no rollover, can go negative
     * valid range is 23040 to -23040
     */
    public double getHeading() {
        double[] ypr = new double[3];

        _pigeon.getYawPitchRoll(ypr);

        return ypr[0];
    }

    public double getPitch() {
        double[] ypr = new double[3];

        _pigeon.getYawPitchRoll(ypr);

        return ypr[1];
    }

    public short getData() {
        short[] xyz = new short[3];

        _pigeon.getBiasedAccelerometer(xyz);

        return xyz[2];
    }

    public double getRoll() {
        double[] ypr = new double[3];

        _pigeon.getYawPitchRoll(ypr);

        return ypr[2];
    }
    /**
     * Returns heading values between -180 and 180
     * Does this by using the IEEEremainder function which
     * splits the remainder in half and returns anything greater than
     * half as negative.
     */
    public double get180Heading() {
        return Math.IEEEremainder(getHeading(), 360.0d);
    }

    /**
     * Returns heading from pigeon
     * from 0 to 359.99 turning counterclockwise
     */
    public double get360Heading() {
        double heading = getHeading(); // Returns heading from 23040 to -23040

        // Need to convert the heading to a value between 0 and 360
        heading = heading < 0 ? 360 + (heading % 360) : heading % 360;

        return heading;
    }

    /**
     * The heading value from the pigeon increases counterclockwise (0 North, 90 West, 180 South, 270 East)
     * Some features need degrees to look like a compass,
     * increasing clockwise (0 North, 90 East, 180 South, 270 West)
     * with rollover (max value 359.99, min 0)
     * <p>
     * Returns heading from pigeon
     * from 0 to 359.99 turning clockwise
     */
    public double getCompassHeading() {
        double heading = getHeading();

        // Get the heading. If the value is negative, need to flip it positive
        // also need to subtract from 360 to flip value to the correct compass heading
        heading = heading < 0 ? (0 - heading % 360) : (360 - (heading % 360));

        // 0 degree will result in 360, so set it back to 0
        heading = heading == 360.0 ? 0 : heading;

        return heading;
    }

    public boolean isActive() {
        _pigeon.getGeneralStatus(_pigeonGenStatus);

        return _pigeonGenStatus.state == PigeonIMU.PigeonState.Ready;
    }

    public void resetPigeonPosition() {
        setYaw(0);
    }

    public void setYaw(double yaw) {
        _pigeon.setYaw(yaw);
    }

    public double[] getYPR() {
        double[] ypr = new double[3];
        _pigeon.getYawPitchRoll(ypr);
        System.out.format("YPR %.1f %.1f %.1f", ypr[0], ypr[1], ypr[2]);
        return ypr;
    }

    public double[] getQuaternions() {
        // Quaternions
        double[] quaternions = new double[4];
        _pigeon.get6dQuaternion(quaternions);
        return quaternions;
    }

    public double getTemp() {
        return _pigeon.getTemp();
    }

    public double[] getAccumulatedGyro() {
        double[] accumGyro = new double[3];
        _pigeon.getAccumGyro(accumGyro);
        return accumGyro;
    }

    public short[] getBiasedAccel() {
        short[] biasedAccel = new short[3];
        _pigeon.getBiasedAccelerometer(biasedAccel);
        return biasedAccel;
    }

    public double[] getRawGyro() {
        double[] rawGyro = new double[3];
        _pigeon.getRawGyro(rawGyro);
        return rawGyro;
    } // Angular velocities

    public double[] getAccelAngles() {
        double[] accelAngles = new double[3];
        _pigeon.getAccelerometerAngles(accelAngles);
        return accelAngles;
    }

    public short[] getBiasedMagnetometer() {
        short[] biasedMagnet = new short[3];
        _pigeon.getBiasedMagnetometer(biasedMagnet);
        return biasedMagnet;
    }

    // Raw magnetometer
    public short[] getRawMagnet() {
        short[] rawMagnet = new short[3];
        _pigeon.getRawMagnetometer(rawMagnet);
        return rawMagnet;
    }
}
