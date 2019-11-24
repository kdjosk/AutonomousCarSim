package carsimulator;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Game extends StateBasedGame{

    public static final String gamename = "Car Simulator";
    public static final int menu = 0;
    public static final int play = 1;
    public static final int maxFPS = 144;

    public Game(String gamename) {
        super(gamename);
        this.addState(new Menu(menu));
        this.addState(new Play(play));
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        this.getState(menu).init(gc, this);
        this.getState(play).init(gc, this);
        this.enterState(play);
    }

    public static void main(String[] args) {
        AppGameContainer appgc;
        try{
            appgc = new AppGameContainer(new Game(gamename));
            appgc.setDisplayMode(1280, 720, false);
            appgc.setTargetFrameRate(maxFPS);
            appgc.start();
        }catch(SlickException e){
            e.printStackTrace();
        }
    }
}
