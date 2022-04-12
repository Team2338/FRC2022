package team.gif.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

public class RevFlywheel extends CommandBase {
    private final double setpoint;

    public RevFlywheel(double setpoint) {
        addRequirements(Robot.shooter);
        this.setpoint = setpoint;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Robot.limelightaim.setLEDMode(3);
        Robot.shooter.setSpeedPID(setpoint);
        Globals.shooterRunning = true;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        Robot.limelightaim.setLEDMode(3);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        //Robot.shooter.setSpeedPercent(0);
        Robot.limelightaim.setLEDMode(0);//force off

        // This is used to inform RapidFire to stop running the belt.
        // Otherwise, RapidFire will continue to run the belt after the
        // shooter has stopped and could cause a possible jam
        Globals.shooterRunning = false;
    }
}
