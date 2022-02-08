// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.commands.indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

/** An example command that uses an example subsystem. */
public class IndexScheduler extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    private CommandBase currCommand = null;
    public IndexScheduler() {
        super();
        // Use addRequirements() here to declare subsystem dependencies.
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
        if(currCommand != null && currCommand.isFinished()) {
            currCommand = null;
        }
        if(currCommand == null && Globals.indexerEnabled) {
            if(!Robot.indexer.sensorStates()[2] && Robot.indexer.sensorStates()[1]) {
                currCommand = new IndexMidToTop();
                CommandScheduler.getInstance().schedule(currCommand);
            }
            if(!Robot.indexer.sensorStates()[1] && Robot.indexer.sensorStates()[0]) {
                currCommand = new IndexCollectorToMid();
                CommandScheduler.getInstance().schedule(currCommand);
            }
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return !Robot.indexer.sensorStates()[0] || Robot.indexer.sensorStates()[1];
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.indexer.setIndexMotorSpeed(0);
    }
}