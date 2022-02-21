package team.gif.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import team.gif.lib.AxisButton;
import edu.wpi.first.wpilibj.GenericHID;
import team.gif.robot.commands.Hood.HoodDown;
import team.gif.robot.commands.Hood.HoodUp;
import team.gif.robot.commands.autoaim.LimelightAutoAim;
import team.gif.robot.commands.climber.ClimberMax;
import team.gif.robot.commands.climber.ClimberManualControl;
import team.gif.robot.commands.climber.Lower4Inches;
import team.gif.robot.commands.climber.LowerClimber;
import team.gif.robot.commands.collector.CollectorDown;
import team.gif.robot.commands.indexer.ReverseIndex;
import team.gif.robot.commands.indexer.ToggleIndexer;
import team.gif.robot.commands.shooter.RapidFire;
import team.gif.robot.commands.shooter.RevFlywheel;
import team.gif.robot.commands.shooter.Shoot;
import team.gif.robot.commands.collector.CollectorReverse;
import team.gif.robot.commands.collector.CollectorRun;
import team.gif.robot.commands.shooter.ShootShort;


public class OI {
    private static OI instance = null;

    /*
     * TODO: Instantiate all joysticks/controllers and their buttons here
     *
     * Examples:
     * public final Joystick leftStick = new Joystick(0);
     * public final XboxController driver = new XboxController(0);
     *
     * private final JoystickButton leftTrigger = new JoystickButton(leftStick, 0);
     */

    public final XboxController driver = new XboxController(RobotMap.DRIVER_CONTROLLER_ID);
    public final XboxController aux = new XboxController(RobotMap.AUX_CONTROLLER_ID);
    public final XboxController test = new XboxController(RobotMap.TEST_CONTROLLER_ID);

    public final JoystickButton dA = new JoystickButton(driver, 1);
    public final JoystickButton dB = new JoystickButton(driver, 2);
    public final JoystickButton dX = new JoystickButton(driver, 3);
    public final JoystickButton dY = new JoystickButton(driver, 4);
    public final JoystickButton dLBump = new JoystickButton(driver, 5);
    public final JoystickButton dRBump = new JoystickButton(driver, 6);
    public final JoystickButton dBack = new JoystickButton(driver, 7);
    public final JoystickButton dStart = new JoystickButton(driver, 8);
    public final JoystickButton dLStickBtn = new JoystickButton(driver, 9);
    public final JoystickButton dRStickBtn = new JoystickButton(driver, 10);
    public final AxisButton dRTrigger = new AxisButton(driver,3,.05);
    public final AxisButton dLTrigger = new AxisButton(driver,2,.05);

    public final POVButton dDPadUp = new POVButton(driver, 0);
    public final POVButton dDPadRight = new POVButton(driver, 90);
    public final POVButton dDPadDown = new POVButton(driver, 180);
    public final POVButton dDPadLeft = new POVButton(driver, 270);

    public final JoystickButton aA = new JoystickButton(aux, 1);
    public final JoystickButton aB = new JoystickButton(aux, 2);
    public final JoystickButton aX = new JoystickButton(aux, 3);
    public final JoystickButton aY = new JoystickButton(aux, 4);
    public final JoystickButton aLBump = new JoystickButton(aux, 5);
    public final JoystickButton aRBump = new JoystickButton(aux, 6);
    public final JoystickButton aBack = new JoystickButton(aux, 7);
    public final JoystickButton aStart = new JoystickButton(aux, 8);
    public final JoystickButton aLStickBtn = new JoystickButton(aux, 9);
    public final JoystickButton aRStickBtn = new JoystickButton(aux, 10);
    public final AxisButton aRTrigger = new AxisButton(aux,3,.05);
    public final AxisButton aLTrigger = new AxisButton(aux,2,.05);
    public final POVButton aDPadUp = new POVButton(aux, 0);
    public final POVButton aDPadRight = new POVButton(aux, 90);
    public final POVButton aDPadDown = new POVButton(aux, 180);
    public final POVButton aDPadLeft = new POVButton(aux, 270);

    public final JoystickButton tA = new JoystickButton(test, 1);
    public final JoystickButton tB = new JoystickButton(test, 2);
    public final JoystickButton tX = new JoystickButton(test, 3);
    public final JoystickButton tY = new JoystickButton(test, 4);
    public final JoystickButton tLBump = new JoystickButton(test, 5);
    public final JoystickButton tRBump = new JoystickButton(test, 6);
    public final JoystickButton tBack = new JoystickButton(test, 7);
    public final JoystickButton tStart = new JoystickButton(test, 8);
    public final JoystickButton tLStickBtn = new JoystickButton(test, 9);
    public final JoystickButton tRStickBtn = new JoystickButton(test, 10);
    public final AxisButton tRTrigger = new AxisButton(test,3,.05);
    public final AxisButton tLTrigger = new AxisButton(test,2,.05);
    public final POVButton tDPadUp = new POVButton(test, 0);
    public final POVButton tDPadRight = new POVButton(test, 90);
    public final POVButton tDPadDown = new POVButton(test, 180);
    public final POVButton tDPadLeft = new POVButton(test, 270);

    public OI() {
        /*
         * TODO: Define what each button does
         *
         * Examples:
         * leftTrigger.whenPressed(new CollectCommand());
         * rightTrigger.whileHeld(new EjectCommand());
         *
         */

        // Driver Controls
//        dLT.whileHeld(new Pivot());
        dLBump.whenHeld(new CollectorReverse());
        dRBump.whenHeld(new CollectorRun());
        dRBump.whenPressed(new CollectorDown()); //TODO: CREATE SOLENOID CLASS
        dLTrigger.whileHeld(new LimelightAutoAim());
        dRTrigger.whileHeld(new RapidFire());

        dA.whenHeld(new Shoot());
        dX.whenPressed(new HoodDown());
        dB.whenHeld(new ShootShort());
        dB.whenPressed(new HoodUp());
        dY.whenPressed(new HoodUp());
        dStart.whenHeld(new ReverseIndex());
        dLStickBtn.toggleWhenPressed(new ToggleIndexer());
        dBack.whenHeld(new RevFlywheel());

        aLBump.whenHeld(new RevFlywheel());
        aRBump.whileHeld(new RapidFire());
        aLTrigger.whileHeld(new LimelightAutoAim());

        aY.toggleWhenPressed(new ClimberManualControl());
        aDPadDown.whenPressed(new ClimberMax());
        aDPadLeft.whenPressed(new LowerClimber());
        aDPadUp.whenPressed(new Lower4Inches());
    }

    public void setRumble(boolean rumble) {
        driver.setRumble(GenericHID.RumbleType.kLeftRumble, rumble ? 1.0 : 0.0);
        driver.setRumble(GenericHID.RumbleType.kRightRumble, rumble ? 1.0: 0.0);
        aux.setRumble(GenericHID.RumbleType.kLeftRumble, rumble ? 1.0 : 0.0);
        aux.setRumble(GenericHID.RumbleType.kRightRumble, rumble ? 1.0: 0.0);
    }
}