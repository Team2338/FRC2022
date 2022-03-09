// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import team.gif.lib.autoMode;
import team.gif.lib.delay;
import team.gif.lib.logging.FileLogger;
import team.gif.robot.commands.autos.Mobility;
import team.gif.robot.commands.autos.ThreeBallTerminalMiddle;
import team.gif.robot.commands.autos.TwoBallLeft;
import team.gif.robot.commands.autos.TwoBallRight;
import team.gif.robot.commands.climber.ResetClimber;
import team.gif.robot.commands.drivetrain.DriveTank;
import team.gif.robot.commands.drivetrain.ResetHeading;
import team.gif.robot.commands.exampleShuffleboardEntryCommand;
import team.gif.robot.commands.shooter.setShooterRpmCommand;
import team.gif.robot.subsystems.Climber;
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

import java.io.IOException;

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
    private static FileLogger logger;

    private final SendableChooser<autoMode> autoModeChooser = new SendableChooser<>();
    private final SendableChooser<delay> delayChooser = new SendableChooser<>();

    private autoMode chosenAuto;
    private delay chosenDelay;
    private final Timer elapsedTime = new Timer();

    public static Hood hood = null;
    public static CollectorPneumatics collectorPneumatics = null;
    public static Collector collector = null;
    public static Indexer indexer = null;
    public static Command indexCommand = null;
    public static Shooter shooter = null;
    public static Climber climber = null;
    public static Compressor compressor = null;
    public static NetworkTableEntry exampleShuffleboardEntry;
    public static ShuffleboardTab autoTab = Shuffleboard.getTab("PreMatch");
//    public static Pigeon myPigeon;

    public static DriveArcade arcadeDrive;
    public static DriveTank tankDrive;

    public static ShuffleboardTab shuffleboardTab = Shuffleboard.getTab("FRC2022 test"); // Create a new tab in shuffleboard.
    public exampleShuffleboardEntryCommand exampleShuffleboardEntryCommand;
    // TS: the value of the something what is changing,(Example PID control).
    public static double exampleShuffleboardEntrySyncValue;
    // TS: the value is getting the getEntry number
    public static double exampleShuffleboardValue  = exampleShuffleboardEntrySyncValue;

    // ts: variables to getEntry RPM
    public static NetworkTableEntry shooterRpmGetEntry;
    public static double shooterRpm;
    public static double shooterRpmSync;
    public setShooterRpmCommand shooterRpmCommand;

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        tabsetup();
        robotContainer = new RobotContainer();
        limelight = new Limelight();

        drivetrain = new Drivetrain();

        compressor = new Compressor(RobotMap.COMPRESSOR, PneumaticsModuleType.CTREPCM);
        climber = new Climber();
        collector = new Collector();
        indexer = new Indexer();
        indexCommand = new IndexScheduler();
        shooter = new Shooter();
        hood = new Hood();
        collectorPneumatics = new CollectorPneumatics();
        tankDrive = new DriveTank();
        arcadeDrive = new DriveArcade();

        indexer.setDefaultCommand(indexCommand); //indexer.setDefaultCommand(new IndexScheduler());
        shooter.setDefaultCommand(new ShooterIdle());
//        collectorPneumatics.setDefaultCommand(new CollectorUp());
//        hood.setDefaultCommand(new HoodDown());
        drivetrain.setDefaultCommand(arcadeDrive);

        // TS: getting the submit button when you click the commend.
        exampleShuffleboardEntryCommand = new exampleShuffleboardEntryCommand();

//        myPigeon = new Pigeon(Drivetrain.rightTalon2);
        shooterRpm = shooter.getSpeed();
        shooterRpmSync = shooterRpm;
        shooterRpmGetEntry = shuffleboardTab.add("Target RPM",shooterRpm).getEntry();
        shooterRpmCommand = new setShooterRpmCommand();

