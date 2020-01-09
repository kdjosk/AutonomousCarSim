package view;

import controller.Controller;
import model.Car;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Simulation extends BasicGameState {

    Image carImage, mapImage;
    // Depends on where you want the car to start on the map
    public final static float initialMapX = 2838, initialMapY = 3542;
    Controller controller;


    public Simulation(int state){

    }

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        controller = new Controller(1.0/ViewManager.maxFPS);
        carImage = new Image("res/carImage.png");
        mapImage = new Image("res/trackImage.png");
        carImage.setCenterOfRotation(controller.getCarRotationCenterX(carImage.getWidth()),
                                     controller.getCarRotationCenterY(carImage.getHeight()));
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawImage(mapImage, controller.getMapX(), controller.getMapY());
        graphics.drawImage(carImage, (ViewManager.width - carImage.getWidth())/2.0f, (ViewManager.height - carImage.getHeight())/2.0f);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        controller.update();
        carImage.setRotation(controller.getCarRotation());
    }
}
