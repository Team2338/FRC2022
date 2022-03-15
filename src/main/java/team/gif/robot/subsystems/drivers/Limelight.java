package team.gif.robot.subsystems.drivers;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import team.gif.lib.LimelightLedMode;

public class Limelight {

    private final NetworkTable table;

    /**
     * Create a new limelight object.
     *
     * @param key NetworkTable key specified in limelight web config
     */
    public Limelight(String key) {
        System.out.println("limelight init");
        table = NetworkTableInstance.getDefault().getTable(key);
        setLEDMode(LimelightLedMode.ON);
    }

    /**
     * Create a new limelight object with default key.
     */
    public Limelight() {
        this("limelight");
    }

    /**
     * Sets the mode of the limelight's LED array.
     * mode 0 uses LED mode in current pipeline (see {@link this#setPipeline(int)}
     * mode 1 is 'force off'
     * mode 2 is 'force blink'
     * mode 3 is 'force on'
     *
     * @param mode desired LED mode
     */
    public void setLEDMode(LimelightLedMode mode) {
        table.getEntry("ledMode").setNumber(mode.getValue());
    }

    /**
     * Sets the limelight's mode of operation.
     * mode 0 activates vision processing (decreased exposure)
     * mode 1 activates driver vision (increased exposure, no processing)
     *
     * @param mode desired camera mode
     */
    public void setCamMode(int mode) {
        if (mode >= 0 && mode <= 1) {
            table.getEntry("camMode").setNumber(mode);
            System.out.println("cammode reset " + mode);
        }
    }

    /**
     * Sets the limelight's active vision pipeline.
     * The limelight stores up to 10 pipelines indexed 0-9 (These can be configured through the Web UI).
     *
     * @param pipeline desired vision pipeline
     */
    public void setPipeline(int pipeline) {
        if (pipeline >= 0 && pipeline <= 9) {
            table.getEntry("pipeline").setNumber(pipeline);

        }
    }

    /**
     * Sets the limelight's streaming mode.
     * mode 0 is "Standard - Side-by-side streams if a webcam is attached to Limelight"
     * mode 1 is "PiP Main - The secondary camera stream is placed in the lower-right corner of the primary camera stream"
     * mode 2 is "PiP Secondary - The primary camera stream is placed in the lower-right corner of the secondary camera stream"
     *
     * @param mode desired streaming mode
     */
    public void setStreamMode(int mode) {
        if (mode >= 0 && mode <= 2) {
            table.getEntry("stream").setNumber(mode);
        }
    }

    /**
     * Sets the limelight's snapshot mode. The limelight allows for taking pictures throughout a match.
     * mode 0 stops taking snapshots
     * mode 1 takes two snapshots per second.
     *
     * @param mode desired snapshot mode
     */
    public void setSnapshotMode(int mode) {
        if (mode >= 0 && mode <= 1) {
            table.getEntry("snapshot").setNumber(mode);
        }
    }

    /**
     * Returns whether or not the limelight has any valid targets.
     *
     * @return true if has target, false if not
     */
    public boolean hasTarget() {
        return (double) (table.getEntry("tv").getNumber(0)) == 1;
    }

    /**
     * Returns the horizontal offset from crosshair to target.
     *
     * @return offset in degrees (-29.8 to +29.8)
     */
    public double getXOffset() {
        return table.getEntry("tx").getDouble(0.0);
    }

    /**
     * Returns the vertical offset from crosshair to target.
     *
     * @return offset in degrees (-24.85 to +24.85)
     */

    public double getYOffset() {
        return table.getEntry("ty").getDouble(0.0);
    }

    /**
     * Returns the percentage of screen area that the target fills.
     *
     * @return percentage of image (0 to 100)
     */
    public double getArea() {
        return table.getEntry("ta").getDouble(0.0);
    }

    /**
     * Returns the skew or rotation of the target.
     *
     * @return rotation in degrees (-90 to 0)
     */
    public double getSkew() {
        return table.getEntry("ts").getDouble(0.0);
    }

    /**
     * Returns the pipeline's latency contribution. Add at least 11 ms for image capture latency.
     *
     * @return latency in ms
     */
    public double getLatency() {
        return table.getEntry("tl").getDouble(0.0);
    }

    /**
     * Returns sidelength of the shortest side of the fitted bounding box.
     *
     * @return sidelength in pixels
     */
    public int getShortLength() {
        return table.getEntry("tshort").getNumber(0).intValue();
    }

    /**
     * Returns sidelength of the longest side of the fitted bounding box.
     *
     * @return sidelength in pixels
     */
    public int getLongLength() {
        return table.getEntry("tlong").getNumber(0).intValue();
    }

    /**
     * Returns horizontal sidelength of the rough bounding box.
     *
     * @return sidelength in pixels (0 to 320)
     */
    public int getHorizLength() {
        return table.getEntry("thoriz").getNumber(0).intValue();
    }

    /**
     * Returns vertical sidelength of the rough bounding box.
     *
     * @return sidelength in pixels (0 to 240)
     */
    public int getVertLength() {
        return table.getEntry("tvert").getNumber(0).intValue();
    }

    /**
     * Gets the pose of the camera relative to the target.
     *
     * @return x[0], y[1], z[2], pitch[3], yaw[4], roll[5]
     */
    public double[] getCamTran() {
        return table.getEntry("camtran").getDoubleArray(new double[] { 0, 0, 0, 0, 0, 0 });
    }

    /**
     * Returns a data value from 1 of 3 raw (ungrouped) contours given a valid key.
     * <p>
     * "tx[num]" : x position in normalized screenspace (-1 to +1)
     * "ty[num]" : y position in normalized screenspace (-1 to +1)
     * "ta[num]" : area (0 to 100)
     * "ts[num]" : skew (-90 to 0 degrees)
     * <p>
     * Can also be used to retrieve a data value from 1 of 2 crosshairs.
     * "cx[num]" : x position in normalized screenspace (-1 to +1)
     * "cy[num]" : y position in normalized screenspace (-1 to +1)
     *
     * @param key String key for desired data
     * @return raw data value (units vary)
     */
    public double getCustomValue(String key) {
        return table.getEntry(key).getDouble(0.0);
    }

    /**
     * Returns array of x values of each pixel coordinates*
     * the x valuse of the 4 corners in what order sadly i dont know this must be tested
     */
    public double[] getTcornx() {
        return table.getEntry("tcornx").getDoubleArray(new double[] { 0, 0, 0, 0 });
    }

    /**
     * Returns array of x values of each pixel coordinates*
     * the y valuse of the 4 corners in what order sadly i dont know this must be tested
     */
    public double[] getTcorny() {
        return table.getEntry("tcorny").getDoubleArray(new double[] { 0, 0, 0, 0 });
    }


}
