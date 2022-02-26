package team.gif.robot.commands.indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;

public class ToggleIndexer extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    public ToggleIndexer() {
    }

    /*
     * When used with toggleWhenPressed, the command runs
     * alternately between initialize() and end().
     *
     * This allows us to toggle the state of the field
     * between true and false.
     */

    @Override
    public void initialize() {
        Globals.indexerEnabled = false;
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Globals.indexerEnabled = true;
    }
}
