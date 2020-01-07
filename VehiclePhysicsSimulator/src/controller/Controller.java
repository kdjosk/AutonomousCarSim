package controller;

import comms.Publisher;
import comms.Subscriber;
import model.Car;
import nav.Controls;
import nav.MapState;
import view.Simulation;
import view.ViewManager;

public class Controller {

    Car car;
    Publisher mapStatePub;
    Subscriber controlsSub;
    MapState mapState;
    final float pxPerMeter;

    {
        pxPerMeter = 57.23f;
    }

    public Controller(double dt){
        car = new Car("res/car.properties", "res/tyre.properties",
                      "res/tyre.properties", dt);
        mapStatePub = new Publisher();
        controlsSub = new Subscriber();
        mapState = new MapState(0,0, ViewManager.width/2.0 - Simulation.initialMapX,
                                ViewManager.height/2.0 - Simulation.initialMapY);
    }

    public void update(){
        mapStatePub.publishMessage(mapState);
        Controls controls = (Controls) controlsSub.getMessage();
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
        return (float)(Simulation.initialMapX + mapState.getX());
    }

    public float getMapY(){
        return (float)(Simulation.initialMapY + mapState.getY());
    }

    public float getMapRotation(){
        return (float)(180.0 * mapState.getPsi()/Math.PI);
    }

}
