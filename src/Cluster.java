import java.util.ArrayList;

public class Cluster {
    ArrayList<Point> points;
    Point center;
    double minDist, maxDist;
    Point closestPoint, furthestPoint;
    ColorPoint randomColor;
    private double THRESHOLD = 15;

    public Cluster(Point p) {
        points = new ArrayList<>();
        center = p;
        randomColor = ColorPoint.getRandomColor();
    }

    public void add(Point pixel) {
        double dist = pixel.getSquaredDistanceTo(center);
        if (dist < minDist) {
            minDist = dist;
            closestPoint = pixel;
        }

        if (dist > maxDist) {
            maxDist = dist;
            furthestPoint = pixel;
        }

        points.add(pixel);
    }

    public Point getCenter() {
        return this.center;
    }

    public boolean reCalculateCenter() {
        if (points.size() == 0) {
           // System.out.println("Warning: recalculating centers with no points added");
            return false;
        }

        int originalRow = this.center.getRow();
        int originalCol = this.center.getCol();

        double rowSum = 0, colSum = 0;
        for (Point p : this.points) {
            rowSum += p.getRow();
            colSum += p.getCol();
        }

        int newR = (int) (rowSum/ points.size());
        int newC = (int) (colSum/ points.size());

        if (newR == originalRow && newC == originalCol) {
            return false;
        }

        center = new Point(newR, newC);
        return true;
    }

    public double getMSE() {
        return getDistanceSums()/this.points.size();
    }

    public double getDistanceSums() {
        double distSum = 0;

        for (Point p : this.points) {
            distSum += p.getSquaredDistanceTo(this.center);
        }

        return distSum;
    }

    public double averageDistanceOfPoint() {
        return getDistanceSums()/this.points.size();
    }

    public void clear() {
        this.points.clear();
        closestPoint = null;
        furthestPoint = null;
        minDist = Double.MAX_VALUE;
        maxDist = Double.MIN_VALUE;
    }

    public int removeOutliers(double percent) {
        double MSE = getMSE();
        int counter = 0;

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            double dist = p.getSquaredDistanceTo(center);

            if (dist > percent*MSE) {
                points.remove(i);
                i--;
            }
        }

        return counter;
    }

    public int size() {
        return points.size();
    }

    public boolean shouldMergeWith(Cluster other) {
        double centerDistance = other.getCenter().getSquaredDistanceTo(this.getCenter());

        if (centerDistance < THRESHOLD*this.getMSE() || centerDistance < THRESHOLD*other.getMSE()) {
            return true;
        }

        return false;
    }

    public void absorb(Cluster c2) {
        for (Point p : c2.points) {
            this.points.add(p);
        }
        c2.clear();

        this.reCalculateCenter();
    }

    public double getMeanDist() {
        double distSum = 0;

        for (Point p : this.points) {
            distSum += p.getDistanceTo(this.center);
        }

        return distSum/points.size();
    }
}