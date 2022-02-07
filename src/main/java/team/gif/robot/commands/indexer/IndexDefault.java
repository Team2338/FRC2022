// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.commands.indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

/** An example command that uses an example subsystem. */
public class IndexDefault extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    public IndexDefault() {
        super();
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.indexer);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        Robot.indexer.setIndexMotor(Robot.indexer.sensorStates()[0] && !Robot.indexer.sensorStates()[1] ? 0.8 : 0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if(Robot.indexer.sensorStates()[0] && !Robot.indexer.sensorStates()[1]){
            return false;
        }
        return true;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.indexer.setIndexMotor(0);
    }
}
