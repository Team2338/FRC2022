// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.lib;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.drivers.Pigeon;

public class DriveUntilLevel extends CommandBase {

    public DriveUntilLevel() {
        super();
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        double pitch = Pigeon.getInstance().getPitch();
        //Pigeon.getInstance().addPitch( pitch );

        System.out.println("                Backing Up @ " + pitch);
        //if( Math.abs(Pigeon.getInstance().comparePitch()) > 5.0)
        if(pitch < 8.0 && pitch > -8.0)
            return true;
        else
            return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        System.out.println("Reversing - Exiting");
    }
}
