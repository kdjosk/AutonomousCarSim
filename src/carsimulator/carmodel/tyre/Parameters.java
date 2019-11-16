package carsimulator.carmodel.tyre;

import java.util.*;
import java.io.*;

public class Parameters {

    // Cornering stiffness
    double Cf;
    double Cr;
    //Gravity
    double g;

    // Center of gravity height
    double h;

    // Center of gravity to axle distance
    double lr;
    double lf;

    // Vehicle mass
    double m;

    Parameters(String propertiesFile){
        Properties params = new Properties();
        try(InputStream input = new FileInputStream(propertiesFile)){
           params = new Properties();
            params.load(input);
        } catch(IOException e){
            e.printStackTrace();
        }

        Cf = Double.parseDouble(params.getProperty("Cf"));
        Cr = Double.parseDouble(params.getProperty("Cr"));
        g = Double.parseDouble(params.getProperty("g"));
        h = Double.parseDouble(params.getProperty("h"));
        lr = Double.parseDouble(params.getProperty("lr"));
        lf = Double.parseDouble(params.getProperty("lf"));
        m = Double.parseDouble(params.getProperty("m"));

    }

}
