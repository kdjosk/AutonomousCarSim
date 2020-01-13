package guivehiclephysicssim.nav;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.io.Serializable;

public class Controls implements Serializable {
    private double delta;
    private double velocity;
    private double acceleration;
    private Vector2D[] predictedPath;
    private Vector2D[] polynomialFit;

    public Controls(double delta, double velocity, double acceleration, Vector2D[] predictedPath, Vector2D[] polynomialFit) {
        this.delta = delta;
        this.velocity = velocity;
        this.acceleration = acceleration;
        if(predictedPath != null)
            this.predictedPath = predictedPath.clone();
        if(polynomialFit != null)
            this.polynomialFit = polynomialFit.clone();
    }


    public double getDelta() {
        return delta;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getVelocity() {
        return velocity;
    }

    public Vector2D[] getPolynomialFit() {
        return polynomialFit;
    }

    public Vector2D[] getPredictedPath() {
        return predictedPath;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public void setPredictedPath(Vector2D[] predictedPath) {
        this.predictedPath = predictedPath;
    }

    public void setPolynomialFit(Vector2D[] polynomialFit) {
        this.polynomialFit = polynomialFit;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }
}

