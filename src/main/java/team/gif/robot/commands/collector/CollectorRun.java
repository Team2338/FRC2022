package team.gif.robot.commands.collector;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

public class CollectorRun extends CommandBase {
 
	public CollectorRun() {
		super();
		addRequirements(Robot.intake);
	}
	
	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
	}
	
	// Called every time the scheduler runs while the command is scheduled.
	public void execute() {
//        if ((Robot.indexer.getSensorCollector() && Robot.indexer.getSensorWheel()) || (Robot.indexer.getSensorWheel() && Robot.indexer.getSensorBelt())) {
//            Robot.intake.setSpeedPercent(0);
//            System.out.println("Robot Full");
//        } else {
//            Robot.intake.setSpeedPercent(0.75);
//        }
		Robot.intake.setSpeedPercent(0.6);
		Robot.indexer.setIndexMotorSpeed(0.8);
		Robot.indexer.setBeltMotorSpeed(1);
	}
	
	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
	
	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		Robot.intake.setSpeedPercent(0);
	}
}








