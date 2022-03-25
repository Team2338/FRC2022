// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import team.gif.lib.autoMode;
import team.gif.lib.delay;
import team.gif.lib.logging.EventFileLogger;
import team.gif.lib.logging.TelemetryFileLogger;
import team.gif.robot.commands.climber.ClimberManualControl;
import team.gif.robot.subsystems.Climber;
import team.gif.robot.subsystems.ClimberPneumatics;
import team.gif.robot.subsystems.CollectorPneumatics;
import team.gif.robot.subsystems.Hood;
import team.gif.robot.subsystems.drivers.Limelight;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import team.gif.robot.commands.indexer.IndexScheduler;
import team.gif.robot.commands.shooter.ShooterIdle;
import team.gif.robot.subsystems.Indexer;
import team.gif.robot.subsystems.Collector;
import team.gif.robot.commands.drivetrain.DriveArcade;
import team.gif.robot.subsystems.Drivetrain;
import team.gif.robot.subsystems.Shooter;

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
    public static UiSmartDashboard uiSmartDashboard;
    public static EventFileLogger eventLogger;
    private static TelemetryFileLogger telemetryLogger;

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
    public static AnalogInput pressureSensor = null;

    public static DriveArcade arcadeDrive;
//    public static DriveTank tankDrive;


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
        pressureSensor = new AnalogInput(RobotMap.SENSOR_AIR_PRESSURE);

//        tankDrive = new DriveTank();
        arcadeDrive = new DriveArcade();

        indexer.setDefaultCommand(indexCommand); //indexer.setDefaultCommand(new IndexScheduler());
        shooter.setDefaultCommand(new ShooterIdle());
        drivetrain.setDefaultCommand(arcadeDrive);
        climber.setDefaultCommand(new ClimberManualControl());

        limelight.setLEDMode(1);//force off

        oi = new OI();
