package team.gif.robot;

import team.gif.robot.commands.climber.ResetClimber;
import team.gif.robot.commands.drivetrain.DriveArcade;
import team.gif.robot.commands.drivetrain.DriveTank;
import team.gif.robot.commands.drivetrain.ResetHeading;

import static team.gif.robot.Robot.indexer;
import static team.gif.robot.Robot.shooter;

public class UI {
    /*
     * All the shuffleboard entry
     */
    public UI(){
        // Indexer and Indexer Sensors
        Robot.shuffleboardTab.addBoolean("Belt Sensor", indexer::getSensorBelt);
        Robot.shuffleboardTab.addBoolean("Mid Sensor", indexer::getSensorMid);
        Robot.shuffleboardTab.addBoolean("Entry Sensor",indexer::getSensorEntry);
        Robot.shuffleboardTab.add(indexer);
        Robot.shuffleboardTab.addNumber("Belt Velocity", indexer::getBeltMotorSpeed);

        // Indexer logging
        Robot.shuffleboardTab.addBoolean("Belt", indexer::getSensorBelt);

        // Shooter Shuffleboard Entrys
        Robot.shuffleboardTab.addNumber("RPM", shooter::getSpeed);
        Robot.shuffleboardTab.addNumber("Shooter Speed", shooter::getSpeed);
        Robot.shuffleboardTab.addNumber("Shooter Acceleration", shooter::getAcceleration);

        // Reset heading
        Robot.shuffleboardTab.add("ResetHead", new ResetHeading());

        // Reset Climber
        Robot.shuffleboardTab.add("Climber", new ResetClimber());

        // Switching drives mode
        Robot.shuffleboardTab.add("Tank Drive", new DriveTank());
        Robot.shuffleboardTab.add("Arcade Drive", new DriveArcade());

        Robot.shuffleboardTab.addBoolean("Enable Indexer", () -> Globals.indexerEnabled);

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
