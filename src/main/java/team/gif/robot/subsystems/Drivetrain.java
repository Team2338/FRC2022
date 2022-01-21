package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.Constants;
import team.gif.robot.Robot;
import team.gif.robot.RobotMap;
import team.gif.robot.subsystems.drivers.Pigeon;

public class Drivetrain extends SubsystemBase {

    private static Drivetrain instance = null;

    public static WPI_TalonSRX leftTalon1;
    public static WPI_TalonSRX leftTalon2;
    public static WPI_TalonSRX rightTalon1;
    public static WPI_TalonSRX rightTalon2;

    public static MotorControllerGroup m_leftMotors;
    public static MotorControllerGroup m_rightMotors;
    public static DifferentialDrive m_drive;

    // ------------ Variables for Trajectory ---------------
    public static WPI_TalonSRX _leftEncoderTalon;
    public static WPI_TalonSRX _rightEncoderTalon;
    public static DifferentialDriveOdometry m_odometry;
    private static Pigeon m_pigeon;

    private static int maxCurrentAmps = 15;

    /*    public static DifferentialDriveKinematics drivekinematics;
    public static ChassisSpeeds chassisSpeeds;
    public static DifferentialDriveWheelSpeeds wheelSpeeds;
    public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(Constants.drivetrain.kTrackwidthMeters);
*/
    // ------------ Variables for Trajectory End) ----------

    public static Drivetrain getInstance() {
        if (instance == null) {
            System.out.println("drivetrain init");
            instance = new Drivetrain();
        }
        return instance;
    }

