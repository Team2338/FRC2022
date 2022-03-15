package team.gif.robot;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team.gif.lib.autoMode;
import team.gif.lib.delay;
import team.gif.robot.commands.climber.ResetClimber;
import team.gif.robot.commands.drivetrain.ResetHeading;
import team.gif.robot.commands.limelight.LimelightLED;
import team.gif.robot.subsystems.drivers.Pigeon;

import static team.gif.robot.Robot.climber;
import static team.gif.robot.Robot.indexer;
import static team.gif.robot.Robot.shooter;

public class UiSmartDashboard {

    public static SendableChooser<autoMode> autoModeChooser = new SendableChooser<>();
    public static SendableChooser<delay> delayChooser = new SendableChooser<>();

    /*
     * All the shuffleboard entry
     */
    public UiSmartDashboard(){
        // Indexer and Indexer Sensors
        SmartDashboard.putBoolean("Belt Sensor", indexer.getSensorBelt());
        SmartDashboard.putBoolean("Mid Sensor", indexer.getSensorMid());
        SmartDashboard.putBoolean("Entry Sensor",indexer.getSensorEntry());
        SmartDashboard.putBoolean("Enable Indexer", Globals.indexerEnabled);

        // Shooter
        SmartDashboard.putNumber("Shooter Speed", shooter.getSpeed());

        // Climber
        SmartDashboard.putString("Climber Position", climber.getPosition_Shuffleboard());
        SmartDashboard.putData("Climber", new ResetClimber());

        // Limelight Toggle
        SmartDashboard.putData("LED", new LimelightLED());

//        Robot.shuffleboardTab.addCamera("limelight","limelight","mjpg:http://10.23.38.95:5800")

        // Pigeon
        ShuffleboardTab tab  = Shuffleboard.getTab("SmartDashboard"); //gets a reference to the shuffleboard tab
        tab.add("BotHead",(x)->{x.setSmartDashboardType("Gyro");x.addDoubleProperty("Value", ()-> Pigeon.getInstance().getCompassHeading(),null);});
        SmartDashboard.putData("Reset", new ResetHeading());

        // Auto selections
        autoModeChooser.addOption("Mobility", autoMode.MOBILITY);
        autoModeChooser.setDefaultOption("Two Ball Left", autoMode.TWO_BALL_LEFT);
        autoModeChooser.addOption("Two Ball Middle", autoMode.TWO_BALL_MIDDLE);
        autoModeChooser.addOption("Two Ball Right", autoMode.TWO_BALL_RIGHT);
        autoModeChooser.addOption("Three+ Ball Terminal Middle", autoMode.THREE_BALL_TERMINAL_MIDDLE);
        autoModeChooser.addOption("Three+ Ball Terminal Right", autoMode.THREE_BALL_TERMINAL_RIGHT);
        autoModeChooser.addOption("Four Ball Terminal Right", autoMode.FOUR_BALL_TERMINAL_RIGHT);
        autoModeChooser.addOption("Four+ Ball Terminal Right", autoMode.FIVE_BALL_TERMINAL_RIGHT);

        tab.add("Auto Select",autoModeChooser)
            .withWidget(BuiltInWidgets.kComboBoxChooser)
            .withPosition(8,2)
            .withSize(2,1);

        //Auto delay
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

        tab.add("Delay", delayChooser)
            .withPosition(7,2)
            .withSize(1,1);
    }
}
