// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.lib;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

public class SleepMe extends CommandBase {

    int counter = 0;

    public SleepMe() {
        super();
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        counter = 0;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if(++counter>50*5) // 20 ms intervals e.g. 50 = 1 second
            return true;
        else
            return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }
}
