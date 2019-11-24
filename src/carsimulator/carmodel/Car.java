package carsimulator.carmodel;

import org.lwjgl.Sys;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;
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
    // Yaw inertia
    final double IZ;

    // Tyres
    public Tyre frontTyre;
    public Tyre rearTyre;

    // Instantaneous longitudinal acceleration
    double ax;
    double ay;

    // Velocity
    double vx;
    double vy;

    // Yaw variables
    public double yawAngle;
    double yawRate;

    // Steering angle
    double delta;

    // Simulation time
    double t;
    double dt;

    // Tyre angular velocities
    double omegaf;
    double omegar;

    // Coordinates to fixed reference frame
    public double fixedX;
    public double fixedY;


    public Car(String bodyProperties, String frontTyreProperties, String rearTyreProperties, double dt) {
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
        IZ = Double.parseDouble(p.getProperty("IZ"));

        frontTyre = new Tyre(frontTyreProperties);
        rearTyre = new Tyre(rearTyreProperties);
        this.dt = dt;
        fixedY = 0;
        fixedX = 0;
        ax = 0;
        ay = 0;
        vx = 0;
        vy = 0;
        yawAngle = 0;
        yawRate = 0;
        delta = 0;
        t = 0;
        omegar = 0;
        omegaf = 0;
    }

    interface Differential {
        double dydx(double x, double[] y);
    }

    private double[] rungeKutta4(double x0, double[] y0, double h, Differential[] f) {

        int n = f.length;
        double[][] k = new double[5][n];

        for(int i = 0; i < n; i++) k[0][i] = 0;
        // step length multiplier
        double[] a = {0, 0, 0.5, 0.5, 1};

        double[] y = y0;
        for(int i = 1; i < 5; i++) {

            for(int j = 0; j < n; j++) {
                y[j] = y0[j] + k[i - 1][j] * a[i];
            }

            for(int j = 0; j < n; j++) {
                k[i][j] = h * f[j].dydx(x0 + a[i] * h, y);
            }

        }

        for(int i = 0; i < n; i++) {
            y[i] = y0[i] + 1.0/6.0 * (k[1][i] + 2*k[2][i] + 2*k[3][i] + k[4][i]) * h;
        }

        return y;
    }

    public void updateModel(double frontTorque, double rearTorque, double steeringAngle) {

        delta = steeringAngle;

        double Fxf = frontTyre.Fx;
        double Fyf = frontTyre.Fy;
        double Fxr = rearTyre.Fx;
        double Fyr = rearTyre.Fy;

        /*
        Differential equations:
        0 - yawAngle
        1 - yawRate
        2 - vx
        3 - vy
        4 - fixedX
        5 - fixedY
        6 - omegaf
        7 - omegar
        */

        Differential[] f = new Differential[8];

        f[0] = (double x, double[] y) -> y[1];
        f[1] = (double x, double[] y) -> (1.0/IZ) * (LF * (Fyf*Math.cos(delta) - Fxf*Math.sin(delta) - LR*Fyr));
        f[2] = (double x, double[] y) -> (1.0/M) * (Fxf*Math.cos(delta) - Fyf*Math.sin(delta) - Fxr) + y[3]*y[1];
        f[3] = (double x, double[] y) -> (1.0/M) * (Fyf*Math.cos(delta) - Fxf*Math.sin(delta) + Fyr) + y[2]*y[1];
        f[4] = (double x, double[] y) -> y[2]*Math.cos(y[0]) - y[3]*Math.sin(y[0]);
        f[5] = (double x, double[] y) -> y[2]*Math.sin(y[0]) + y[3]*Math.cos(y[0]);
        f[6] = (double x, double[] y) -> (1.0/frontTyre.Iy) * (Fxf*frontTyre.R + frontTorque);
        f[7] = (double x, double[] y) -> (1.0/rearTyre.Iy) * (Fxr*rearTyre.R + rearTorque);

        double[] y = {yawAngle, yawRate, vx, vy, fixedX, fixedY, omegaf, omegar};
        y = rungeKutta4(t, y, dt, f);
        ax = (y[2] - vx)/dt;
        ay = (y[3] - vy)/dt;
        yawAngle = y[0]; yawRate = y[1]; vx = y[2]; vy = y[3]; fixedX = y[4]; fixedY = y[5]; omegaf = y[6]; omegar = y[7];

        updateTyreForces();

        t += dt;

        DecimalFormat df = new DecimalFormat("0.00");
        Arrays.stream(y).forEach(e -> System.out.print(df.format(e) + " "));
        System.out.println();
    }

    private void updateTyreForces() {

        // Normal loads
        double Fzf = M*(G*LR - ax*HCG)/(LF + LR);
        double Fzr = M*(G*LF - ax*HCG)/(LF + LR);

        // Slip angles
        double alphaf = Math.atan2((vy + LF * yawRate), vx) - delta;
        double alphar = Math.atan2((vy + LR * yawRate), vx);
        if(Math.abs(alphaf) > Math.PI/2) alphaf = Math.PI/2*Math.signum(alphaf);
        if(Math.abs(alphar) > Math.PI/2) alphar = Math.PI/2*Math.signum(alphar);

        // Tyre velocities
        double vtf = Math.sqrt((vy + LF* yawRate)*(vy + LF* yawRate) + vx*vx);
        double vtr = Math.sqrt((vy - LR* yawRate)*(vy - LR* yawRate) + vx*vx);

        // Tyre velocity longitudinal components
        double vwxf = vtf * Math.cos(alphaf);
        double vwxr = vtr * Math.cos(alphar);
        double kappaf = 0, kappar = 0;

        if(vwxf != 0 || omegaf != 0) {
            kappaf = vwxf > omegaf*frontTyre.R ? (vwxf - omegaf*frontTyre.R)/vwxf : (omegaf*frontTyre.R - vwxf)/omegaf*frontTyre.R;
            if(Math.abs(kappaf) >= 1) kappaf = (1 - 1e-5) * Math.signum(kappaf) ;
        }
        if(vwxr != 0 || omegar != 0) {
            kappar = vwxr > omegar * rearTyre.R ? (vwxr - omegar * rearTyre.R) / vwxr : (omegar * rearTyre.R - vwxr) / omegar * rearTyre.R;
            if(Math.abs(kappar) >= 1) kappar = (1 - 1e-5) * Math.signum(kappar) ;
        }
        frontTyre.updateForces(Fzf, kappaf, alphaf);
        rearTyre.updateForces(Fzr, kappar, alphar);
    }



}
