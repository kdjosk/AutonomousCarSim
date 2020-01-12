package guivehiclephysicssim.model;

import guivehiclephysicssim.nav.Controls;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    // Center of rotation normalized distance from the front axis
    final double COR;
    // Portion of torque applied on front axis
    double GAMMA;

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
    public double delta;

    // Simulation time
    double t;
    double dt;

    // Tyre angular velocities
    double omegaf;
    double omegar;

    // Coordinates to fixed reference frame
    public double fixedX;
    public double fixedY;

    //Front and rear torque
    double frontTorque;
    double rearTorque;

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
        GAMMA = Double.parseDouble(p.getProperty("GAMMA"));
        COR = Double.parseDouble(p.getProperty("COR"));

        frontTyre = new Tyre(frontTyreProperties);
        rearTyre = new Tyre(rearTyreProperties);
        this.dt = dt;
        fixedY = 0;
        fixedX = 0;
        ax = 0;
        ay = 0;
        vx = 0;
        vy = 0;
        yawAngle = Math.PI;
        yawRate = 0;
        delta = 0;
        t = 0;
        omegar = 0;
        omegaf = 0;
        frontTorque = 0;
        rearTorque = 0;

    }

    @FunctionalInterface
    interface Differential {
        double dy(double[] y);
    }

    private double[] rungeKutta4(double[] y0, double h, Differential[] f) {

        int n = f.length;
        double[][] k = new double[5][n];

        for(int i = 0; i < n; i++) k[0][i] = 0;
        // step length multiplier
        double[] a = {0, 0, 0.5, 0.5, 1};

        double[] y = new double[n];
        for(int i = 1; i < 5; i++) {

            for(int j = 0; j < n; j++){
                y[j] = y0[j] + k[i - 1][j] * a[i] * h;
            }


            for(int j = 0; j < n; j++)
                k[i][j] = f[j].dy(y);

        }

        for(int i = 0; i < n; i++)
        {
            double avgSlope = 1.0 / 6.0 * (k[1][i] + 2.0 * k[2][i] + 2.0 * k[3][i] + k[4][i]);
            y[i] = y0[i] + avgSlope * h;
        }

        return y;
    }

    public void updateModel(Controls controls) {

        if(controls != null){
            frontTorque = controls.getAcceleration() * M * GAMMA;
            rearTorque = controls.getAcceleration() * M * (1 - GAMMA);
            delta = controls.getDelta();
        }

        //System.out.println("ft,rt: " + frontTorque + ", " + rearTorque);
        //System.out.println("X, Y: " + fixedX + ", " + fixedY);

        this.updateTyreForces();

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
        //System.out.println("ft: " + frontTorque + " rt: " + rearTorque);

        Differential[] f = new Differential[8];

        f[0] = (double[] y) -> y[1];
        f[1] = (double[] y) -> (1.0/IZ) * (LF * (Fyf*Math.cos(delta) - Fxf*Math.sin(delta)) - LR*Fyr);
        f[2] = (double[] y) -> (1.0/M) * (-Fxf*Math.cos(delta) - Fyf*Math.sin(delta) - Fxr) + y[3]*y[1];
        f[3] = (double[] y) -> (1.0/M) * (-Fyf*Math.cos(delta) + Fxf*Math.sin(delta) - Fyr) - y[2]*y[1];
        f[4] = (double[] y) -> -y[3]*Math.sin(y[0]) - y[2]*Math.cos(y[0]);
        f[5] = (double[] y) -> -y[3]*Math.cos(y[0]) + y[2]*Math.sin(y[0]);
        f[6] = (double[] y) -> (1.0/frontTyre.Iy) * (Fxf*frontTyre.R + frontTorque);
        f[7] = (double[] y) -> (1.0/rearTyre.Iy) * (Fxr*rearTyre.R + rearTorque);

        double[] y = {yawAngle, yawRate, vx, vy, fixedX, fixedY, omegaf, omegar};
        y = rungeKutta4(y, dt, f);
        ax = (y[2] - vx)/dt;
        ay = (y[3] - vy)/dt;
        yawAngle = y[0]; yawRate = y[1]; vx = y[2]; vy = y[3]; fixedX = y[4]; fixedY = y[5]; omegaf = y[6]; omegar = y[7];

       /* yawAngle += yawRate*dt;
        yawRate += (1.0/IZ) * (LF * (Fyf*Math.cos(delta) - Fxf*Math.sin(delta)) - LR*Fyr) * dt;
        vx += ((1.0/M) * (Fxf*Math.cos(delta) + Fyf*Math.sin(delta) - Fxr) + vy*yawRate)*dt;
        vy += ((1.0/M) * (-Fyf*Math.cos(delta) + Fxf*Math.sin(delta) - Fyr) - vx*yawRate)*dt;
        fixedX += (vx*Math.cos(yawAngle) - vy*Math.sin(yawAngle) )* dt;
        fixedY += (vx*Math.sin(yawAngle) + vy*Math.cos(yawAngle)) * dt;
        omegaf += (1.0/frontTyre.Iy) * (Fxf*frontTyre.R + frontTorque) * dt;
        omegar += (1.0/rearTyre.Iy) * (Fxr*rearTyre.R + rearTorque) * dt;

        double[] y = {yawAngle, yawRate, vx, vy, fixedX, fixedY, omegaf, omegar};*/
        t += dt;


       // DecimalFormat df = new DecimalFormat("0.00");
       // Arrays.stream(y).forEach(e -> System.out.print(df.format(e) + " "));
       // System.out.print("Fxf: " + df.format(frontTyre.Fx) + " Fyf: " + df.format(frontTyre.Fy) + " Fxr: " +  df.format(rearTyre.Fx) + " Fyr: " +  df.format(rearTyre.Fy) + " ");
       // System.out.print("kappaf: " + frontTyre.kappa + " kappar: " + rearTyre.kappa + " alphaf: " + frontTyre.alpha + " alphar: " + rearTyre.alpha + " delta: " + delta);
       // System.out.println();
    }

    private void updateTyreForces() {

        // Normal loads
        double Fzf = M*(G*LR - ax*HCG)/(LF + LR);
        double Fzr = M*(G*LF + ax*HCG)/(LF + LR);



        // Wheel centre lateral velocities
        double frontVy = vy - LF * yawRate;
        double rearVy = vy + LR * yawRate;
        // Slip angles
        double alphaf;
        double alphar;

        if(vx == 0 || Math.sqrt(vy*vy + vx*vx) == 0) {
            alphaf = 0;
            alphar = 0;
        }
        else{
            alphaf = Math.atan(frontVy/Math.abs(vx)) - delta;
            alphar = Math.atan(rearVy/Math.abs(vx));
            //System.out.println("frontVy: " + frontVy + " rearVy: " + rearVy);
            if(Math.abs(alphaf) > Math.PI/2) alphaf = Math.PI/2.0*Math.signum(alphaf);
            if(Math.abs(alphar) > Math.PI/2) alphar = Math.PI/2.0*Math.signum(alphar);
        }


        // Tyre velocities
        double vtf = Math.sqrt((vy - LF* yawRate)*(vy - LF* yawRate) + vx*vx);
        double vtr = Math.sqrt((vy + LR* yawRate)*(vy + LR* yawRate) + vx*vx);

        // Tyre velocity longitudinal components
        double vwxf = vtf * Math.cos(alphaf);
        double vwxr = vtr * Math.cos(alphar);
        double kappaf = 0, kappar = 0;

        if(vwxf != 0 || omegaf != 0) {
            kappaf = vwxf > omegaf*frontTyre.R ? (vwxf - omegaf*frontTyre.R)/vwxf : (vwxf - omegaf * frontTyre.R) / omegaf*frontTyre.R;
            if(Math.abs(kappaf) >= 1) kappaf = (1 - 1e-5) * Math.signum(kappaf) ;
        }
        if(vwxr != 0 || omegar != 0) {
            kappar = vwxr > omegar * rearTyre.R ? (vwxr - omegar * rearTyre.R) / vwxr : (vwxr - omegar * rearTyre.R) / omegar * rearTyre.R;
            if(Math.abs(kappar) >= 1) kappar = (1 - 1e-5) * Math.signum(kappar) ;
        }
        //System.out.println("front tyre");
        frontTyre.updateForces(Fzf, kappaf, alphaf);
        //System.out.println("rear tyre");
        rearTyre.updateForces(Fzr, kappar, alphar);
    }

    public double getVelocity(){
        return Math.sqrt(vx*vx + vy*vy);
    }

    public double getCenterOfRotationX(){
        return COR;
    }

    public double getCenterOfRotationY(){
        return 0.5;
    }

}
