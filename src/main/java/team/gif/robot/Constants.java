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
        public static final double ksVolts = 0.61696; // cims 1.2596;
        public static final double kvVoltSecondsPerMeter = 0.59136; // cims 2.5664;
        public static final double kaVoltSecondsSquaredPerMeter = 0.28479; // cims 1.0142;
        public static final double kPDriveVelLeft = 10.0;
        public static final double kPDriveVelRight = kPDriveVelLeft;
        public static final double kTrackWidthMeters = 0.6604; // cims 1.0;
        public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackWidthMeters);
    }

    public static class Auto {
        // part of trajectory but numbers are from example
        public static final double kStdSpeedMetersPerSecond = 3.8;
        public static final double kStdAccelerationMetersPerSecondSquared = 3.0;
        public static final double kSlowSpeedMetersPerSecond = 2.3; // 1.5
        public static final double kSlowAccelerationMetersPerSecondSquared = 3.5;
        public static final double kMediumAccelerationMetersPerSecondSquared = 3.5;
        public static final double kMediumSpeedMetersPerSecond = 2.3;
        public static final double kFastSpeedMetersPerSecond = 4.0;
        public static final double kFastAccelerationMetersPerSecondSquared = 4.0;
        public static final double kRamseteB = 2;
        public static final double kRamseteZeta = 0.7;
    }

    public static class Shooter {
        public static final double kP = 0.1; // 0.2
        public static final double kI = 0;
        public static final double kD = 0;
        public static final double kF = 0.055; // 0.065

//        public static final double RPM_LOW = 7500; // Currently only used in LimelightAutoAim
//        public static final double RPM_HIGH = 10000; // Currently only used in LimelightAutoAim
        public static final double RPM_FENDER_LOWER_HUB = 3500; // no flywheel 3000;
        public static final double RPM_FENDER_UPPER_HUB = 6800; // no flywheel 5900; // 5800 not good with ball in way
        public static final double RPM_FENDER_LOWER_HUB_BLOCKED = 5000; // no flywheel 4000;
        public static final double RPM_RING_UPPER_HUB = 7500; // no flywheel 6500;
        public static final double RPM_AUTO_UPPER_HUB = RPM_RING_UPPER_HUB + 300;
        public static final double RPM_LAUNCHPAD = 8000;
        public static final double RPM_FAR_COURT = 10800;
        public static final double RPM_AUTO_RIGHT_RING = 7300; // Used for auto ring shot on right side

        public static final double FLYWHEEL_TOLERANCE = 150;

        // Limelight
        public static final double klP = 1;

        public static final double LIMELIGHT_ANGLE = 30.0;
        public static final double LIMELIGHT_HEIGHT = 36.0; // 36.5
        public static final double UPPER_HUB_HEIGHT = 96.0;

        public static final double MAX_PIVOT_VOLTS = 4.0; // 3.3;
    }

    public static class Indexer {
//        public static final double kPBelt = 0;
//        public static final double kIBelt = 0;
//        public static final double kDBelt = 0;
//        public static final double kFFBelt = 0;
//        public static final double kIZoneBelt = 0;
    }

    public static class Climber {
        // Elevator
        public static final double P = 0.8184;
        public static final double I = 0.0;
        public static final double D = 0.0;
        public static final double F = 0.006; //0.425

        public static final double MAX_POSITION = 24400; //24000;
        public static final double ROBOT_UP_POSITION = 125;
        public static final double ROBOT_DOWN_TO_RELEASE_POSITION = 18000;
        public static final double RELEASE_FANGS_POSITION = 3000;

        public static final double UP_UNLOADED_VOLTAGE = 1.0;
        public static final double DOWN_LOADED_VOLTAGE = -1.0;
        public static final double HOLD_LOADED_VOLTAGE = -0.2;
        public static final double LOADED_DROP_VOLTAGE = 0.25;
    }
}
