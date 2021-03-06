package guivehiclephysicssim.view;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ViewManager extends StateBasedGame{

    public static final String gamename = "Car Simulator";
    public static final int simulation = 1;
    public static final int maxFPS = 300;
    public static final int width = 1280;
    public static final int height = 720;

    public ViewManager(String gamename) {
        super(gamename);
        this.addState(new Simulation(simulation));
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.getState(simulation).init(gameContainer, this);
        this.enterState(simulation);
    }

    public static void main(String[] args) {
        AppGameContainer appGameContainer;
        try{
            appGameContainer = new AppGameContainer(new ViewManager(gamename));
            appGameContainer.setDisplayMode(1280, 720, false);
            appGameContainer.setTargetFrameRate(maxFPS);
            appGameContainer.start();
        } catch(SlickException e){
            e.printStackTrace();
        }
    }
}
