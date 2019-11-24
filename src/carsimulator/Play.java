package carsimulator;

import carsimulator.carmodel.Car;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Play extends BasicGameState{

    // Images
    Image carImage;
    float carX;
    float carY;
    double carPhi;

    Image track;
    float trackX;
    float trackY;

    float Xinitial = 1210;
    float Yinitial = 2744;

    Car carModel;

    public Play(int state){

    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{

        carModel = new Car("res/car.properties", "res/tyre.properties", "res/tyre.properties");
        carImage = new Image("res/Car.png");
        carX = 640 - carImage.getWidth()/2.0f;
        carY = 360 - carImage.getHeight()/2.0f;

        carImage.setCenterOfRotation(carImage.getWidth()/2.0f, carImage.getHeight()/2.0f);


        track = new Image("res/Track.png");
        trackX = 640 - Xinitial;
        trackY = 360 - Yinitial;
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
        g.drawImage(track, trackX, trackY);
        g.drawImage(carImage, carX, carY);
        g.drawString("Car rotation: " + Math.sin(carImage.getRotation() * Math.PI/180), 20, 20);
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
        Input input = gc.getInput();
        double torque = 0, steeringAngle = 0;
        if(input.isKeyDown(Input.KEY_UP)) {
            torque = 100;
        }
        if(input.isKeyDown(Input.KEY_DOWN)) {
            torque = -100;
        }
        if(input.isKeyDown(Input.KEY_LEFT)) {
             steeringAngle = -0.15;
        }
        if(input.isKeyDown(Input.KEY_RIGHT)) {
           steeringAngle = 0.15;
        }

        carModel.updateModel(torque, 0, steeringAngle);
        carX += carModel.fixedX;
        carY += carModel.fixedY;
    }

    public int getID(){
        return 1;
    }
}
