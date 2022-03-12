package team.gif.robot.commands.autoaim;

import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import team.gif.robot.Constants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.Drivetrain;

public class LimelightAutoAim extends CommandBase {
    public LimelightAutoAim(){
        super();
        addRequirements(Robot.hood, Robot.drivetrain, Robot.shooter,Robot.indexer);
    }

    private boolean targetLocked = false;
    private boolean robotHasSettled = false;
    private final double velocityCap = 0.5;
    private int delayCounter;

    @Override
    public void initialize() {
        System.out.println("Auto Aim Start");

        Robot.limelight.setLEDMode(3);

        Drivetrain.leftTalon1.enableCurrentLimit(false);
        Drivetrain.leftTalon2.enableCurrentLimit(false);
        Drivetrain.rightTalon1.enableCurrentLimit(false);
        Drivetrain.rightTalon2.enableCurrentLimit(false);


        targetLocked = false;
        
        Globals.indexerEnabled = false;

        delayCounter = 0;
    }

    @Override
    public void execute() {
        double distanceFromHub;

        if (++delayCounter < 12) return; // Give limelight enough time to turn on LEDs before taking snapshot

        // bot must not be moving anymore
        if (!robotHasSettled) {
            DifferentialDriveWheelSpeeds wheelSpeeds = Robot.drivetrain.getWheelSpeeds();
            if (Math.abs(wheelSpeeds.leftMetersPerSecond) < velocityCap && Math.abs(wheelSpeeds.rightMetersPerSecond) < velocityCap) {
                robotHasSettled = true;
                System.out.println("AutoFire: Robot has settled");
            }
        }
        if(robotHasSettled){
            if (targetLocked) {
                //inches
                distanceFromHub = Math.abs((Constants.Shooter.UPPER_HUB_HEIGHT - Constants.Shooter.LIMELIGHT_HEIGHT) / Math.tan(Math.toRadians(Constants.Shooter.LIMELIGHT_ANGLE + Robot.limelight.getYOffset())));

                //distance zones //more accurate than rohan (TM)
                //Far Shot
                if (distanceFromHub >= 200) {
                    Robot.hood.setHoodUp();
                    Robot.shooter.setSpeedPID(Constants.Shooter.RPM_FAR_COURT);
                    //System.out.println("distanceFromHubT: " + distanceFromHub);

                    //LaunchPad
                } else if (distanceFromHub >= 100) {
                    Robot.hood.setHoodUp();
                    Robot.shooter.setSpeedPID(Constants.Shooter.RPM_LAUNCHPAD);
                    //System.out.println("distanceFromHubL: " + distanceFromHub);

                    //ring
                } else if (distanceFromHub >= 50) {
                    Robot.hood.setHoodUp();
                    Robot.shooter.setSpeedPID(Constants.Shooter.RPM_RING_UPPER_HUB);
                    //System.out.println("distanceFromHubR: " + distanceFromHub);

                    //fender
                } else {
                    Robot.hood.setHoodDown();
                    Robot.shooter.setSpeedPID(Constants.Shooter.RPM_FENDER_UPPER_HUB);
                    //System.out.println("distanceFromHubF: " + distanceFromHub);
                }
            }

            //shoot
            if (Robot.shooter.isInTolerance() && targetLocked) {
                Robot.indexer.setBeltMotorSpeedPercent(1.0);
                Robot.indexer.setMidMotorSpeed(1.0);
            }
            
            double offset = Robot.limelight.getXOffset();

            // we need to check again to make sure the robot hasn't overshot the target
            if (Robot.shooter.isInTolerance() && targetLocked) {
                if (offset > -1.0 && offset < 1.0) {
                    Robot.indexer.setBeltMotorSpeedPercent(1.0);
                    Robot.indexer.setMidMotorSpeed(1.0);

                } else {
                    System.out.println("Offset Adjusting at: " + Robot.limelight.getXOffset());
                    // need to relock
                    targetLocked = false;
                    robotHasSettled = false;
                }
            } else {
                if (Robot.limelight.getXOffset() > -1.0 && Robot.limelight.getXOffset() < 1.0) {
                    Robot.drivetrain.tankDriveVolts(0, 0);
                    targetLocked = true;
                } else if (Robot.limelight.getXOffset() < 0) {
                    Robot.drivetrain.tankDriveVolts(-Constants.Shooter.MAX_PIVOT_VOLTS, -Constants.Shooter.MAX_PIVOT_VOLTS);
                    targetLocked = false;
                } else {
                    Robot.drivetrain.tankDriveVolts(Constants.Shooter.MAX_PIVOT_VOLTS, Constants.Shooter.MAX_PIVOT_VOLTS);
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
        Robot.shooter.setSpeedPercent(0);
        Robot.indexer.setBeltMotorSpeedPercent(0);
        Robot.indexer.setMidMotorSpeed(0);

        Drivetrain.leftTalon1.enableCurrentLimit(true);
        Drivetrain.leftTalon2.enableCurrentLimit(true);
        Drivetrain.rightTalon1.enableCurrentLimit(true);
        Drivetrain.rightTalon2.enableCurrentLimit(true);

        System.out.println("Auto Aim Finished");
        Robot.limelight.setLEDMode(3);

        Globals.indexerEnabled = true;
    }

    @Override
    public boolean isFinished() {return false;}
}