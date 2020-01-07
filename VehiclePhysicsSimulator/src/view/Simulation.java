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
    public final static float initialMapX = 2838, initialMapY = 3542;
    Controller controller;


    public Simulation(int state){

    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        controller = new Controller(1.0/ViewManager.maxFPS);
        carImage = new Image("res/carImage.png");
        mapImage = new Image("res/trackImage.png");

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }
}
