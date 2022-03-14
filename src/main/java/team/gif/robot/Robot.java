// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.*;
import team.gif.lib.autoMode;
import team.gif.lib.delay;
import team.gif.lib.logging.FileLogger;
import team.gif.robot.commands.climber.ClimberManualControl;
import team.gif.robot.commands.drivetrain.DriveTank;
import team.gif.robot.subsystems.Climber;
import team.gif.robot.subsystems.ClimberPneumatics;
import team.gif.robot.subsystems.CollectorPneumatics;
import team.gif.robot.subsystems.Hood;
import team.gif.robot.subsystems.drivers.Limelight;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import team.gif.robot.commands.indexer.IndexScheduler;
import team.gif.robot.commands.shooter.ShooterIdle;
import team.gif.robot.subsystems.Indexer;
import team.gif.robot.subsystems.Collector;
import team.gif.robot.commands.drivetrain.DriveArcade;
import team.gif.robot.subsystems.Drivetrain;
import team.gif.robot.subsystems.Shooter;

import java.util.Map;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    public static final boolean isCompBot = true;

    private Command autonomousCommand;
    private RobotContainer robotContainer;
    public static Limelight limelight = null;
    public static Drivetrain drivetrain = null;
    private boolean runAutoScheduler = true;
    public static OI oi;
    public static UI ui;
    public static FileLogger logger;

    private autoMode chosenAuto;
    private delay chosenDelay;
    private final Timer elapsedTime = new Timer();

    private static PowerDistribution pdp = null;
    public static Hood hood = null;
    public static CollectorPneumatics collectorPneumatics = null;
    public static ClimberPneumatics climberPneumatics = null;
    public static Collector collector = null;
    public static Indexer indexer = null;
    public static Command indexCommand = null;
    public static Shooter shooter = null;
    public static Climber climber = null;
    public static Compressor compressor = null;

    public static DriveArcade arcadeDrive;
//    public static DriveTank tankDrive;

    // Creating a new tab in shuffleboard.
    public static ShuffleboardTab shuffleboardTab = Shuffleboard.getTab("2022");
    public static ShuffleboardLayout shuffleboardLayoutSensor = shuffleboardTab
            .getLayout("Sensors", BuiltInLayouts.kGrid)
            .withSize(1,3) // make the widget 2x1
            .withProperties(Map.of("Label","HIDDEN")); //place it in the top-left cornor

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        limelight = new Limelight();

        pdp = new PowerDistribution();
        drivetrain = new Drivetrain();
        compressor = new Compressor(RobotMap.COMPRESSOR, PneumaticsModuleType.CTREPCM);
        collector = new Collector();
        climber = new Climber();
        indexer = new Indexer();
        indexCommand = new IndexScheduler();
        shooter = new Shooter();
        hood = new Hood();
        collectorPneumatics = new CollectorPneumatics();
        climberPneumatics = new ClimberPneumatics();

