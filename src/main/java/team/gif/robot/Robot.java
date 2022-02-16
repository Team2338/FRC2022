// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import team.gif.robot.commands.exampleShuffleboardEntryCommand;
import team.gif.robot.subsystems.Hood;
import team.gif.robot.subsystems.drivers.Pigeon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import team.gif.robot.commands.indexer.IndexScheduler;
import team.gif.robot.commands.indexer.IndexerIdle;
import team.gif.robot.commands.shooter.ShooterIdle;
import team.gif.robot.subsystems.Indexer;
import team.gif.robot.subsystems.Intake;
import team.gif.robot.commands.drivetrain.Drive;
import team.gif.robot.subsystems.Drivetrain;
import team.gif.robot.subsystems.Shooter;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    public static final boolean isCompBot = false;

    private Command m_autonomousCommand;
    private RobotContainer m_robotContainer;
    public static Pigeon m_pigeon = null;
    private static Command driveCommand = null;
    public static Drivetrain drivetrain = null;
    public static OI oi;

    public static Hood hood = null;
    public static Intake intake = null;
    public static Indexer indexer = null;
    public static Shooter shooter = null;
    public static Compressor compressor = null;
    public static NetworkTableEntry exampleShuffleboardEntry;
    // T.S: Creating an new tab in shuffleboard.
    ShuffleboardTab tab = Shuffleboard.getTab("FRC2022 test");
    public exampleShuffleboardEntryCommand exampleShuffleboardEntryCommand;
    // TS: the value of the something what is changing,(Example PID control).
    public static double exampleShuffleboardEntrySyncValue;
    // TS: the value is getting the getEntry number
    public static double exampleShuffleboardValue  = exampleShuffleboardEntrySyncValue;

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        m_robotContainer = new RobotContainer();

        drivetrain = new Drivetrain();
        driveCommand = new Drive();

        compressor = new Compressor(RobotMap.COMPRESSOR_HOOD, PneumaticsModuleType.CTREPCM);
        intake = new Intake();
        indexer = new Indexer();
        shooter = new Shooter();
        hood = new Hood();

        indexer.setDefaultCommand(new IndexerIdle());
        shooter.setDefaultCommand(new ShooterIdle());

        // TS: getting the submit button when you click the commend.
        exampleShuffleboardEntryCommand = new exampleShuffleboardEntryCommand();

        m_pigeon = new Pigeon();
        m_pigeon.addToShuffleboard("Shuffleboard", "Pigeon");

        // TS: add an getEntry tab in shuffleboard
        exampleShuffleboardEntry = tab.add("Example Input",exampleShuffleboardValue )
                .getEntry();
        // TS: add the example input submit button to the shuffleboard.
        tab.add("Command", exampleShuffleboardEntryCommand);
        exampleShuffleboardEntry.setDouble(exampleShuffleboardEntrySyncValue);
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
        // commands, running already-scheduled commands, removing finished or interrupted commands,
        // and running subsystem periodic() methods.  This must be called from the robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();
    }

    /** This function is called once each time the robot enters Disabled mode. */
    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}

    /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
    @Override
    public void autonomousInit() {
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();

        // schedule the autonomous command (example)
        if (m_autonomousCommand != null) {
            m_autonomousCommand.schedule();
        }
    }

    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
        }

        oi = new OI();
        driveCommand.schedule();
    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {
        double timeLeft = DriverStation.getMatchTime();
        oi.setRumble((timeLeft <= 40.0 && timeLeft >= 36.0) ||
                     (timeLeft <=  5.0 && timeLeft >=  3.0));
    }

    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    /** This function is called periodically during test mode. */
    @Override
    public void testPeriodic() {}
}
