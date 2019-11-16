package carsimulator.carmodel.tyre;

import java.util.*;
import java.io.*;

public class Parameters {

    // User scaling factors

    // Pure slip
    double userNominalLoad;
    double userPeakFrictionCoef;
    double userWithSlipSpeedDecayingFriction;
    double userBrakeSlipStiffness;
    double userCorneringStiffness;
    double userShapeFactor;
    double userCurvatureFactor;
    double userHorizontalShift;
    double userVerticalShift;
    double userCamberForceStiffness;
    double userCamberTorqueStiffness;
    double userPneumaticTrail; // Effecting aligning torque stiffness
    double userResidualTorque;

    // Combined slip
    double userAlphaInfluenceOnFx;
    double userKappaInfluenceOnFy;
    double userKappaInducedPlySteerFy;
    double userMzMomentArmOfFx;

    // Other
    double userRadialTireStiffness;
    double userOverturningCoupleStiffness;
    double userOverturningCoupleVerticalShift;
    double userRollingResistanceMoment;

    // Model
    double V0;

    // Dimension
    double r0;
    double w;
    double rRim;

    // Operating conditions
    double pi0;

    // Vertical
    double Fz0;

    // Longitudinal coefficients
    double pCx1;
    double pDx1;
    double pDx2;
    double pDx3;
    double pEx1;
    double pEx2;
    double pEx3;
    double pEx4;
    double pKx1;
    double pKx2;
    double pKx3;
    double pHx1;
    double pHx2;
    double pVx1;
    double pVx2;
    double ppx1;
    double ppx2;
    double ppx3;
    double ppx4;
    double rBx1;
    double rBx2;
    double rBx3;
    double rCx1;
    double rHx1;
    double rEx1;
    double rEx2;

    // Lateral Coefficients

    double pCy1;
    double pDy1;
    double pDy2;
    double pDy3;
    double pEy1;
    double pEy2;
    double pEy3;
    double pEy4;
    double pKy1;
    double pKy2;
    double pKy3;
    double pKy4;
    double pKy5;
    double pKy6;
    double pKy7;
    double pHy1;
    double pHy2;
    double pVy1;
    double pVy2;
    double pVy3;
    double pVy4;
    double rBy1;
    double rBy2;
    double rBy3;
    double rBy4;
    double rCy1;
    double rEy1;
    double rEy2;
    double rHy1;
    double rHy2;
    double rVy1;
    double rVy2;
    double rVy3;
    double rVy4;
    double rVy5;
    double rVy6;

    // Aligning Coefficients

    double qBz1;
    double qBz2;
    double qBz3;
    double qBz4;
    double qBz5;
    double qBz9;
    double qBz10;
    double qCz1;
    double qDz1;
    double qDz2;
    double qDz3;
    double qDz4;
    double qDz6;
    double qDz7;
    double qDz8;
    double qDz9;
    double qDz10;
    double qDz11;
    double qEz1;
    double qEz2;
    double qEz3;
    double qEz4;
    double qEz5;
    double qHz1;
    double qHz2;
    double qHz3;
    double qHz4;
    double sSz1;
    double sSz2;
    double sSz3;
    double sSz4;

    // Turnslip coefficients

    double pDxPhi1;
    double pDxPhi23;
    double pKyPhi1;
    double pDyPhi1;
    double pDyPhi2;
    double pDyPhi34;
    double pHyPhi1;
    double pHyPhi2;
    double pHyPhi3;
    double pHyPhi4;
    double pEpsGammaPhi1;
    double pEpsGammaPhi2;
    double qDtPhi1;
    double qCrPhi1;
    double qCrPhi2;
    double qBrPhi1;
    double qDrPhi1;

    // zetas, TODO
    double zeta[];

