import java.util.ArrayList;

public class ClusterSet {
    private int k;
    private ArrayList<Cluster> clusters;
    private int size;

    public ClusterSet(int k) {
        this.k = k;
        clusters = new ArrayList<>();
    }

    public int getSize() {
        int size = 0;
        for (Cluster c:clusters) {
            size += c.size();
        }

        return size;
    }

    public void findClusters(ArrayList<Point> pointsToCluster) {
        if (clusters.size() == 0) {
            System.out.println("No points yet!");
            return;
        }

        clearClusters();

        boolean done = false;
        int loops = 0;
        while (!done) {
            clearClusters();
            assignPixelsToClusters(pointsToCluster, clusters);
            int changed = reCalculateClusterCenters(clusters);
            if (changed == 0) done = true;
            loops++;
        }
    }

    public void clearClusters() {
        for (Cluster c : clusters) {
            c.clear();
        }
    }

    private int reCalculateClusterCenters(ArrayList<Cluster> clusters) {
        int changed = 0;
        for (Cluster c : clusters) {
            if (c.reCalculateCenter() == true) {
                changed++;
            }
        }

        return changed;
    }

    private void assignPixelsToClusters(ArrayList<Point> points, ArrayList<Cluster> clusters) {
        for (Point p:points) {
            Cluster nearest = findNearestClusterFor(p, clusters);
            nearest.add(p);
        }
    }

    private Cluster findNearestClusterFor(Point p, ArrayList<Cluster> clusters) {
        if (clusters.size() == 0) return null;

        Cluster nearest = clusters.get(0);
        double distToNearest = p.getSquaredDistanceTo(nearest.getCenter());
        for (Cluster c : clusters) {
            double dist = p.getSquaredDistanceTo(c.getCenter());
            if (dist < distToNearest) {
                distToNearest = dist;
                nearest = c;
            }
        }

        return nearest;
    }

    public void initClusters(ArrayList<Point> points) {
        ArrayList<Cluster> clusters = new ArrayList<>();
        if (points.size() == 0) {
            return;
        }

        ArrayList<Point> assigned = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            Point p;
            do {
                p = points.get((int)(Math.random()*points.size()));
            } while (assigned.contains(p));

            Cluster c = new Cluster(p);
            assigned.add(p);
            clusters.add(c);
        }

        this.clusters = clusters;
    }

    public void removeOutliers(double percent) {
        for (Cluster c : clusters) {
            c.removeOutliers(percent);
        }
    }

    public double getMSE() {
        double mse = 0;
        for (Cluster c : clusters) {
            mse += c.getDistanceSums();
        }

        return mse;
    }

    public ArrayList<Cluster> getClusters() {
        return this.clusters;
    }

    public int getK() {
        return k;
    }

    public void eliminateSmallClusters(int percent) {
        double numPoints = getSize();

        System.out.println("Total points: " + numPoints);
        int count = 0;

        for (int i = 0; i < clusters.size(); i++) {
            Cluster c = clusters.get(i);
            if (c.size() < percent*numPoints) {
                clusters.remove(i);
                i--;
                count++;
                k--;        // decrement k!
                System.out.println("new k is: " + k);
            }
        }

        System.out.println("removed " + count + " small clusters");
    }

    public void mergeClusters() {
        for (int i = 0; i < clusters.size(); i++) {
            for (int j = i+1; j<clusters.size(); j++) {
                Cluster c1 = clusters.get(i);
                Cluster c2 = clusters.get(j);

                if (c1.shouldMergeWith(c2)) {
                    c1.absorb(c2);
                    clusters.remove(c2);
                    j--;
                    k--;   // Decrement k!
                    System.out.println("New k is: " + k);
                }

            }
        }
    }
}
