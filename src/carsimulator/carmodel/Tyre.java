package carsimulator.carmodel;

import java.util.*;
import java.io.*;

public class Tyre {

    double Fx, Fy, Mz;
    double R;
    double Iy;
    double Tw;
    double Tp;
    double Fzt;
    double C1;
    double C2;
    double C3;
    double C4;
    double G1;
    double G2;
    double A0;
    double A1;
    double A2;
    double A3;
    double A4;
    double Ka;
    double K1;
    double Kmu;
    double CSFZ;
    double mu0;
    double gamma;

    public Tyre(String tyreProperties) {
        Fx = 0;
        Fy = 0;
        Mz = 0;

        Properties p = new Properties();
        try(InputStream inputTyre = new FileInputStream(tyreProperties)){
            p.load(inputTyre);
        } catch(IOException e) {
            e.printStackTrace();
        }

        R = Double.parseDouble(p.getProperty("R"));
        Iy = Double.parseDouble(p.getProperty("Iy"));
        Tw = Double.parseDouble(p.getProperty("Tw"));
        Tp = Double.parseDouble(p.getProperty("Tp"));
        Fzt = Double.parseDouble(p.getProperty("Fzt"));
        C1 = Double.parseDouble(p.getProperty("C1"));
        C2 = Double.parseDouble(p.getProperty("C2"));
        C3 = Double.parseDouble(p.getProperty("C3"));
        C4 = Double.parseDouble(p.getProperty("C4"));
        G1 = Double.parseDouble(p.getProperty("G1"));
        G2 = Double.parseDouble(p.getProperty("G2"));
        A0 = Double.parseDouble(p.getProperty("A0"));
        A1 = Double.parseDouble(p.getProperty("A1"));
        A2 = Double.parseDouble(p.getProperty("A2"));
        A3 = Double.parseDouble(p.getProperty("A3"));
        A4 = Double.parseDouble(p.getProperty("A4"));
        Ka = Double.parseDouble(p.getProperty("Ka"));
        K1 = Double.parseDouble(p.getProperty("K1"));
        CSFZ = Double.parseDouble(p.getProperty("CSFZ"));
        mu0 = Double.parseDouble(p.getProperty("mu0"));
        gamma = Double.parseDouble(p.getProperty("gamma"));
    }

    void updateForces(double Fz, double kappa, double alpha) {

        // Normal contact patch length
        double ap0 = 0.0768 * Math.sqrt(Fz * Fzt)/(Tw * (Tp + 5));
        // Stretched/compressed contact patch length
        double ap = ap0 * (1 - Ka * Fx/Fz);
        double Ks = (2/Math.pow(ap0, 2)) * (A0 + A1*Fz + (A1/A2) * Math.pow(Fz, 2));
        double Kc = (2/Math.pow(ap0, 2)) * Fz * CSFZ;
        // Composite slip
        double sigma = Math.PI * Math.pow(ap, 2) / (8 * mu0 * Fz)
                     * Math.sqrt(Math.pow(Ks * Math.tan(alpha), 2) + Math.pow(Kc * kappa/(1 - kappa), 2));

        // Slip to slide transition
        double temp1 = Math.pow(Math.sin(alpha), 2) + Math.pow(kappa * Math.cos(alpha), 2);
        double mu = mu0 * (1 - Kmu * Math.sqrt(temp1));
        double KcPrim = Kc + (Ks - Kc) * Math.sqrt(temp1);

        // Camber stiffness
        double Ygamma = A3 * Fz - (A3/A4) * Math.pow(Fz, 2);

        double temp2 = Math.sqrt(Math.pow(Ks * Math.tan(alpha), 2) + Math.pow(KcPrim * kappa, 2));

        Fy = f(sigma) * Ks * Math.tan(alpha) / temp2 + Ygamma * gamma;
        Fy *= mu * Fz;

        Fx = -f(sigma) * KcPrim * kappa / temp2;
        Fx *= mu * Fz;

        // Aligning torque TODO




    }

    // Saturation function
    double f(double sigma) {
        double res = (C1 * Math.pow(sigma, 3) + C2 * Math.pow(sigma, 2) + (4/Math.PI)*sigma)
                   / (C1 * Math.pow(sigma, 3) + C3 * Math.pow(sigma, 2) + C4 * sigma + 1);
        return res;
    }

}
