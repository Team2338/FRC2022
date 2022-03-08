// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.commands.indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

/** An example command that uses an example subsystem. */
public class IndexMidToBelt extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    public IndexMidToBelt() {
        super();
        addRequirements(Robot.indexer);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        Robot.indexer.setBeltMotorSpeedPID(Constants.Indexer.RPM_BELT);
        Robot.indexer.setMidMotorSpeed(0.8);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if(Globals.indexerEnabled) {
            return Robot.indexer.getSensorBelt();
        } else {
            return true;
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.indexer.setMidMotorSpeed(0);
        Robot.indexer.setBeltMotorSpeedPercent(0);
    }
}
