// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import team.gif.lib.autoMode;
import team.gif.robot.commands.autos.FiveBallTerminalRight;
import team.gif.robot.commands.autos.FourBallTerminalRight;
import team.gif.robot.commands.autos.Mobility;
import team.gif.robot.commands.autos.ThreeBallTerminalMiddle;
import team.gif.robot.commands.autos.ThreeBallTerminalRight;
import team.gif.robot.commands.autos.TwoBallLeft;
import team.gif.robot.commands.autos.TwoBallLeftOpp2Ball;
import team.gif.robot.commands.autos.TwoBallMiddle;
import team.gif.robot.commands.autos.TwoBallRight;

import java.util.HashMap;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    private final HashMap<autoMode, Command> autoCommands = new HashMap<>();

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        // Configure the button bindings
        configureButtonBindings();
        buildAutoCommands();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
    }

    private void buildAutoCommands() {
        autoCommands.put(autoMode.MOBILITY, new Mobility());
        autoCommands.put(autoMode.TWO_BALL_LEFT, new TwoBallLeft());
        autoCommands.put(autoMode.TWO_BALL_LEFT_OPP_2_BALL,new TwoBallLeftOpp2Ball());
        autoCommands.put(autoMode.TWO_BALL_RIGHT, new TwoBallRight());
        autoCommands.put(autoMode.TWO_BALL_MIDDLE, new TwoBallMiddle());
        autoCommands.put(autoMode.THREE_BALL_TERMINAL_MIDDLE, new ThreeBallTerminalMiddle());
        autoCommands.put(autoMode.THREE_BALL_TERMINAL_RIGHT, new ThreeBallTerminalRight());
        autoCommands.put(autoMode.FOUR_BALL_TERMINAL_RIGHT, new FourBallTerminalRight());
        autoCommands.put(autoMode.FIVE_BALL_TERMINAL_RIGHT, new FiveBallTerminalRight());
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand(autoMode chosenAuto) {
        Command autonomousCommand = autoCommands.get(chosenAuto);

        if (chosenAuto == null) {
            System.out.println("Autonomous selection is null. Robot will do nothing in auto :(");
        }

        return autonomousCommand;
    }
}