//        tankDrive = new DriveTank();
        arcadeDrive = new DriveArcade();

        indexer.setDefaultCommand(indexCommand); //indexer.setDefaultCommand(new IndexScheduler());
        shooter.setDefaultCommand(new ShooterIdle());
        drivetrain.setDefaultCommand(arcadeDrive);
        climber.setDefaultCommand(new ClimberManualControl());

        limelight.setLEDMode(1);//force off

        oi = new OI();
        ui = new UI();
        logger = new FileLogger();
        addMetricsToLogger();
        logger.init();

        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        logger.addEvent("INIT", "Start building container");
        robotContainer = new RobotContainer();
        logger.addEvent("INIT", "End building container");
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

        chosenAuto = ui.autoModeChooser.getSelected();
        chosenDelay = ui.delayChooser.getSelected();
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     */
    @Override
    public void disabledInit() {
        limelight.setLEDMode(1); // Force off
    }

    @Override
    public void disabledPeriodic() {}

    /**
     * This runs the autonomous command selected by your {@link RobotContainer} class.
     */
    @Override
    public void autonomousInit() {
        logger.addEvent("AUTO", "Auto Init");
        drivetrain.resetPigeon();
        drivetrain.resetEncoders();
        drivetrain.resetPose();
        logger.addEvent("AUTO", "Reset sensors");

        autonomousCommand = robotContainer.getAutonomousCommand(chosenAuto);
        logger.addEvent("AUTO", "Got command from container");

        Globals.autonomousModeActive = true;
        indexCommand.schedule();
        
        // used for delaying the start of autonomous
        elapsedTime.reset();
        elapsedTime.start();

        limelight.setLEDMode(1);//turn off

        compressor.disable();

        runAutoScheduler = true;
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        if ( runAutoScheduler && (elapsedTime.get() > (chosenDelay.getValue()))) {
            if (autonomousCommand != null) {
                System.out.println("Delay over. Auto selection scheduler started.");
                autonomousCommand.schedule();
                logger.addEvent("AUTO", "Scheduled auto command");
            }
            runAutoScheduler = false;
            elapsedTime.stop();
        }
    }

    @Override
    public void teleopInit() {

        Globals.autonomousModeActive = false;
        limelight.setLEDMode(3);//force off
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
        compressor.enableDigital();
        indexCommand.schedule();
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        double timeLeft = DriverStation.getMatchTime();
        oi.setRumble((timeLeft <= 40.0 && timeLeft >= 36.0) ||
                     (timeLeft <=  5.0 && timeLeft >=  3.0));

        if ( indexer.getCargoCount() == 2 ){
            collectorPneumatics.entryRaise();
            collectorPneumatics.collectorRaise();
        }

        logger.run();
    }

    @Override
    public void testInit() {}

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {}
    
    public void addMetricsToLogger() {
        // Shooter
        logger.addMetric("Shooter_Velocity", shooter::getSpeed);
        logger.addMetric("Shooter_Acceleration", shooter::getAcceleration);
        logger.addMetric("Shooter_Percent", shooter::getOutputPercent);
    
        // Drivetrain
        logger.addMetric("DriveLeft1_Voltage_In", drivetrain::getInputVoltageL1);
        logger.addMetric("DriveLeft2_Voltage_In", drivetrain::getInputVoltageL2);
        logger.addMetric("DriveRight1_Voltage_In", drivetrain::getInputVoltageR1);
        logger.addMetric("DriveRight2_Voltage_In", drivetrain::getInputVoltageR2);
    
        logger.addMetric("DriveLeft1_Current_In", drivetrain::getInputCurrentL1);
        logger.addMetric("DriveLeft2_Current_In", drivetrain::getInputCurrentL2);
        logger.addMetric("DriveRight1_Current_In", drivetrain::getInputCurrentR1);
        logger.addMetric("DriveRight2_Current_In", drivetrain::getInputCurrentR2);
    
        logger.addMetric("DriveLeft1_Percent", drivetrain::getOutputPercentL1);
        logger.addMetric("DriveLeft2_Percent", drivetrain::getOutputPercentL2);
        logger.addMetric("DriveRight1_Percent", drivetrain::getOutputPercentR1);
        logger.addMetric("DriveRight2_Percent", drivetrain::getOutputPercentR2);
    
        logger.addMetric("DriveLeft1_Velocity", drivetrain::getLeftEncoderVelocity_Ticks);
        logger.addMetric("DriveRight1_Velocity", drivetrain::getRightEncoderVelocity_Ticks);
    
        // PDP and Compressor
        logger.addMetric("PDP_Voltage", pdp::getVoltage);
        logger.addMetric("PDP_Total_Current", pdp::getTotalCurrent);
        logger.addMetric("Compressor_State", () -> compressor.enabled() ? 1 : 0);
    
        // Climber
        logger.addMetric("Climber_Voltage_In", climber::getInputVoltage);
        logger.addMetric("Climber_Voltage_Out", climber::getOutputVoltage);
        logger.addMetric("Climber_Current_In", climber::getInputCurrent);
        logger.addMetric("Climber_Current_Out", climber::getOutputCurrent);
        logger.addMetric("Climber_Percent", climber::getOutputPercent);
        logger.addMetric("Climber_Velocity", climber::getVelocity);
    }
}
