package team.gif.robot.commands.limelight;

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
    private final double xTolerance = 1.5;

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Globals.indexerEnabled = false;
        Robot.collectorLimelight.setLEDMode(3); // turn on - just in case they were turned off somehow
        Robot.collectorLimelight.setCamMode(0);
        Robot.collectorLimelight.setPipeline(Globals.collectorLimelightBallMode ? 0 : 1);
    }
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Checks if the limelight can see a target
        if (Robot.collectorLimelight.noTarget()) {return;}

        double xOffset = Robot.collectorLimelight.getXOffset(); // Sets x offset
        double yOffset = Robot.collectorLimelight.getYOffset(); // Sets y offset

        // More Accurate Than Shalin
        if (abs(xOffset) > xTolerance) {
            double pivotVolts = (xOffset < 0 ? 1 : -1.0) * (Constants.Shooter.MIN_PIVOT_VOLTS_BALL + abs(xOffset) * 0.01 * Constants.Shooter.MAX_PIVOT_VOLTS_BALL);
            Robot.drivetrain.tankDriveVolts(Robot.drivetrain.getInputVoltageL1() - pivotVolts,Robot.drivetrain.getInputVoltageL1() + pivotVolts);
            return;
        }
        // Reverses into ball and when no see ball algorithm stops
        double reverseVolts = ((yOffset + Constants.Shooter.LIMELIGHT_LOW_BOUND_ANGLE_BALLS) * 0.01 * Constants.Shooter.MAX_REVERSE_VOLTS) + Constants.Shooter.MIN_REVERSE_VOLTS;
        Robot.drivetrain.tankDriveVolts(-reverseVolts, -reverseVolts); // slows robot down until collects ball
    }
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (Globals.autonomousModeActive && (Robot.collectorLimelight.noTarget())) {
        return true;
    }
        return false;}
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.drivetrain.tankDriveVolts(0,0);
        Robot.collectorLimelight.setLEDMode(3); // Leave LED on after ball detected
        Globals.indexerEnabled = true;
    }
}
