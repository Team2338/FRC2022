package team.gif.lib;


import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.Button;

/**
 * A {@link Button} that gets its state from an axis of {@link GenericHID}.
 */
public class AxisButton extends Button {

    private final GenericHID joystick;
    private final int axisNumber;
    private final double threshold;

    /**
     * Create an axis button for triggering commands.
     *
     * Returns true when the axis passes a specified threshold.
     *
     * @param joystick {@link GenericHID} that has the axis (e.g. Joystick, KinectStick, etc)
     * @param axisNumber the axis number (see {@link GenericHID#getRawAxis(int)})
     * @param threshold the threshold to surpass
     */
    public AxisButton(GenericHID joystick, int axisNumber, double threshold) {
        this.joystick = joystick;
        this.axisNumber = axisNumber;
        this.threshold = threshold;
    }

    /**
     * Gets the boolean value of the axis button.
     * Returns true when surpassing the threshold, returns false otherwise
     *
     * @return the value of the axis button
     */
    @Override
    public boolean get() {
        if (threshold > 0.0) {
            return joystick.getRawAxis(axisNumber) > threshold;
        } else {
            return joystick.getRawAxis(axisNumber) < threshold;
        }
    }
}
