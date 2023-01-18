// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.lib;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.drivers.Pigeon;

public class DriveUntilAngle extends CommandBase {

    public DriveUntilAngle() {
        super();
    }

    boolean checkForDown;

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        checkForDown = false;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        double pitch = Pigeon.getInstance().getPitch();
        Pigeon.getInstance().addPitch( pitch );

        if( !checkForDown ) {
            System.out.println("   Going up " + pitch);
            if (pitch < -10.0) {
            //if( Math.abs(Pigeon.getInstance().comparePitch()) > 10.0) {
                checkForDown = true;
                System.out.println("         exiting");
            }
        } else {
            System.out.println("        Levelling " + pitch);
            if (pitch > -8.0) {
            //if( Math.abs(Pigeon.getInstance().comparePitch()) > 8.0) {
                System.out.println("         exiting");
                return true;
            }
        }
        return false;

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }
}
