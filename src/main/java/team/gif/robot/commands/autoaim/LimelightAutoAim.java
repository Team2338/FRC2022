package team.gif.robot.commands.autoaim;

import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import team.gif.robot.Constants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;
import team.gif.robot.Robot;
import team.gif.robot.commands.shooter.Shoot;
import team.gif.robot.subsystems.Drivetrain;
import team.gif.robot.subsystems.Hood;
import team.gif.robot.subsystems.Shooter;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

public class LimelightAutoAim extends CommandBase {
    public LimelightAutoAim(){
        super();
        addRequirements(Robot.hood, Robot.shooter, Robot.indexer);
    }

    private boolean targetLocked = false;
    private boolean robotHasSettled = false;
    private final double velocityCap = 0.5;
    private final double xTolerance = 1.5;
//    private int delayCounter; // may need if we are required to turn off limelight during a match

    @Override
    public void initialize() {
//        System.out.println("Auto Aim Start");

        targetLocked = false;
        Globals.indexerEnabled = false;

//        delayCounter = 0;
        Robot.limelight.setLEDMode(3); // turn on - just in case they were turned off somehow
    }

    @Override
    public void execute() {

        // we don't need this if limelight can stay on all the time
//        if (++delayCounter < 12) return; // Give limelight enough time to turn on LEDs before taking snapshot

        // we want the shooter to start revving up so the robot can shoot as soon as it settles
        double distanceFromHubInches = abs((Constants.Shooter.UPPER_HUB_HEIGHT - Constants.Shooter.LIMELIGHT_HEIGHT) / Math.tan(Math.toRadians(Constants.Shooter.LIMELIGHT_ANGLE + Robot.limelight.getYOffset())));

        System.out.println(distanceFromHubInches);
        // More Accurate than Rohan 2.0 (TM)
        // hood position
        Robot.hood.setHoodUp();
/*        if(distanceFromHubInches >= 50){
            Robot.hood.setHoodUp();
        }
        else {
            Robot.hood.setHoodDown();
        }
*/
        // RPM calculation
        //double targetRPM = Constants.Shooter.klP * (distanceFromHubInches / Constants.Shooter.FLYWHEEL_RADIUS) * sqrt((2 * (Constants.Shooter.UPPER_HUB_HEIGHT - Constants.Shooter.SHOOTER_HEIGHT))/((2* distanceFromHubInches * sin(toRadians(Globals.hoodAngle)) * cos(toRadians(Globals.hoodAngle))) + (-386.4) * (Math.pow(cos(toRadians(Globals.hoodAngle)),2))));
        //Robot.shooter.setSpeedPID(targetRPM);
//        System.out.println("targetRPM: " + targetRPM);

        /*
        //More Accurate Than Rohan (TM)// distance zones
        if (distanceFromHub >= 200) { // Far Shot
            Robot.hood.setHoodUp();
            Robot.shooter.setSpeedPID(Constants.Shooter.RPM_FAR_COURT);
            System.out.println("Distance - Far: " + distanceFromHub);

        } else if (distanceFromHub >= 130) { // LaunchPad shot
            Robot.hood.setHoodUp();
            Robot.shooter.setSpeedPID(Constants.Shooter.RPM_LAUNCHPAD);
            System.out.println("Distance - Launch: " + distanceFromHub);

        } else if (distanceFromHub >= 50) { // Ring shot
            Robot.hood.setHoodUp();
            Robot.shooter.setSpeedPID(Constants.Shooter.RPM_RING_UPPER_HUB);
            System.out.println("Distance - Ring: " + distanceFromHub);

        } else {
            Robot.hood.setHoodDown(); // Fender shot
            Robot.shooter.setSpeedPID(Constants.Shooter.RPM_FENDER_UPPER_HUB);
            System.out.println("Distance - Fender: " + distanceFromHub);
        }
        */


        // bot must not be moving anymore
        if (!robotHasSettled) {
            DifferentialDriveWheelSpeeds wheelSpeeds = Robot.drivetrain.getWheelSpeeds();
            // Make sure both wheels are within tolerance of not moving
            if (abs(wheelSpeeds.leftMetersPerSecond) < velocityCap && abs(wheelSpeeds.rightMetersPerSecond) < velocityCap) {
                robotHasSettled = true;
//                System.out.println("AutoFire: Robot has settled");
            }
        }

        if (robotHasSettled) { // Note: can't combine this using else because robotHasSettled can be set to true in the above section
            double xOffset = Robot.limelight.getXOffset();//commented bc as the robot turns the amount of ring detected change meaning this value needs to change too
            //double pivVolts = Robot.limelight.getXOffset() * 0.01 * Constants.Shooter.MAX_PIVOT_VOLTS;

            if (targetLocked) {
                // we need to check again to make sure the robot hasn't overshot the target
                if (xOffset > -xTolerance && xOffset < xTolerance) {
                    if (Robot.shooter.isInTolerance()) {
                        // fire away!
//                        System.out.println("Shooting - I hope it went in");
                        Robot.indexer.setBeltMotorSpeedPercent(1.0);
                        Robot.indexer.setMidMotorSpeed(1.0);
                    } else {
                        // shooter is still spinning up or just can't get to the desired speed
//                        System.out.println("Robot is settled and locked. Flywheel not in tolerance.");
                    }
                } else {
//                    System.out.println("Offset Adjusting at: " + xOffset);
                    // need to relock
                    targetLocked = false;
                    robotHasSettled = false;
                }
            } else {
                if (xOffset > -xTolerance && xOffset < xTolerance) { // target is locked
                    Robot.drivetrain.tankDriveVolts(0, 0);
                    targetLocked = true;
                } else { // still not in tolerance, need to rotate
                    double pivotVolts = 1.5 + abs(xOffset) * 0.01 * Constants.Shooter.MAX_PIVOT_VOLTS;
                    pivotVolts = xOffset > 0 ? pivotVolts : -1.0 * pivotVolts;
                    Robot.drivetrain.tankDriveVolts(pivotVolts, -pivotVolts);
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
        Robot.shooter.setSpeedPercent(0);
        Robot.indexer.setBeltMotorSpeedPercent(0);
        Robot.indexer.setMidMotorSpeed(0);

//        System.out.println("Auto Aim Finished");
        Robot.limelight.setLEDMode(3); // leave LED on after autoaim so we can still use during manual

        Globals.indexerEnabled = true;
    }

    @Override
    public boolean isFinished() {return false;}
}
