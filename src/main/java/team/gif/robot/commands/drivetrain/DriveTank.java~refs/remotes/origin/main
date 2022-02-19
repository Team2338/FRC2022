package team.gif.robot.commands.drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

public class DriveTank extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    public DriveTank() {
        super();
        addRequirements(Robot.drivetrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        /*
         * True Tank Drive
         */
        double currL = -Robot.oi.driver.getLeftY(); //assuming negative because motors have .setInverted(false);
        double currR = -Robot.oi.driver.getRightY();
        if(Robot.isCompBot) {
            Robot.drivetrain.setSpeed(currL, -currR);
        } else {
            Robot.drivetrain.setSpeed(currL, currR);
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}
}
