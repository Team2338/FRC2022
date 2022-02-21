package team.gif.lib;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import team.gif.robot.Constants;
import team.gif.robot.Robot;

public class RobotTrajectory {

    public RobotTrajectory(){}

    private static RobotTrajectory instance = null;

    public static RobotTrajectory getInstance() {
        if (instance == null) {
            instance = new RobotTrajectory();
        }
        return instance;
    }

    /**
     * Creates a common DifferentialDriveVoltageConstraint
     * for all autonomous commands to utilize
     */
    public DifferentialDriveVoltageConstraint autoVoltageConstraint =
        new DifferentialDriveVoltageConstraint(
            new SimpleMotorFeedforward(Constants.Drivetrain.ksVolts,
                Constants.Drivetrain.kvVoltSecondsPerMeter,
                Constants.Drivetrain.kaVoltSecondsSquaredPerMeter),
            Constants.Drivetrain.kDriveKinematics,
            10);

    /**
     * Creates a config for Forward trajectory
     */
    public TrajectoryConfig configForward = new TrajectoryConfig(
        Constants.Auto.kMaxSpeedMetersPerSecond, //kMaxSpeedMetersPerSecond
        Constants.Auto.kMaxAccelerationMetersPerSecondSquared) //kMaxAccelerationMetersPerSecondSquared
        // Add kinematics to ensure max speed is actually obeyed
        .setKinematics(Constants.Drivetrain.kDriveKinematics)
        //.setReversed(false)
        // Apply the voltage constraint
        .addConstraint(autoVoltageConstraint)
        .addConstraint( new CentripetalAccelerationConstraint(1));

    public TrajectoryConfig configForwardFast = new TrajectoryConfig(
            Constants.Auto.kFastSpeedMetersPerSecond,
            Constants.Auto.kFastAccelerationMetersPerSecondSquared)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(Constants.Drivetrain.kDriveKinematics)
            //.setReversed(false)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint);

    public TrajectoryConfig configForwardSlow = new TrajectoryConfig(
            Constants.Auto.kSlowSpeedMetersPerSecond,
            Constants.Auto.kSlowAccelerationMetersPerSecondSquared)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(Constants.Drivetrain.kDriveKinematics)
            //.setReversed(true)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint);

    public TrajectoryConfig configForwardIRFast = new TrajectoryConfig(
        6.0,
        5.0)
        // Add kinematics to ensure max speed is actually obeyed
        .setKinematics(Constants.Drivetrain.kDriveKinematics)
        //.setReversed(false)
        // Apply the voltage constraint
        .addConstraint(autoVoltageConstraint)
        .addConstraint( new CentripetalAccelerationConstraint(1.25));

    public TrajectoryConfig configReverseIRFast = new TrajectoryConfig(
            6.0,
            5.0)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(Constants.Drivetrain.kDriveKinematics)
            .setReversed(true)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint)
            .addConstraint( new CentripetalAccelerationConstraint(1.25));

    public TrajectoryConfig configForwardIRSlow = new TrajectoryConfig(
            2.5,
            2.5)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(Constants.Drivetrain.kDriveKinematics)
            //.setReversed(false)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint)
            .addConstraint( new CentripetalAccelerationConstraint(1.0));

    public TrajectoryConfig configReverseIRSlow = new TrajectoryConfig(
            2.5,
            2.5)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(Constants.Drivetrain.kDriveKinematics)
            .setReversed(true)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint)
            .addConstraint( new CentripetalAccelerationConstraint(1.0));

    /**
     * Creates a config for Reverse trajectory
     */
    public TrajectoryConfig configReverse = new TrajectoryConfig(
        Constants.Auto.kMaxSpeedMetersPerSecond,
        Constants.Auto.kMaxAccelerationMetersPerSecondSquared)
        // Add kinematics to ensure max speed is actually obeyed
        .setKinematics(Constants.Drivetrain.kDriveKinematics)
        .setReversed(true)
        // Apply the voltage constraint
        .addConstraint(autoVoltageConstraint)
        .addConstraint( new CentripetalAccelerationConstraint(1));

    /**
     * Creates a config for Reverse trajectory
     */
    public TrajectoryConfig configReverseSlow = new TrajectoryConfig(
        Constants.Auto.kSlowSpeedMetersPerSecond,
        Constants.Auto.kSlowAccelerationMetersPerSecondSquared)
        // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(Constants.Drivetrain.kDriveKinematics)
            .setReversed(true)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint);
    /**
     * Creates a Ramsete command given the defined trajectory
     */
    public RamseteCommand createRamseteCommand(Trajectory trajectory) {
        RamseteCommand rc = new RamseteCommand(trajectory,
            Robot.drivetrain::getPose,
            new RamseteController(Constants.Auto.kRamseteB, Constants.Auto.kRamseteZeta),
            new SimpleMotorFeedforward(Constants.Drivetrain.ksVolts,
                Constants.Drivetrain.kvVoltSecondsPerMeter,
                Constants.Drivetrain.kaVoltSecondsSquaredPerMeter),
            Constants.Drivetrain.kDriveKinematics,
            Robot.drivetrain::getWheelSpeeds,
            new PIDController(Constants.Drivetrain.kPDriveVelLeft, 0, 0),
            new PIDController(Constants.Drivetrain.kPDriveVelRight, 0, 0),
            // RamseteCommand passes volts to the callback
            Robot.drivetrain::tankDriveVolts,
            Robot.drivetrain);

        return rc;
    }
}
