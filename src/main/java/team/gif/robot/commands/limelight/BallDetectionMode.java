package team.gif.robot.commands.limelight;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

public class BallDetectionMode extends CommandBase {
    public BallDetectionMode() {
        super();
    }
    /*
     * When used with toggleWhenPressed, the command runs
     * alternately between initialize() and end().
     */

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // Turn on LED
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        Globals.collectorLimelightBallMode = !Globals.collectorLimelightBallMode;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.shooterLimelight.setLEDMode(1); // Turn off LED
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}

