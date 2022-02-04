package team.gif.robot.util;

public class util
{

    public static double limit(double value)
    {
        if(value <= -1.0)
            return -1.0;
        else if(value >= 1.0)
            return 1.0;
        else
            return value;
    }

    /**
     * Treat values within specified deadband as zero.
     */
    public static double applyDeadband(double value, double deadband)
    {
        if(Math.abs(value) < deadband)
            return 0.0;
        else
            return value;
    }


}
