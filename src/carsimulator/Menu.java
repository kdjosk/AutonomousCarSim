package carsimulator;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Menu extends BasicGameState{

    // Images
    Image car;
    float carX;
    float carY;
    double carPhi;

    Image track;
    float trackX;
    float trackY;

    float Xinitial = 1210;
    float Yinitial = 2744;



    public Menu(int state){

    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
        car = new Image("res/Car.png");
        carX = 640 - car.getWidth()/2.0f;
        carY = 360 - car.getHeight()/2.0f;

        car.setCenterOfRotation((float)car.getWidth()/2, (float)car.getHeight()/2);


        track = new Image("res/Track.png");
        trackX = 640 - Xinitial;
        trackY = 360 - Yinitial;
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.drawImage(track, trackX, trackY);
        g.drawImage(car, carX, carY);
        g.drawString("Car rotation: " + Math.sin(car.getRotation() * Math.PI/180), 20, 20);
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
        Input input = gc.getInput();
        if(input.isKeyDown(Input.KEY_UP)) {
            trackX += Math.cos(car.getRotation() * Math.PI/180);
            trackY += Math.sin(car.getRotation() * Math.PI/180);
        }
        if(input.isKeyDown(Input.KEY_DOWN)){

        }
        if(input.isKeyDown(Input.KEY_LEFT)){ car.rotate(-0.2f); }
        if(input.isKeyDown(Input.KEY_RIGHT)){ car.rotate(0.2f); }
    }

    public int getID(){
        return 0;
    }
}

