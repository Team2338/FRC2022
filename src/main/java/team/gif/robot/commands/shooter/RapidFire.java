package team.gif.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Globals;
import team.gif.robot.Robot;
import team.gif.robot.commands.indexer.ToggleIndexer;
import team.gif.robot.subsystems.Indexer;
import team.gif.robot.subsystems.Intake;
import team.gif.robot.subsystems.Shooter;

public class RapidFire extends CommandBase {

    public RapidFire() {
    }

    @Override
    public void initialize() {
        Globals.indexerEnabled = false;
    }

    @Override
    public void execute() {
        Robot.limelight.setLEDMode(3);

        boolean isFarShot = Robot.oi != null && (Robot.oi.dStart.get() || Robot.oi.aDPadRight.get());
        double speed = isFarShot ? Constants.Shooter.RPM_HIGH : Constants.Shooter.RPM_LOW;

        if ( ( Shooter.getInstance().getVelocity() > (speed - 20.0) )
//        if ( ( Shooter.getInstance().getVelocity() > (Constants.Shooter.RPM_LOW - 20.0) )
        ) {

//            System.out.println("Firing speed " + Shooter.getInstance().getVelocity());

            Indexer.getInstance().setSpeedFive(0.5); // 0.5
            Indexer.getInstance().setSpeedFour(0.4); // 0.4
            Indexer.getInstance().setSpeedThree(0.3); // 0.35
            Indexer.getInstance().setSpeedTwo(0.3); // 0.35
            Intake.getInstance().setSpeed(0.3); // 0.35
        } else {
//            if (isFarShot) { // allows flywheel to rev up for far shot (not needed for close shot)
//                Indexer.getInstance().setSpeedFive(0);
//                Indexer.getInstance().setSpeedFour(0);
//                Indexer.getInstance().setSpeedThree(0);
//                Indexer.getInstance().setSpeedTwo(0);
//                Intake.getInstance().setSpeed(0);
//            }
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Indexer.getInstance().setSpeedFive(0);
        Indexer.getInstance().setSpeedFour(0);
        Indexer.getInstance().setSpeedThree(0);
        Indexer.getInstance().setSpeedTwo(0);
        Intake.getInstance().setSpeed(0);

        Globals.indexerEnabled = true;
    }
}
