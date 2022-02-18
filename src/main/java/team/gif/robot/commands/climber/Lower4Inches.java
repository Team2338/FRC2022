/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package team.gif.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Robot;

/**
 * Describe the Command functionality here
 */
public class Lower4Inches extends CommandBase {


  // Called when the command is initially scheduled.
  public Lower4Inches() {
      super();
      addRequirements(Robot.climber);
  }
  @Override
  public void initialize() {
    //Use Robot weight to go down
      System.out.println("Drop Start");
      Robot.climber.setSpeed(Constants.Climber.LOADED_DROP_VOLTAGE);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      //uses this power to stay constant
      Robot.climber.setSpeed(Constants.Climber.LOADED_DROP_VOLTAGE);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (Robot.climber.getPosition() > Constants.Climber.FALL_POSITION);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
      System.out.println("Finished Drop");
  }
}
