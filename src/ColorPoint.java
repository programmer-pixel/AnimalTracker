public class ColorPoint {
    private int r, g, b;

    public ColorPoint(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static ColorPoint getRandomColor() {
        int r = (int)(Math.random()*256);
        int g = (int)(Math.random()*256);
        int b = (int)(Math.random()*256);

        ColorPoint p = new ColorPoint(r, g, b);
        return p;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public double distanceTo(ColorPoint other) {
        return distance(this.getR(), this.getG(), this.getB(), other.getR(), other.getG(), other.getB());
    }

    private double distance(int r, int g, int b, int r1, int g1, int b1) {
        int dr = r - r1;
        int dg = g - g1;
        int db = b - b1;
        return Math.sqrt(dr*dr + dg*dg + db*db);
    }
}
