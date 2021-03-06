/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package team.gif.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Globals;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.drivers.Limelight;

public class ClimberMaxUp extends CommandBase {

    public ClimberMaxUp() {
        super();
        addRequirements(Robot.climber, Robot.climberPneumatics);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Globals.climbingActive = true;
        Robot.shooterLimelight.setLEDMode(Limelight.LED_OFF); // turn LED off during climb
        Robot.collectorPneumatics.collectorLower();
        Robot.climber.setPidPosition(Constants.Climber.MAX_POSITION);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (Robot.climber.getPosition() > Constants.Climber.RELEASE_FANGS_POSITION) {
            Robot.climberPneumatics.setFangsOut();
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return Robot.climber.getPosition() > Constants.Climber.MAX_POSITION;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.climber.setSpeed(0);
    }
}
