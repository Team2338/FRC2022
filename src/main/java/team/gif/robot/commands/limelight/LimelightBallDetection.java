package team.gif.robot.commands.limelight;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Globals;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.drivers.Limelight;

import static java.lang.Math.abs;

public class LimelightBallDetection extends CommandBase {
    public LimelightBallDetection(){
        super();
        addRequirements();
    }

    private final double xTolerance = 1.5;
    private final double travelVoltage = 4.0;
    private boolean firstCargoPassedThreshold = false;
    private boolean trackingSecondCargo = false;
    private int inThresholdCount = 0;
    private int settleCount = 0;
    private int failSafeCount = 0;

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Robot.collectorLimelight.setLEDMode(Limelight.LED_OFF); // turn off LED
        Robot.collectorLimelight.setCamMode(Limelight.MODE_TRACKING);
        Robot.collectorLimelight.setPipeline( (DriverStation.getAlliance() == DriverStation.Alliance.Blue) ? 3 : 2);
        firstCargoPassedThreshold = false; // Indicates that we are close to the first cargo and we should stop when this
                                           // cargo is collected (and goes out of the field of view)
        trackingSecondCargo = false;
        inThresholdCount = 0;
        settleCount = 0;
        failSafeCount = 0;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Checks if the limelight can see a target
        if (Robot.collectorLimelight.noTarget()) {
            return;
        }

        double xOffset = Robot.collectorLimelight.getXOffset(); // Sets x offset
        double yOffset = Robot.collectorLimelight.getYOffset(); // Sets y offset
        double pivotVolts = 0;

        // This prevents us from picking up a second ball
        // The current ball should go below a yOffset of -10 degrees
        // then if another ball is then detected which is at a higher yOffset
        // we know to stop
//        System.out.println(firstCargoPassedThreshold + "     " + yOffset + " " + count + settleCount);
        if (yOffset < -10) { // -10 is the angle we set as locking onto a (or the first) cargo
            ++inThresholdCount;
            if (inThresholdCount > 2) { // prevents jitter
                firstCargoPassedThreshold = true;
            }
        }

        // Determines if a second ball is now being tracked. We know this because the
        // yOffset jumps up and the first cargo was already close to us
        // -5 is used as the delineation between first and second cargo
        if (yOffset > -5 && firstCargoPassedThreshold) {
            trackingSecondCargo = true;
            return;
        }

        // More Accurate Than Shalin
        if (abs(xOffset) > xTolerance) {
            pivotVolts = -xOffset * 0.02 * Constants.Shooter.MAX_PIVOT_VOLTS_BALL; // 0.02 is used as a proportional gain
        }

        // continue to move but adjust each side slightly so it turns
        // negative travel voltage since we are traveling backwards
        Robot.drivetrain.tankDriveVolts(-travelVoltage - pivotVolts,-travelVoltage + pivotVolts);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        // If we are still detecting the ball after 3 seconds, end command
        if (++failSafeCount >150 ) { // 1 second = 50
            return true;
        }

        settleCount++;
        if (Globals.autonomousModeActive && (Robot.collectorLimelight.noTarget())) {
            if (settleCount > 10) { // 1 second = 50 ... make sure we give the robot time to lock on
                System.out.println("No cargo target - aborting");
                return true;
            }
        }

        if (trackingSecondCargo) {
            System.out.println("Tracking 2nd cargo - stop now - exiting");
            return true;
        }

        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.drivetrain.tankDriveVolts(0,0);
        Robot.collectorLimelight.setLEDMode(Limelight.LED_OFF); // Leave LED off after ball detected
    }
}
