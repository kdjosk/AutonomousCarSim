package nav;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.io.Serializable;

public class Controls implements Serializable {
    double delta;
    double velocity;
    double acceleration;
    Vector2D[] predictedPath;
    Vector2D[] polynomialFit;

    public Controls(double delta, double velocity, double acceleration, Vector2D[] predictedPath, Vector2D[] polynomialFit) {
        this.delta = delta;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.predictedPath = predictedPath.clone();
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

}
