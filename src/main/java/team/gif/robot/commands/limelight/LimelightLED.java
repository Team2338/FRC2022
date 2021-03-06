package team.gif.robot.commands.limelight;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.drivers.Limelight;

public class LimelightLED extends CommandBase {

    public LimelightLED() {
        super();
    }

    /*
     * When used with toggleWhenPressed, the command runs
     * alternately between initialize() and end().
     */

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Robot.shooterLimelight.setLEDMode(Limelight.LED_ON); // Turn on LED
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.shooterLimelight.setLEDMode(Limelight.LED_OFF); // Turn off LED
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
