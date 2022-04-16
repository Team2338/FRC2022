// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.commands.drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

public class DriveArcade extends CommandBase {

    public DriveArcade() {
        super();
        addRequirements(Robot.drivetrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        /*
         * Arcade Drive
         */
        double rotation = Robot.oi.driver.getRightX() * 0.95 ;
        double currSpeed = Robot.oi.driver.getLeftY();
        if (Robot.isCompBot) {
            Robot.drivetrain.driveArcade(rotation, -currSpeed);
        } else {
            Robot.drivetrain.driveArcade(-currSpeed, rotation);
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
    }
}
