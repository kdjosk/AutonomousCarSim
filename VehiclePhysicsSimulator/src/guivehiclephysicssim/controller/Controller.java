package guivehiclephysicssim.controller;

import autonomous.nav.Path;
import guivehiclephysicssim.comms.Publisher;
import guivehiclephysicssim.comms.Subscriber;
import guivehiclephysicssim.model.Car;
import guivehiclephysicssim.nav.Controls;
import guivehiclephysicssim.nav.MapState;
import guivehiclephysicssim.view.Simulation;
import guivehiclephysicssim.view.ViewManager;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

public class Controller {

    Car car;
    Publisher mapStatePub;
    Subscriber controlsSub;
    // Vehicle state in map reference frame
    Controls controls;
    MapState mapState;
    Path pathPoints;
    Vector2D[] predictedPathPoints;

    final float pxPerMeter;

    {
        // Comes from irl car length and image dimensions
        // Probably shouldn't be hardcoded
        pxPerMeter = 57.23f;
    }

    public Controller(double dt) {
        car = new Car("res/car.properties", "res/tyre.properties",
                "res/tyre.properties", dt);
        mapStatePub = new Publisher();
        controlsSub = new Subscriber();
        mapState = new MapState(0, 0,  Simulation.initialMapX,
                Simulation.initialMapY, pxPerMeter);
        pathPoints = new Path("res/path.csv");

    }

    public void update(){

        mapStatePub.publishMessage(mapState);
        ObjectMessage msg = controlsSub.getMessage();
        if(msg != null){
            try {
                controls = (Controls) msg.getObject();
                predictedPathPoints = controls.getPredictedPath();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

        car.updateModel(controls);
        mapState.setX(Simulation.initialMapX + car.fixedX*pxPerMeter);
        mapState.setY(Simulation.initialMapY - car.fixedY*pxPerMeter);
        mapState.setPsi(car.yawAngle);
        mapState.setV(car.getVelocity());

        mapState.printInfo();

    }

    public float getCarRotation(){
        return (float)(180.0 * mapState.getPsi()/Math.PI);
    }

    public float getMapX(){
        return (float)(ViewManager.width/2.0 - mapState.getX());
    }

    public float getMapY(){ return (float)(ViewManager.height/2.0 - mapState.getY()); }

    public float getCarRotationCenterX(int width) {
        return (float) (width * car.getCenterOfRotationX());
    }

    public float getCarRotationCenterY(int height) {
        return (float) (height * car.getCenterOfRotationY());
    }

    public double[] getPathPoint(int i){
        double[] tmp = pathPoints.getPathPoint(i);
        tmp[0] = ViewManager.width/2.0 - mapState.getX() + tmp[0];
        tmp[1] = ViewManager.height/2.0 - mapState.getY() + tmp[1];
        return tmp;
    }

    public int getPathPointsSize(){
        return pathPoints.getPathPointsSize();
    }

    public Vector2D[] getPredictedPathPoints(){
        if(predictedPathPoints != null) {
            Vector2D[] res = new Vector2D[predictedPathPoints.length];
            for(int i = 0; i < predictedPathPoints.length; i++){
                double x = predictedPathPoints[i].getX(), y = predictedPathPoints[i].getY();
                x = -x * pxPerMeter;
                y = y * pxPerMeter;

                x = Math.cos(car.yawAngle) * x - Math.sin(car.yawAngle) * y;
                y = Math.cos(car.yawAngle) * y + Math.sin(car.yawAngle) * x;

                x = x + ViewManager.width/2.0;
                y = y + ViewManager.height/2.0;

                res[i] = new Vector2D(x, y);
            }
            return res;
        }
        else return null;
    }
}
