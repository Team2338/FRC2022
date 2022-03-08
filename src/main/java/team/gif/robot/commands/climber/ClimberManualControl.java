package team.gif.robot.commands.climber;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

public class ClimberManualControl extends CommandBase {

    //@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    public static final double ClimberupGain = 1;
    public static final double ClimberdownGain = 1;

    public ClimberManualControl() {
        addRequirements(Robot.climber);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // TODO: can't call multiple times or we crash
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double speed = -Robot.oi.aux.getLeftY();

        if ( speed > -0.05 && speed < 0.05) {
            speed = 0;
        }

        if (Globals.climberMotorEnabled) {

            // run the elevator either up or down
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
        // TODO: can't call multiple times or we crash
//        Robot.shuffleboardTab.add("Hang Control", false);
    }
}
