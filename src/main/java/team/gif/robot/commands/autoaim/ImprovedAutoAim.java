package team.gif.robot.commands.autoaim;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

import static java.lang.Math.abs;
public class ImprovedAutoAim extends CommandBase {
    private double xOffset;
    private boolean robotHasSettled = false;
    private final double velocityCap = 0.5;
    private final double xTolerance = 1.5;
    public ImprovedAutoAim() {
        super();
        addRequirements();
    }
    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Globals.indexerEnabled = false;
        Robot.limelight.setLEDMode(3); // turn on - just in case they were turned off somehow
    }
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Checks if the limelight can see a target
        if (!Robot.limelight.hasTarget()) {return;}
        // Make sure both wheels are within tolerance of not moving
        if (robotHasSettled||!(abs(Robot.drivetrain.getWheelSpeeds().leftMetersPerSecond) < velocityCap && abs(Robot.drivetrain.getWheelSpeeds().rightMetersPerSecond) < velocityCap)) {return;}
        robotHasSettled = true; // Allows pivot to work
        xOffset = Robot.limelight.getXOffset(); // Sets x offset
        Robot.hood.setHoodUp();// Hood Up
        if (abs(xOffset) > xTolerance) { // Pivot
            double pivotVolts = (xOffset < 0 ? 1 : -1.0) * (Constants.Shooter.MIN_FRICTION_VOLTS + abs(xOffset) * 0.01 * Constants.Shooter.MAX_PIVOT_VOLTS);
            Robot.drivetrain.tankDriveVolts(pivotVolts, -pivotVolts);
            return;
        }
        Robot.drivetrain.tankDriveVolts(0, 0);// Set speed to 0
        if (abs(xOffset) > xTolerance) {return;} //checks again
        if (Robot.shooter.isInTolerance()) { //Shoot
            Robot.indexer.setBeltMotorSpeedPercent(1.0);
            Robot.indexer.setMidMotorSpeed(1.0);
        }
    }
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {return true;}
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.indexer.setBeltMotorSpeedPercent(0);
        Robot.indexer.setMidMotorSpeed(0);
        Robot.limelight.setLEDMode(3); // Leave LED on after autoaim so we can still use during manual
        Globals.indexerEnabled = true;
        robotHasSettled = false;
    }
}

