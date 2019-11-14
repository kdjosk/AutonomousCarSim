package carsimulator.carmodel.tyre;

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

    // Tire model parameters
    double refVel;
    double r0;
    double w;
    double rRim;
    double pi0;
    double Fz0;
    double

    // p, q, r, s parameters (a lot of them)
    double pCx1;
    double pDx1;
    double pDx2;
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
    // ------------
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
    //--------------
    double rBx1;
    double rBx2;
    double rBx3;
    double rCx1;
    double rHx1;
    double rBy1;
    double rBy2;
    double rBy3;
    double rBy4;
    double rCy1;
    double rVy1;
    double rVy2;
    double rVy3;
    double rVy4;
    double rVy5;
    double rVy6;
    double sSz1;
    double sSz2;
    double sSz3;
    double sSz4;
    //---------------
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
    double qDrPhi2;

}
