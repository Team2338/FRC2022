/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package team.gif.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Robot;

public class RobotUp extends CommandBase {
    private boolean isHoldState = false;

    public RobotUp() {
        super();
        addRequirements(Robot.climber);
    }

    @Override
    public void initialize() {
        isHoldState = false;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double climberPos = Robot.climber.getPosition();
        if (climberPos < Constants.Climber.ROBOT_UP_POSITION) { // robot is near bar and hanging. Need to apply small voltage to overcome gravity.
            isHoldState = true;
        }

        if (climberPos < 100) { // Don't allow the climber to go below 100 ticks
            Robot.climber.setSpeed(0);
            return;
        }

        if (isHoldState) { // Robot is near bar and hanging. Need to apply small voltage to overcome gravity.
            Robot.climber.setSpeed(Constants.Climber.HOLD_LOADED_VOLTAGE);
            return;
        }

        Robot.climber.setSpeed(Constants.Climber.DOWN_LOADED_VOLTAGE);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }
}
