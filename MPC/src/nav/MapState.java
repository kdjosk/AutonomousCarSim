package nav;

public class MapState {
    // coordinates in map reference frame
    double v;
    double psi;
    double x;
    double y;

    public MapState(double v, double psi, double x, double y){
        this.v = v;
        this.psi = psi;
        this.x = x;
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
}
