package team.gif.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

public class ClimberManualControl extends CommandBase {

    public ClimberManualControl() {
        addRequirements(Robot.climber);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double speed = -Robot.oi.aux.getLeftY();

        if (speed > -0.05 && speed < 0.05) {
            speed = 0;
        }

        if (Globals.climberMotorEnabled) {
            // Run the elevator either up or down
            Robot.climber.setSpeed(speed);
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.climber.setSpeed(0);
    }
}
