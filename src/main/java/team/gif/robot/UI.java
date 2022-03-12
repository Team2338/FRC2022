package team.gif.robot;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import org.w3c.dom.html.HTMLOptionElement;
import team.gif.lib.autoMode;
import team.gif.lib.delay;
import team.gif.robot.commands.climber.ResetClimber;
import team.gif.robot.commands.drivetrain.DriveArcade;
import team.gif.robot.commands.drivetrain.DriveTank;
import team.gif.robot.commands.drivetrain.ResetHeading;
import team.gif.robot.subsystems.Climber;

import java.util.function.Supplier;

import static team.gif.robot.Robot.*;

public class UI {

    public static SendableChooser<autoMode> autoModeChooser = new SendableChooser<>();
    public static SendableChooser<delay> delayChooser = new SendableChooser<>();

    /*
     * All the shuffleboard entry
     */
    public UI(){
        // Indexer and Indexer Sensors
        Robot.shuffleboardLayoutSensor.addBoolean("Belt Sensor", indexer::getSensorBelt);
        Robot.shuffleboardLayoutSensor.addBoolean("Mid Sensor", indexer::getSensorMid);
        Robot.shuffleboardLayoutSensor.addBoolean("Entry Sensor",indexer::getSensorEntry);

        // Shooter Shuffleboard Entrys
        Robot.shuffleboardTab.addNumber("Shooter Speed", shooter::getSpeed);
        // Robot.shuffleboardTab.addNumber("Shooter Acceleration", shooter::getAcceleration);

        // Climber
        Robot.shuffleboardTab.addString("Climber Position", () -> climber.getPosition_Shuffleboard());

        // Reset heading
        Robot.shuffleboardTab.add("ResetHead", new ResetHeading());

        // Reset Climber
        Robot.shuffleboardTab.add("Climber", new ResetClimber());

        Robot.shuffleboardTab.addBoolean("Enable Indexer", () -> Globals.indexerEnabled);

        // pigion heading
        Robot.shuffleboardTab.add("BotHead",(x)->{x.setSmartDashboardType("Gyro");x.addDoubleProperty("Value", ()->Robot.myPigeon.getCompassHeading(),null);});

        // auto
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

        Robot.shuffleboardTab.add("Auto Select",autoModeChooser)
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

        Robot.shuffleboardTab.add("Delay", delayChooser)
                .withPosition(0,0)
                .withSize(1,1);

        // Hanger
        //shuffleboardTab.add("Hang Position", Robot.climber.getPosition_Shuffleboard());

        //shuffleboardTab.add("BotHeating",(x)->{x.setSmartDashboardType("Gyro");x.addDoubleProperty("value",()->myPigeon.getCompassHeading(),null);});

        // calibration information
        // RGB_Shuffleboard
//    calibrationTab = Shuffleboard.getTab("Calibration");          // adds the calibration tab to the shuffleboard (getTab creates if not exist)
//    Shuffleboard.getTab("Calibration").add("Red",0);    // adds the Red text box, persists over power down
//    Shuffleboard.getTab("Calibration").add("Green",0);  // adds the Green text box, persists over power down
//    Shuffleboard.getTab("Calibration").add("Blue",0);   // adds the Blue text box, persists over power down
    }
}