    Parameters(){
        Properties params = new Properties();
        try(InputStream input = new FileInputStream("tyre.properties")){
           params = new Properties();
            params.load(input);
        } catch(IOException e){
            e.printStackTrace();
        }
        userNominalLoad = Double.parseDouble(params.getProperty("userNominalLoad"));
        userPeakFrictionCoef = Double.parseDouble(params.getProperty("userPeakFrictionCoef"));
        userWithSlipSpeedDecayingFriction = Double.parseDouble(params.getProperty("userWithSlipSpeedDecayingFriction"));
        userBrakeSlipStiffness = Double.parseDouble(params.getProperty("userBrakeSlipStiffness"));
        userCorneringStiffness = Double.parseDouble(params.getProperty("userCorneringStiffness"));
        userShapeFactor = Double.parseDouble(params.getProperty("userShapeFactor"));
        userCurvatureFactor = Double.parseDouble(params.getProperty("userCurvatureFactor"));
        userHorizontalShift = Double.parseDouble(params.getProperty("userHorizontalShift"));
        userVerticalShift = Double.parseDouble(params.getProperty("userVerticalShift"));
        userCamberForceStiffness = Double.parseDouble(params.getProperty("userCamberForceStiffness"));
        userCamberTorqueStiffness = Double.parseDouble(params.getProperty("userCamberTorqueStiffness"));
        userResidualTorque = Double.parseDouble(params.getProperty("userResidualTorque"));
        userAlphaInfluenceOnFx = Double.parseDouble(params.getProperty("userAlphaInfluenceOnFx"));
        userKappaInfluenceOnFy = Double.parseDouble(params.getProperty("userKappaInfluenceOnFy"));
        userKappaInducedPlySteerFy = Double.parseDouble(params.getProperty("userKappaInducedPlySteerFy"));
        userMzMomentArmOfFx = Double.parseDouble(params.getProperty("userMzMomentArmOfFx"));
        userRadialTireStiffness = Double.parseDouble(params.getProperty("userRadialTireStiffness"));
        userOverturningCoupleStiffness = Double.parseDouble(params.getProperty("userOverturningCoupleStiffness"));
        userOverturningCoupleVerticalShift = Double.parseDouble(params.getProperty("userOverturningCoupleVerticalShift"));
        userRollingResistanceMoment = Double.parseDouble(params.getProperty("userRollingResistanceMoment"));
        V0 = Double.parseDouble(params.getProperty("V0"));
        r0 = Double.parseDouble(params.getProperty("r0"));
        w = Double.parseDouble(params.getProperty("w"));
        rRim = Double.parseDouble(params.getProperty("rRim"));
        pi0 = Double.parseDouble(params.getProperty("pi0"));
        Fz0 = Double.parseDouble(params.getProperty("Fz0"));
        pCx1 = Double.parseDouble(params.getProperty("pCx1"));
        pDx1 = Double.parseDouble(params.getProperty("pDx1"));
        pDx2 = Double.parseDouble(params.getProperty("pDx2"));
        pDx3 = Double.parseDouble(params.getProperty("pDx3"));
        pEx1 = Double.parseDouble(params.getProperty("pEx1"));
        pEx2 = Double.parseDouble(params.getProperty("pEx2"));
        pEx3 = Double.parseDouble(params.getProperty("pEx3"));
        pEx4 = Double.parseDouble(params.getProperty("pEx4"));
        pKx1 = Double.parseDouble(params.getProperty("pKx1"));
        pKx2 = Double.parseDouble(params.getProperty("pKx2"));
        pKx3 = Double.parseDouble(params.getProperty("pKx3"));
        pHx1 = Double.parseDouble(params.getProperty("pHx1"));
        pHx2 = Double.parseDouble(params.getProperty("pHx2"));
        pVx1 = Double.parseDouble(params.getProperty("pVx1"));
        pVx2 = Double.parseDouble(params.getProperty("pVx2"));
        ppx1 = Double.parseDouble(params.getProperty("ppx1"));
        ppx2 = Double.parseDouble(params.getProperty("ppx2"));
        ppx3 = Double.parseDouble(params.getProperty("ppx3"));
        ppx4 = Double.parseDouble(params.getProperty("ppx4"));
        rBx1 = Double.parseDouble(params.getProperty("rBx1"));
        rBx2 = Double.parseDouble(params.getProperty("rBx2"));
        rBx3 = Double.parseDouble(params.getProperty("rBx3"));
        rCx1 = Double.parseDouble(params.getProperty("rCx1"));
        rHx1 = Double.parseDouble(params.getProperty("rHx1"));
        rEx1 = Double.parseDouble(params.getProperty("rEx1"));
        rEx2 = Double.parseDouble(params.getProperty("rEx2"));
        pCy1 = Double.parseDouble(params.getProperty("pCy1"));
        pDy1 = Double.parseDouble(params.getProperty("pDy1"));
        pDy2 = Double.parseDouble(params.getProperty("pDy2"));
        pDy3 = Double.parseDouble(params.getProperty("pDy3"));
        pEy1 = Double.parseDouble(params.getProperty("pEy1"));
        pEy2 = Double.parseDouble(params.getProperty("pEy2"));
        pEy3 = Double.parseDouble(params.getProperty("pEy3"));
        pEy4 = Double.parseDouble(params.getProperty("pEy4"));
        pKy1 = Double.parseDouble(params.getProperty("pKy1"));
        pKy2 = Double.parseDouble(params.getProperty("pKy2"));
        pKy3 = Double.parseDouble(params.getProperty("pKy3"));
        pKy4 = Double.parseDouble(params.getProperty("pKy4"));
        pKy5 = Double.parseDouble(params.getProperty("pKy5"));
        pKy6 = Double.parseDouble(params.getProperty("pKy6"));
        pKy7 = Double.parseDouble(params.getProperty("pKy7"));
        pHy1 = Double.parseDouble(params.getProperty("pHy1"));
        pHy2 = Double.parseDouble(params.getProperty("pHy2"));
        pVy1 = Double.parseDouble(params.getProperty("pVy1"));
        pVy2 = Double.parseDouble(params.getProperty("pVy2"));
        pVy3 = Double.parseDouble(params.getProperty("pVy3"));
        pVy4 = Double.parseDouble(params.getProperty("pVy4"));
        rBy1 = Double.parseDouble(params.getProperty("rBy1"));
        rBy2 = Double.parseDouble(params.getProperty("rBy2"));
        rBy3 = Double.parseDouble(params.getProperty("rBy3"));
        rBy4 = Double.parseDouble(params.getProperty("rBy4"));
        rCy1 = Double.parseDouble(params.getProperty("rCy1"));
        rEy1 = Double.parseDouble(params.getProperty("rEy1"));
        rEy2 = Double.parseDouble(params.getProperty("rEy2"));
        rHy1 = Double.parseDouble(params.getProperty("rHy1"));
        rHy2 = Double.parseDouble(params.getProperty("rHy2"));
        rVy1 = Double.parseDouble(params.getProperty("rVy1"));
        rVy2 = Double.parseDouble(params.getProperty("rVy2"));
        rVy3 = Double.parseDouble(params.getProperty("rVy3"));
        rVy4 = Double.parseDouble(params.getProperty("rVy4"));
        rVy5 = Double.parseDouble(params.getProperty("rVy5"));
        rVy6 = Double.parseDouble(params.getProperty("rVy6"));
        qBz1 = Double.parseDouble(params.getProperty("qBz1"));
        qBz2 = Double.parseDouble(params.getProperty("qBz2"));
        qBz3 = Double.parseDouble(params.getProperty("qBz3"));
        qBz4 = Double.parseDouble(params.getProperty("qBz4"));
        qBz5 = Double.parseDouble(params.getProperty("qBz5"));
        qBz9 = Double.parseDouble(params.getProperty("qBz9"));
        qBz10 = Double.parseDouble(params.getProperty("qBz10"));
        qCz1 = Double.parseDouble(params.getProperty("qCz1"));
        qDz1 = Double.parseDouble(params.getProperty("qDz1"));
        qDz2 = Double.parseDouble(params.getProperty("qDz2"));
        qDz3 = Double.parseDouble(params.getProperty("qDz3"));
        qDz4 = Double.parseDouble(params.getProperty("qDz4"));
        qDz6 = Double.parseDouble(params.getProperty("qDz6"));
        qDz7 = Double.parseDouble(params.getProperty("qDz7"));
        qDz8 = Double.parseDouble(params.getProperty("qDz8"));
        qDz9 = Double.parseDouble(params.getProperty("qDz9"));
        qDz10 = Double.parseDouble(params.getProperty("qDz10"));
        qDz11 = Double.parseDouble(params.getProperty("qDz11"));
        qEz1 = Double.parseDouble(params.getProperty("qEz1"));
        qEz2 = Double.parseDouble(params.getProperty("qEz2"));
        qEz3 = Double.parseDouble(params.getProperty("qEz3"));
        qEz4 = Double.parseDouble(params.getProperty("qEz4"));
        qEz5 = Double.parseDouble(params.getProperty("qEz5"));
        qHz1 = Double.parseDouble(params.getProperty("qHz1"));
        qHz2 = Double.parseDouble(params.getProperty("qHz2"));
        qHz3 = Double.parseDouble(params.getProperty("qHz3"));
        qHz4 = Double.parseDouble(params.getProperty("qHz4"));
        sSz1 = Double.parseDouble(params.getProperty("sSz1"));
        sSz2 = Double.parseDouble(params.getProperty("sSz2"));
        sSz3 = Double.parseDouble(params.getProperty("sSz3"));
        sSz4 = Double.parseDouble(params.getProperty("sSz4"));
        pDxPhi1 = Double.parseDouble(params.getProperty("pDxPhi1"));
        pDxPhi23 = Double.parseDouble(params.getProperty("pDxPhi23"));
        pKyPhi1 = Double.parseDouble(params.getProperty("pKyPhi1"));
        pDyPhi1 = Double.parseDouble(params.getProperty("pDyPhi1"));
        pDyPhi2 = Double.parseDouble(params.getProperty("pDyPhi2"));
        pDyPhi34 = Double.parseDouble(params.getProperty("pDyPhi34"));
        pHyPhi1 = Double.parseDouble(params.getProperty("pHyPhi1"));
        pHyPhi2 = Double.parseDouble(params.getProperty("pHyPhi2"));
        pHyPhi3 = Double.parseDouble(params.getProperty("pHyPhi3"));
        pHyPhi4 = Double.parseDouble(params.getProperty("pHyPhi4"));
        pEpsGammaPhi1 = Double.parseDouble(params.getProperty("pEpsGammaPhi1"));
        pEpsGammaPhi2 = Double.parseDouble(params.getProperty("pEpsGammaPhi2"));
        qDtPhi1 = Double.parseDouble(params.getProperty("qDtPhi1"));
        qCrPhi1 = Double.parseDouble(params.getProperty("qCrPhi1"));
        qCrPhi2 = Double.parseDouble(params.getProperty("qCrPhi2"));
        qBrPhi1 = Double.parseDouble(params.getProperty("qBrPhi1"));
        qDrPhi1 = Double.parseDouble(params.getProperty("qDrPhi1"));

        zeta = new double[9];
        for(int i = 0; i < 9; ++i)
        {
            zeta[i] = 1;
        }

    }

}
