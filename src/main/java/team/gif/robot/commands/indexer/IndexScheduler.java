// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.commands.indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

public class IndexScheduler extends CommandBase {

    private CommandBase currCommand = null;

    public IndexScheduler() {
        super();
        addRequirements(Robot.indexer);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        currCommand = null;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (currCommand != null && currCommand.isFinished()) {
            currCommand = null;
        }

        if (currCommand == null && Globals.indexerEnabled) {
            if (!Robot.indexer.getSensorBelt() && Robot.indexer.getSensorMid()) {
                currCommand = new IndexMidToBelt();
                currCommand.schedule();
            }

            if (!Robot.indexer.getSensorMid() && Robot.indexer.getSensorEntry()) {
                currCommand = new IndexCollectorToMid();
                currCommand.schedule();
            }
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