//        shuffleboardTab.add("BotHeating",(x)->{x.setSmartDashboardType("Gyro");x.addDoubleProperty("value",()->myPigeon.getCompassHeading(),null);});

        // TS: add an getEntry tab in shuffleboard
        exampleShuffleboardEntry = shuffleboardTab.add("Example Input",exampleShuffleboardValue )
                .getEntry();
        // TS: add the example input submit button to the shuffleboard.
        //shuffleboardTab.add("Command", exampleShuffleboardEntryCommand); // TODO: Cleanup the exampleShuffleboardEntry
        //exampleShuffleboardEntry.setDouble(exampleShuffleboardEntrySyncValue);

        // Indexer logging
        shuffleboardTab.addBoolean("Enable Indexer", () -> Globals.indexerEnabled);
        shuffleboardTab.addBoolean("Belt Sensor", indexer::getSensorBelt);
        shuffleboardTab.addBoolean("Mid Sensor", indexer::getSensorMid);
        shuffleboardTab.addBoolean("Entry Sensor",indexer::getSensorEntry);
        shuffleboardTab.add(indexer);
        shuffleboardTab.addNumber("Belt Velocity", indexer::getBeltMotorSpeed);
        shuffleboardTab.add("Climber", new ResetClimber());
        shuffleboardTab.add("ResetHead", new ResetHeading());

        // Shooter
        shuffleboardTab.addNumber("Shooter Speed", shooter::getSpeed);
        shuffleboardTab.addNumber("Shooter Acceleration", shooter::getAcceleration);

        //ts: switching drives mode
        shuffleboardTab.add("Tank Drive", new DriveTank());
        shuffleboardTab.add("Arcade Drive", new DriveArcade());

        limelight.setLEDMode(1); // Force off
        oi = new OI();
        
        try {
            logger = new FileLogger();
            
            // Shooter
            logger.addMetric("shooter_vel", shooter::getSpeed);
            logger.addMetric("shooter_acc", shooter::getAcceleration);
            logger.addMetric("shooter_percent", shooter::getOutputPercent);
            
            // Left 1 drive motor
            logger.addMetric("DL1_vin", drivetrain::getInputVoltageL1);
            logger.addMetric("DL1_vout", drivetrain::getOutputVoltageL1);
            logger.addMetric("DL1_cin", drivetrain::getInputCurrentL1);
            logger.addMetric("DL1_cout", drivetrain::getOutputCurrentL1);
            logger.addMetric("DL1_pout", drivetrain::getOutputPercentL1);
            logger.addMetric("DL1_vel", drivetrain::getLeftEncoderVelocity_Ticks);
            
            // Left 2 drive motor
            logger.addMetric("DL2_vin", drivetrain::getInputVoltageL2);
            logger.addMetric("DL2_vout", drivetrain::getOutputVoltageL2);
            logger.addMetric("DL2_cin", drivetrain::getInputCurrentL2);
            logger.addMetric("DL2_cout", drivetrain::getOutputCurrentL2);
            logger.addMetric("DL2_pout", drivetrain::getOutputPercentL2);
            
            // Right 1 drive motor
            logger.addMetric("DR1_vin", drivetrain::getInputVoltageR1);
            logger.addMetric("DR1_vout", drivetrain::getOutputVoltageR1);
            logger.addMetric("DR1_cin", drivetrain::getInputCurrentR1);
            logger.addMetric("DR1_cout", drivetrain::getOutputCurrentR1);
            logger.addMetric("DR1_pout", drivetrain::getOutputPercentR1);
            logger.addMetric("DR1_vel", drivetrain::getRightEncoderVelocity_Ticks);
            
            // Right 2 drive motor
            logger.addMetric("DR2_vin", drivetrain::getInputVoltageR2);
            logger.addMetric("DR2_vout", drivetrain::getOutputVoltageR2);
            logger.addMetric("DR2_cin", drivetrain::getInputCurrentR2);
            logger.addMetric("DR2_cout", drivetrain::getOutputCurrentR2);
            logger.addMetric("DR2_pout", drivetrain::getOutputPercentR2);
            
            logger.init();
        } catch (IOException e) {
            System.err.println("Failed to initialize logger");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
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
        chosenAuto = autoModeChooser.getSelected();
        chosenDelay = delayChooser.getSelected();
        
        if (logger != null) {
            try {
                logger.run();
            } catch (IOException e) {
                System.err.println("Failed to run logger");
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
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
        autonomousCommand = robotContainer.getAutonomousCommand();

        // schedule the autonomous command (example)
        if (autonomousCommand != null) {
            autonomousCommand.schedule();
        }

        Globals.autonomousModeActive = true;
        indexCommand.schedule();
        
        // used for delaying the start of autonomous
        elapsedTime.reset();
        elapsedTime.start();

        drivetrain.resetEncoders();
        drivetrain.resetPose();

        limelight.setLEDMode(1); // Turn off

        compressor.disable();

        runAutoScheduler = true;
        updateAuto();
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
            }
            runAutoScheduler = false;
            elapsedTime.stop();
        }
    }

    @Override
    public void teleopInit() {

        Globals.autonomousModeActive = false;
        limelight.setLEDMode(1);//force off
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
//        oi = new OI();
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
    }

    @Override
    public void testInit() {}

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {}

    public void tabsetup(){

        autoTab = Shuffleboard.getTab("PreMatch");

        autoModeChooser.addOption("Mobility", autoMode.MOBILITY);
        autoModeChooser.addOption("Two Ball Left", autoMode.TWO_BALL_LEFT);
        autoModeChooser.addOption("Two Ball Right", autoMode.TWO_BALL_RIGHT);
        autoModeChooser.addOption("Three Ball Terminal Middle", autoMode.THREE_BALL_TERMINAL_MIDDLE);
//        autoModeChooser.addOption("Opp 5 Ball Auto", autoMode.OPP_5_BALL);
//        autoModeChooser.addOption("8 Ball Auto", autoMode.SAFE_8_BALL);
////    autoModeChooser.addOption("Barrel Racing", autoMode.BARREL_RACING);
////    autoModeChooser.addOption("Slalom", autoMode.SLALOM);
////    autoModeChooser.addOption("Bounce", autoMode.BOUNCE);
//        autoModeChooser.setDefaultOption("6 Ball Auto", autoMode.SAFE_6_BALL);

        autoTab.add("Auto Select",autoModeChooser)
                .withWidget(BuiltInWidgets.kComboBoxChooser)
                .withPosition(1,0)
                .withSize(2,1);

        delayChooser.setDefaultOption("0", delay.DELAY_0);
        delayChooser.addOption("1", delay.DELAY_1);
        delayChooser.addOption("2", delay.DELAY_2);
        delayChooser.addOption("3", delay.DELAY_3);
        delayChooser.addOption("4", delay.DELAY_4);
        delayChooser.addOption("5", delay.DELAY_5);
        delayChooser.addOption("6", delay.DELAY_6);
        delayChooser.addOption("7", delay.DELAY_7);
        delayChooser.addOption("8", delay.DELAY_8);
        delayChooser.addOption("9", delay.DELAY_9);
        delayChooser.addOption("10", delay.DELAY_10);
        delayChooser.addOption("11", delay.DELAY_11);
        delayChooser.addOption("12", delay.DELAY_12);
        delayChooser.addOption("13", delay.DELAY_13);
        delayChooser.addOption("14", delay.DELAY_14);
        delayChooser.addOption("15", delay.DELAY_15);

        autoTab.add("Delay", delayChooser)
                .withPosition(0,0)
                .withSize(1,1);

        // calibration information
        // RGB_Shuffleboard
//    calibrationTab = Shuffleboard.getTab("Calibration");          // adds the calibration tab to the shuffleboard (getTab creates if not exist)
//    Shuffleboard.getTab("Calibration").add("Red",0);    // adds the Red text box, persists over power down
//    Shuffleboard.getTab("Calibration").add("Green",0);  // adds the Green text box, persists over power down
//    Shuffleboard.getTab("Calibration").add("Blue",0);   // adds the Blue text box, persists over power down
    }

    public void updateAuto() {

        if(chosenAuto == autoMode.MOBILITY){
            autonomousCommand = new Mobility();
        } else if(chosenAuto == autoMode.TWO_BALL_LEFT){
            autonomousCommand = new TwoBallLeft();
        } else if(chosenAuto == autoMode.TWO_BALL_RIGHT){
            autonomousCommand = new TwoBallRight();
        } else if(chosenAuto == autoMode.THREE_BALL_TERMINAL_MIDDLE){
            autonomousCommand = new ThreeBallTerminalMiddle();
        } else if(chosenAuto ==null) {
            System.out.println("Autonomous selection is null. Robot will do nothing in auto :(");
        }
    }
}
