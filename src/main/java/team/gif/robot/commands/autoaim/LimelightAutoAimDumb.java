// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.commands.autoaim;

import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Globals;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.Drivetrain;

public class LimelightAutoAimDumb extends CommandBase {
    private boolean targetLocked = false;
    private boolean robotHasSettled = false;
    private double velocitycap = .5;
    private double distanceFromHub;
    private int delayCounter;
    private final double MAX_PIVOT_VOLTS = 5.0;


    public LimelightAutoAimDumb() {
        super();
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.shooter);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        System.out.println("Rohan's big dumb auto aim start");
        Robot.limelight.setLEDMode(3);

        Drivetrain.leftTalon1.enableCurrentLimit(false);
        Drivetrain.leftTalon2.enableCurrentLimit(false);
        Drivetrain.rightTalon1.enableCurrentLimit(false);
        Drivetrain.rightTalon2.enableCurrentLimit(false);

        targetLocked = false;

        Globals.indexerEnabled = false;

        delayCounter = 0;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (++delayCounter < 12) return;

        // bot must not be moving anymore
        if (!robotHasSettled) {
            DifferentialDriveWheelSpeeds wheelSpeeds = Robot.drivetrain.getWheelSpeeds();
            if ( Math.abs(wheelSpeeds.leftMetersPerSecond) < velocitycap && Math.abs(wheelSpeeds.rightMetersPerSecond)< velocitycap ){
                robotHasSettled = true;
                System.out.println("AutoFire: Robot has settled");
            }
        }
        if (robotHasSettled) {
            if (targetLocked) {

                //inches
                distanceFromHub = (Constants.Shooter.UPPER_HUB_HEIGHT - Constants.Shooter.LIMELIGHT_HEIGHT) /
                        Math.tan(Math.toRadians(Constants.Shooter.LIMELIGHT_ANGLE + Robot.limelight.getYOffset()));
                System.out.println(distanceFromHub);


                double tgSpeed = (distanceFromHub > 200 && distanceFromHub < 205) ? Constants.Shooter.RPM_LAUNCHPAD : (distanceFromHub >= 205) ? Constants.Shooter.RPM_FAR_COURT : Constants.Shooter.RPM_FENDER_UPPER_HUB;
                System.out.println(tgSpeed);


                if(tgSpeed >= Constants.Shooter.RPM_HIGH) {
                    Robot.hood.setHoodUp();
                } else {
                    Robot.hood.setHoodDown();
                }
                if (Robot.shooter.getSpeed() > tgSpeed - 20.0) {

                    // we need to check again to make sure the robot hasn't overshot the target
                    double offset = Robot.limelight.getXOffset();
                    if (offset > -1.0 && offset < 1.0) {
                        Robot.indexer.setBeltMotorSpeedPercent(0.5);
                        Robot.indexer.setMidMotorSpeed(0.4);
                    } else {
                        System.out.println("Offset Adjusting at: " + offset);
                        // need to relock
                        targetLocked = false;
                        robotHasSettled = false;
                    }
                }
            } else {
                double offset = Robot.limelight.getXOffset();
                if (offset > -0.5 && offset < 0.5) {
                    Robot.drivetrain.tankDriveVolts(0, 0);
                    targetLocked = true;
                } else {
                    double pivVolts = ((offset / 10) / MAX_PIVOT_VOLTS > 1) ? (offset / 10) / MAX_PIVOT_VOLTS : 1;
                    if (offset > -1.0 && offset < 1.0) {
                        Robot.drivetrain.tankDriveVolts(0, 0);
                        targetLocked = true;
                    } else {
                        if (offset < 0) {
                            Robot.drivetrain.tankDriveVolts(pivVolts, pivVolts);

                        } else {
                            Robot.drivetrain.tankDriveVolts(-pivVolts, -pivVolts);

                        }
                    }



                    /*
                    if (offset < 0) {
                        Robot.drivetrain.tankDriveVolts(-pivVolts, pivVolts);

                    } else {
                        Robot.drivetrain.tankDriveVolts(pivVolts, -pivVolts);

                    }
                     */
                    targetLocked = false;
                }
            }
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
        robotHasSettled = false;
        Robot.shooter.setSpeedPercent(0);
        Robot.indexer.setBeltMotorSpeedPercent(0);
        Robot.indexer.setMidMotorSpeed(0);

        Drivetrain.leftTalon1.enableCurrentLimit(true);
        Drivetrain.leftTalon2.enableCurrentLimit(true);
        Drivetrain.rightTalon1.enableCurrentLimit(true);
        Drivetrain.rightTalon2.enableCurrentLimit(true);

        System.out.println("Auto Aim Finished");
        Robot.limelight.setLEDMode(1);

        Globals.indexerEnabled = true;}
}
