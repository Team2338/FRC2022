// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import team.gif.lib.autoMode;
import team.gif.lib.delay;
import team.gif.robot.commands.autos.Mobility;
import team.gif.robot.commands.autos.ThreeBallTerminalRight;
import team.gif.robot.commands.autos.TwoBall;
import team.gif.robot.commands.drivetrain.DriveTank;
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

    private SendableChooser<autoMode> autoModeChooser = new SendableChooser<>();
    private SendableChooser<delay> delayChooser = new SendableChooser<>();

    private autoMode chosenAuto;
    private delay chosenDelay;
    private Timer elapsedTime = new Timer();

    public static Hood hood = null;
    public static CollectorPneumatics collectorPneumatics = null;
    public static Collector collector = null;
    public static Indexer indexer = null;
    public static Shooter shooter = null;
    public static Climber climber = null;
    public static Compressor compressor = null;
//    public static Pigeon myPigeon;

    public static DriveArcade arcadeDrive;
    public static DriveTank tankDrive;

    // Creating an new tab in shuffleboard.
    public static ShuffleboardTab autoTab = Shuffleboard.getTab("PreMatch");
    public static ShuffleboardTab shuffleboardTab = Shuffleboard.getTab("2022 Shuffleboard");

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

        compressor = new Compressor(RobotMap.COMPRESSOR_HOOD, PneumaticsModuleType.CTREPCM);
        climber = new Climber();
        collector = new Collector();
        indexer = new Indexer();
        shooter = new Shooter();
        hood = new Hood();
        collectorPneumatics = new CollectorPneumatics();
        tankDrive = new DriveTank();
        arcadeDrive = new DriveArcade();

        indexer.setDefaultCommand(new IndexScheduler());
        shooter.setDefaultCommand(new ShooterIdle());
//        collectorPneumatics.setDefaultCommand(new CollectorUp());
//        hood.setDefaultCommand(new HoodDown());
        drivetrain.setDefaultCommand(arcadeDrive);

//        myPigeon = new Pigeon(Drivetrain.rig
//        htTalon2);

        limelight.setLEDMode(1);//force off

        oi = new OI();
        ui = new UI(); // Calling it like how OI does.
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
        //System.out.println("robot periodic");
        chosenAuto = autoModeChooser.getSelected();
        chosenDelay = delayChooser.getSelected();

//    SmartDashboard.putNumber("tx",limelight.getXOffset());
//    SmartDashboard.putNumber("ty",limelight.getYOffset());
        // pneumatics
//    SmartDashboard.putBoolean("Pressure", compressor.getPressureSwitchValue());
//    SmartDashboard.putBoolean("hastarget",limelight.hasTarget());
    }

    /** This function is called once each time the robot enters Disabled mode. */
    @Override
    public void disabledInit() {
        limelight.setLEDMode(1);//force off
    }

    @Override
    public void disabledPeriodic() {}

    /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
    @Override
    public void autonomousInit() {
        autonomousCommand = robotContainer.getAutonomousCommand();

        // schedule the autonomous command (example)
        if (autonomousCommand != null) {
            autonomousCommand.schedule();
        }

        Globals.autonomousModeActive = true;
        // used for delaying the start of autonomous
        elapsedTime.reset();
        elapsedTime.start();

        drivetrain.resetEncoders();
        drivetrain.resetPose();

        limelight.setLEDMode(1);//turn off

        compressor.disable();

        runAutoScheduler = true;
        updateauto();
    }

    /** This function is called periodically during autonomous. */
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
    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {
        double timeLeft = DriverStation.getMatchTime();
        oi.setRumble((timeLeft <= 40.0 && timeLeft >= 36.0) ||
                     (timeLeft <=  5.0 && timeLeft >=  3.0));
    }

    @Override
    public void testInit() {}

    /** This function is called periodically during test mode. */
    @Override
    public void testPeriodic() {}

    public void tabsetup(){

        autoTab = Shuffleboard.getTab("PreMatch");

        autoModeChooser.addOption("Mobility", autoMode.MOBILITY);
        autoModeChooser.addOption("Two Ball", autoMode.TWO_BALL);
        autoModeChooser.addOption("Three Ball Terminal Right", autoMode.THREE_BALL_TERMINAL_RIGHT);
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
    }

    public void updateauto(){

        if(chosenAuto == autoMode.MOBILITY){
            autonomousCommand = new Mobility();
        }
        else if(chosenAuto == autoMode.TWO_BALL){
            autonomousCommand = new TwoBall();
        }
      else if(chosenAuto == autoMode.THREE_BALL_TERMINAL_RIGHT){
            autonomousCommand = new ThreeBallTerminalRight();}
//         else if(chosenAuto == autoMode.SAFE_6_BALL){
//            m_autonomousCommand = new SafeSixBall();
//        } else if(chosenAuto == autoMode.OPP_5_BALL){
//            m_autonomousCommand = new OppFiveBall();
//        } else if(chosenAuto == autoMode.SAFE_8_BALL){
//            m_autonomousCommand = new SafeEightBall();
/*    } else if (chosenAuto == autoMode.BARREL_RACING){
      m_autonomousCommand = new BarrelRacing();
    } else if(chosenAuto == autoMode.SLALOM) {
      m_autonomousCommand = new Slalom();
    } else if(chosenAuto == autoMode.BOUNCE){
      m_autonomousCommand = new Bounce(); */
         else if(chosenAuto ==null) {
            System.out.println("Autonomous selection is null. Robot will do nothing in auto :(");
        }
    }
}
