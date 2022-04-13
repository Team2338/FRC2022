package team.gif.robot.commands.limelight;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

import static java.lang.Math.abs;

public class LimelightHubDetection extends CommandBase {
    public LimelightHubDetection(){
        super();
        addRequirements();
    }

    private final double xTolerance = 1.5;
    private int count = 0;
    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Globals.indexerEnabled = false;
        Robot.shooterLimelight.setLEDMode(3); // turn on - just in case they were turned off somehow
        Robot.shooterLimelight.setCamMode(0);
//        Robot.shooterLimelight.setPipeline(Globals.collectorLimelightBallMode ? 0 : 1);
        count = 0;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Checks if the limelight can see a target
        if (Robot.shooterLimelight.noTarget()) {
            return;
        }

        double xOffset = Robot.shooterLimelight.getXOffset(); // Sets x offset
        double yOffset = Robot.shooterLimelight.getYOffset(); // Sets y offset
        double pivotVolts = 0;

//        Robot.drivetrain.tankDriveVolts(-2.5,-2.5);
/*        double reverseVolts = -((yOffset + Constants.Shooter.CARGO_DETECT_MINIMUM) * 0.01 * Constants.Shooter.MAX_REVERSE_VOLTS) + Constants.Shooter.MIN_REVERSE_VOLTS;
*/
        // More Accurate Than Shalin
        if (abs(xOffset) > xTolerance) {
            pivotVolts = -xOffset * 0.015 * Constants.Shooter.MAX_PIVOT_VOLTS_BALL;

//            pivotVolts = (xOffset < 0 ? 1 : -1.0) * 0.5;
        }
        Robot.drivetrain.tankDriveVolts(7 - pivotVolts,7 + pivotVolts);

        // Reverses into ball and when no see ball algorithm stops
//        double reverseVolts = ((yOffset + Constants.Shooter.LIMELIGHT_LOW_BOUND_ANGLE_BALLS) * 0.01 * Constants.Shooter.MAX_REVERSE_VOLTS) + Constants.Shooter.MIN_REVERSE_VOLTS;
//        Robot.drivetrain.tankDriveVolts(reverseVolts, reverseVolts); // slows robot down until collects ball
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (Globals.autonomousModeActive && (Robot.shooterLimelight.noTarget())) {
            System.out.println("No hub target - exiting");
            return true;
        }

        System.out.println("hub: " + Robot.shooterLimelight.getYOffset() + " " + count);
        if( Robot.shooterLimelight.getYOffset() > -8){
            count++;
            if( count > 10) {
                System.out.println("close enough");
                return true;
            }
        }

        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.drivetrain.tankDriveVolts(0,0);
        Robot.shooterLimelight.setLEDMode(3); // Leave LED on after ball detected
        Globals.indexerEnabled = true;
    }
}
