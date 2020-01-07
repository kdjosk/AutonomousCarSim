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
    final float initialMapX, initialMapY, pxPerMeter;
    Controller controller;

    {
        initialMapX = 2838;
        initialMapY = 3542;
        pxPerMeter = 57.23f;
    }


    public Simulation(int state){
        controller = new Controller(1.0/ViewManager.maxFPS);
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }
}
