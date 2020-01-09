package mpc;

import com.cureos.numerics.*;
import comms.Publisher;
import comms.Subscriber;
import nav.Controls;
import nav.MapState;
import nav.Path;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class Mpc {

    double dt;
    double lr;
    double lf;
    double vref;
    double amax;
    double wa;
    double wcte;
    double weps;
    double wv;
    double wdelta;
    double wdeltavar;
    double wavar;
    double[] pathCoeffs;
    double initialV;
    int stateVars;
    int steeringVars;
    int predictionHorizon;
    int initialDelta;
    int initialA;
    int constraints;
    int nfevals;
    int pathPoints;
    double rhobeg, rhoend;
    final int modelEquations = 5;
    // order of state variables
    final int X = 0, Y = 1, PSI = 2, V = 3;
    // order of steering variables
    final int DELTA = 0, A = 1;

    public Mpc(String mpcProperties){

        Properties p = new Properties();
        try(InputStream inputTyre = new FileInputStream(mpcProperties)){
            p.load(inputTyre);
        } catch(IOException e) {
            e.printStackTrace();
        }

        dt = Double.parseDouble(p.getProperty("dt"));
        lr = Double.parseDouble(p.getProperty("lr"));
        lf = Double.parseDouble(p.getProperty("lf"));
        vref = Double.parseDouble(p.getProperty("vref"));
        amax = Double.parseDouble(p.getProperty("amax"));
        wa = Double.parseDouble(p.getProperty("wa"));
        wcte = Double.parseDouble(p.getProperty("wcte"));
        weps = Double.parseDouble(p.getProperty("weps"));
        wv = Double.parseDouble(p.getProperty("wv"));
        wdelta = Double.parseDouble(p.getProperty("wdelta"));
        wdeltavar = Double.parseDouble(p.getProperty("wdeltavar"));
        wavar = Double.parseDouble(p.getProperty("wavar"));
        stateVars = Integer.parseInt(p.getProperty("stateVars"));
        steeringVars = Integer.parseInt(p.getProperty("steeringVars"));
        predictionHorizon = Integer.parseInt(p.getProperty("predictionHorizon"));
        nfevals = Integer.parseInt(p.getProperty("nfevals"));
        rhobeg = Double.parseDouble(p.getProperty("rhobeg"));
        rhoend = Double.parseDouble(p.getProperty("rhoend"));
        pathPoints = Integer.parseInt(p.getProperty("pathPoints"));
        constraints = 0;

    }

    private class CobylaCalcfc implements Calcfc{
        @Override
        public double Compute(int n, int m, double[] x, double[] con) {

            double objVal = 0;
            int steeringStart = stateVars * (predictionHorizon + 1);
            for (int t = 0; t < predictionHorizon; ++t){

                // index of steering variables
                int iu0 = steeringStart + steeringVars * t;
                int iu1 = iu0 + steeringVars;
                // index of state variables
                int is0 = stateVars * t;
                int is1 = is0 + stateVars;

                double x0 = x[X + is0];
                double y0 = x[Y + is0];
                double psi0 = x[PSI + is0];
                double v0 = x[V + is0];
                double a0 = x[A + iu0];
                double delta0 = x[DELTA + iu0];

                double x1 = x[X + is1];
                double y1 = x[Y + is1];
                double psi1 = x[PSI + is1];
                double v1 = x[V + is1];
                double a1 = x[A + iu1];
                double delta1 = x[DELTA + iu1];

                double vavg = v0 + 0.5 * a0 * dt;
                double beta0 = lr * delta0/(lf + lr);
                //double beta1 = Math.atan(lr/(lf + lr) * Math.tan(delta1));
                double ypath = pathCoeffs[0] + pathCoeffs[1] * x1 + pathCoeffs[2] * x1 * x1;
                double psipath = pathCoeffs[1] + 2 * pathCoeffs[2] * x1;

                objVal += wcte * Math.pow(y1 - ypath, 2);
                objVal += weps * Math.pow(psi1 - psipath, 2);
                objVal += wv * Math.pow(vavg - vref, 2);
                objVal += wdelta * Math.pow(delta0, 2);
                objVal += wavar * Math.pow(a1 - a0, 2);
                objVal += wdeltavar * Math.pow(delta0 - delta1, 2);
                // Model equations
                objVal +=  5 * Math.pow(x1 - (x0 + vavg * dt), 2);
                objVal +=  5 * Math.pow(y1 - (y0 + vavg * dt * (psi0 + beta0)), 2);
                objVal +=  5 * Math.pow(psi1 - (psi0 + vavg * dt * beta0/lr), 2);
                objVal +=  5 * Math.pow(v1 - (v0 + a0 * dt), 2);
                objVal +=  5 * Math.pow(Math.abs(delta0) - 0.44, 2);
            }

            objVal += 10 * Math.pow(x[X], 2);
            objVal += 10 * Math.pow(x[Y], 2);
            objVal += 10 * Math.pow(x[PSI], 2);
            objVal += 10 * Math.pow(x[V] - initialV, 2);

            return objVal;
        }
    }

    public Controls getControls(double initialVelocity, double[] pathCoeffs){

        this.pathCoeffs = pathCoeffs;
        this.initialV = initialVelocity;
        int steeringStart = stateVars * (predictionHorizon + 1);
        double[] vars = new double[(stateVars + steeringVars) * (predictionHorizon + 1)];
        Arrays.fill(vars, 1.0);
        vars[X] = 0;
        vars[Y] = 0;
        vars[PSI] = 0;
        vars[V] = initialV;

        initialV = vars[V];

        CobylaCalcfc calcfc = new CobylaCalcfc();
        CobylaExitStatus result = Cobyla.FindMinimum(calcfc, (stateVars + steeringVars) * (predictionHorizon + 1),
                constraints, vars, rhobeg, rhoend, 1, nfevals);

        System.out.println("Predicted points: \n");
        for(int i = 0; i < predictionHorizon; ++i){
            System.out.println(vars[stateVars * i + X] + ", " + vars[stateVars * i + Y] + "\n");
        }

        System.out.println("Predicted steering: \n");
        for(int i = 0; i < predictionHorizon; ++i){
            System.out.println(vars[steeringStart + steeringVars * i + DELTA] + ", " + vars[steeringStart + steeringVars * i + A] + "\n");
        }

        Vector2D[] predictedPath = new Vector2D[predictionHorizon];
        Vector2D[] polynomialPath = new Vector2D[predictionHorizon];

        return new Controls(vars[steeringStart + steeringVars + DELTA],
                vars[stateVars + V], vars[steeringVars + V],
                predictedPath, polynomialPath);
    }

    public static void main(String[] args) {
        String mpcProps = "res/mpc.properties", pathFile = "res/path.csv";
        Mpc mpc = new Mpc(mpcProps);
        Path path = new Path(pathFile);
        Publisher controlsPub = new Publisher();
        Subscriber mapStateSub = new Subscriber();
        MapState mapState;
        ObjectMessage msg;

        while(true){
            try{
                msg = mapStateSub.getMessage();
                System.out.println(msg == null);
                if(msg != null){
                    System.out.println("jołjoł");
                    mapState = (MapState) msg.getObject();
                    double[] pathCoeffs = path.getPathCoeffs(mapState.getX(), mapState.getY(),
                            mapState.getPsi(), mpc.pathPoints);
                    Controls controls = mpc.getControls(mapState.getV(), pathCoeffs);
                    controlsPub.publishMessage(controls);
                }
            } catch(JMSException e){System.out.println(e.toString());}
        }
    }

}
