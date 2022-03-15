// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

/**
 * Runs the belt to push a ball into the flywheel once we reach shooting tolerances
 */
public class Shoot extends CommandBase {

    public Shoot() {
        super();
        addRequirements(Robot.indexer);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (Robot.shooter.isInTolerance()) { // && Robot.indexer.getSensorBelt()) {
            Robot.indexer.setBeltMotorSpeedPercent(1.0);
            //Robot.indexer.setBeltMotorSpeedPID(1000); TODO: Tune and add PID values so we can have even belt movement
        } else {
            Robot.indexer.setBeltMotorSpeedPercent(0);
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
        Robot.indexer.setBeltMotorSpeedPercent(0);
    }
}
