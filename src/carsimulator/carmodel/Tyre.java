package carsimulator.carmodel;

import java.util.*;
import java.io.*;

public class Tyre {

    public double Fx, Fy, Mz, alpha, kappa;
    double R;
    double Iy;


    public Tyre(String tyreProperties) {
        Fx = 0;
        Fy = 0;
        Mz = 0;
        R = 0.35;
        Iy = 10;
        Properties p = new Properties();
        try(InputStream inputTyre = new FileInputStream(tyreProperties)){
            p.load(inputTyre);
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    void updateForces(double Fz, double kappa, double alpha) {

        this.kappa = kappa;
        this.alpha = alpha;
        double Bx = 10, Cx = 1.9, Dx = 1.0, Ex = 0.97;
        double By = 10, Cy = 1.5, Dy = 1.0, Ey = 0.97;

        double Fx0 = Fz * Dx * Math.sin(Cx * psatan(Bx*kappa - Ex*(Bx*kappa - psatan(Bx*kappa))));
        double Fy0 = Fz * Dy * Math.sin(Cy * psatan(By*alpha - Ey*(By*alpha - psatan(By*alpha))));

        Fy = Fy0 * Math.sqrt(1 - Math.pow(Fx0/(Fz*Dx), 2));
        // TODO Aligning torque
    }

    private double psatan(double x) {
        return x * (1.0 + 1.1 * Math.abs(x))/(1 + 0.63662*(1.6 * Math.abs(x) + 1.1 * x * x));
    }

}
