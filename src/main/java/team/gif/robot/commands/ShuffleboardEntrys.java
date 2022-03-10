package team.gif.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;
import team.gif.robot.Robot;
import team.gif.robot.commands.climber.ResetClimber;
import team.gif.robot.commands.drivetrain.DriveArcade;
import team.gif.robot.commands.drivetrain.DriveTank;
import team.gif.robot.commands.drivetrain.ResetHeading;

import static team.gif.robot.Robot.indexer;
import static team.gif.robot.Robot.shooter;

public class ShuffleboardEntrys extends CommandBase {
    public ShuffleboardEntrys(){}

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
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
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}
}
