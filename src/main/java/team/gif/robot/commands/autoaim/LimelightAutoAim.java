package team.gif.robot.commands.autoaim;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import team.gif.robot.Constants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;
import team.gif.robot.Robot;
import team.gif.robot.commands.drivetrain.Drive;
import team.gif.robot.commands.shooter.RevFlywheel;
import team.gif.robot.subsystems.Drivetrain;
import team.gif.robot.subsystems.Indexer;
import team.gif.robot.subsystems.Intake;
import team.gif.robot.subsystems.Shooter;
import team.gif.robot.subsystems.drivers.Pigeon;

public class LimelightAutoAim extends CommandBase {

    private boolean targetLocked = false;
    private boolean robotHasSettled = false;
    private double velocitycap = .5;
    private int delayCounter;

    // amount of voltage we want to apply to the motors for this test
    private double motorVolts = 3.50;

    @Override
    public void initialize() {
        System.out.println("Auto Aim Start");

        Robot.limelight.setLEDMode(3);

        Drivetrain.getInstance().leftTalon1.enableCurrentLimit(false);
        Drivetrain.getInstance().leftTalon2.enableCurrentLimit(false);
        Drivetrain.getInstance().rightTalon1.enableCurrentLimit(false);
        Drivetrain.getInstance().rightTalon2.enableCurrentLimit(false);

        targetLocked = false;

        Globals.indexerEnabled = false;

        delayCounter = 0;
    }

    @Override
    public void execute() {

        if (++delayCounter < 12) return; // Give limelight enough time to turn on LEDs before taking snapshot

        double targetSpeed = Constants.Shooter.RPM_LOW;

        if (Robot.oi != null && (Robot.oi.dStart.get() || Robot.oi.aDPadRight.get())) {
            targetSpeed = Constants.Shooter.RPM_HIGH;
        }

        if ( Math.abs (Robot.limelight.getXOffset()) < 5 ) {
            Shooter.getInstance().setPID(targetSpeed);
        }

        // bot must not be moving anymore
        if ( !robotHasSettled ) {
            DifferentialDriveWheelSpeeds wheelSpeeds = Drivetrain.getInstance().getWheelSpeeds();
            if ( Math.abs(wheelSpeeds.leftMetersPerSecond) < velocitycap && Math.abs(wheelSpeeds.rightMetersPerSecond)< velocitycap ){
                robotHasSettled = true;
                System.out.println("AutoFire: Robot has settled");
            }
        }

        if ( robotHasSettled ) {
            if (targetLocked) {
                //System.out.println(Shooter.getInstance().getVelocity());
                if (Shooter.getInstance().getVelocity() > (targetSpeed - 20.0)) {

                    // we need to check again to make sure the robot hasn't overshot the target
                    double offset = Robot.limelight.getXOffset();
                    if (offset > -1.0 && offset < 1.0) {
                        Indexer.getInstance().setSpeedFive(0.5); // 0.5
                        Indexer.getInstance().setSpeedFour(0.4); // 0.4
                        Indexer.getInstance().setSpeedThree(0.3); // 0.35
                        Indexer.getInstance().setSpeedTwo(0.3); // 0.35
                        Intake.getInstance().setSpeed(0.3); // 0.35
                    } else {
                        System.out.println("Offset Adjusting at: " + offset);
                        // need to relock
                        targetLocked = false;
                        robotHasSettled = false;
                    }
                }
            } else {
                double offset = Robot.limelight.getXOffset();
                if (offset > -1.0 && offset < 1.0) {
                    Drivetrain.getInstance().tankDriveVolts(0, 0);
                    targetLocked = true;
                } else {
                    if (offset < 0) {
                        Drivetrain.getInstance().tankDriveVolts(-motorVolts, motorVolts);

                    } else {
                        Drivetrain.getInstance().tankDriveVolts(motorVolts, -motorVolts);

                    }
                    targetLocked = false;
                }
            }
        }
    }

    //
    // Called as a whileHeld. When user releases the toggle, end() is called
    //
    @Override
    public void end(boolean interrupted) {
        robotHasSettled = false;
        Shooter.getInstance().setVoltage(0);
        Indexer.getInstance().setSpeedFive(0);
        Indexer.getInstance().setSpeedFour(0);
        Indexer.getInstance().setSpeedThree(0);
        Indexer.getInstance().setSpeedTwo(0);
        Intake.getInstance().setSpeed(0);

        Drivetrain.getInstance().leftTalon1.enableCurrentLimit(true);
        Drivetrain.getInstance().leftTalon2.enableCurrentLimit(true);
        Drivetrain.getInstance().rightTalon1.enableCurrentLimit(true);
        Drivetrain.getInstance().rightTalon2.enableCurrentLimit(true);

        System.out.println("Auto Aim Finished");
        Robot.limelight.setLEDMode(1);

        Globals.indexerEnabled = true;
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}