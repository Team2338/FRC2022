package team.gif.lib;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.math.trajectory.constraint.TrajectoryConstraint;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import team.gif.robot.Constants;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.Drivetrain;

import java.awt.*;

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
            new SimpleMotorFeedforward(Constants.drivetrain.ksVolts,
                Constants.drivetrain.kvVoltSecondsPerMeter,
                Constants.drivetrain.kaVoltSecondsSquaredPerMeter),
            Constants.drivetrain.kDriveKinematics,
            10);

    /**
     * Creates a config for Forward trajectory
     */
    public TrajectoryConfig configForward = new TrajectoryConfig(
        Constants.autoConstants.kMaxSpeedMetersPerSecond, //kMaxSpeedMetersPerSecond
        Constants.autoConstants.kMaxAccelerationMetersPerSecondSquared) //kMaxAccelerationMetersPerSecondSquared
        // Add kinematics to ensure max speed is actually obeyed
        .setKinematics(Constants.drivetrain.kDriveKinematics)
        //.setReversed(false)
        // Apply the voltage constraint
        .addConstraint(autoVoltageConstraint)
        .addConstraint( new CentripetalAccelerationConstraint(1));

    public TrajectoryConfig configForwardFast = new TrajectoryConfig(
            Constants.autoConstants.kFastSpeedMetersPerSecond,
            Constants.autoConstants.kFastAccelerationMetersPerSecondSquared)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(Constants.drivetrain.kDriveKinematics)
            //.setReversed(false)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint);

    public TrajectoryConfig configForwardSlow = new TrajectoryConfig(
            Constants.autoConstants.kSlowSpeedMetersPerSecond,
            Constants.autoConstants.kSlowAccelerationMetersPerSecondSquared)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(Constants.drivetrain.kDriveKinematics)
            //.setReversed(true)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint);

    public TrajectoryConfig configForwardIRFast = new TrajectoryConfig(
        6.0,
        5.0)
        // Add kinematics to ensure max speed is actually obeyed
        .setKinematics(Constants.drivetrain.kDriveKinematics)
        //.setReversed(false)
        // Apply the voltage constraint
        .addConstraint(autoVoltageConstraint)
        .addConstraint( new CentripetalAccelerationConstraint(1.25));

    public TrajectoryConfig configReverseIRFast = new TrajectoryConfig(
            6.0,
            5.0)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(Constants.drivetrain.kDriveKinematics)
            .setReversed(true)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint)
            .addConstraint( new CentripetalAccelerationConstraint(1.25));

    public TrajectoryConfig configForwardIRSlow = new TrajectoryConfig(
            2.5,
            2.5)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(Constants.drivetrain.kDriveKinematics)
            //.setReversed(false)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint)
            .addConstraint( new CentripetalAccelerationConstraint(1.0));

    public TrajectoryConfig configReverseIRSlow = new TrajectoryConfig(
            2.5,
            2.5)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(Constants.drivetrain.kDriveKinematics)
            .setReversed(true)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint)
            .addConstraint( new CentripetalAccelerationConstraint(1.0));

    /**
     * Creates a config for Reverse trajectory
     */
    public TrajectoryConfig configReverse = new TrajectoryConfig(
        Constants.autoConstants.kMaxSpeedMetersPerSecond,
        Constants.autoConstants.kMaxAccelerationMetersPerSecondSquared)
        // Add kinematics to ensure max speed is actually obeyed
        .setKinematics(Constants.drivetrain.kDriveKinematics)
        .setReversed(true)
        // Apply the voltage constraint
        .addConstraint(autoVoltageConstraint)
        .addConstraint( new CentripetalAccelerationConstraint(1));

    /**
     * Creates a config for Reverse trajectory
     */
    public TrajectoryConfig configReverseSlow = new TrajectoryConfig(
        Constants.autoConstants.kSlowSpeedMetersPerSecond,
        Constants.autoConstants.kSlowAccelerationMetersPerSecondSquared)
        // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(Constants.drivetrain.kDriveKinematics)
            .setReversed(true)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint);
    /**
     * Creates a Ramsete command given the defined trajectory
     */
    public RamseteCommand createRamseteCommand(Trajectory trajectory) {
        RamseteCommand rc = new RamseteCommand(trajectory,
            Robot.drivetrain::getPose,
            new RamseteController(Constants.autoConstants.kRamseteB, Constants.autoConstants.kRamseteZeta),
            new SimpleMotorFeedforward(Constants.drivetrain.ksVolts,
                Constants.drivetrain.kvVoltSecondsPerMeter,
                Constants.drivetrain.kaVoltSecondsSquaredPerMeter),
            Constants.drivetrain.kDriveKinematics,
            Robot.drivetrain::getWheelSpeeds,
            new PIDController(Constants.drivetrain.kPDriveVelLeft, 0, 0),
            new PIDController(Constants.drivetrain.kPDriveVelRight, 0, 0),
            // RamseteCommand passes volts to the callback
            Robot.drivetrain::tankDriveVolts,
            Robot.drivetrain);

        return rc;
    }
}
