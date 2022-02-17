package team.gif.robot.commands.climber;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

public class HangerManualControl extends CommandBase {

    //@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    public static final double ClimberupGain = 1;
    public static final double ClimberdownGain = 1;

    public HangerManualControl() {
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        SmartDashboard.putBoolean("Hang Control", true);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double speed = -Robot.oi.aux.getLeftY();

        if ( speed > -0.05 && speed < 0.05) {
            speed = 0;
        }

        if (!Globals.climberMotorEnabled) {
            // do not allow the elevator to go below the lowest point
            // overrides if the Aux A button is pressed
            // This prevents the elevator for overrunning in normal condition
            // but allows us to reset the 0 position when the robot is turned on
            // and the elevator is not in the starting position
            if (Robot.oi.aux.getAButton()) {
                Robot.climber.enableLowerSoftLimit(false);
            } else {
                Robot.climber.enableLowerSoftLimit(true);
            }

            // run the elevator either up or down
            Robot.climber.setSpeed(speed);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.climber.setSpeed(0);
        SmartDashboard.putBoolean("Hang Control", false);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
