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

    private Car car;
    private Publisher mapStatePub;
    private Subscriber controlsSub;
    // Vehicle state in map reference frame
    private Controls controls;
    private MapState mapState;
    private Path pathPoints;
    private Vector2D[] predictedPathPoints;
    private final float pxPerMeter;


    public Controller(double dt) {
        pxPerMeter = 57.23f;
        setCar(new Car("res/car.properties", "res/tyre.properties",
                "res/tyre.properties", dt));
        setMapStatePub(new Publisher());
        setControlsSub(new Subscriber());
        setMapState(new MapState(0, 0,  Simulation.initialMapX,
                Simulation.initialMapY, pxPerMeter));
        setPathPoints(new Path("res/path.csv"));

    }

    public void update(){

        getMapStatePub().publishMessage(getMapState());
        ObjectMessage msg = getControlsSub().getMessage();
        if(msg != null){
            try {
                setControls((Controls) msg.getObject());
                setPredictedPathPoints(getControls().getPredictedPath());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

        getCar().updateModel(getControls());
        getMapState().setX(Simulation.initialMapX + getCar().getFixedX()* getPxPerMeter());
        getMapState().setY(Simulation.initialMapY - getCar().getFixedY()* getPxPerMeter());
        getMapState().setPsi(getCar().getYawAngle());
        getMapState().setV(getCar().getVelocity());

    }

    public float getCarRotation(){
        return (float)(180.0 * getMapState().getPsi()/Math.PI);
    }

    public float getMapX(){
        return (float)(ViewManager.width/2.0 - getMapState().getX());
    }

    public float getMapY(){ return (float)(ViewManager.height/2.0 - getMapState().getY()); }

    public float getCarRotationCenterX(int width) {
        return (float) (width * getCar().getCenterOfRotationX());
    }

    public float getCarRotationCenterY(int height) {
        return (float) (height * getCar().getCenterOfRotationY());
    }

    public double[] getPathPoint(int i){
        double[] tmp = getPathPoints().getPathPoint(i);
        tmp[0] = ViewManager.width/2.0 - getMapState().getX() + tmp[0];
        tmp[1] = ViewManager.height/2.0 - getMapState().getY() + tmp[1];
        return tmp;
    }

    public int getPathPointsSize(){
        return getPathPoints().getPathPointsSize();
    }

    public Vector2D[] getPredictedPathPoints(){
        if(predictedPathPoints != null) {
            Vector2D[] res = new Vector2D[predictedPathPoints.length];
            for(int i = 0; i < predictedPathPoints.length; i++){
                double x = predictedPathPoints[i].getX(), y = predictedPathPoints[i].getY();
                x = -x * getPxPerMeter();
                y = y * getPxPerMeter();

                x = Math.cos(getCar().getYawAngle()) * x - Math.sin(getCar().getYawAngle()) * y;
                y = Math.cos(getCar().getYawAngle()) * y + Math.sin(getCar().getYawAngle()) * x;

                x = x + ViewManager.width/2.0;
                y = y + ViewManager.height/2.0;

                res[i] = new Vector2D(x, y);
            }
            return res;
        }
        else return null;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Publisher getMapStatePub() {
        return mapStatePub;
    }

    public void setMapStatePub(Publisher mapStatePub) {
        this.mapStatePub = mapStatePub;
    }

    public Subscriber getControlsSub() {
        return controlsSub;
    }

    public void setControlsSub(Subscriber controlsSub) {
        this.controlsSub = controlsSub;
    }

    public Controls getControls() {
        return controls;
    }

    public void setControls(Controls controls) {
        this.controls = controls;
    }

    public MapState getMapState() {
        return mapState;
    }

    public void setMapState(MapState mapState) {
        this.mapState = mapState;
    }

    public Path getPathPoints() {
        return pathPoints;
    }

    public void setPathPoints(Path pathPoints) {
        this.pathPoints = pathPoints;
    }

    public void setPredictedPathPoints(Vector2D[] predictedPathPoints) {
        this.predictedPathPoints = predictedPathPoints;
    }

    public float getPxPerMeter() {
        return pxPerMeter;
    }
}
