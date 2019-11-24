package carsimulator;

import carsimulator.carmodel.Car;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import java.text.DecimalFormat;

public class Play extends BasicGameState{

    // Images
    Image carImage;
    float carX;
    float carY;
    double carPhi;

    Image trackImage;
    float trackX;
    float trackY;

    float Xinitial = 1210;
    float Yinitial = 2744;

    Car carModel;

    float pxPerMeter = 68;

    public Play(int state){

    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{

        carModel = new Car("res/car.properties", "res/tyre.properties", "res/tyre.properties", 1.0/Game.maxFPS);
        carImage = new Image("res/Car.png");
        carX = 640 - carImage.getWidth()/2.0f;
        carY = 360 - carImage.getHeight()/2.0f;

        carImage.setCenterOfRotation(carImage.getWidth()/2.0f, carImage.getHeight()/2.0f);
        carImage.setRotation(90.0f);

        trackImage = new Image("res/Track.png");
        trackX = 640 - Xinitial;
        trackY = 360 - Yinitial;
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
        g.drawImage(trackImage, trackX, trackY);
        g.drawImage(carImage, carX, carY);
        DecimalFormat df = new DecimalFormat("0.000");
        g.drawString("Car rotation: " + df.format(Math.sin(carImage.getRotation() * Math.PI/180)), 20, 20);
        g.drawString("Fxf: " + df.format(carModel.frontTyre.Fx) + ", Fyf: " + df.format(carModel.frontTyre.Fy), 20, 40);
        g.drawString("Fxr: " + df.format(carModel.rearTyre.Fx) + ", Fyr: " + df.format(carModel.rearTyre.Fy), 20, 60);
        g.drawString("alphaf: " + df.format(carModel.frontTyre.alpha) + ", alphar: " + df.format(carModel.rearTyre.alpha), 20, 80);
        g.drawString("kappaf: " + df.format(carModel.frontTyre.kappa) + ", kappar: " + df.format(carModel.rearTyre.kappa), 20, 100);
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{

        Input input = gc.getInput();
        double torque, steeringAngle;
        if(input.isKeyDown(Input.KEY_UP)) {
            torque = 10;
        }
        else if(input.isKeyDown(Input.KEY_DOWN)) {
            torque = -10;
        }
        else torque = 0;


        if(input.isKeyDown(Input.KEY_LEFT)) {
             steeringAngle = -0.10;
        }
        else if(input.isKeyDown(Input.KEY_RIGHT)) {
           steeringAngle = 0.10;
        }
        else steeringAngle = 0;

        carModel.updateModel(torque, 0, steeringAngle);
        trackX -= carModel.fixedX/pxPerMeter;
        trackY -= carModel.fixedY/pxPerMeter;
        carImage.setRotation((float)(carModel.yawAngle * 180/Math.PI));
    }

    public int getID(){
        return 1;
    }
}
