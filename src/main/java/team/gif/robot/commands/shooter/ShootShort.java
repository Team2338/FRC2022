package team.gif.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

import static team.gif.robot.Globals.shooterIsInTolerance;

public class ShootShort extends CommandBase {
    public ShootShort() {
        super();

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.shooter);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if ( ( Robot.shooter.isInToleranceLow())
                && (Robot.indexer.getSensorBelt())) {

//            System.out.println("Firing speed " + Robot.shooter.getVelocity());
            Robot.indexer.setBeltMotorSpeedPercent(1.0);
            // The indexer will move the remaining power cells forward. No need to move them here.
        } else {
            Robot.indexer.setBeltMotorSpeedPercent(0);
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
        Robot.shooter.setSpeedPercent(0);
        shooterIsInTolerance = false;
    }
}
