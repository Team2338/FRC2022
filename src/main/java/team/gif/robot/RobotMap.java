package team.gif.robot;

public abstract class RobotMap {

    // Drive Motors
    public static final int MOTOR_DRIVE_LEFT_ONE = 0;
    public static final int MOTOR_DRIVE_LEFT_TWO = 1;
    public static final int MOTOR_DRIVE_RIGHT_ONE = 4;
    public static final int MOTOR_DRIVE_RIGHT_TWO = 5;

    // Other Motors
    public static final int MOTOR_INTAKE = 3;
    public static final int MOTOR_MID_INDEX = 10;
    public static final int MOTOR_BELT_PRACTICE = 31; //2
    public static final int MOTOR_BELT_COMPBOT = 2;
    public static final int MOTOR_SHOOTER = 13;
    public static final int MOTOR_HANGER = 15; //TODO
    public static final int MOTOR_ENTRY = 16;

    // Sensors
    public static final int SENSOR_MID = 0;
    public static final int SENSOR_BELT = 1;
    public static final int SENSOR_ENTRY = 2;

    // Solenoids
    public static final int SOLENOID_FANG_FORWARD = 1;
    public static final int SOLENOID_FANG_REVERSE = 6;
    public static final int SOLENOID_HOOD_UP = 5;
    public static final int SOLENOID_HOOD_DOWN = 2; // 6;
    public static final int SOLENOID_COLLECTOR_FORWARD = 7; // 4;
    public static final int SOLENOID_COLLECTOR_REVERSE = 0; // 5;
    public static final int SOLENOID_ENTRY = 5;

    // Compressors //
    public static final int COMPRESSOR_HOOD = 0; // todo rename

    // Color Sensor
    public static final int COLOR_SENSOR = 0;

    // Controllers
    public static final int DRIVER_CONTROLLER_ID = 0;
    public static final int AUX_CONTROLLER_ID = 1;
    public static final int TEST_CONTROLLER_ID = 2;

    //pigeon
    public static final int PIGEON_CAN = 5;
    /*
    public static final int SOLENOID_ZERO = 0;
    public static final int SOLENOID_ONE = 1;
    public static final int SOLENOID_TWO = 2;
    */

}
