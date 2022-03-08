package team.gif.robot.commands.hanger;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

public class ControlPanelDown extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    private static boolean finished = false;

    public ControlPanelDown() {
        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Robot.hanger.setOpen();
        finished = false;
        Globals.controlPanelMotorEnabled = true;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (Robot.hanger.getPosition() > Constants.Hanger.MIN_POS) {
            Robot.hanger.setSpeed(-0.6);
        } else {
            finished = true;
        }
    }


    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return finished;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.hanger.setSpeed(0);
        Globals.controlPanelMotorEnabled = false;
    }
}
