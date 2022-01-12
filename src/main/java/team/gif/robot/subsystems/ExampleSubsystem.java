// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ExampleSubsystem extends SubsystemBase {
    /* Used to declare singleton class */
    private static ExampleSubsystem instance = null;
    public static ExampleSubsystem getInstance() {
        if (instance == null) {
            instance = new ExampleSubsystem();
        }
        return instance;
    }

    /** Creates a new ExampleSubsystem. */
    public ExampleSubsystem() {}

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}
