package carsimulator.carmodel;

import java.io.*;
import java.util.Properties;

public class Car {

    // COG to axle lengths
    final double LF;
    final double LR;
    // Height of the Center of Gravity
    final double HCG;
    // Mass
    final double M;
    // Gravity
    final double G;

    // Tyres
    Tyre frontTyre;
    Tyre rearTyre;

    // Instantaneous longitudinal acceleration
    double ax;
    // Velocity
    double vx;
    double vy;
    // Angular velocity
    double omega;
    // Steering angle
    double delta;

    // Simulation time
    double t;
    double dt;

    // Tyre angular velocities
    double omegaf;
    double omegar;


    public Car(String bodyProperties, String frontTyreProperties, String rearTyreProperties) {
        Properties p = new Properties();
        try(InputStream inputTyre = new FileInputStream(bodyProperties)){
            p.load(inputTyre);
        } catch(IOException e) {
            e.printStackTrace();
        }

        LF = Double.parseDouble(p.getProperty("LF"));
        LR = Double.parseDouble(p.getProperty("LR"));
        HCG = Double.parseDouble(p.getProperty("HCG"));
        M = Double.parseDouble(p.getProperty("M"));
        G = Double.parseDouble(p.getProperty("G"));

        frontTyre = new Tyre(frontTyreProperties);
        rearTyre = new Tyre(rearTyreProperties);
    }

    interface Differential {
        double dydx(double x, double y);
    }

    double rungeKutta(double t0, double y0, Differential diff) {

        double k1, k2, k3, k4, k5;
        k1 = diff.dydx(t0, y0);
        k2 = diff.dydx(t0 + 0.5 * dt, y0 + 0.5 * k1 * dt);
        k3 = diff.dydx(t0 + 0.5 * dt, y0 + 0.5 * k2 * dt);
        k4 = diff.dydx(t0 + dt, y0 + k3 * dt);

        double avgSlope = (1.0/6.0) * (k1 + 2*k2 + 2*k3 + k4);

        double y = y0 + avgSlope * dt;

        return y;
    }


    // TODO
    public void updateTyreForces(double frontTorque, double rearTorque) {

        // Normal loads
        double Fzf = M*(G*LR - ax*HCG)/(LF + LR);
        double Fzr = M*(G*LF - ax*HCG)/(LF + LR);

        // Slip angles
        double alphaf = Math.atan2(vy + LF*omega, vx) - delta;
        double alphar = Math.atan2(vy + LR*omega, vx);

        // Tyre velocities
        double vtf = Math.sqrt((vy + LF*omega)*(vy + LF*omega) + vx*vx);
        double vtr = Math.sqrt((vy - LR*omega)*(vy - LR*omega) + vx*vx);

        // Tyre velocity longitudinal components
        double vwxf = vtf * Math.cos(alphaf);
        double vwxr = vtr * Math.cos(alphar);

        omegaf = rungeKutta(t, omegaf, new Differential() {
            @Override
            public double dydx(double x, double y) {
                return 1.0/frontTyre.Iy * (frontTyre.Fx*frontTyre.R + frontTorque);
            }
        });

        omegar = rungeKutta(t, omegar, new Differential() {
            @Override
            public double dydx(double x, double y) {
                return 1.0/rearTyre.Iy * (rearTyre.Fx*rearTyre.R + rearTorque);
            }
        });

        double kappaf = vwxf > omegaf*frontTyre.R ? (vwxf - omegaf*frontTyre.R)/vwxf : (omegaf*frontTyre.R - vwxf)/vwxf;


    }



}
