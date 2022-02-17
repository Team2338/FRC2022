package team.gif.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.subsystems.Intake;

public class IntakeUp extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    private final Intake intake = Intake.getInstance();

    public IntakeUp() {
        //addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.setSolenoids(false, false, true);
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() { return false; }
}
