package guivehiclephysicssim.view;

import guivehiclephysicssim.controller.Controller;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lwjgl.Sys;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;
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
        controller = new Controller(1.0/(2.0 * ViewManager.maxFPS));
        try{
            carImage = new Image("res/carImage.png");
            mapImage = new Image("res/trackImage.png");
            carImage.setCenterOfRotation(controller.getCarRotationCenterX(carImage.getWidth()),
                    controller.getCarRotationCenterY(carImage.getHeight()));
        }catch(Exception e){e.printStackTrace();}


    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawImage(mapImage, controller.getMapX(), controller.getMapY());
        graphics.setColor(Color.red);
        for(int i = 0; i < controller.getPathPointsSize(); i++) {
            graphics.fillOval((float)controller.getPathPoint(i)[0], (float)controller.getPathPoint(i)[1], 10, 10);
        }

        graphics.drawImage(carImage, (ViewManager.width - carImage.getWidth())/2.0f, (ViewManager.height - carImage.getHeight())/2.0f);

        Vector2D[] predictedPathPoints = controller.getPredictedPathPoints();
        if(predictedPathPoints != null){
            graphics.setColor(Color.pink);
            for(int i = 0; i < predictedPathPoints.length; i++) {
                graphics.fillOval((float)predictedPathPoints[i].getX(), (float)predictedPathPoints[i].getY(), 10, 10);
            }
        }

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        controller.update();
        carImage.setRotation(controller.getCarRotation());
    }
}
