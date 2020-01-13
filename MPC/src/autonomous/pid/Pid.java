package autonomous.pid;

import autonomous.comms.Publisher;
import autonomous.comms.Subscriber;
import autonomous.nav.Path;
import guivehiclephysicssim.nav.Controls;
import guivehiclephysicssim.nav.MapState;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Pid {

    private double vKP, vKD, vKI, dKP, dKD, dKI,
                   pathOffsetIntegral, velocityIntegral,
                   lastVelocityError, lastPathOffsetError,
                   maxSteeringAngle, maxVelocity, refVelocity;

    private int pathPoints;


    public Pid(String pidProperties){
        Properties p = new Properties();
        try(InputStream inputTyre = new FileInputStream(pidProperties)){
            p.load(inputTyre);
        } catch(IOException e) {
            e.printStackTrace();
        }

        // velocity control constants
        vKP = Double.parseDouble(p.getProperty("vKP"));
        vKD = Double.parseDouble(p.getProperty("vKD"));
        vKI = Double.parseDouble(p.getProperty("vKI"));
        // steering angle control constants
        dKP = Double.parseDouble(p.getProperty("dKP"));
        dKD = Double.parseDouble(p.getProperty("dKD"));
        dKI = Double.parseDouble(p.getProperty("dKI"));
        // parameters
        maxSteeringAngle = Double.parseDouble(p.getProperty("maxSteeringAngle"));
        maxVelocity = Double.parseDouble(p.getProperty("maxVelocity"));
        refVelocity = Double.parseDouble(p.getProperty("refVelocity"));
        pathPoints = Integer.parseInt(p.getProperty("pathPoints"));
        lastVelocityError = 0;
        lastPathOffsetError = 0;
    }

    public Controls getControls(double velocity, double[] pathCoeffs){

        // offset from path
        double y = pathCoeffs[0];

        double steeringAngle = dKP * y + dKD * (y - lastPathOffsetError) + dKI * pathOffsetIntegral;

        if(steeringAngle > maxSteeringAngle){
            steeringAngle = maxSteeringAngle;
        }
        else if(steeringAngle < -maxSteeringAngle){
            steeringAngle = -maxSteeringAngle;
        }

        double vErr = refVelocity - velocity;

        double acceleration = vKP * vErr + vKD * (vErr - lastVelocityError) + dKI * velocityIntegral;

        velocityIntegral += vErr;
        pathOffsetIntegral += y;

        Controls result = new Controls(steeringAngle, refVelocity, acceleration, null, null);
        return result;

    }

    public static void main(String[] args){
        String pidProperties = "res/pid.properties", pathFile = "res/path.csv";
        Pid pid = new Pid(pidProperties);
        Path path = new Path(pathFile);
        Publisher controlsPub = new Publisher();
        Subscriber mapStateSub = new Subscriber();
        MapState mapState;
        ObjectMessage msg;

        while(true){
            try{
                msg = mapStateSub.getMessage();
                if(msg != null){
                    mapState = (MapState) msg.getObject();
                    double[] pathCoeffs = path.getPathCoeffs(mapState.getX(), mapState.getY(),
                            mapState.getPsi(), pid.pathPoints);
                    pathCoeffs[2] *= mapState.getPxPerMeter();
                    pathCoeffs[0] /= mapState.getPxPerMeter();
                    Controls controls = pid.getControls(mapState.getV(), pathCoeffs);
                    controlsPub.publishMessage(controls);
                }
            }catch (JMSException e){e.printStackTrace();}
        }
    }

}