//        ui = new UI();
        uiSmartDashboard = new UiSmartDashboard();
        
        eventLogger = new EventFileLogger();
        eventLogger.init();
        
        telemetryLogger = new TelemetryFileLogger();
        addMetricsToLogger();
        telemetryLogger.init();

        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        eventLogger.addEvent("INIT", "Start building container");
        robotContainer = new RobotContainer();
        eventLogger.addEvent("INIT", "End building container");

        hood.setHoodDown();
        collectorPneumatics.collectorRaise();
        climberPneumatics.setFangsIn();
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

        uiSmartDashboard.updateUI();

        chosenAuto = uiSmartDashboard.autoModeChooser.getSelected();
        chosenDelay = uiSmartDashboard.delayChooser.getSelected();

    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     */
    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}

    /**
     * This runs the autonomous command selected by your {@link RobotContainer} class.
     */
    @Override
    public void autonomousInit() {
        eventLogger.addEvent("AUTO", "Auto Init");
        drivetrain.resetPigeon();
        drivetrain.resetEncoders();
        drivetrain.resetPose();
        eventLogger.addEvent("AUTO", "Reset sensors");

        autonomousCommand = robotContainer.getAutonomousCommand(chosenAuto);
        eventLogger.addEvent("AUTO", "Got command from container");

        Globals.autonomousModeActive = true;
        indexCommand.schedule();
        
        // used for delaying the start of autonomous
        elapsedTime.reset();
        elapsedTime.start();

        limelight.setLEDMode(1);//turn off during autonomous

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
                eventLogger.addEvent("AUTO", "Scheduled auto command");
            }
            runAutoScheduler = false;
            elapsedTime.stop();
        }
    }

    @Override
    public void teleopInit() {

        limelight.setLEDMode(3); // turn LED on for entire teleop

        Globals.autonomousModeActive = false;
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
        compressor.enableDigital();
        climber.releaseClimberBrake();
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

        telemetryLogger.run();
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
        telemetryLogger.addMetric("Shooter_Velocity", shooter::getSpeed);
        telemetryLogger.addMetric("Shooter_Acceleration", shooter::getAcceleration);
        telemetryLogger.addMetric("Shooter_Percent", shooter::getOutputPercent);
        telemetryLogger.addMetric("Shooter_Voltage_In", shooter::getInputVoltage);
        telemetryLogger.addMetric("Shooter_Voltage_Out", shooter::getOutputVoltage);
        telemetryLogger.addMetric("Shooter_Current_In", shooter::getInputCurrent);
        telemetryLogger.addMetric("Shooter_Current_Out", shooter::getOutputCurrent);
        telemetryLogger.addMetric("Shooter_Gain_P", shooter::getGainP);
        telemetryLogger.addMetric("Shooter_Gain_I", shooter::getGainI);
        telemetryLogger.addMetric("Shooter_Gain_D", shooter::getGainD);
        telemetryLogger.addMetric("Shooter_Gain_F", shooter::getGainF);

        // Drivetrain
        telemetryLogger.addMetric("DriveLeft1_Voltage_In", drivetrain::getInputVoltageL1);
        telemetryLogger.addMetric("DriveLeft2_Voltage_In", drivetrain::getInputVoltageL2);
        telemetryLogger.addMetric("DriveRight1_Voltage_In", drivetrain::getInputVoltageR1);
        telemetryLogger.addMetric("DriveRight2_Voltage_In", drivetrain::getInputVoltageR2);

        telemetryLogger.addMetric("DriveLeft1_Voltage_Out", drivetrain::getOutputVoltageL1);
        telemetryLogger.addMetric("DriveLeft2_Voltage_Out", drivetrain::getOutputVoltageL2);
        telemetryLogger.addMetric("DriveRight1_Voltage_Out", drivetrain::getOutputVoltageR1);
        telemetryLogger.addMetric("DriveRight2_Voltage_Out", drivetrain::getOutputVoltageR2);

        telemetryLogger.addMetric("DriveLeft1_Current_In", drivetrain::getInputCurrentL1);
        telemetryLogger.addMetric("DriveLeft2_Current_In", drivetrain::getInputCurrentL2);
        telemetryLogger.addMetric("DriveRight1_Current_In", drivetrain::getInputCurrentR1);
        telemetryLogger.addMetric("DriveRight2_Current_In", drivetrain::getInputCurrentR2);

        telemetryLogger.addMetric("DriveLeft1_Current_Out", drivetrain::getOutputCurrentL1);
        telemetryLogger.addMetric("DriveLeft2_Current_Out", drivetrain::getOutputCurrentL2);
        telemetryLogger.addMetric("DriveRight1_Current_Out", drivetrain::getOutputCurrentR1);
        telemetryLogger.addMetric("DriveRight2_Current_Out", drivetrain::getOutputCurrentR2);

        telemetryLogger.addMetric("DriveLeft1_Percent", drivetrain::getOutputPercentL1);
        telemetryLogger.addMetric("DriveLeft2_Percent", drivetrain::getOutputPercentL2);
        telemetryLogger.addMetric("DriveRight1_Percent", drivetrain::getOutputPercentR1);
        telemetryLogger.addMetric("DriveRight2_Percent", drivetrain::getOutputPercentR2);
    
        telemetryLogger.addMetric("DriveLeft1_Velocity", drivetrain::getLeftEncoderVelocity_Ticks);
        telemetryLogger.addMetric("DriveRight1_Velocity", drivetrain::getRightEncoderVelocity_Ticks);
    
        // PDP and Compressor
        telemetryLogger.addMetric("PDP_Voltage", pdp::getVoltage);
        telemetryLogger.addMetric("PDP_Current_DriveLeft1", () -> pdp.getCurrent(0));
        telemetryLogger.addMetric("PDP_Total_Current", pdp::getTotalCurrent);
        telemetryLogger.addMetric("Compressor_State", () -> compressor.enabled() ? 1 : 0);
    
        // Climber
        telemetryLogger.addMetric("Climber_Voltage_In", climber::getInputVoltage);
        telemetryLogger.addMetric("Climber_Voltage_Out", climber::getOutputVoltage);
        telemetryLogger.addMetric("Climber_Current_In", climber::getInputCurrent);
        telemetryLogger.addMetric("Climber_Current_Out", climber::getOutputCurrent);
        telemetryLogger.addMetric("Climber_Percent", climber::getOutputPercent);
        telemetryLogger.addMetric("Climber_Velocity", climber::getVelocity);

        // Input
        telemetryLogger.addMetric("Driver_Left_Y", () -> -Robot.oi.driver.getLeftY());
        telemetryLogger.addMetric("Driver_Right_X", () -> Robot.oi.driver.getRightX());
    }
}