    private Drivetrain() {
        super();

        leftTalon1 = new WPI_TalonSRX(RobotMap.DRIVE_LEFT_ONE);
        leftTalon2 = new WPI_TalonSRX(RobotMap.DRIVE_LEFT_TWO);
        rightTalon1 = new WPI_TalonSRX(RobotMap.DRIVE_RIGHT_ONE);
        rightTalon2 = new WPI_TalonSRX(RobotMap.DRIVE_RIGHT_TWO);

        leftTalon1.setNeutralMode(NeutralMode.Brake);
        leftTalon2.setNeutralMode(NeutralMode.Brake);
        rightTalon1.setNeutralMode(NeutralMode.Brake);
        rightTalon2.setNeutralMode(NeutralMode.Brake);

        m_leftMotors = new MotorControllerGroup(leftTalon1, leftTalon2);
        m_rightMotors = new MotorControllerGroup(rightTalon1, rightTalon2);
        m_drive = new DifferentialDrive(m_leftMotors, m_rightMotors);

        // turn off the drive train watchdog - otherwise it outputs unnecessary errors on the console
        m_drive.setSafetyEnabled(false);

        // any input to the motors less than this number will be converted to 0
        // any input about this number will scale from 0 to 1.0
        m_drive.setDeadband(Robot.isCompBot ? .02 : .05);

        // ------------  Trajectory Functionality ----------
        _leftEncoderTalon = leftTalon1;
        _rightEncoderTalon = rightTalon1;

        _leftEncoderTalon.setSelectedSensorPosition(0);
        _rightEncoderTalon.setSelectedSensorPosition(0);

        // left sensor needs to be inverted to match the drive train
        _leftEncoderTalon.setSensorPhase(true);

        currentLimitingSetup();
        currentLimitingEnable(true);

        // Per WPILib, motor outputs for the right side are negated
        // within the differentialDrive class. No need to negate them again.
        leftTalon1.setInverted(false);
        leftTalon2.setInverted(false);
        rightTalon1.setInverted(false);
        rightTalon2.setInverted(false);

        m_pigeon = Robot.isCompBot ? new Pigeon(leftTalon2) : new Pigeon();

        m_pigeon.resetPigeonPosition(); // set initial heading of pigeon to zero degrees

        resetEncoders();
        m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(0));
        resetPose();
    }

    public void currentLimitingSetup(){

        leftTalon1.configContinuousCurrentLimit(maxCurrentAmps);
        leftTalon2.configContinuousCurrentLimit(maxCurrentAmps);
        rightTalon1.configContinuousCurrentLimit(maxCurrentAmps);
        rightTalon2.configContinuousCurrentLimit(maxCurrentAmps);

        leftTalon1.configPeakCurrentLimit(maxCurrentAmps);
        leftTalon2.configPeakCurrentLimit(maxCurrentAmps);
        rightTalon1.configPeakCurrentLimit(maxCurrentAmps);
        rightTalon2.configPeakCurrentLimit(maxCurrentAmps);

    }

    public void currentLimitingEnable(boolean enableLimit){
        leftTalon1.enableCurrentLimit(enableLimit);
        leftTalon2.enableCurrentLimit(enableLimit);
        rightTalon1.enableCurrentLimit(enableLimit);
        rightTalon2.enableCurrentLimit(enableLimit);
    }

    // -------------- Teleop Driving -----------------------
    public void driveArcade(double speed, double rotation){
        m_drive.arcadeDrive(speed,rotation);
    }

    // -------------- Teleop Driving -----------------------
    public void driveCurvature(double speed, double rotation, boolean isQuick){
        m_drive.curvatureDrive(speed,rotation,isQuick);
    }

    // ---------- Previous Auto Driving & Tank Drive -------
    public void setSpeed(double leftPercent, double rightPercent) {
        m_drive.tankDrive(leftPercent,rightPercent);
    }


    // -------------- new for Trajectory -------------------
    @Override
    public void periodic() {
        // Update the odometry

        if (m_pigeon.isActive()) {
            m_odometry.update( Rotation2d.fromDegrees(m_pigeon.get180Heading()),
                getLeftEncoderPos_Meters(),
                getRightEncoderPos_Meters());
        } else {
            System.out.println("Cannot set robot odometry. Pigeon is not in ready state.");
        }
    }

    /**
     * Returns the currently-estimated pose of the robot.
     *
     * @return The pose.
     */
    public Pose2d getPose() {
        return m_odometry.getPoseMeters();
    }

    /**
     * Returns the current wheel speeds of the robot.
     *
     * @return The current wheel speeds.
     */
    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(_leftEncoderTalon.getSelectedSensorVelocity() * (10.0/4096) * Constants.drivetrain.WHEEL_CIRCUMFERENCE,
                                                _rightEncoderTalon.getSelectedSensorVelocity()* (10.0/4096) * Constants.drivetrain.WHEEL_CIRCUMFERENCE);
    }

    /**
     * Controls the left and right sides of the drive directly with voltages.
     *
     * @param leftVolts  the commanded left output
     * @param rightVolts the commanded right output
     */
    public void tankDriveVolts(double leftVolts, double rightVolts) {
        SmartDashboard.putNumber("Left", leftVolts);
        SmartDashboard.putNumber("Right", rightVolts);
//        System.out.format("LV: %.2f   RV: %.2f\n", leftVolts,rightVolts);
        m_leftMotors.setVoltage(leftVolts);
        m_rightMotors.setVoltage(-rightVolts);
    }

    public void resetEncoders() {
        _leftEncoderTalon.setSelectedSensorPosition(0);
        _rightEncoderTalon.setSelectedSensorPosition(0);
    }

    public void resetPose(){
        m_odometry.resetPosition(new Pose2d(0, 0, Rotation2d.fromDegrees(0)), Rotation2d.fromDegrees(0));
    }

    public void resetPigeon() {
        m_pigeon.resetPigeonPosition();
    }

    //encoder positions in Ticks
    public double getLeftEncoderPos_Ticks() {
        return _leftEncoderTalon.getSelectedSensorPosition();
    }

    public double getRightEncoderPos_Ticks() {
        return _rightEncoderTalon.getSelectedSensorPosition();
    }

    //encoder positions in Meters
    public double getLeftEncoderPos_Meters() {
        return _leftEncoderTalon.getSelectedSensorPosition() / Constants.drivetrain.TICKS_TO_METERS_LEFT;
    }

    public double getRightEncoderPos_Meters() {
        return _rightEncoderTalon.getSelectedSensorPosition() / Constants.drivetrain.TICKS_TO_METERS_RIGHT;
    }

    public double Ticks2Feet(double ticks) {
        return ticks / Constants.drivetrain.TICKS_TO_METERS * 3.28084; // 3.28084 = feet to meters
    }
}