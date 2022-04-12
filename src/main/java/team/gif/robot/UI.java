package team.gif.robot;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import team.gif.lib.autoMode;
import team.gif.lib.delay;
import team.gif.robot.commands.climber.ResetClimber;
import team.gif.robot.commands.drivetrain.ResetHeading;
import team.gif.robot.commands.limelight.LimelightLED;
import team.gif.robot.subsystems.drivers.Pigeon;

import java.util.Map;

import static team.gif.robot.Robot.*;

public class UI {
	
	public static SendableChooser<autoMode> autoModeChooser = new SendableChooser<>();
	public static SendableChooser<delay> delayChooser = new SendableChooser<>();
	/*
	 * All the shuffleboard entry
	 */
	public UI() {
		
		// Creating a new tab in shuffleboard.
		ShuffleboardTab shuffleboardTab = Shuffleboard.getTab("FRC2022");
		ShuffleboardLayout shuffleboardLayoutSensor = shuffleboardTab
			.getLayout("Sensors", BuiltInLayouts.kGrid)
			.withPosition(6, 0)
			.withSize(1, 3)
			.withProperties(Map.of("Label", "HIDDEN"));

    /*    public static ShuffleboardLayout shuffleboardLayoutHeading = shuffleboardTab
        .getLayout("BotHeading", BuiltInLayouts.kGrid)
        .withSize(2,3)
        .withProperties(Map.of("Label", "HIDDEN"));
*/
		// Indexer and Indexer Sensors
		shuffleboardLayoutSensor.addBoolean("Belt Sensor", indexer::getSensorBelt);
		shuffleboardLayoutSensor.addBoolean("Mid Sensor", indexer::getSensorMid);
		shuffleboardLayoutSensor.addBoolean("Entry Sensor", indexer::getSensorEntry);
		shuffleboardTab.addBoolean("Enable Indexer", () -> Globals.indexerEnabled)
			.withPosition(7, 0)
			.withSize(1, 1);
		;
		
		// Shooter
		shuffleboardTab.addNumber("Shooter Speed", shooter::getSpeed)
			.withPosition(7, 1);
		
		// Climber
		shuffleboardTab.addString("Climber Position", () -> climber.getPosition_Shuffleboard())
			.withPosition(8, 0);
		shuffleboardTab.add("Climber", new ResetClimber())
			.withPosition(8, 1)
			.withSize(1, 1);
		
		// Limelight Toggle
		shuffleboardTab.add("LED", new LimelightLED())
			.withPosition(9, 0)
			.withSize(1, 1);

//		shuffleboardTab.addCamera("shooterLL","shooterLL","mjpg:http://10.23.38.95:5800")
//            .withPosition(0,0)
//            .withSize(4,4);
//		shuffleboardTab.addCamera("collectorLL","collectorLL","mjpg:https://10.23.38.12:5800")
//			.withPosition(5,0)
//			.withSize(4,4);
		// Pigeon
		shuffleboardTab.add("Heading", (x) -> {
				x.setSmartDashboardType("Gyro");
				x.addDoubleProperty("Value", () -> Pigeon.getInstance().getCompassHeading(), null);
			})
			.withPosition(4, 0)
			.withSize(2, 2);
		shuffleboardTab.add("Reset", new ResetHeading())
			.withPosition(4, 2)
			.withSize(2, 1);
		
		// Auto selections
		autoModeChooser.addOption("Mobility", autoMode.MOBILITY);
		autoModeChooser.setDefaultOption("Two Ball Left", autoMode.TWO_BALL_LEFT);
		autoModeChooser.addOption("Two Ball Middle", autoMode.TWO_BALL_MIDDLE);
		autoModeChooser.addOption("Two Ball Right", autoMode.TWO_BALL_RIGHT);
		autoModeChooser.addOption("Three+ Ball Terminal Middle", autoMode.THREE_BALL_TERMINAL_MIDDLE);
		autoModeChooser.addOption("Three+ Ball Terminal Right", autoMode.THREE_BALL_TERMINAL_RIGHT);
		autoModeChooser.addOption("Four Ball Terminal Right", autoMode.FOUR_BALL_TERMINAL_RIGHT);
		autoModeChooser.addOption("Four+ Ball Terminal Right", autoMode.FIVE_BALL_TERMINAL_RIGHT);
		
		shuffleboardTab.add("Auto Select", autoModeChooser)
			.withWidget(BuiltInWidgets.kComboBoxChooser)
			.withPosition(8, 2)
			.withSize(2, 1);
		
		//Auto delay
		delayChooser.setDefaultOption("0", delay.DELAY_0);
		delayChooser.addOption("1", delay.DELAY_1);
		delayChooser.addOption("2", delay.DELAY_2);
		delayChooser.addOption("3", delay.DELAY_3);
		delayChooser.addOption("4", delay.DELAY_4);
		delayChooser.addOption("5", delay.DELAY_5);
		delayChooser.addOption("6", delay.DELAY_6);
		delayChooser.addOption("7", delay.DELAY_7);
		delayChooser.addOption("8", delay.DELAY_8);
		delayChooser.addOption("9", delay.DELAY_9);
		delayChooser.addOption("10", delay.DELAY_10);
		delayChooser.addOption("11", delay.DELAY_11);
		delayChooser.addOption("12", delay.DELAY_12);
		delayChooser.addOption("13", delay.DELAY_13);
		delayChooser.addOption("14", delay.DELAY_14);
		delayChooser.addOption("15", delay.DELAY_15);
		
		shuffleboardTab.add("Delay", delayChooser)
			.withPosition(7, 2)
			.withSize(1, 1);
	}
}
