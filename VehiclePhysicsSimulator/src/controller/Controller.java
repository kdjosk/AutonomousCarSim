package controller;

import comms.Publisher;
import comms.Subscriber;
import model.Car;
import nav.Controls;

public class Controller {

    Car car;
    Publisher mapStatePub;
    Subscriber controlsSub;

    public Controller(double dt){
        car = new Car("res/car.properties", "res/tyre.properties",
                      "res/tyre.properties", dt);
        mapStatePub = new Publisher();
        controlsSub = new Subscriber();
    }

    public int getDisplayX(){
        return 1;
    }

    public int getDisplayY(){
        return 1;
    }

    public int getDisplayRotation(){
        return 90;
    }

}
