package team.gif.robot.commands.limelight;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;

public class LimelightToggle extends CommandBase {

    public LimelightToggle() {
        super();
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Globals.limelightEnabledAutoAim = !Globals.limelightEnabledAutoAim;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return true;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
