package team.gif.robot.commands.balldetection;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

import static java.lang.Math.abs;

public class LimelightBallDetection extends CommandBase {
    public LimelightBallDetection(){
        super();
        addRequirements();
    }
    private boolean robotHasSettled = false;
    private double xOffset;
    private double yOffset;
    private double xTolerance;

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
        if (robotHasSettled||!(abs(Robot.drivetrain.getWheelSpeeds().leftMetersPerSecond) < Constants.Shooter.velocityCap && abs(Robot.drivetrain.getWheelSpeeds().rightMetersPerSecond) < Constants.Shooter.velocityCap)) {return;}
        robotHasSettled = true; // Allows pivot to work
        xOffset = Robot.limelight.getXOffset(); // Sets x offset
        yOffset = Robot.limelight.getYOffset();
        if (abs(xOffset) > xTolerance) { // More Accurate Than Aaron 2.0
            double pivotVolts = (xOffset < 0 ? 1 : -1.0) * (Constants.Shooter.MIN_FRICTION_VOLTS + abs(xOffset) * 0.01 * Constants.Shooter.MAX_PIVOT_VOLTS);
            Robot.drivetrain.tankDriveVolts(-pivotVolts, pivotVolts);
            return;
        }
        Robot.drivetrain.tankDriveVolts(0, 0);// Set speed to 0
        if (abs(xOffset) > xTolerance) {return;} //checks again
        // Reverses into ball and when no see ball algorithm stops
        double reverseVolts = ((yOffset + 45) * 0.01 * Constants.Shooter.MAX_REVERSE_VOLTS) + Constants.Shooter.MIN_FRICTION_VOLTS;
        Robot.drivetrain.tankDriveVolts(-reverseVolts, -reverseVolts); // slows robot down until collects ball
    }
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {return true;}
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.limelight.setLEDMode(3); // Leave LED on after autoaim so we can still use during manual
        Globals.indexerEnabled = true;
        robotHasSettled = false;
    }
}
