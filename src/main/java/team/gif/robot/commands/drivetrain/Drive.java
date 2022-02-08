// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.commands.drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

/** An example command that uses an example subsystem. */
public class Drive extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    //private final ExampleSubsystem m_subsystem;

    public Drive() {
        super();
        addRequirements(Robot.drivetrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        /*
         * Tank Drive
         */
        double currSpeed = Robot.oi.driver.getLeftY();
        double rotation = Robot.oi.driver.getRightX();
        Robot.drivetrain.driveArcade(rotation, currSpeed);

        /*
         * True Tank Drive
         */
//        double currL = -Robot.oi.driver.getLeftY(); //assuming negative because motors have .setInverted(false);
//        double currR = -Robot.oi.driver.getRightY();
//        Robot.drivetrain.setSpeed(currL, currR);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}
}
