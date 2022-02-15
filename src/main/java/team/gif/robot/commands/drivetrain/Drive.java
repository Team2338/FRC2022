// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.commands.drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

public class Drive extends CommandBase {
	
	public Drive() {
		super();
		addRequirements(Robot.drivetrain);
	}
	
	@Override
	public void initialize() {
	}
	
	@Override
	public void execute() {
		/*
		 * Arcade Drive
		 */
//		double currSpeed = Robot.oi.driver.getLeftY();
//		double rotation = Robot.oi.driver.getRightX();
//		Robot.drivetrain.driveArcade(rotation, currSpeed);
		
		/*
		 * True Tank Drive
		 */
        double currL = -Robot.oi.driver.getLeftY(); //assuming negative because motors have .setInverted(false);
        double currR = -Robot.oi.driver.getRightY();
        Robot.drivetrain.setSpeed(currL, currR);
	}
	
	@Override
	public boolean isFinished() {
		return false;
	}
	
	@Override
	public void end(boolean interrupted) {
	}
}
