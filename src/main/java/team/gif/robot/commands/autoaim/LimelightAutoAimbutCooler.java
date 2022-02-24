package team.gif.robot.commands.autoaim;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Globals;
import team.gif.robot.Robot;
import team.gif.robot.commands.Hood.HoodUp;
import team.gif.robot.commands.shooter.Shoot;
import team.gif.robot.commands.shooter.ShootShort;
import team.gif.robot.subsystems.Drivetrain;

import static team.gif.robot.Globals.shooterIsInTolerance;

public class LimelightAutoAimbutCooler extends CommandBase {

    private boolean targetLocked = false;
    private boolean robotHasSettled = false;
    private double velocitycap = .5;
    private int delayCounter;

    // amount of voltage we want to apply to the motors for this test

    @Override
    public void initialize() {
        System.out.println("Auto Aim Start");

        Robot.limelight.setLEDMode(3);

        Drivetrain.leftTalon1.enableCurrentLimit(false);
        Drivetrain.leftTalon2.enableCurrentLimit(false);
        Drivetrain.rightTalon1.enableCurrentLimit(false);
        Drivetrain.rightTalon2.enableCurrentLimit(false);


    }

    @Override
    public void execute(){
        if (++delayCounter < 12) return; // Give limelight enough time to turn on LEDs before taking snapshot
        //double targetSpeed = Constants.Shooter.RPM_LOW;

        if(Math.abs(Robot.limelight.getXOffset()) > 1) {
            Robot.drivetrain.pivot(Robot.pigeon.get180Heading() - Robot.limelight.getXOffset());

        }
        //shooterIsInTolerance = Math.abs(Robot.shooter.getSpeed() - 20000) < 1000 ;

        if(Robot.limelight.getYOffset() < 10){
            new ShootShort();
        }
        else{
            Robot.hood.setHoodUp(true);
            new Shoot();
        }
        //Robot.shooter.setSpeedPID(targetSpeed);
    }

    @Override
    public void end(boolean interrupted) {
        robotHasSettled = false;
        Robot.shooter.setSpeedPercent(0);
        Robot.indexer.setBeltMotorSpeedPercent(0);
        Robot.indexer.setMidMotorSpeed(0);
        Robot.hood.setHoodUp(false);

        /*
        Drivetrain.leftTalon1.enableCurrentLimit(true);
        Drivetrain.leftTalon2.enableCurrentLimit(true);
        Drivetrain.rightTalon1.enableCurrentLimit(true);
        Drivetrain.rightTalon2.enableCurrentLimit(true);
        */

        System.out.println("Auto Aim Finished");
        //Robot.limelight.setLEDMode(1);


    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
