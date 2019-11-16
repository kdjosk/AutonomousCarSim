package carsimulator.carmodel.tyre;

import java.util.*;
import java.io.*;

public class Tyre {

    Parameters p;

    public Tyre(String propertiesFile)
    {
        p = new Parameters();
    }

    public double getLongitudinalForcePureSlip(double kappa, double Fz, double dfz, double pi, double dpi, double gamma, double degressiveFrictionFactor, double compositeFrictionFactor){

        double S_Vx = Fz * (p.pVx1 + p.pVx2 * dfz) * p.userVerticalShift * degressiveFrictionFactor * p.zeta[1];
        double S_Hx = (p.pHx1 + p.pHx2 * dfz) * p.userHorizontalShift;
        double Kxk = Fz*(p.pKx1 + p.pKx2 * dfz) * Math.exp(p.pKx3 * dfz) * (1 + p.ppx1*dpi + p.ppx2*dpi*dpi);
        double kappa_x = kappa + S_Hx;
        double Ex = (p.pEx1 + p.pEx2*dfz + p.pEx3*dfz*dfz) * (1 - p.pEx4*Math.signum(kappa_x)) * p.userCurvatureFactor;
        double mu_x = (p.pDx1 + p.pDx2*dfz) * (1 + p.ppx3*dpi + p.ppx4*dpi*dpi) * (1 - p.pDx3*gamma*gamma) * compositeFrictionFactor;
        double Dx = mu_x * Fz * p.zeta[1];
    }

    public double psatan(double x) {
        return x * (1 + 1.1 * Math.abs(x))/(1 + 0.63662*(1.6 * Math.abs(x) + 1.1 * x * x));
    }
}
