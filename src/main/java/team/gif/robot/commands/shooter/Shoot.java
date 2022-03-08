// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Robot;

/** An example command that uses an example subsystem. */
public class Shoot extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

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
        if (Robot.shooter.isInTolerance() && Robot.indexer.getSensorBelt()) {
            //Robot.indexer.setBeltMotorSpeedPercent(1.0);
            Robot.indexer.setBeltMotorSpeedPID(Constants.Indexer.RPM_BELT);
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
