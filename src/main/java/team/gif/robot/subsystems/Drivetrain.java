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

    public static WPI_TalonSRX leftTalon1;
    public static WPI_TalonSRX leftTalon2;
    public static WPI_TalonSRX rightTalon1;
    public static WPI_TalonSRX rightTalon2;

    public static MotorControllerGroup leftMotors;
    public static MotorControllerGroup rightMotors;
    public static DifferentialDrive drive;

    // ------------ Variables for Trajectory ---------------
    public static WPI_TalonSRX leftEncoderTalon;
    public static WPI_TalonSRX rightEncoderTalon;
    public static DifferentialDriveOdometry odometry;
    private static Pigeon pigeon;
    private static int pigeonErrorCount;

    private static final int maxContinuousCurrentAmps = 15;
    private static final int maxPeakCurrentAmps = 20;

    /*    public static DifferentialDriveKinematics drivekinematics;
    public static ChassisSpeeds chassisSpeeds;
    public static DifferentialDriveWheelSpeeds wheelSpeeds;
    public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(Constants.drivetrain.kTrackwidthMeters);
*/
    // ------------ Variables for Trajectory End) ----------

    public Drivetrain() {
        super();
        leftTalon1 = new WPI_TalonSRX(RobotMap.MOTOR_DRIVE_LEFT_ONE);
        leftTalon2 = new WPI_TalonSRX(RobotMap.MOTOR_DRIVE_LEFT_TWO);
        rightTalon1 = new WPI_TalonSRX(RobotMap.MOTOR_DRIVE_RIGHT_ONE);
        rightTalon2 = new WPI_TalonSRX(RobotMap.MOTOR_DRIVE_RIGHT_TWO);

        leftTalon1.setNeutralMode(NeutralMode.Brake);
        leftTalon2.setNeutralMode(NeutralMode.Brake);
        rightTalon1.setNeutralMode(NeutralMode.Brake);
        rightTalon2.setNeutralMode(NeutralMode.Brake);

        leftMotors = new MotorControllerGroup(leftTalon1, leftTalon2);
        rightMotors = new MotorControllerGroup(rightTalon1, rightTalon2);
        drive = new DifferentialDrive(leftMotors, rightMotors);

        // turn off the drive train watchdog - otherwise it outputs unnecessary errors on the console
        drive.setSafetyEnabled(false);

        // any input to the motors less than this number will be converted to 0
        // any input about this number will scale from 0 to 1.0
        drive.setDeadband(Robot.isCompBot ? .02 : .05);

        // ------------  Trajectory Functionality ----------
        leftEncoderTalon = leftTalon2;
        rightEncoderTalon = rightTalon2;

        //leftEncoderTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        //rightEncoderTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);

        leftEncoderTalon.setSelectedSensorPosition(0);
        rightEncoderTalon.setSelectedSensorPosition(0);

        // left sensor needs to be inverted to match the drive train
        leftEncoderTalon.setSensorPhase(true);

        currentLimitingSetup();
        currentLimitingEnable(true);

        leftTalon1.configOpenloopRamp(0.1);
        leftTalon2.configOpenloopRamp(0.1);
        rightTalon1.configOpenloopRamp(0.1);
        rightTalon2.configOpenloopRamp(0.1);

        // Per WPILib, motor outputs for the right side are negated
        // within the differentialDrive class. No need to negate them again.
        leftTalon1.setInverted(false);
        leftTalon2.setInverted(false);
        rightTalon1.setInverted(false);
        rightTalon2.setInverted(false);

        pigeon = Robot.isCompBot ? new Pigeon(leftTalon1) : new Pigeon(rightTalon2);

        pigeon.resetPigeonPosition(); // set initial heading of pigeon to zero degrees

        resetEncoders();
        odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(0));
        resetPose();
    }

    public void currentLimitingSetup(){

        leftTalon1.configContinuousCurrentLimit(maxContinuousCurrentAmps);
        leftTalon2.configContinuousCurrentLimit(maxContinuousCurrentAmps);
        rightTalon1.configContinuousCurrentLimit(maxContinuousCurrentAmps);
        rightTalon2.configContinuousCurrentLimit(maxContinuousCurrentAmps);

        leftTalon1.configPeakCurrentLimit(maxPeakCurrentAmps);
        leftTalon2.configPeakCurrentLimit(maxPeakCurrentAmps);
        rightTalon1.configPeakCurrentLimit(maxPeakCurrentAmps);
        rightTalon2.configPeakCurrentLimit(maxPeakCurrentAmps);

    }

    public void currentLimitingEnable(boolean enableLimit){
        leftTalon1.enableCurrentLimit(enableLimit);
        leftTalon2.enableCurrentLimit(enableLimit);
        rightTalon1.enableCurrentLimit(enableLimit);
        rightTalon2.enableCurrentLimit(enableLimit);
    }

    // -------------- Teleop Driving -----------------------
    public void driveArcade(double speed, double rotation){
        drive.arcadeDrive(speed,rotation);
    }

    // -------------- Teleop Driving -----------------------
    public void driveCurvature(double speed, double rotation, boolean isQuick){
        drive.curvatureDrive(speed,rotation,isQuick);
    }

    // ---------- Previous Auto Driving & Tank Drive -------
    public void setSpeed(double leftPercent, double rightPercent) {
        drive.tankDrive(leftPercent,rightPercent);
    }


    // -------------- new for Trajectory -------------------
    @Override
    public void periodic() {
        // Update the odometry

        if (pigeon.isActive()) {
            odometry.update( Rotation2d.fromDegrees(pigeon.get180Heading()),
                getLeftEncoderPos_Meters(),
                getRightEncoderPos_Meters());
        } else {
            if(++pigeonErrorCount >= 100) { // only print every 2 seconds
                System.out.println("***   WARNING      \n***\n*** Cannot set robot odometry. Pigeon is not in ready state.\n***\n***");
                pigeonErrorCount = 0;
            }
        }
    }

    /**
     * Returns the currently-estimated pose of the robot.
     *
     * @return The pose.
     */
    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    /**
     * Returns the current wheel speeds of the robot.
     *
     * @return The current wheel speeds.
     */
    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(leftEncoderTalon.getSelectedSensorVelocity() * (10.0/4096) * Constants.Drivetrain.WHEEL_CIRCUMFERENCE,
                                                rightEncoderTalon.getSelectedSensorVelocity()* (10.0/4096) * Constants.Drivetrain.WHEEL_CIRCUMFERENCE);
    }

    /**
     * Controls the left and right sides of the drive directly with voltages.
     *
     * @param leftVolts  the commanded left output
     * @param rightVolts the commanded right output
     */
    public void tankDriveVolts(double leftVolts, double rightVolts) {
        // TODO
        SmartDashboard.putNumber("Left", leftVolts);
        SmartDashboard.putNumber("Right", rightVolts);
//        System.out.format("LV: %.2f   RV: %.2f\n", leftVolts,rightVolts);
        leftMotors.setVoltage(leftVolts);
        rightMotors.setVoltage(-rightVolts);
    }

    public void resetEncoders() {
        leftEncoderTalon.setSelectedSensorPosition(0);
        rightEncoderTalon.setSelectedSensorPosition(0);
    }

    public void resetPose(){
        odometry.resetPosition(new Pose2d(0, 0, Rotation2d.fromDegrees(0)), Rotation2d.fromDegrees(0));
    }

    public void resetPigeon() {
        pigeon.resetPigeonPosition();
    }

    // Encoder positions in Ticks
    public double getLeftEncoderPos_Ticks() {
        return leftEncoderTalon.getSelectedSensorPosition();
    }

    public double getRightEncoderPos_Ticks() {
        return rightEncoderTalon.getSelectedSensorPosition();
    }
    
    public double getLeftEncoderVelocity_Ticks() {
        return leftEncoderTalon.getSelectedSensorVelocity();
    }
    
    public double getRightEncoderVelocity_Ticks() {
        return rightEncoderTalon.getSelectedSensorVelocity();
    }

    // Encoder positions in Meters
    public double getLeftEncoderPos_Meters() {
        return leftEncoderTalon.getSelectedSensorPosition() / Constants.Drivetrain.TICKS_TO_METERS_LEFT;
    }

    public double getRightEncoderPos_Meters() {
        return rightEncoderTalon.getSelectedSensorPosition() / Constants.Drivetrain.TICKS_TO_METERS_RIGHT;
    }
    
    public double getInputVoltageL1() {
        return leftTalon1.getBusVoltage();
    }
    
    public double getInputVoltageL2() {
        return leftTalon2.getBusVoltage();
    }
    
    public double getInputVoltageR1() {
        return rightTalon1.getBusVoltage();
    }
    
    public double getInputVoltageR2() {
        return rightTalon2.getBusVoltage();
    }
    
    public double getOutputVoltageL1() {
        return leftTalon1.getMotorOutputVoltage();
    }
    
    public double getOutputVoltageL2() {
        return leftTalon2.getMotorOutputVoltage();
    }
    
    public double getOutputVoltageR1() {
        return rightTalon1.getMotorOutputVoltage();
    }
    
    public double getOutputVoltageR2() {
        return rightTalon2.getMotorOutputVoltage();
    }
    
    public double getOutputPercentL1() {
        return leftTalon1.getMotorOutputPercent();
    }
    
    public double getOutputPercentL2() {
        return leftTalon2.getMotorOutputPercent();
    }
    
    public double getOutputPercentR1() {
        return rightTalon1.getMotorOutputPercent();
    }
    
    public double getOutputPercentR2() {
        return rightTalon2.getMotorOutputPercent();
    }
    
    public double getInputCurrentL1() {
        return leftTalon1.getSupplyCurrent();
    }
    
    public double getInputCurrentL2() {
        return leftTalon2.getSupplyCurrent();
    }
    
    public double getInputCurrentR1() {
        return rightTalon1.getSupplyCurrent();
    }
    
    public double getInputCurrentR2() {
        return rightTalon2.getSupplyCurrent();
    }
    
    public double getOutputCurrentL1() {
        return leftTalon1.getStatorCurrent();
    }
    
    public double getOutputCurrentL2() {
        return leftTalon2.getStatorCurrent();
    }
    
    public double getOutputCurrentR1() {
        return rightTalon1.getStatorCurrent();
    }
    
    public double getOutputCurrentR2() {
        return rightTalon2.getStatorCurrent();
    }

    public double Ticks2Feet(double ticks) {
        return ticks / Constants.Drivetrain.TICKS_TO_METERS * 3.28084; // 3.28084 = feet to meters
    }
}
