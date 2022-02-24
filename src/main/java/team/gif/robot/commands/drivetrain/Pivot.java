package team.gif.robot.commands.drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

import static team.gif.robot.Robot.pigeon;

public class Pivot extends CommandBase {
    double setpoint;
    double error;
    public Pivot(double setpoint){
        super();
        addRequirements(Robot.drivetrain);
        this.setpoint = setpoint;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        error = ((setpoint - pigeon.getCompassHeading())/setpoint);
        while(Math.abs(error) > 0.1 && Math.abs(error) < 1.0){
            Robot.drivetrain.setSpeed(error, - error);
            error = ((setpoint - pigeon.getCompassHeading())/setpoint) * 100;
        }

    }


    @Override
    public boolean isFinished() {
        return error == 0;
    }

    @Override
    public void end(boolean interrupted) {
        Robot.drivetrain.setSpeed(0,0);
    }
}
