// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static class Drivetrain {
        public static double WHEEL_DIAMETER = 0.127; // IN METERS
        public static double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * 3.14159; // IN METERS

        public static double TICKS_TO_METERS = 10000;
        public static double TICKS_TO_METERS_LEFT = 10115; //10058; // Pushed bot 17 feet, recorded ticks (51816), converted to meters
        public static double TICKS_TO_METERS_RIGHT = 10005; // 9915; // 9915; // Pushed bot 17 feet, recorded ticks (51816), converted to meters

        // trajectory
        // from FRC Characterization Tool
        public static final double ksVolts = 1.37;
        public static final double kvVoltSecondsPerMeter = 2.46;
        public static final double kaVoltSecondsSquaredPerMeter = 0.773;
        public static final double kPDriveVelLeft = 10.0;
        public static final double kPDriveVelRight = kPDriveVelLeft;
        public static final double kTrackWidthMeters = 1.0;
        public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackWidthMeters);
}
    public static class Auto {
        // part of trajectory but numbers are from example
        public static final double kMaxSpeedMetersPerSecond = 3.0;
        public static final double kMaxAccelerationMetersPerSecondSquared = 3.0;
        public static final double kSlowSpeedMetersPerSecond = 1.5;
        public static final double kSlowAccelerationMetersPerSecondSquared = 1.5;
        public static final double kFastSpeedMetersPerSecond = 4.0;
        public static final double kFastAccelerationMetersPerSecondSquared = 4.0;
        public static final double kRamseteB = 2;
        public static final double kRamseteZeta = 0.7;
    }

    public static class Shooter {
        public static final double kP = 0.2;
        public static final double kI = 0;
        public static final double kD = 0;
        public static final double kF = 0.056;

        public static final double RPM_LOW = Robot.isCompBot ? 14000 : 14000;
        public static final double RPM_HIGH = 7500;
    }

    public static class Indexer {
        public static final double kPBelt = 0;
        public static final double kIBelt = 0;
        public static final double kDBelt = 0;
        public static final double kFFBelt = 0;
        public static final double kIZoneBelt = 0;
    }

    public static class Climber {
        // Elevator
        public static final double P = 0.0; // Connor said 4
        public static final double I = 0.0;
        public static final double D = 0.0;
        public static final double F = 0; //0.425
        public static final double REV_F = 0.38;

        public static final double GRAV_FEED_FORWARD = 300 / 1023.0; // Percent constant to counteract gravity

        public static final double MAX_POSITION = 500000;
        public static final double ASCEND_POSITION = 15000;
        public static final double FALL_POSITION = 250000;

        public static final double UP_UNLOADED_VOLTAGE = 0.8;
        public static final double DOWN_LOADED_VOLTAGE = -0.8;
        public static final double HOLD_LOADED_VOLTAGE = -0.4;
        public static final double LOADED_DROP_VOLTAGE = 0.2;
    }
}
