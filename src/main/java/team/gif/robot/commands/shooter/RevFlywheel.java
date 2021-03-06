package team.gif.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.drivers.Limelight;

public class RevFlywheel extends CommandBase {
    private final double setpoint;

    public RevFlywheel(double setpoint) {
        addRequirements(Robot.shooter);
        this.setpoint = setpoint;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Robot.shooterLimelight.setLEDMode(Limelight.LED_ON);
        Robot.shooter.setSpeedPID(setpoint);
        Globals.shooterRunning = true;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        Robot.shooterLimelight.setLEDMode(Limelight.LED_ON);
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
        Robot.shooterLimelight.setLEDMode(Limelight.LED_ON);//force off

        // This is used to inform RapidFire to stop running the belt.
        // Otherwise, RapidFire will continue to run the belt after the
        // shooter has stopped and could cause a possible jam
        Globals.shooterRunning = false;
    }
}
