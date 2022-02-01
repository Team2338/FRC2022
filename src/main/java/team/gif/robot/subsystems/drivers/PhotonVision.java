package team.gif.robot.subsystems.drivers;

import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.util.Units;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.common.hardware.VisionLEDMode;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import java.util.List;

public class PhotonVision {
    private PhotonCamera photonCamera;
    private PhotonPipelineResult pipeResult;
    private PhotonTrackedTarget target;

    public PhotonVision() {
        photonCamera = new PhotonCamera("Gloworm");
        pipeResult = new PhotonPipelineResult();
        setLEDMode(0);
    }

    public PhotonPipelineResult updateLatest() {
        return photonCamera.getLatestResult();
    }

    public void takeOutputShot() {
        photonCamera.takeOutputSnapshot();
    }

    public void setCameraMode(boolean isDriverEnd) {
        photonCamera.setDriverMode(isDriverEnd);
    }

    public void setPipeline(int index) {
        photonCamera.setPipelineIndex(index);
    }

    public boolean isTargeted() {
        return pipeResult.hasTargets();
    }

    public List photonTargets() {
        return pipeResult.getTargets();
    }

    /*
    * Able to rank targets however we see fit; basics involve choosing by position:

    *Largest
    *Smallest
    *Highest (towards the top of the image)
    *Lowest
    *Rightmost (Best target on the right, worst on left)
    *Leftmost
    *Centermost

    * */
    public PhotonTrackedTarget bestTarget() {
        target = pipeResult.getBestTarget();
        return target;
    }

    public double[] getTargetInfo() {
        double[] j = new double[4];
        j[0] = target.getYaw();
        j[1] = target.getPitch();
        j[2] = target.getArea();
        j[3] = target.getSkew();
        return j;
    }

    public Transform2d getTargetPose() {
        return target.getCameraToTarget();
    }

    public List getTargetCorners() {
        return target.getCorners();
    }

    public void setLEDMode(int mode) {
        switch (mode) {
            case 1:
                photonCamera.setLED(VisionLEDMode.kBlink);
                break;
            case  2:
                photonCamera.setLED(VisionLEDMode.kOn);
                break;
            case 3:
                photonCamera.setLED(VisionLEDMode.kOff);
                break;
            default:
                photonCamera.setLED(VisionLEDMode.kDefault);
        }
    }

    public double getDistance(double camHeight, double tarHeight, double camPitch) {

        updateLatest();
        if(isTargeted()) {
            return PhotonUtils.calculateDistanceToTargetMeters(camHeight, tarHeight, camPitch, Units.degreesToRadians(bestTarget().getPitch()));
        } else {
            System.out.println("No Target");
            return -1;
        }
    }
}
