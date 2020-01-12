package autonomous.nav;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Path {

    ArrayList<Vector2D> pathPoints;

    public Path(String pointsFile){

        pathPoints = new ArrayList<>();
        String row = "";
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(pointsFile));
            while((row = csvReader.readLine()) != null){
                String[] xy = row.split(",");
                Vector2D pathPoint = new Vector2D(Double.parseDouble(xy[0]), Double.parseDouble(xy[1]));
                pathPoints.add(pathPoint);
            }
        }
        catch(java.io.FileNotFoundException e){
            System.out.println("File not found\n");
        }
        catch(java.io.IOException e){
            System.out.println("Nothing to read\n");
        }
    }

    private double dist(double x1x2, double y1y2){
        return Math.sqrt(x1x2*x1x2 + y1y2*y1y2);
    }

    public double[] getPathCoeffs(double x, double y, double psi, int nPathPoints){

        // find closest path point
        double minDist = 1e9;
        int closestPointIndex = 0;
        double currentDist = 0;
        for(int i = 0; i < pathPoints.size(); i++){
            currentDist = dist(x - pathPoints.get(i).getX(), y - pathPoints.get(i).getY());
            if(currentDist < minDist){
                closestPointIndex = i;
                minDist = currentDist;
            }
        }

        // extract those points
        WeightedObservedPoint[] weightedPathPoints = new WeightedObservedPoint[nPathPoints];
        for(int i = 0; i < nPathPoints; i++){
            weightedPathPoints[i] = new WeightedObservedPoint(1.0, pathPoints.get(closestPointIndex).getX(),
                    pathPoints.get(closestPointIndex).getY());
            closestPointIndex = (closestPointIndex + 1) % pathPoints.size();
        }

        // transform points
        double xi, yi;
        for(int i = 0; i < weightedPathPoints.length; i++){
            xi = weightedPathPoints[i].getX(); yi = weightedPathPoints[i].getY();
            xi -= x;
            yi -= y;
            xi = xi * Math.cos(psi) - yi * Math.sin(psi);
            yi = xi * Math.sin(psi) + yi * Math.cos(psi);
            weightedPathPoints[i] = new WeightedObservedPoint(1.0, xi, yi);
        }

        //for(int i = 0; i < weightedPathPoints.length; i++) System.out.println(weightedPathPoints[i].getX() +","+weightedPathPoints[i].getY());

        PolynomialCurveFitter polyfit = PolynomialCurveFitter.create(2);
        double[] pathCoeffs = polyfit.fit(Arrays.asList(weightedPathPoints));

        return pathCoeffs;
    }

    public int getPathPointsSize() { return pathPoints.size(); }

    public double[] getPathPoint(int i){
        return new double[]{pathPoints.get(i).getX(), pathPoints.get(i).getY()};
    }

}
