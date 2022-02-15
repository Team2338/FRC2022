// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.commands.indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

public class IndexCollectorToMid extends CommandBase {
	
	public IndexCollectorToMid() {
		super();
		addRequirements(Robot.indexer);
	}
	
	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
	}
	
	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		Robot.indexer.setIndexMotorSpeed(0.5);
	}
	
	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		if (!Globals.indexerEnabled) {
			return true;
		} else {
			return Robot.indexer.getSensorCollector();
		}
	}
	
	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		Robot.indexer.setIndexMotorSpeed(0);
	}
}
