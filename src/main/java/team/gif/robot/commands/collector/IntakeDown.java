package team.gif.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.subsystems.Intake;

public class IntakeDown extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    private final Intake intake = Intake.getInstance();

    public IntakeDown() {
        //addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.setSolenoids(true, true, false);
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() { return true; }
}
