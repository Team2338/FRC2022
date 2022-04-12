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
    private final double xTolerance = 1.5;

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Globals.indexerEnabled = false;
        Robot.limelightballs.setLEDMode(3); // turn on - just in case they were turned off somehow
        Robot.limelightballs.setCamMode(0);
        Robot.limelightballs.setPipeline(Globals.limelightballmode? 0 : 1);
    }
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Checks if the limelight can see a target
        if (Robot.limelightballs.noTarget()) {return;}

        // Make sure both wheels are within tolerance of not moving
        if (!robotHasSettled||!(abs(Robot.drivetrain.getWheelSpeeds().leftMetersPerSecond) < Constants.Shooter.velocityCap && abs(Robot.drivetrain.getWheelSpeeds().rightMetersPerSecond) < Constants.Shooter.velocityCap)) {return;}

        robotHasSettled = true; // Allows pivot to work

        double xOffset = Robot.limelightballs.getXOffset(); // Sets x offset
        double yOffset = Robot.limelightballs.getYOffset(); // Sets y offset

        // More Accurate Than Shalin
        if (abs(xOffset) > xTolerance) {
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
    public boolean isFinished() {
        if (Globals.autonomousModeActive && (Robot.limelightballs.noTarget())) {
        return true;
    }
        return false;}
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.drivetrain.tankDriveVolts(0,0);
        Robot.limelightballs.setLEDMode(3); // Leave LED on after ball detected
        Globals.indexerEnabled = true;
        robotHasSettled = false;
    }
}
