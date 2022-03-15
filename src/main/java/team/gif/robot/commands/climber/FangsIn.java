// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

public class FangsIn extends CommandBase {

    public FangsIn() {
        super();
        addRequirements(Robot.climberPneumatics);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Robot.climberPneumatics.setFangsIn();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}
}
