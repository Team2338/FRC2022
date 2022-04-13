package team.gif.robot.commands.limelight;

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

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Globals.indexerEnabled = false;
        Robot.collectorLimelight.setLEDMode(Limelight.LED_OFF); // turn off LED
        Robot.collectorLimelight.setCamMode(0);
        Robot.collectorLimelight.setPipeline(Globals.collectorLimelightBallMode ? 0 : 1);
        firstCargoPassedThreshold = false; // Indicates that we are close to the first cargo and we should stop when this
                                           // cargo is collected (and goes out of the field of view)
        trackingSecondCargo = false;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Checks if the limelight can see a target
        if (Robot.collectorLimelight.noTarget()) {
            System.out.println("No cargo target - exiting limelight ball detection execution loop");
            return;
        }

        double xOffset = Robot.collectorLimelight.getXOffset(); // Sets x offset
        double yOffset = Robot.collectorLimelight.getYOffset(); // Sets y offset
        double pivotVolts = 0;

        // This prevents us from picking up a second ball
        // The current ball should go below a yOffset of -10 degrees
        // then if another ball is then detected which is at a higher yOffset
        // we know to stop
        System.out.println(firstCargoPassedThreshold + "     " + yOffset);
        if (yOffset < -10) {
            firstCargoPassedThreshold = true;
        }

        if (yOffset > -5 && firstCargoPassedThreshold) { // A second ball is now being tracked. We know this because the
            trackingSecondCargo = true;                  // yOffset jumped up and the first cargo was already close to us.
            return;
        }

        // More Accurate Than Shalin
        if (abs(xOffset) > xTolerance) {
            pivotVolts = -xOffset * 0.02 * Constants.Shooter.MAX_PIVOT_VOLTS_BALL;
        }
        // drive at a constant voltage with some turning adjustment (add to one side, subtract to the other)
        // negative travel voltage since we are traveling backwards
        Robot.drivetrain.tankDriveVolts(-travelVoltage - pivotVolts,-travelVoltage + pivotVolts);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (Globals.autonomousModeActive && (Robot.collectorLimelight.noTarget())) {
            System.out.println("No cargo target - exiting");
            return true;
        }

        if (trackingSecondCargo) {
            System.out.println("Tracking 2nd cargo - stopNow");
            return true;
        }

        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.drivetrain.tankDriveVolts(0,0);
        Robot.collectorLimelight.setLEDMode(Limelight.LED_ON); // Leave LED on after ball detected
        Globals.indexerEnabled = true;
    }
}
