package guivehiclephysicssim.nav;

import java.io.Serializable;

public class MapState implements Serializable {
    // coordinates in map reference frame
    private double v;
    private double psi;
    private double x;
    private double y;
    private double pxPerMeter;

    public MapState(double v, double psi, double x, double y, double pxPerMeter){
        this.v = v;
        this.psi = psi;
        this.x = x;
        this.y = y;
        this.pxPerMeter = pxPerMeter;
    }

    public void setV(double v) {
        this.v = v;
    }

    public void setPsi(double psi) {
        this.psi = psi;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getV() {
        return v;
    }

    public double getPsi() {
        return psi;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getPxPerMeter() {
        return pxPerMeter;
    }

    public void setPxPerMeter(double pxPerMeter) {
        this.pxPerMeter = pxPerMeter;
    }

    public void printInfo() {
        System.out.println("vehicle state:\nx: " + x + "\ny: " + y + "\npsi: " + psi + "\nv: " + v + "\n\n");
    }
}
