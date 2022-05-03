package team.gif.robot.commands.limelight;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Globals;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.drivers.Limelight;

import static java.lang.Math.abs;

public class LimelightHubDetection extends CommandBase {
    public LimelightHubDetection(){
        super();
        addRequirements();
    }

    private final double xTolerance = 1.5;
    private final double travelVoltage = 6.0;
    private int lockAttempts = 0;
    private int inShootingPositionCount = 0;

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Robot.shooterLimelight.setLEDMode(Limelight.LED_ON); // turn on - just in case they were turned off somehow
        Robot.shooterLimelight.setCamMode(Limelight.MODE_TRACKING);
        lockAttempts = 0;
        inShootingPositionCount = 0;
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

//        double reverseVolts = -((yOffset + Constants.Shooter.CARGO_DETECT_MINIMUM) * 0.01 * Constants.Shooter.MAX_REVERSE_VOLTS) + Constants.Shooter.MIN_REVERSE_VOLTS;

        // More Accurate Than Shalin
        if (abs(xOffset) > xTolerance) {
            pivotVolts = -xOffset * 0.015 * Constants.Shooter.MAX_PIVOT_VOLTS_BALL; // 0.015 is used as a proportional gain
        }

        // continue to move but adjust each side slightly so it turns
        Robot.drivetrain.tankDriveVolts(travelVoltage - pivotVolts,travelVoltage + pivotVolts);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (Globals.autonomousModeActive && (Robot.shooterLimelight.noTarget())) {
            if (++lockAttempts > 50) {
                System.out.println("No valid hub target after 50 attempts - aborting");
                return true;
            } else {
                return false;
            }
        }

//        System.out.println("hub: " + Robot.shooterLimelight.getYOffset());
        if (Robot.shooterLimelight.getYOffset() > -8) { // -8 is the angle we see the hub when we want to shoot
            inShootingPositionCount++; // Have to see an angle of -8 or less 3 times to prevent jitter
            if (inShootingPositionCount > 3) {
                System.out.println("Reached shooting position");
                return true;
            }
        }
        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.drivetrain.tankDriveVolts(0,0);
        Robot.shooterLimelight.setLEDMode(Limelight.LED_ON); // Leave LED on after ball detected
    }
}
