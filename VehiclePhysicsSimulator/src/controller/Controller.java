package controller;

import comms.Publisher;
import comms.Subscriber;
import model.Car;
import nav.Controls;
import nav.MapState;
import view.Simulation;
import view.ViewManager;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.swing.text.View;

public class Controller {

    Car car;
    Publisher mapStatePub;
    Subscriber controlsSub;
    // Vehicle state in map reference frame
    Controls controls;
    MapState mapState;
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
        mapState = new MapState(0, 0, ViewManager.width / 2.0 - Simulation.initialMapX,
                ViewManager.height / 2.0 - Simulation.initialMapY);
        controls = null;
    }

    public void update(){

        mapStatePub.publishMessage(mapState);
        ObjectMessage msg = controlsSub.getMessage();
        if(msg != null){
            try {
                controls = (Controls) msg.getObject();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

        car.updateModel(controls);
        mapState.setX(car.fixedX);
        mapState.setY(car.fixedY);
        mapState.setPsi(car.yawAngle);
        mapState.setV(car.getVelocity());

    }

    public float getCarRotation(){
        return (float)(180.0 * mapState.getPsi()/Math.PI);
    }

    public float getMapX(){
        return (float)(ViewManager.width/2.0 - Simulation.initialMapX + mapState.getX());
    }

    public float getMapY(){
        return (float)(ViewManager.height/2.0 - Simulation.initialMapY + mapState.getY());
    }

    public float getMapRotation(){
        return (float)(180.0 * mapState.getPsi()/Math.PI);
    }

    public float getCarRotationCenterX(int width) {
        return (float) (width * car.getCenterOfRotationX());
    }

    public float getCarRotationCenterY(int height) {
        return (float) (height * car.getCenterOfRotationY());
    }
}
