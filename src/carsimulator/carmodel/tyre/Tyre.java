package carsimulator.carmodel.tyre;

import java.util.*;
import java.io.*;

public class Tyre {

    public Tyre() {

    }

    public double getLatForceFront(Parameters p, double slip_angle, double frictionCoef, double longForceFront, double longForceRear) {

        double normForce = (p.m * p.g * p.lr - (longForceFront + longForceRear) * p.h)/(p.lf + p.lr);
        double alphaWiggle = p.Cf * slip_angle / (frictionCoef * normForce);
        double Fy = frictionCoef * normForce * (alphaWiggle - alphaWiggle * Math.abs(alphaWiggle)/3 + Math.pow(alphaWiggle, 3)/27)
                  * Math.sqrt(1 - Math.pow(longForceFront/(frictionCoef * normForce), 2) + Math.pow(longForceFront/p.Cf, 2));
        return Fy;
    }

    public double getLatForceRear(Parameters p, double slip_angle, double frictionCoef, double longForceFront, double longForceRear) {

        double normForce = (p.m * p.g * p.lf + (longForceFront + longForceRear) * p.h)/(p.lf + p.lr);
        double alphaWiggle = p.Cr * slip_angle / (frictionCoef * normForce);
        double Fr = frictionCoef * normForce * (alphaWiggle - alphaWiggle * Math.abs(alphaWiggle)/3 + Math.pow(alphaWiggle, 3)/27)
                * Math.sqrt(1 - Math.pow(longForceRear/(frictionCoef * normForce), 2) + Math.pow(longForceRear/p.Cr, 2));
        return Fr;
    }

}
